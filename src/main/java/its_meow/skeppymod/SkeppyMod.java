package its_meow.skeppymod;

import java.util.UUID;

import its_meow.skeppymod.block.Block14;
import its_meow.skeppymod.block.BlockStatue;
import its_meow.skeppymod.entity.EntityMrSqueegy;
import its_meow.skeppymod.item.ItemBlockStatue;
import its_meow.skeppymod.item.ItemEZFood;
import its_meow.skeppymod.item.ItemSqueegyBucket;
import its_meow.skeppymod.tileentity.TileEntityStatue;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = SkeppyMod.MODID)
@Mod(modid = SkeppyMod.MODID, name = SkeppyMod.NAME, version = SkeppyMod.VERSION)
public class SkeppyMod {

    /* Constants */
    public static final String MODID = "skeppymod";
    public static final String NAME = "Skeppy Mod";
    public static final String VERSION = "@VERSION@";
    public static final UUID SKEPPY_UUID = UUID.fromString("8e176c5a-c26d-4c14-8efe-77b598b8b3ea");
    public static final UUID BBH_UUID = UUID.fromString("26bdff37-fec8-48f1-980f-66bf69ee751c");
    public static final UUID A6D_UUID = UUID.fromString("f9a79945-a5de-4bb5-b1df-4915e7e5eb26");
    public static final CreativeTabs SKEPPY_TAB = new CreativeTabs("skeppy_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Item.getItemFromBlock(BLOCK_14));
        }};
    
    /* Item Instances */
    public static ItemEZFood BAGUETTE = new ItemEZFood("baguette", 8, 1, 128, false);
    public static ItemEZFood MUFFIN = new ItemEZFood("muffin", 3, 3, 32, false);
    public static ItemSqueegyBucket SQUEEGY_BUCKET = new ItemSqueegyBucket();
    
    /* Block Instances */
    public static Block14 BLOCK_14 = new Block14();
    public static BlockStatue SKEPPY_STATUE = new BlockStatue("skeppy");
    public static BlockStatue BBH_STATUE = new BlockStatue("badboyhalo");
    public static BlockStatue A6D_STATUE = new BlockStatue("a6d");
    
    /* Registry */
    
    private static int modEntities = 0;
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BLOCK_14, SKEPPY_STATUE, BBH_STATUE, A6D_STATUE);
        GameRegistry.registerTileEntity(TileEntityStatue.class, new ResourceLocation(MODID, "statue_te"));
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemBlock(BLOCK_14).setRegistryName(BLOCK_14.getRegistryName()),
        new ItemBlockStatue(SKEPPY_STATUE), new ItemBlockStatue(BBH_STATUE), new ItemBlockStatue(A6D_STATUE),
        BAGUETTE, MUFFIN, SQUEEGY_BUCKET);
    }
    
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityEntryBuilder.<EntityMrSqueegy>create().factory(EntityMrSqueegy::new).entity(EntityMrSqueegy.class).id(new ResourceLocation(MODID, "mrsqueegy"), modEntities++).name("skeppymod.mrsqueegy").tracker(64, 1, true).build());
    }
    
    /* Events */
    
    @SubscribeEvent
    public static void onInteract(EntityInteract event) {
        if(event.getEntity() instanceof EntityPlayer && !event.getEntityPlayer().world.isRemote) {
            if(event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.BUCKET) {
                event.getTarget().attackEntityFrom(new EntityDamageSource("bucket", event.getEntityPlayer()).setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute(), 2000F);
            }
        }
    }
    
    /* Util */
    
    public static boolean isSkeppy(EntityPlayer player) {
        return player.getGameProfile().getId() == SKEPPY_UUID;
    }
    
    public static boolean isA6D(EntityPlayer player) {
        return player.getGameProfile().getId() == A6D_UUID;
    }

    public static boolean isBBH(EntityPlayer player) {
        return player.getGameProfile().getId() == BBH_UUID;
    }

}