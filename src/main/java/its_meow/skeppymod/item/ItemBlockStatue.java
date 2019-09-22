package its_meow.skeppymod.item;

import its_meow.skeppymod.block.BlockStatue;
import net.minecraft.item.ItemBlock;

public class ItemBlockStatue extends ItemBlock {

    public final String STATUE_USER;

    public ItemBlockStatue(BlockStatue block) {
        super(block);
        this.setRegistryName(block.getRegistryName());
        this.STATUE_USER = block.STATUE_USER;
        this.setMaxStackSize(1);
    }

}
