package its_meow.skeppymod.item;

import its_meow.skeppymod.SkeppyMod;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemEZFood extends ItemFood {

    public final int itemUseDuration;

    public ItemEZFood(String name, int foodToFill, float saturationMultiplier, int eatLength, boolean isMeat) {
        super(foodToFill, saturationMultiplier, isMeat);
        this.setRegistryName(name);
        this.setTranslationKey(SkeppyMod.MODID + "." + name);
        this.itemUseDuration = eatLength;
        this.setCreativeTab(SkeppyMod.SKEPPY_TAB);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return this.itemUseDuration;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.EAT;
    }

}