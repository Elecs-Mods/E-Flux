package elec332.eflux.network;

import elec332.core.network.packets.AbstractPacket;
import elec332.eflux.inventory.ContainerEnderContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Elec332 on 9-5-2016.
 */
public class PacketSyncEnderContainerGui extends AbstractPacket {

    public PacketSyncEnderContainerGui(){
    }

    public PacketSyncEnderContainerGui(NBTTagCompound tag){
        super(tag);
    }

    @Override
    public IMessage onMessageThreadSafe(AbstractPacket message, MessageContext ctx) {
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;
        if (container instanceof ContainerEnderContainer){
            ((ContainerEnderContainer) container).readPacket(message.networkPackageObject);
        }
        return null;
    }

}
