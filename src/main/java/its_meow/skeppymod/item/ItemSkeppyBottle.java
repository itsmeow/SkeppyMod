package its_meow.skeppymod.item;

import its_meow.skeppymod.SkeppyMod;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemSkeppyBottle extends ItemGlassBottle {

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if(raytraceresult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        } else {
            if(raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if(!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
                }

                if(worldIn.getBlockState(blockpos).getMaterial() == Material.WATER) {
                    worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(SkeppyMod.SKEPPY_BOTTLE_FULL));
                }
            }

            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
    }

}
