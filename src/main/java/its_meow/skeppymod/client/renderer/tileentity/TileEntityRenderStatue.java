package its_meow.skeppymod.client.renderer.tileentity;

import its_meow.skeppymod.SkeppyMod;
import its_meow.skeppymod.block.BlockStatue;
import its_meow.skeppymod.client.model.ModelStatue;
import its_meow.skeppymod.tileentity.TileEntityStatue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileEntityRenderStatue extends TileEntitySpecialRenderer<TileEntityStatue> {

    public static final ModelStatue BIPED = new ModelStatue();
    public static final ResourceLocation SKEPPY_SKIN = new ResourceLocation(SkeppyMod.MODID, "textures/skins/skeppy.png");
    public static final ResourceLocation BBH_SKIN = new ResourceLocation(SkeppyMod.MODID, "textures/skins/badboyhalo.png");
    public static final ResourceLocation A6D_SKIN = new ResourceLocation(SkeppyMod.MODID, "textures/skins/a6d.png");

    @Override
    public void render(TileEntityStatue te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
            int rotation = 0;
            if(te.getWorld() != null && te.getWorld().isBlockLoaded(te.getPos()) && te.getWorld().getBlockState(te.getPos()).getBlock() instanceof BlockStatue) {
                rotation = (int) te.getWorld().getBlockState(te.getPos()).getValue(BlockStatue.FACING).getHorizontalAngle();
                if(rotation == 0 || rotation == 180) rotation = rotation == 180 ? 0 : 180;
                String name = ((BlockStatue)te.getWorld().getBlockState(te.getPos()).getBlock()).STATUE_USER;
                this.bindTexture(name.equals("skeppy") ? SKEPPY_SKIN : (name.equals("badboyhalo") ? BBH_SKIN : A6D_SKIN));
            }
            BIPED.render(null, rotation, 0, 0, 0, 0, 0.0625F);
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(0, 1, 0);
            super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        }
        GlStateManager.popMatrix();
    }



}
