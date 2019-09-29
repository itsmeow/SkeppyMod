package its_meow.skeppymod.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import its_meow.skeppymod.ISidedProxy;
import its_meow.skeppymod.SkeppyMod;
import its_meow.skeppymod.client.model.ModelBipedArmorColoredLayer;
import its_meow.skeppymod.client.model.ModelBipedArmorLayer;
import its_meow.skeppymod.client.model.ModelJoggers;
import its_meow.skeppymod.client.renderer.entity.RenderMrSqueegy;
import its_meow.skeppymod.client.renderer.tileentity.TileEntityItemRenderStatue;
import its_meow.skeppymod.client.renderer.tileentity.TileEntityRenderStatue;
import its_meow.skeppymod.entity.EntityMrSqueegy;
import its_meow.skeppymod.item.ItemMerchArmor;
import its_meow.skeppymod.network.CPacketSetHoodStatus;
import its_meow.skeppymod.tileentity.TileEntityStatue;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = SkeppyMod.MODID, value = Side.CLIENT)
public class SkeppyModClient implements ISidedProxy {

    private static final SoundType[] SOUND_TYPES = new SoundType[] {SoundType.ANVIL,SoundType.CLOTH,SoundType.GLASS,SoundType.GROUND,SoundType.LADDER,SoundType.METAL,SoundType.PLANT,SoundType.SAND,SoundType.SLIME,SoundType.SNOW,SoundType.STONE,SoundType.WOOD};

    private static final HashMap<SoundType, SoundEvent> ORIGINAL_SOUNDS = new HashMap<SoundType, SoundEvent>();

    public static final ModelBipedArmorLayer ARMOR_SLIM = new ModelBipedArmorLayer(true);
    public static final ModelBipedArmorLayer ARMOR_DEFAULT = new ModelBipedArmorLayer(false);
    public static final ModelBipedArmorColoredLayer COLOR_ARMOR_SLIM = new ModelBipedArmorColoredLayer(true);
    public static final ModelBipedArmorColoredLayer COLOR_ARMOR_DEFAULT = new ModelBipedArmorColoredLayer(false);
    public static final ModelJoggers JOGGERS_DEFAULT = new ModelJoggers(false);
    public static final ModelJoggers JOGGERS_SLIM = new ModelJoggers(true);

    public static KeyBinding hoodie_control = null;

    public static boolean hood_up = true;

    public static ModelResourceLocation SKEPPY_STATUE_MLR = new ModelResourceLocation("skeppymod:statue_skeppy", "inventory");
    public static ModelResourceLocation A6D_STATUE_MLR = new ModelResourceLocation("skeppymod:statue_a6d", "inventory");
    public static ModelResourceLocation BBH_STATUE_MLR = new ModelResourceLocation("skeppymod:statue_badboyhalo", "inventory");

    public static Field stepSoundF = null;
    
    private static boolean flopLastTick = false;
    private static boolean flop = false;

    @Override
    public void init(FMLInitializationEvent event) {
        hoodie_control = new KeyBinding("key.skeppymod.hoodie_control", Keyboard.KEY_H, "key.skeppymod.category");
        ClientRegistry.registerKeyBinding(hoodie_control);
        try {
            stepSoundF = SoundType.class.getDeclaredField("stepSound");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(stepSoundF, stepSoundF.getModifiers() & ~Modifier.FINAL);
            stepSoundF.setAccessible(true);
        } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent event) {
        if(event.phase == Phase.START) {
            if(Minecraft.getMinecraft().player != null) {
                flop = Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == SkeppyMod.FLIP_FLOPS;
                if(flop && !flopLastTick) {
                    flipFlopIfy();
                } else if(!flop && flopLastTick) {
                    resetSounds();
                }
                
                flopLastTick = flop;
            }
        }
    }

