package its_meow.skeppymod.client.model;

import its_meow.skeppymod.client.SkeppyModClient;
import its_meow.skeppymod.item.ItemMerchArmorColored;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelBipedArmorColoredLayer extends ModelBipedArmorLayer {

    public final ItemMerchArmorColored armor;
    public final ResourceLocation baseTex;
    public final ResourceLocation topTex;

    public ModelBipedArmorColoredLayer(AbstractClientPlayer player, ItemMerchArmorColored armor) {
        super(player, armor);
        this.armor = armor;
        this.baseTex = new ResourceLocation("skeppymod", "textures/models/armor/" + armor.baseTexture + "_" + player.getSkinType() + ".png");
        this.topTex = new ResourceLocation("skeppymod", "textures/models/armor/" + armor.topTexture + "_" + player.getSkinType() + ".png");
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
                            GlStateManager.color(((float) armor.red) / 255F, ((float) armor.green) / 255F, ((float) armor.blue) / 255F);
                        } else {
                            Minecraft.getMinecraft().getTextureManager().bindTexture(topTex);
                            armor.gl.get().run();
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
                            if(SkeppyModClient.hood_up && Minecraft.getMinecraft().player == player) {
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