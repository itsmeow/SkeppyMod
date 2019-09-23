package its_meow.skeppymod.item;

import its_meow.skeppymod.client.model.ModelJoggers;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemJoggers extends ItemMerchArmor {

    public ItemJoggers() {
        super("skeppy_joggers", "skeppy_joggers", EntityEquipmentSlot.LEGS, "empty_texture");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if(entityLiving instanceof AbstractClientPlayer) {
            return new ModelJoggers((AbstractClientPlayer) entityLiving, this);
        }
        return null;
    }

}
