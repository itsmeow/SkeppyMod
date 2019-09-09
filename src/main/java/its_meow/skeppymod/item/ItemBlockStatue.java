package its_meow.skeppymod.item;

import java.util.function.Supplier;

import its_meow.skeppymod.block.BlockStatue;
import its_meow.skeppymod.client.renderer.tileentity.TileEntityItemRenderStatue;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ItemBlockStatue extends ItemBlock {

    public final String STATUE_USER;

    public ItemBlockStatue(BlockStatue block) {
        super(block);
        this.setRegistryName(block.getRegistryName());
        this.STATUE_USER = block.STATUE_USER;
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            Supplier<Runnable> runMe = () -> () -> {
                this.setTileEntityItemStackRenderer(new TileEntityItemRenderStatue());
            };
            runMe.get().run();
        }
        this.setMaxStackSize(1);
    }

}
