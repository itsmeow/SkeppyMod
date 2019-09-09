package its_meow.skeppymod.client.renderer.tileentity;

import its_meow.skeppymod.SkeppyMod;
import its_meow.skeppymod.client.model.ModelStatue;
import its_meow.skeppymod.item.ItemBlockStatue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TileEntityItemRenderStatue extends TileEntityItemStackRenderer {
    public static final ModelStatue BIPED = new ModelStatue();
    public static final ResourceLocation SKEPPY_SKIN = new ResourceLocation(SkeppyMod.MODID, "textures/skins/skeppy.png");
    public static final ResourceLocation BBH_SKIN = new ResourceLocation(SkeppyMod.MODID, "textures/skins/badboyhalo.png");
    public static final ResourceLocation A6D_SKIN = new ResourceLocation(SkeppyMod.MODID, "textures/skins/a6d.png");
    public static TransformType transform = TransformType.NONE;

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        if(!(itemStackIn.getItem() instanceof ItemBlockStatue)) return;
        GlStateManager.pushMatrix();
        {
            String name = ((ItemBlockStatue)itemStackIn.getItem()).STATUE_USER;
            Minecraft.getMinecraft().getTextureManager().bindTexture(name.equals("skeppy") ? SKEPPY_SKIN : (name.equals("badboyhalo") ? BBH_SKIN : A6D_SKIN));
            if(transform == TransformType.GUI) {
                GlStateManager.enableLighting();
                GlStateManager.scale(-0.98F, 0.98F, -0.98F);
                GlStateManager.translate(-0.55F, 0.5F, -0.5F);
                GlStateManager.rotate(90, -1, 0, 0);
                GlStateManager.rotate(90, 4, 0, 1);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.rotate(5, -1, -4, 0);
            } else if(transform == TransformType.GROUND) {
                GlStateManager.scale(0.25F, 0.25F, 0.25F);
                GlStateManager.translate(1.5F, 1.5F, 1.5F);
            } else if(transform == TransformType.FIXED) {
                GlStateManager.translate(0.5F, 0.8F, 0.5F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                GlStateManager.rotate(180F, 0F, 1F, 0F);
            } else if(transform == TransformType.THIRD_PERSON_RIGHT_HAND) {
                GlStateManager.rotate(1F, 75F, 45F, 0F);
                GlStateManager.translate(0.3F, 0.5F, 0.35F);
                GlStateManager.scale(0.375F, 0.375F, 0.375F);
            } else if(transform == TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.translate(0.5F, 0.7F, 0.3F);
                GlStateManager.rotate(90F, 0F, 0F, 1F);
                GlStateManager.rotate(90F, 0F, 0F, -1F);
                GlStateManager.scale(0.35F, 0.35F, 0.35F);
            } else if(transform == TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.translate(0.5F, 0.7F, 0.3F);
                GlStateManager.rotate(90F, 0F, 0F, 1F);
                GlStateManager.rotate(90F, 0F, 0F, -1F);
                GlStateManager.scale(0.35F, 0.35F, 0.35F);
            } else if(transform == TransformType.THIRD_PERSON_LEFT_HAND) {
                GlStateManager.rotate(1F, 75F, 45F, 0F);
                GlStateManager.translate(0.3F, 0.5F, 0.35F);
                GlStateManager.scale(0.375F, 0.375F, 0.375F);
            }
            
            BIPED.render(null, 0, 0, 0, 0, 0, 0.0625F);
        }
        GlStateManager.popMatrix();
    }



}
