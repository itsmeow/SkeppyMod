package its_meow.skeppymod.item;

import java.util.function.Supplier;

import its_meow.skeppymod.client.SkeppyModClient;
import its_meow.skeppymod.client.model.ModelBipedArmorColoredLayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMerchArmorColored extends ItemMerchArmor {

    public final int red;
    public final int green;
    public final int blue;
    public final Supplier<Runnable> gl;
    public final ResourceLocation baseTex_def;
    public final ResourceLocation topTex_def;
    public final ResourceLocation baseTex_slim;
    public final ResourceLocation topTex_slim;

    public ItemMerchArmorColored(String name, EntityEquipmentSlot slot, int red, int green, int blue, String baseTex, String topTex, String hoodTex, Supplier<Runnable> gl) {
        this(name, getOrCreateMaterial(name), slot, red, green, blue, baseTex, topTex, hoodTex, gl);
    }

    public ItemMerchArmorColored(String name, String materialName, EntityEquipmentSlot slot, int red, int green, int blue, String baseTex, String topTex, String hoodTex, Supplier<Runnable> gl) {
        this(name, getOrCreateMaterial(materialName), slot, red, green, blue, baseTex, topTex, hoodTex, gl);
    }

    public ItemMerchArmorColored(String name, ArmorMaterial mat, EntityEquipmentSlot slot, int red, int green, int blue, String baseTex, String topTex, String hoodTex, Supplier<Runnable> gl) {
        super(name, mat, slot, hoodTex);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.gl = gl;
        this.baseTex_def = new ResourceLocation("skeppymod", "textures/models/armor/" + baseTex + "_" + "default" + ".png");
        this.topTex_def = new ResourceLocation("skeppymod", "textures/models/armor/" + topTex + "_" + "default" + ".png");
        this.baseTex_slim = new ResourceLocation("skeppymod", "textures/models/armor/" + baseTex + "_" + "slim" + ".png");
        this.topTex_slim = new ResourceLocation("skeppymod", "textures/models/armor/" + topTex + "_" + "slim" + ".png");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if(entityLiving instanceof AbstractClientPlayer) {
            boolean isSlim = ((AbstractClientPlayer) entityLiving).getSkinType().equals("slim");
            ModelBipedArmorColoredLayer model = isSlim ? SkeppyModClient.COLOR_ARMOR_SLIM : SkeppyModClient.COLOR_ARMOR_DEFAULT;
            model.hoodTex = this.hoodTex;
            model.baseTex = isSlim ? baseTex_slim : baseTex_def;
            model.topTex = isSlim ? topTex_slim : topTex_def;
            model.red = red;
            model.green = green;
            model.blue = blue;
            model.gl = gl;
            return model;
        }
        return null;
    }

}
