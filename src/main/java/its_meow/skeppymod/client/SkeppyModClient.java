package its_meow.skeppymod.client;

import org.lwjgl.input.Keyboard;

import its_meow.skeppymod.ISidedProxy;
import its_meow.skeppymod.SkeppyMod;
import its_meow.skeppymod.client.renderer.entity.RenderMrSqueegy;
import its_meow.skeppymod.client.renderer.tileentity.TileEntityItemRenderStatue;
import its_meow.skeppymod.client.renderer.tileentity.TileEntityRenderStatue;
import its_meow.skeppymod.entity.EntityMrSqueegy;
import its_meow.skeppymod.item.ItemMerchArmor;
import its_meow.skeppymod.tileentity.TileEntityStatue;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = SkeppyMod.MODID, value = Side.CLIENT)
public class SkeppyModClient implements ISidedProxy {

    public static KeyBinding hoodie_control = null;

    public static boolean hood_up = true;

    public static ModelResourceLocation SKEPPY_STATUE_MLR = new ModelResourceLocation("skeppymod:statue_skeppy", "inventory");
    public static ModelResourceLocation A6D_STATUE_MLR = new ModelResourceLocation("skeppymod:statue_a6d", "inventory");
    public static ModelResourceLocation BBH_STATUE_MLR = new ModelResourceLocation("skeppymod:statue_badboyhalo", "inventory");

    @Override
    public void init(FMLInitializationEvent event) {
        hoodie_control = new KeyBinding("key.skeppymod.hoodie_control", Keyboard.KEY_H, "key.skeppymod.category");
        ClientRegistry.registerKeyBinding(hoodie_control);
    }

    private static boolean wasDownLastInput = false;

    @SubscribeEvent
    public static void input(InputEvent event) {
        boolean isKey = Keyboard.isKeyDown(hoodie_control.getKeyCode());
        if(isKey && !wasDownLastInput) {
            if(Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemMerchArmor) {
                hood_up = !hood_up;
                Minecraft.getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 1F, 0.8F);
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
