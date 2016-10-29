package elec332.eflux.network;

import elec332.core.network.packets.AbstractPacket;
import elec332.core.util.NBTHelper;
import elec332.eflux.EFlux;
import elec332.eflux.endernetwork.EnderNetwork;
import elec332.eflux.endernetwork.EnderNetworkManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Elec332 on 5-5-2016.
 */
public class PacketSyncEnderNetwork extends AbstractPacket {

    public PacketSyncEnderNetwork(){
    }

    public PacketSyncEnderNetwork(EnderNetwork network){
        super(new NBTHelper().addToTag(network.getNetworkId(), "id").addToTag(network.serializeNBT(), "tag").serializeNBT());
    }

    @Override
    public IMessage onMessageThreadSafe(AbstractPacket message, MessageContext ctx) {
        NBTHelper nbt = new NBTHelper(message.networkPackageObject);
        EnderNetwork network = EnderNetworkManager.get(EFlux.proxy.getClientWorld()).get(nbt.getUUID("id"));
        network.deserializeNBT(nbt.getCompoundTag("tag"));
        return null;
    }

}
