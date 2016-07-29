package elec332.eflux.network;

import elec332.core.network.AbstractPacket;
import elec332.eflux.endernetwork.EnderNetworkManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Elec332 on 11-5-2016.
 */
public class PacketPlayerConnection extends AbstractPacket {

    public PacketPlayerConnection(){
    }

    public PacketPlayerConnection(EntityPlayerMP player){
        NBTTagCompound send = new NBTTagCompound();
        EnderNetworkManager networkManager = EnderNetworkManager.get(player.worldObj);
        send.setTag("net", networkManager.serializeNBT());
        networkPackageObject = send;
    }

    @Override
    public IMessage onMessageThreadSafe(AbstractPacket abstractPacket, MessageContext messageContext) {
        NBTTagCompound receive = abstractPacket.networkPackageObject;
        EnderNetworkManager.onPacket(receive.getCompoundTag("net"), messageContext);
        return null;
    }

}
