package its_meow.skeppymod.item;

import its_meow.skeppymod.SkeppyMod;
import net.minecraft.item.Item;

public class ItemSimpleNamed extends Item {

    public ItemSimpleNamed(String name) {
        this.setRegistryName(SkeppyMod.MODID, name);
        this.setTranslationKey(SkeppyMod.MODID + "." + name);
        this.setCreativeTab(SkeppyMod.SKEPPY_TAB);
    }
    
}
