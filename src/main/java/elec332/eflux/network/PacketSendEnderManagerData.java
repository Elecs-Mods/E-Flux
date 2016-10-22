package elec332.eflux.network;

import elec332.core.network.AbstractPacket;
import elec332.core.util.NBTHelper;
import elec332.eflux.EFlux;
import elec332.eflux.endernetwork.EnderNetworkManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Elec332 on 11-5-2016.
 */
public class PacketSendEnderManagerData extends AbstractPacket {

    public PacketSendEnderManagerData(){
    }

    public PacketSendEnderManagerData(int i, NBTTagCompound data){
        super(new NBTHelper().addToTag(data, "tag").addToTag(i, "nr").serializeNBT());
    }

    @Override
    public IMessage onMessageThreadSafe(AbstractPacket message, MessageContext ctx) {
        EnderNetworkManager.get(EFlux.proxy.getClientWorld()).onPacket(message.networkPackageObject.getInteger("nr"), message.networkPackageObject.getCompoundTag("tag"));
        return null;
    }

}
