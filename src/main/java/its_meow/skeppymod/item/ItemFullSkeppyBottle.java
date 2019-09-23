package its_meow.skeppymod.item;

import its_meow.skeppymod.SkeppyMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFullSkeppyBottle extends Item {

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;

        if(entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        if(entityplayer instanceof EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityplayer, stack);
        }

        if(entityplayer != null) {
            entityplayer.addStat(StatList.getObjectUseStats(this));
        }

        if(entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
            if(stack.isEmpty()) {
                return new ItemStack(SkeppyMod.SKEPPY_BOTTLE_EMPTY);
            }

            if(entityplayer != null) {
                entityplayer.inventory.addItemStackToInventory(new ItemStack(SkeppyMod.SKEPPY_BOTTLE_EMPTY));
            }
        }

        return stack;
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

}
