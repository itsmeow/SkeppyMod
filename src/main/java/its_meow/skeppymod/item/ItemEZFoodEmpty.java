package its_meow.skeppymod.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEZFoodEmpty extends ItemEZFood {
    
    public final Item empty;
    
    public ItemEZFoodEmpty(String name, int foodToFill, float saturationMultiplier, int eatLength, boolean isMeat, Item empty) {
        super(name, foodToFill, saturationMultiplier, eatLength, isMeat);
        this.empty = empty;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if(entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            player.inventory.addItemStackToInventory(new ItemStack(empty));
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

}
