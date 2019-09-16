package its_meow.skeppymod.client.model;

import its_meow.skeppymod.entity.EntityMrSqueegy;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class ModelSalmon extends ModelBase {
    private final ModelRenderer field_203761_a;
    private final ModelRenderer field_203762_b;
    private final ModelRenderer field_203763_c;
    private final ModelRenderer field_203764_d;
    private final ModelRenderer field_203765_e;
    private final ModelRenderer field_203766_f;
    private final ModelRenderer field_203767_g;
    private final ModelRenderer field_203768_h;

    public ModelSalmon() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.field_203761_a = new ModelRenderer(this, 0, 0);
        this.field_203761_a.addBox(-1.5F, -2.5F, 0.0F, 3, 5, 8);
        this.field_203761_a.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.field_203762_b = new ModelRenderer(this, 0, 13);
        this.field_203762_b.addBox(-1.5F, -2.5F, 0.0F, 3, 5, 8);
        this.field_203762_b.setRotationPoint(0.0F, 20.0F, 8.0F);
        this.field_203763_c = new ModelRenderer(this, 22, 0);
        this.field_203763_c.addBox(-1.0F, -2.0F, -3.0F, 2, 4, 3);
        this.field_203763_c.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.field_203766_f = new ModelRenderer(this, 20, 10);
        this.field_203766_f.addBox(0.0F, -2.5F, 0.0F, 0, 5, 6);
        this.field_203766_f.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.field_203762_b.addChild(this.field_203766_f);
        this.field_203764_d = new ModelRenderer(this, 2, 1);
        this.field_203764_d.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3);
        this.field_203764_d.setRotationPoint(0.0F, -4.5F, 5.0F);
        this.field_203761_a.addChild(this.field_203764_d);
        this.field_203765_e = new ModelRenderer(this, 0, 2);
        this.field_203765_e.addBox(0.0F, 0.0F, 0.0F, 0, 2, 4);
        this.field_203765_e.setRotationPoint(0.0F, -4.5F, -1.0F);
        this.field_203762_b.addChild(this.field_203765_e);
        this.field_203767_g = new ModelRenderer(this, -4, 0);
        this.field_203767_g.addBox(-2.0F, 0.0F, 0.0F, 2, 0, 2);
        this.field_203767_g.setRotationPoint(-1.5F, 21.5F, 0.0F);
        this.field_203767_g.rotateAngleZ = -0.7853982F;
        this.field_203768_h = new ModelRenderer(this, 0, 0);
        this.field_203768_h.addBox(0.0F, 0.0F, 0.0F, 2, 0, 2);
        this.field_203768_h.setRotationPoint(1.5F, 21.5F, 0.0F);
        this.field_203768_h.rotateAngleZ = 0.7853982F;
    }

    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
    float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.field_203761_a.render(p_78088_7_);
        this.field_203762_b.render(p_78088_7_);
        this.field_203763_c.render(p_78088_7_);
        this.field_203767_g.render(p_78088_7_);
        this.field_203768_h.render(p_78088_7_);
        if(p_78088_1_ instanceof EntityMrSqueegy) {
            EntityMrSqueegy sq = (EntityMrSqueegy) p_78088_1_;
            if(sq.getAttackTarget() != null) {
                double x = sq.posX;
                double y = sq.posY;
                double z = sq.posZ;
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                GlStateManager.pushMatrix();
                {
                    GlStateManager.disableTexture2D();
                    GlStateManager.disableLighting();
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                    GlStateManager.rotate(0, 0, 0, 0);
                    GlStateManager.translate(-x, -y, -z);
                    BlockPos diff = sq.getPosition().subtract(sq.getAttackTarget().getPosition());
                    bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    bufferbuilder.pos(x, y, z).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
                    bufferbuilder.pos(x + diff.getX(), y + diff.getY() + 1, z + diff.getZ()).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
                    bufferbuilder.pos(x + diff.getX(), y + diff.getY(), z + diff.getZ()).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
                    tessellator.draw();
                    GlStateManager.disableBlend();
                    GlStateManager.enableLighting();
                    GlStateManager.enableTexture2D();
                }
                GlStateManager.popMatrix();
            }
        }

    }

    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
    float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        float lvt_8_1_ = 1.0F;
        float lvt_9_1_ = 1.0F;
        this.field_203762_b.rotateAngleY = -lvt_8_1_ * 0.25F * MathHelper.sin(lvt_9_1_ * 0.6F * p_78087_3_);
    }
}