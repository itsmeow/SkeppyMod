package its_meow.skeppymod.tileentity;

import its_meow.skeppymod.block.BlockStatue;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileEntityStatue extends TileEntity {
    
    public TileEntityStatue() {}
    
    public TileEntityStatue(World world) {
        this.world = world;
    }

    @Override
    public ITextComponent getDisplayName() {
        if(this.world != null && this.world.isBlockLoaded(this.getPos()) && this.world.getBlockState(this.getPos()).getBlock() instanceof BlockStatue) {
            return new TextComponentString(((BlockStatue) this.world.getBlockState(this.getPos()).getBlock()).STATUE_USER);
        }
        return super.getDisplayName();
    }

}
