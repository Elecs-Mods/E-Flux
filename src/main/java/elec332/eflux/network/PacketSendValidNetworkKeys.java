package elec332.eflux.network;

import elec332.core.network.AbstractPacket;
import elec332.core.util.NBT;
import elec332.core.util.NBTHelper;
import elec332.eflux.EFlux;
import elec332.eflux.endernetwork.EnderNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Elec332 on 11-5-2016.
 */
public class PacketSendValidNetworkKeys extends AbstractPacket {

    public PacketSendValidNetworkKeys(){
    }

    public PacketSendValidNetworkKeys(NBTTagList tag){
        super(new NBTHelper().addToTag(tag, "list").serializeNBT());
    }

    @Override
    public IMessage onMessageThreadSafe(AbstractPacket abstractPacket, MessageContext messageContext) {
        EnderNetworkManager.get(EFlux.proxy.getClientWorld()).deserializekeys(abstractPacket.networkPackageObject.getTagList("list", NBT.NBTData.STRING.getID()));
        return null;
    }

}
