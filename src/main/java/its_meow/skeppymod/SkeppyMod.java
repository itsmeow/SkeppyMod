package its_meow.skeppymod;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import its_meow.skeppymod.block.Block14;
import its_meow.skeppymod.block.BlockStatue;
import its_meow.skeppymod.command.CommandF;
import its_meow.skeppymod.entity.EntityMrSqueegy;
import its_meow.skeppymod.item.ItemBlockStatue;
import its_meow.skeppymod.item.ItemEZFood;
import its_meow.skeppymod.item.ItemFullSkeppyBottle;
import its_meow.skeppymod.item.ItemJoggers;
import its_meow.skeppymod.item.ItemMerchArmor;
import its_meow.skeppymod.item.ItemMerchArmorColored;
import its_meow.skeppymod.item.ItemSkeppyBottle;
import its_meow.skeppymod.item.ItemSqueegyBucket;
import its_meow.skeppymod.network.CPacketSetHoodStatus;
import its_meow.skeppymod.network.SPacketSetClientHoodStatus;
import its_meow.skeppymod.tileentity.TileEntityStatue;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

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
        }
    };

    @SidedProxy(clientSide = "its_meow.skeppymod.client.SkeppyModClient", serverSide = "its_meow.skeppymod.DummyProxy")
    public static ISidedProxy proxy;

    public static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    public static int packets = 0;

    @EventHandler
    public static void preinit(FMLPreInitializationEvent event) {
        NETWORK_INSTANCE.registerMessage(CPacketSetHoodStatus.class, CPacketSetHoodStatus.class, packets++, Side.SERVER);
        NETWORK_INSTANCE.registerMessage(SPacketSetClientHoodStatus.class, SPacketSetClientHoodStatus.class, packets++, Side.CLIENT);
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public static void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandF());
    }

    /* Item Instances */
    public static ItemEZFood BAGUETTE = new ItemEZFood("baguette", 8, 1, 128, false);
    public static ItemEZFood MUFFIN = new ItemEZFood("muffin", 3, 3, 32, false);
    public static ItemSqueegyBucket SQUEEGY_BUCKET = new ItemSqueegyBucket();
    public static Item MUFFIN_ON_A_STICK = new Item().setRegistryName(MODID, "muffin_on_a_stick").setTranslationKey("skeppymod.muffin_on_a_stick").setCreativeTab(SKEPPY_TAB).setMaxStackSize(1);
    public static ItemMerchArmor CRAFT_HOODIE_CHEST = new ItemMerchArmor("craft_hoodie_chest", "craft_hoodie", EntityEquipmentSlot.CHEST, "blue_hood");
    public static ItemMerchArmor CARTOON_HOODIE_CHEST = new ItemMerchArmor("cartoon_hoodie_chest", "cartoon_hoodie", EntityEquipmentSlot.CHEST, "blue_hood");
    public static ItemMerchArmorColored SKEPPY_HOODIE_FRONT_CHEST_BLUE = new ItemMerchArmorColored("skeppy_hoodie_front_chest_blue", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 120, 173, 219, "base_hoodie", "skeppy_name_front", "base_hood", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });
    public static ItemMerchArmorColored SKEPPY_HOODIE_FRONT_CHEST_WHITE = new ItemMerchArmorColored("skeppy_hoodie_front_chest_white", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 250, 250, 250, "base_hoodie", "skeppy_name_front", "base_hood", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });
    public static ItemMerchArmorColored SKEPPY_HOODIE_FRONT_CHEST_BLACK = new ItemMerchArmorColored("skeppy_hoodie_front_chest_black", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 30, 30, 30, "base_hoodie", "skeppy_name_front", "base_hood", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });
    public static ItemMerchArmorColored SKEPPY_HOODIE_FRONT_CHEST_PINK = new ItemMerchArmorColored("skeppy_hoodie_front_chest_pink", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 226, 134, 177, "base_hoodie", "skeppy_name_front", "base_hood", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });
    public static ItemMerchArmorColored SKEPPY_HOODIE_FRONT_CHEST_GREY = new ItemMerchArmorColored("skeppy_hoodie_front_chest_grey", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 73, 73, 73, "base_hoodie", "skeppy_name_front", "base_hood", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });

    public static ItemMerchArmorColored SKEPPY_HOODIE_ARMS_CHEST_WHITE = new ItemMerchArmorColored("skeppy_hoodie_arms_chest_white", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 250, 250, 250, "base_hoodie", "skeppy_name_arms_white", "base_hood", () -> () -> {
    });

    public static ItemMerchArmorColored SKEPPY_HOODIE_ARMS_CHEST_BLACK = new ItemMerchArmorColored("skeppy_hoodie_arms_chest_black", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 30, 30, 30, "base_hoodie", "skeppy_name_arms_black", "base_hood", () -> () -> {
    });

    public static ItemJoggers SKEPPY_JOGGERS = new ItemJoggers();

    public static ItemMerchArmorColored SKEPPY_TSHIRT_FRONT_BLUE = new ItemMerchArmorColored("skeppy_tshirt_front_blue", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 120, 173, 219, "base_tshirt", "skeppy_name_front", "empty_texture", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });
    public static ItemMerchArmorColored SKEPPY_TSHIRT_FRONT_WHITE = new ItemMerchArmorColored("skeppy_tshirt_front_white", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 250, 250, 250, "base_tshirt", "skeppy_name_front", "empty_texture", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });
    public static ItemMerchArmorColored SKEPPY_TSHIRT_FRONT_BLACK = new ItemMerchArmorColored("skeppy_tshirt_front_black", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 30, 30, 30, "base_tshirt", "skeppy_name_front", "empty_texture", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });
    public static ItemMerchArmorColored SKEPPY_TSHIRT_FRONT_PINK = new ItemMerchArmorColored("skeppy_tshirt_front_pink", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 226, 134, 177, "base_tshirt", "skeppy_name_front", "empty_texture", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });
    public static ItemMerchArmorColored SKEPPY_TSHIRT_FRONT_GREY = new ItemMerchArmorColored("skeppy_tshirt_front_grey", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 73, 73, 73, "base_tshirt", "skeppy_name_front", "empty_texture", () -> () -> {
        GlStateManager.scale(0.85F, 0.95F, 1.02F);
        GlStateManager.translate(0, 0.03, 0);
    });

    public static ItemMerchArmorColored SKEPPY_HOODIE_DOUBLE_SIDED_WHITE = new ItemMerchArmorColored("skeppy_hoodie_double_sided_white", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 250, 250, 250, "base_hoodie", "skeppy_name_double_sided_black", "base_hood", () -> () -> {
    });

    public static ItemMerchArmorColored SKEPPY_HOODIE_DOUBLE_SIDED_BLACK = new ItemMerchArmorColored("skeppy_hoodie_double_sided_black", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 30, 30, 30, "base_hoodie", "skeppy_name_double_sided_white", "base_hood", () -> () -> {
    });

    public static ItemMerchArmorColored SKEPPY_LONGSLEEVE_WHITE = new ItemMerchArmorColored("skeppy_longsleeve_white", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 250, 250, 250, "base_longsleeve", "skeppy_name_arms_white", "empty_texture", () -> () -> {
    });

    public static ItemMerchArmorColored SKEPPY_LONGSLEEVE_BLACK = new ItemMerchArmorColored("skeppy_longsleeve_black", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 30, 30, 30, "base_longsleeve", "skeppy_name_arms_black", "empty_texture", () -> () -> {
    });

    public static ItemMerchArmorColored SKEPPY_LOGO_HOODIE_BLUE = new ItemMerchArmorColored("skeppy_logo_hoodie_blue", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 120, 173, 219, "base_hoodie", "skeppy_logo_front", "base_hood", () -> () -> {
    });
    public static ItemMerchArmorColored SKEPPY_LOGO_HOODIE_WHITE = new ItemMerchArmorColored("skeppy_logo_hoodie_white", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 250, 250, 250, "base_hoodie", "skeppy_logo_front", "base_hood", () -> () -> {
    });
    public static ItemMerchArmorColored SKEPPY_LOGO_HOODIE_PINK = new ItemMerchArmorColored("skeppy_logo_hoodie_pink", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 226, 134, 177, "base_hoodie", "skeppy_logo_front", "base_hood", () -> () -> {
    });
    public static ItemMerchArmorColored SKEPPY_LOGO_HOODIE_GREY = new ItemMerchArmorColored("skeppy_logo_hoodie_grey", "cartoon_hoodie", EntityEquipmentSlot.CHEST, 73, 73, 73, "base_hoodie", "skeppy_logo_front", "base_hood", () -> () -> {
    });

    public static ItemSkeppyBottle SKEPPY_BOTTLE_EMPTY = (ItemSkeppyBottle) new ItemSkeppyBottle().setCreativeTab(SKEPPY_TAB).setRegistryName(MODID, "skeppy_bottle_empty").setTranslationKey("skeppymod.skeppy_bottle_empty").setMaxStackSize(1);
    public static ItemFullSkeppyBottle SKEPPY_BOTTLE_FULL = (ItemFullSkeppyBottle) new ItemFullSkeppyBottle().setCreativeTab(SKEPPY_TAB).setRegistryName(MODID, "skeppy_bottle_full").setTranslationKey("skeppymod.skeppy_bottle_full").setMaxStackSize(1);

    public static Item CHEESY_FRIES_EMPTY = new Item().setRegistryName(MODID, "cheesy_fries_empty").setTranslationKey("skeppymod.cheesy_fries_empty").setCreativeTab(SKEPPY_TAB).setMaxStackSize(1);

    public static ItemEZFood CHEESY_FRIES = (ItemEZFood) new ItemEZFood("cheesy_fries", 4, 1, 48, false) {
        @Override
        public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
            if(entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                player.inventory.addItemStackToInventory(new ItemStack(SkeppyMod.CHEESY_FRIES_EMPTY));
            }
            return super.onItemUseFinish(stack, worldIn, entityLiving);
        }
    }.setMaxStackSize(1);

    public static ItemEZFood THIN_CRUST_PIZZA = new ItemEZFood("thin_crust_pizza", 8, 2, 32, false);

    public static Item PINECONE = new Item() {
        public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            if(!block.isReplaceable(worldIn, pos)) {
                pos = pos.offset(facing);
            }
            ItemStack itemstack = player.getHeldItem(hand);
            if(!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(Blocks.SAPLING, pos, false, facing, player)) {
                int i = BlockPlanks.EnumType.SPRUCE.ordinal();
                IBlockState iblockstate1 = Blocks.SAPLING.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);
                if(placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
                    iblockstate1 = worldIn.getBlockState(pos);
                    SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
                    worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    itemstack.shrink(1);
                }
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }
        public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
            if(!world.setBlockState(pos, newState, 11))
                return false;
            IBlockState state = world.getBlockState(pos);
            if(state.getBlock() == Blocks.SAPLING) {
                Blocks.SAPLING.onBlockPlacedBy(world, pos, state, player, stack);

                if(player instanceof EntityPlayerMP)
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
            }
            return true;
        }
    }.setRegistryName(MODID, "pinecone").setTranslationKey("skeppymod.pinecone").setCreativeTab(SKEPPY_TAB);

    public static Item DILL_PICKLE_CHIPS_EMPTY = new Item().setRegistryName(MODID, "dill_pickle_chips_empty").setTranslationKey("skeppymod.dill_pickle_chips_empty").setCreativeTab(SKEPPY_TAB).setMaxStackSize(1);

    public static ItemEZFood DILL_PICKLE_CHIPS = (ItemEZFood) new ItemEZFood("dill_pickle_chips", 3, 0, 16, false) {
        @Override
        public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
            if(entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                player.inventory.addItemStackToInventory(new ItemStack(SkeppyMod.DILL_PICKLE_CHIPS_EMPTY));
            }
            return super.onItemUseFinish(stack, worldIn, entityLiving);
        }
    }.setMaxStackSize(1);

    public static Item JAPANESE_SYMBOL = new Item() {
        @Override
        public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            tooltip.add("Right click for fun :)");
        }
    }.setRegistryName(MODID, "japanese_symbol").setTranslationKey("skeppymod.japanese_symbol").setCreativeTab(SKEPPY_TAB);

    /* Block Instances */
    public static Block14 BLOCK_14 = new Block14();
    public static BlockStatue SKEPPY_STATUE = new BlockStatue("skeppy");
    public static BlockStatue BBH_STATUE = new BlockStatue("badboyhalo");
    public static BlockStatue A6D_STATUE = new BlockStatue("a6d");

    /* Sound Events */
    public static final SoundEvent JAPANESE_SYMBOL_SOUND = new SoundEvent(new ResourceLocation(MODID, "japanese_symbol"));
    public static final SoundEvent AND_I_OOP_SOUND = new SoundEvent(new ResourceLocation(MODID, "andioop"));

    /* Misc */
    public static final HashMap<UUID, Boolean> HOODS = new HashMap<UUID, Boolean>();

    /* Registry */

    private static int modEntities = 0;

    @SubscribeEvent
    public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(JAPANESE_SYMBOL_SOUND.setRegistryName(new ResourceLocation(MODID, "japanese_symbol")));
        event.getRegistry().register(AND_I_OOP_SOUND.setRegistryName(new ResourceLocation(MODID, "andioop")));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BLOCK_14, SKEPPY_STATUE, BBH_STATUE, A6D_STATUE);
        GameRegistry.registerTileEntity(TileEntityStatue.class, new ResourceLocation(MODID, "statue_te"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemBlock(BLOCK_14).setRegistryName(BLOCK_14.getRegistryName()),
        new ItemBlockStatue(SKEPPY_STATUE), new ItemBlockStatue(BBH_STATUE), new ItemBlockStatue(A6D_STATUE),
        BAGUETTE, MUFFIN, SQUEEGY_BUCKET, MUFFIN_ON_A_STICK, CRAFT_HOODIE_CHEST, CARTOON_HOODIE_CHEST, SKEPPY_HOODIE_FRONT_CHEST_BLUE, SKEPPY_HOODIE_FRONT_CHEST_WHITE, SKEPPY_HOODIE_FRONT_CHEST_PINK, SKEPPY_HOODIE_FRONT_CHEST_BLACK, SKEPPY_HOODIE_FRONT_CHEST_GREY,
        SKEPPY_HOODIE_ARMS_CHEST_WHITE, SKEPPY_HOODIE_ARMS_CHEST_BLACK, SKEPPY_JOGGERS,
        SKEPPY_TSHIRT_FRONT_BLUE, SKEPPY_TSHIRT_FRONT_WHITE, SKEPPY_TSHIRT_FRONT_PINK, SKEPPY_TSHIRT_FRONT_BLACK, SKEPPY_TSHIRT_FRONT_GREY, SKEPPY_HOODIE_DOUBLE_SIDED_WHITE, SKEPPY_HOODIE_DOUBLE_SIDED_BLACK,
        SKEPPY_LONGSLEEVE_WHITE, SKEPPY_LONGSLEEVE_BLACK,
        SKEPPY_LOGO_HOODIE_BLUE, SKEPPY_LOGO_HOODIE_PINK, SKEPPY_LOGO_HOODIE_WHITE, SKEPPY_LOGO_HOODIE_GREY, SKEPPY_BOTTLE_EMPTY, SKEPPY_BOTTLE_FULL,
        CHEESY_FRIES, CHEESY_FRIES_EMPTY, THIN_CRUST_PIZZA, PINECONE, DILL_PICKLE_CHIPS, DILL_PICKLE_CHIPS_EMPTY, JAPANESE_SYMBOL);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityEntryBuilder.<EntityMrSqueegy>create().entity(EntityMrSqueegy.class).id(new ResourceLocation(MODID, "mrsqueegy"), modEntities++).name("skeppymod.mrsqueegy").tracker(128, 1, true).build());
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

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if(event.getState().getBlock() == Blocks.LEAVES && event.getState() == event.getState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)) {
            if(Math.random() < 0.1) {
                event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(PINECONE)));
            }
        }
    }

    @SubscribeEvent
    public static void onItemDrop(ItemTossEvent event) {
        if(event.getEntityItem().getItem().getItem() == SkeppyMod.SKEPPY_BOTTLE_FULL || event.getEntityItem().getItem().getItem() == SkeppyMod.SKEPPY_BOTTLE_EMPTY) {
            event.getPlayer().sendMessage(new TextComponentString("I DROPPED MY HYDROFLASK!"));
            event.getPlayer().world.playSound(null, event.getPlayer().getPosition(), AND_I_OOP_SOUND, SoundCategory.PLAYERS, 1, 1);
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent event) {
        if(!event.getItemStack().isEmpty() && event.getItemStack().getItem() == JAPANESE_SYMBOL) {
            event.getWorld().playSound(null, event.getEntityPlayer().getPosition(), JAPANESE_SYMBOL_SOUND, SoundCategory.PLAYERS, 1, 1);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(event.player instanceof EntityPlayerMP) {
            for(EntityPlayer player : DimensionManager.getWorld(event.toDim).playerEntities) {
                if(player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) player;
                    boolean isOn = HOODS.getOrDefault(playerMP.getGameProfile().getId(), true);
                    boolean isOn2 = HOODS.getOrDefault(event.player.getGameProfile().getId(), true);
                    NETWORK_INSTANCE.sendTo(new SPacketSetClientHoodStatus(playerMP.getGameProfile().getId(), isOn), (EntityPlayerMP) event.player);
                    NETWORK_INSTANCE.sendTo(new SPacketSetClientHoodStatus(event.player.getGameProfile().getId(), isOn2), (EntityPlayerMP) playerMP);
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.player instanceof EntityPlayerMP) {
            for(EntityPlayer player : event.player.world.playerEntities) {
                if(player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) player;
                    boolean isOn = HOODS.getOrDefault(playerMP.getGameProfile().getId(), true);
                    boolean isOn2 = HOODS.getOrDefault(event.player.getGameProfile().getId(), true);
                    NETWORK_INSTANCE.sendTo(new SPacketSetClientHoodStatus(playerMP.getGameProfile().getId(), isOn), (EntityPlayerMP) event.player);
                    NETWORK_INSTANCE.sendTo(new SPacketSetClientHoodStatus(event.player.getGameProfile().getId(), isOn2), (EntityPlayerMP) playerMP);
                }
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