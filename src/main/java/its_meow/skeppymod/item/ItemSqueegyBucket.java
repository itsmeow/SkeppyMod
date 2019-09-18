package its_meow.skeppymod.item;

import java.util.List;

import javax.annotation.Nullable;

import its_meow.skeppymod.SkeppyMod;
import its_meow.skeppymod.entity.EntityMrSqueegy;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemSqueegyBucket extends Item {

    public ItemSqueegyBucket() {
        this.setTranslationKey("skeppymod.squeegy_bucket");
        this.setRegistryName(SkeppyMod.MODID, "squeegy_bucket");
        this.setCreativeTab(SkeppyMod.SKEPPY_TAB);
        this.setMaxStackSize(1);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);

        if(worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if(!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
            return EnumActionResult.FAIL;
        } else {

            BlockPos blockpos = pos.offset(facing);
            double d0 = this.getYOffset(worldIn, blockpos);
            Entity entity = spawnCreature(worldIn, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D);

            if(entity != null) {

                if(!player.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                    player.addItemStackToInventory(new ItemStack(Items.BUCKET));
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }

    protected double getYOffset(World p_190909_1_, BlockPos p_190909_2_) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(p_190909_2_)).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = p_190909_1_.getCollisionBoxes((Entity) null, axisalignedbb);

        if(list.isEmpty()) {
            return 0.0D;
        } else {
            double d0 = axisalignedbb.minY;

            for(AxisAlignedBB axisalignedbb1 : list) {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }

            return d0 - (double) p_190909_2_.getY();
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if(worldIn.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        } else {
            RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

            if(raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if(!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid)) {
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
                } else if(worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemstack)) {
                    Entity entity = spawnCreature(worldIn, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D, (double) blockpos.getZ() + 0.5D);

                    if(entity == null) {
                        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
                    } else {
                        if(!playerIn.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                            playerIn.addItemStackToInventory(new ItemStack(Items.BUCKET));
                        }

                        playerIn.addStat(StatList.getObjectUseStats(this));
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                    }
                } else {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
            } else {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
            }
        }
    }

    @Nullable
    public static Entity spawnCreature(World worldIn, double x, double y, double z) {
        EntityLiving entity = new EntityMrSqueegy(worldIn);
        entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        entity.rotationYawHead = entity.rotationYaw;
        entity.renderYawOffset = entity.rotationYaw;
        if(net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(entity, worldIn, (float) x, (float) y, (float) z, null))
            return null;
        entity.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData) null);
        worldIn.spawnEntity(entity);
        entity.playLivingSound();
        return entity;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Mr. Squeegy in a bucket... skeppy in a.. wait, don't try that! (it won't work i swear)");
        tooltip.add("Place Mr Squeegy down and give him a right click with your hand- see what happens. Also consider grabbing a muffin on a stick to... persuade him");
        tooltip.add("Also, DON'T PUNCH MR SQUEEGY. PUNCHING WILL RESULT IN HIS ANGER. YOU DO NOT WANT THAT.");
    }

}