    private static void resetSounds() {
        for(SoundType type : SOUND_TYPES) {
            try {
                stepSoundF.set(type, ORIGINAL_SOUNDS.get(type));
            } catch(IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void flipFlopIfy() {
        for(SoundType type : SOUND_TYPES) {
            ORIGINAL_SOUNDS.put(type, type.getStepSound());
            try {
                stepSoundF.set(type, SkeppyMod.FLIP_FLOP_SOUND);
            } catch(IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean wasDownLastInput = false;

    @SubscribeEvent
    public static void input(InputEvent event) {
        boolean isKey = Keyboard.isKeyDown(hoodie_control.getKeyCode());
        if(isKey && !wasDownLastInput) {
            if(Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemMerchArmor) {
                hood_up = !hood_up;
                Minecraft.getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 1F, 0.8F);
                SkeppyMod.NETWORK_INSTANCE.sendToServer(new CPacketSetHoodStatus(hood_up));
            }

        }
        wasDownLastInput = isKey;
    }

    @SubscribeEvent
    public static void mre(ModelRegistryEvent event) {
        initModel(SkeppyMod.BLOCK_14, 0);
        initModel(SkeppyMod.BAGUETTE, 0);
        initModel(SkeppyMod.MUFFIN, 0);
        initModel(SkeppyMod.SQUEEGY_BUCKET, 0);
        initModel(SkeppyMod.MUFFIN_ON_A_STICK, 0);
        initModel(SkeppyMod.CRAFT_HOODIE_CHEST, 0);
        initModel(SkeppyMod.CARTOON_HOODIE_CHEST, 0);
        initModel(SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_BLUE, 0);
        initModel(SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_BLACK, 0);
        initModel(SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_GREY, 0);
        initModel(SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_PINK, 0);
        initModel(SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_WHITE, 0);
        initModel(SkeppyMod.SKEPPY_HOODIE_ARMS_CHEST_WHITE, 0);
        initModel(SkeppyMod.SKEPPY_HOODIE_ARMS_CHEST_BLACK, 0);
        initModel(SkeppyMod.SKEPPY_JOGGERS, 0);
        initModel(SkeppyMod.SKEPPY_TSHIRT_FRONT_BLUE, 0);
        initModel(SkeppyMod.SKEPPY_TSHIRT_FRONT_BLACK, 0);
        initModel(SkeppyMod.SKEPPY_TSHIRT_FRONT_GREY, 0);
        initModel(SkeppyMod.SKEPPY_TSHIRT_FRONT_PINK, 0);
        initModel(SkeppyMod.SKEPPY_TSHIRT_FRONT_WHITE, 0);
        initModel(SkeppyMod.SKEPPY_HOODIE_DOUBLE_SIDED_WHITE, 0);
        initModel(SkeppyMod.SKEPPY_HOODIE_DOUBLE_SIDED_BLACK, 0);
        initModel(SkeppyMod.SKEPPY_LONGSLEEVE_WHITE, 0);
        initModel(SkeppyMod.SKEPPY_LONGSLEEVE_BLACK, 0);
        initModel(SkeppyMod.SKEPPY_LOGO_HOODIE_BLUE, 0);
        initModel(SkeppyMod.SKEPPY_LOGO_HOODIE_GREY, 0);
        initModel(SkeppyMod.SKEPPY_LOGO_HOODIE_PINK, 0);
        initModel(SkeppyMod.SKEPPY_LOGO_HOODIE_WHITE, 0);
        initModel(SkeppyMod.SKEPPY_BOTTLE_EMPTY, 0);
        initModel(SkeppyMod.SKEPPY_BOTTLE_FULL, 0);
        initModel(SkeppyMod.CHEESY_FRIES_EMPTY, 0);
        initModel(SkeppyMod.CHEESY_FRIES, 0);
        initModel(SkeppyMod.THIN_CRUST_PIZZA, 0);
        initModel(SkeppyMod.PINECONE, 0);
        initModel(SkeppyMod.DILL_PICKLE_CHIPS, 0);
        initModel(SkeppyMod.DILL_PICKLE_CHIPS_EMPTY, 0);
        initModel(SkeppyMod.JAPANESE_SYMBOL, 0);
        initModel(SkeppyMod.FLIP_FLOPS, 0);
        initModel(SkeppyMod.SPAGHETTIOS, 0);
        initModel(SkeppyMod.SPAGHETTIOS_EMPTY, 0);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(SkeppyMod.SKEPPY_STATUE), 0, SKEPPY_STATUE_MLR);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(SkeppyMod.A6D_STATUE), 0, A6D_STATUE_MLR);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(SkeppyMod.BBH_STATUE), 0, BBH_STATUE_MLR);
        Item.getItemFromBlock(SkeppyMod.SKEPPY_STATUE).setTileEntityItemStackRenderer(new TileEntityItemRenderStatue());
        Item.getItemFromBlock(SkeppyMod.A6D_STATUE).setTileEntityItemStackRenderer(new TileEntityItemRenderStatue());
        Item.getItemFromBlock(SkeppyMod.BBH_STATUE).setTileEntityItemStackRenderer(new TileEntityItemRenderStatue());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStatue.class, new TileEntityRenderStatue());
        RenderingRegistry.registerEntityRenderingHandler(EntityMrSqueegy.class, RenderMrSqueegy::new);
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        IBakedModel skeppy = event.getModelRegistry().getObject(SKEPPY_STATUE_MLR);
        event.getModelRegistry().putObject(SKEPPY_STATUE_MLR, new ModelWrapper(skeppy));
        IBakedModel a6d = event.getModelRegistry().getObject(A6D_STATUE_MLR);
        event.getModelRegistry().putObject(A6D_STATUE_MLR, new ModelWrapper(a6d));
        IBakedModel bbh = event.getModelRegistry().getObject(BBH_STATUE_MLR);
        event.getModelRegistry().putObject(BBH_STATUE_MLR, new ModelWrapper(bbh));
    }

    public static void initModel(Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    public static void initModel(Block block, int meta) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

}
