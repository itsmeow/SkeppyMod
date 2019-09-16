package its_meow.skeppymod.client.renderer.entity;

import its_meow.skeppymod.SkeppyMod;
import its_meow.skeppymod.client.model.ModelSalmon;
import its_meow.skeppymod.entity.EntityMrSqueegy;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMrSqueegy extends RenderLiving<EntityMrSqueegy> {
    
    public static final ResourceLocation TEXTURE = new ResourceLocation(SkeppyMod.MODID, "textures/entities/salmon.png");

    public RenderMrSqueegy(RenderManager rendermanager) {
        super(rendermanager, new ModelSalmon(), 0.4F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMrSqueegy entity) {
        return TEXTURE;
    }

}
