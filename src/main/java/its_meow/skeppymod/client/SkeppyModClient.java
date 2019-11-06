package its_meow.skeppymod.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;

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
import its_meow.skeppymod.util.ISidedProxy;
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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
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
            stepSoundF = ObfuscationReflectionHelper.findField(SoundType.class, "field_185863_p");
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
        if(stepSoundF != null) {
            for(SoundType type : SOUND_TYPES) {
                try {
                    stepSoundF.set(type, ORIGINAL_SOUNDS.get(type));
                } catch(IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void flipFlopIfy() {
        if(stepSoundF != null) {
            for(SoundType type : SOUND_TYPES) {
                ORIGINAL_SOUNDS.put(type, type.getStepSound());
                try {
                    stepSoundF.set(type, SkeppyMod.FLIP_FLOP_SOUND);
                } catch(IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
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
        initModel(SkeppyMod.BAGUETTE, SkeppyMod.MUFFIN, SkeppyMod.SQUEEGY_BUCKET, SkeppyMod.MUFFIN_ON_A_STICK, SkeppyMod.CRAFT_HOODIE_CHEST, SkeppyMod.CARTOON_HOODIE_CHEST, SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_BLUE, 
        SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_BLACK, SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_GREY, SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_PINK, 
        SkeppyMod.SKEPPY_HOODIE_FRONT_CHEST_WHITE, SkeppyMod.SKEPPY_HOODIE_ARMS_CHEST_WHITE, SkeppyMod.SKEPPY_HOODIE_ARMS_CHEST_BLACK, 
        SkeppyMod.SKEPPY_JOGGERS, SkeppyMod.SKEPPY_TSHIRT_FRONT_BLUE, SkeppyMod.SKEPPY_TSHIRT_FRONT_BLACK, SkeppyMod.SKEPPY_TSHIRT_FRONT_GREY, 
        SkeppyMod.SKEPPY_TSHIRT_FRONT_PINK, SkeppyMod.SKEPPY_TSHIRT_FRONT_WHITE, SkeppyMod.SKEPPY_HOODIE_DOUBLE_SIDED_WHITE, 
        SkeppyMod.SKEPPY_HOODIE_DOUBLE_SIDED_BLACK, SkeppyMod.SKEPPY_LONGSLEEVE_WHITE, SkeppyMod.SKEPPY_LONGSLEEVE_BLACK, SkeppyMod.SKEPPY_LOGO_HOODIE_BLUE, 
        SkeppyMod.SKEPPY_LOGO_HOODIE_GREY, SkeppyMod.SKEPPY_LOGO_HOODIE_PINK, SkeppyMod.SKEPPY_LOGO_HOODIE_WHITE, SkeppyMod.SKEPPY_BOTTLE_EMPTY, 
        SkeppyMod.SKEPPY_BOTTLE_FULL, SkeppyMod.CHEESY_FRIES_EMPTY, SkeppyMod.CHEESY_FRIES, SkeppyMod.THIN_CRUST_PIZZA, SkeppyMod.PINECONE, 
        SkeppyMod.DILL_PICKLE_CHIPS, SkeppyMod.DILL_PICKLE_CHIPS_EMPTY, SkeppyMod.JAPANESE_SYMBOL, SkeppyMod.FLIP_FLOPS, SkeppyMod.SPAGHETTIOS, 
        SkeppyMod.SPAGHETTIOS_EMPTY, SkeppyMod.JIF, SkeppyMod.JIF_EMPTY, SkeppyMod.SKEPPY_FACE);
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

    public static void initModel(Item... items) {
        for(Item item : items) {
            initModel(item, 0);
        }
    }

}
