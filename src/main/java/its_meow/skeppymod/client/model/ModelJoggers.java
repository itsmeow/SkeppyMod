package its_meow.skeppymod.client.model;

import its_meow.skeppymod.item.ItemMerchArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelJoggers extends ModelPlayer {

    private final ResourceLocation texture;

    public ModelJoggers(AbstractClientPlayer player, ItemMerchArmor armor) {
        super(0F, player.getSkinType().equals("slim"));
        this.texture = new ResourceLocation("skeppymod:textures/models/armor/" + armor.getArmorMaterial().getName().replaceAll("skeppymod:", "") + ".png");
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(entityIn instanceof AbstractClientPlayer) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
            AbstractClientPlayer player = (AbstractClientPlayer) entityIn;
            if(!player.isUser() || Minecraft.getMinecraft().getRenderManager().renderViewEntity == player) {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
                GlStateManager.pushMatrix();
                {
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
                    this.bipedBody.render(scale);
                    GlStateManager.pushMatrix();
                    {
                        GlStateManager.translate(0F, 0.15F, 0F);
                        GlStateManager.scale(1.01F, 1F, 1.01F);
                        this.bipedBodyWear.render(scale);
                    }
                    GlStateManager.popMatrix();
                    this.bipedHeadwear.render(scale);
                }
                GlStateManager.popMatrix();
                GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            }
        }
    }

}
