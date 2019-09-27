package its_meow.skeppymod.network;

import io.netty.buffer.ByteBuf;
import its_meow.skeppymod.SkeppyMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketSetHoodStatus implements IMessage, IMessageHandler<CPacketSetHoodStatus, IMessage> {

    public boolean isOn;

    public CPacketSetHoodStatus() {}

    public CPacketSetHoodStatus(boolean isOn) {
        this.isOn = isOn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isOn = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isOn);
    }

    @Override
    public IMessage onMessage(CPacketSetHoodStatus message, MessageContext ctx) {
        SkeppyMod.HOODS.put(ctx.getServerHandler().player.getGameProfile().getId(), message.isOn);
        for(EntityPlayerMP player : ctx.getServerHandler().player.server.getPlayerList().getPlayers()) {
            if(player != ctx.getServerHandler().player) {
                SkeppyMod.NETWORK_INSTANCE.sendTo(new SPacketSetClientHoodStatus(ctx.getServerHandler().player.getGameProfile().getId(), message.isOn), player);
            }
        }
        return null;
    }

}
