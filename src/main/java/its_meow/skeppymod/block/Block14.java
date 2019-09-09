package its_meow.skeppymod.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import its_meow.skeppymod.SkeppyMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class Block14 extends Block {

    public static final Random RAND = new Random();

    public static final String[] QUESTIONS = new String[] {
    "How many times has BadBoyHalo said the word \"muffin\", in thousands?",
    "What is BadBoyHalo's favorite number?",
    "What is the best number?",
    "What is 8 * 2 - 2?",
    "How did the French Server Owner Finally Troll Him?",
    "How many likes will this video get?",
    "How many people, in hundred thousands, should buy Skeppy merch at skeppyshop.com?",
    "How much time can a6d put into building for MunchyMC throughout a week?",
    "How long did it take a6d to build his masterpiece dirt hut?",
    "How much money does Skeppy get in ad revenue?",
    "How awesome is this mod?",
    "Getting close to 14 clicks here now aren't we?",
    "How old is BadBoyHalo?",
    "How many times have you clicked this block now?"
    };

    public static Map<UUID, Integer> userIteration = new HashMap<UUID, Integer>();

    public Block14() {
        super(Material.TNT);
        this.setRegistryName("14");
        this.setTranslationKey("skeppymod.14");
        this.setCreativeTab(SkeppyMod.SKEPPY_TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote && hand == EnumHand.MAIN_HAND) {
            int i;
            if(!userIteration.containsKey(playerIn.getGameProfile().getId())) {
                userIteration.put(playerIn.getGameProfile().getId(), 0);
                i = 0;
            } else if(userIteration.get(playerIn.getGameProfile().getId()) == 14) {
                userIteration.put(playerIn.getGameProfile().getId(), 0);
                i = 14;
            } else {
                i = userIteration.get(playerIn.getGameProfile().getId());
            }
            playerIn.sendMessage(new TextComponentString("Q: ").setStyle(new Style().setColor(TextFormatting.AQUA)).appendSibling(new TextComponentString(QUESTIONS[i]).setStyle(new Style().setColor(TextFormatting.GOLD))));
            playerIn.sendMessage(new TextComponentString("A: ").setStyle(new Style().setColor(TextFormatting.AQUA)).appendSibling(new TextComponentString("14").setStyle(new Style().setColor(TextFormatting.GREEN))));
            if(i != 14) {
                userIteration.put(playerIn.getGameProfile().getId(), i + 1);
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if(!worldIn.isRemote) {
            if(SkeppyMod.isSkeppy(player)) {
                player.sendStatusMessage(new TextComponentString("skeppy stop breaking me >:((("), true);
            } else if(SkeppyMod.isBBH(player)) {
                player.sendStatusMessage(new TextComponentString("before you break me, put a6d in a bucket"), true);
            } else if(SkeppyMod.isA6D(player)) {
                player.sendStatusMessage(new TextComponentString("a6d you better eat them all"), true);
                player.addItemStackToInventory(new ItemStack(Items.BREAD, 64));
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(placer instanceof EntityPlayer && !worldIn.isRemote) {
            EntityPlayer player = (EntityPlayer) placer;
            if(SkeppyMod.isSkeppy(player)) {
                player.sendStatusMessage(new TextComponentString("hi skeppy thank u for placing this block"), true);
            } else if(SkeppyMod.isBBH(player)) {
                player.sendStatusMessage(new TextComponentString("hi bbh u muffin thank u for placing this block, put skeppy in a bucket next pls"), true);
            } else if(SkeppyMod.isA6D(player)) {
                player.sendStatusMessage(new TextComponentString("a6d: eat the baguette or suffer"), true);
                player.addItemStackToInventory(new ItemStack(Items.BREAD));
            }
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Right click me for fun! I promise you will enjoy it.");
    }

}
