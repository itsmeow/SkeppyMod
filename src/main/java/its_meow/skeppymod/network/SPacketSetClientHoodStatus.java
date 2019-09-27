package its_meow.skeppymod.network;

import java.util.UUID;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import its_meow.skeppymod.SkeppyMod;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSetClientHoodStatus implements IMessage, IMessageHandler<SPacketSetClientHoodStatus, IMessage> {
    
    public UUID user;
    public boolean isOn;

    public SPacketSetClientHoodStatus() {}

    public SPacketSetClientHoodStatus(UUID user, boolean isOn) {
        this.user = user;
        this.isOn = isOn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isOn = buf.readBoolean();
        int l = buf.readInt();
        user = UUID.fromString(String.valueOf(buf.readCharSequence(l, Charsets.UTF_8)));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isOn);
        buf.writeInt(user.toString().length());
        buf.writeCharSequence(user.toString(), Charsets.UTF_8);
    }

    @Override
    public IMessage onMessage(SPacketSetClientHoodStatus message, MessageContext ctx) {
        SkeppyMod.HOODS.put(message.user, message.isOn);
        return null;
    }

}
