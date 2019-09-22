package its_meow.skeppymod.item;

import java.util.function.Supplier;

import its_meow.skeppymod.client.model.ModelBipedArmorColoredLayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMerchArmorColored extends ItemMerchArmor {

    public final int red;
    public final int green;
    public final int blue;
    public final String baseTexture;
    public final String topTexture;
    public final Supplier<Runnable> gl;

    public ItemMerchArmorColored(String name, EntityEquipmentSlot slot, int red, int green, int blue, String baseTex, String topTex, Supplier<Runnable> gl) {
        this(name, getOrCreateMaterial(name), slot, red, green, blue, baseTex, topTex, gl);
    }

    public ItemMerchArmorColored(String name, String materialName, EntityEquipmentSlot slot, int red, int green, int blue, String baseTex, String topTex, Supplier<Runnable> gl) {
        this(name, getOrCreateMaterial(materialName), slot, red, green, blue, baseTex, topTex, gl);
    }

    public ItemMerchArmorColored(String name, ArmorMaterial mat, EntityEquipmentSlot slot, int red, int green, int blue, String baseTex, String topTex, Supplier<Runnable> gl) {
        super(name, mat, slot);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.baseTexture = baseTex;
        this.topTexture = topTex;
        this.gl = gl;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if(entityLiving instanceof AbstractClientPlayer) {
            return new ModelBipedArmorColoredLayer((AbstractClientPlayer) entityLiving, this);
        }
        return null;
    }

}
