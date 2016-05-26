package elec332.eflux.network;

import elec332.core.network.AbstractPacket;
import elec332.core.util.NBTHelper;
import elec332.eflux.EFlux;
import elec332.eflux.endernetwork.EnderNetwork;
import elec332.eflux.endernetwork.EnderNetworkManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Elec332 on 11-5-2016.
 */
public class PacketSendEnderNetworkData extends AbstractPacket {

    public PacketSendEnderNetworkData(){
    }

    public PacketSendEnderNetworkData(EnderNetwork network, int i, NBTTagCompound data){
        super(new NBTHelper().addToTag(network.getNetworkId(), "id").addToTag(data, "tag").addToTag(i, "nr").serializeNBT());
    }

    @Override
    public IMessage onMessageThreadSafe(AbstractPacket message, MessageContext ctx) {
        NBTHelper nbt = new NBTHelper(message.networkPackageObject);
        EnderNetwork network = EnderNetworkManager.get(EFlux.proxy.getClientWorld()).get(nbt.getUUID("id"));
        network.onPacket(nbt.getInteger("nr"), nbt.getCompoundTag("tag"));
        return null;
    }

}
