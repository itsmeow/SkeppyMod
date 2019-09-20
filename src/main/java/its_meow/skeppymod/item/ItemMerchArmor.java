package its_meow.skeppymod.item;

import java.util.HashMap;
import java.util.Map;

import its_meow.skeppymod.SkeppyMod;
import its_meow.skeppymod.client.model.ModelBipedArmorLayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMerchArmor extends ItemArmor {

    protected static Map<String, ArmorMaterial> mats = new HashMap<String, ArmorMaterial>();

    public ItemMerchArmor(String name, EntityEquipmentSlot slot) {
        this(name, getOrCreateMaterial(name), slot);
    }

    public ItemMerchArmor(String name, String materialName, EntityEquipmentSlot slot) {
        this(name, getOrCreateMaterial(materialName), slot);
    }

    public ItemMerchArmor(String name, ArmorMaterial mat, EntityEquipmentSlot slot) {
        super(mat, -1, slot);
        this.setRegistryName(SkeppyMod.MODID, name);
        this.setTranslationKey("skeppymod." + name);
        this.setCreativeTab(SkeppyMod.SKEPPY_TAB);
        this.setMaxStackSize(1);
        this.canRepair = true;
    }

    public static ArmorMaterial getOrCreateMaterial(String name) {
        ArmorMaterial map = mats.get(name);
        if(map == null) {
            map = EnumHelper.addArmorMaterial(name, "skeppymod:" + name, 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
            map.setRepairItem(new ItemStack(Items.LEATHER));
            mats.put(name, map);
        }
        return map;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if(entityLiving instanceof AbstractClientPlayer) {
            return new ModelBipedArmorLayer((AbstractClientPlayer) entityLiving, this);
        }
        return null;
    }

}
