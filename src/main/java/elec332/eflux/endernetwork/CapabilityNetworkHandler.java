package elec332.eflux.endernetwork;

import elec332.eflux.api.ender.internal.ICapabilityNetworkHandler;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Elec332 on 15-5-2016.
 */
public class CapabilityNetworkHandler implements ICapabilityNetworkHandler {

    public CapabilityNetworkHandler(int id, EnderNetwork network){
        this.id = id;
        this.network = network;
    }

    private int id;
    private EnderNetwork network;

    @Override
    public void sendPacket(int i, NBTTagCompound data) {
        if (network != null && id != -1) {
            network.sendCapabilityPacket(id, i, data);
        }
    }

    void invalidate(){
        id = -1;
        network = null;
    }

}
