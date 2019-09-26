package its_meow.skeppymod.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandF extends CommandBase {

    @Override
    public String getName() {
        return "f";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/f";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for(EntityPlayerMP playerSaying : server.getPlayerList().getPlayers()) {
            if(!playerSaying.isInvisible() && playerSaying.getChatVisibility() == EnumChatVisibility.FULL) {
                String s = "f";
                ITextComponent itextcomponent = new TextComponentTranslation("chat.type.text", playerSaying.getDisplayName(), net.minecraftforge.common.ForgeHooks.newChatWithLinks(s));
                itextcomponent = net.minecraftforge.common.ForgeHooks.onServerChatEvent(playerSaying.connection, s, itextcomponent);
                if (itextcomponent == null) return;
                server.getPlayerList().sendMessage(itextcomponent, false);
            }
        }
    }

}
