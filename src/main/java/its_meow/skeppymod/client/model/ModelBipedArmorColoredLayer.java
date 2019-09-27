package its_meow.skeppymod.client.model;

import java.util.function.Supplier;

import its_meow.skeppymod.SkeppyMod;
import its_meow.skeppymod.client.SkeppyModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelBipedArmorColoredLayer extends ModelBipedArmorLayer {

    public ResourceLocation baseTex;
    public ResourceLocation topTex;
    public int red;
    public int green;
    public int blue;
    public Supplier<Runnable> gl;

    public ModelBipedArmorColoredLayer(boolean slim) {
        super(slim);
        
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(entityIn instanceof AbstractClientPlayer) {

            AbstractClientPlayer player = (AbstractClientPlayer) entityIn;
            if(!player.isUser() || Minecraft.getMinecraft().getRenderManager().renderViewEntity == player) {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
                for(int i = 0; i < 2; i++) {
                    GlStateManager.pushMatrix();
                    {
                        if(i == 0) {
                            Minecraft.getMinecraft().getTextureManager().bindTexture(baseTex);
                            GlStateManager.color(((float) red) / 255F, ((float) green) / 255F, ((float) blue) / 255F);
                        } else {
                            Minecraft.getMinecraft().getTextureManager().bindTexture(topTex);
                            gl.get().run();
                            GlStateManager.color(1F, 1F, 1F);
                        }
                        if(entityIn.isSneaking()) {
                            GlStateManager.translate(0.0F, 0.2F, 0.0F);
                        }
                        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
                        this.bipedHeadwear.showModel = true;
                        this.bipedBodyWear.showModel = true;
                        this.bipedLeftLegwear.showModel = true;
                        this.bipedRightLegwear.showModel = true;
                        this.bipedLeftArmwear.showModel = true;
                        this.bipedRightArmwear.showModel = true;
                        this.bipedLeftLegwear.render(scale);
                        this.bipedRightLegwear.render(scale);
                        this.bipedLeftArmwear.render(scale);
                        this.bipedRightArmwear.render(scale);
                        this.bipedBodyWear.render(scale);
                        if(i == 0) {
                            if((SkeppyModClient.hood_up && Minecraft.getMinecraft().player == player) || (Minecraft.getMinecraft().player != player && SkeppyMod.HOODS.getOrDefault(player.getGameProfile().getId(), true))) {
                                this.bipedHeadwear.render(scale);
                            } else {
                                Minecraft.getMinecraft().getTextureManager().bindTexture(hoodTex);
                                this.bipedBodyWear.render(scale);
                            }
                        } else {
                            this.bipedHeadwear.render(scale);
                        }
                    }
                    GlStateManager.popMatrix();
                }
                GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            }
        }
    }

}