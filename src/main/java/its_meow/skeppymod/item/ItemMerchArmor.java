package its_meow.skeppymod.item;

import java.util.HashMap;
import java.util.Map;

import its_meow.skeppymod.SkeppyMod;
import its_meow.skeppymod.client.model.ModelBipedArmorLayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = SkeppyMod.MODID)
public class ItemMerchArmor extends ItemArmor {

    protected static Map<String, ArmorMaterial> mats = new HashMap<String, ArmorMaterial>();
    public final String hoodTex;

    public ItemMerchArmor(String name, EntityEquipmentSlot slot, String hoodTex) {
        this(name, getOrCreateMaterial(name), slot, hoodTex);
    }

    public ItemMerchArmor(String name, String materialName, EntityEquipmentSlot slot, String hoodTex) {
        this(name, getOrCreateMaterial(materialName), slot, hoodTex);
    }

    public ItemMerchArmor(String name, ArmorMaterial mat, EntityEquipmentSlot slot, String hoodTex) {
        super(mat, -1, slot);
        this.setRegistryName(SkeppyMod.MODID, name);
        this.setTranslationKey("skeppymod." + name);
        this.setCreativeTab(SkeppyMod.SKEPPY_TAB);
        this.setMaxStackSize(1);
        this.canRepair = true;
        this.hoodTex = hoodTex;
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

    @SubscribeEvent
    public static void onEntityInteract(EntityInteractSpecific event) {
        // stop putting on armor stands pls
        if(event.getTarget() instanceof EntityArmorStand) {
            if(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemMerchArmor || event.getEntityPlayer().getHeldItemOffhand().getItem() instanceof ItemMerchArmor) {
                event.setCanceled(true);
            }
        }
    }

}
