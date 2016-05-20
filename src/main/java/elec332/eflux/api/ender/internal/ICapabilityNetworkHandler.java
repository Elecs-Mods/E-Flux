package elec332.eflux.api.ender.internal;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Elec332 on 15-5-2016.
 */
public interface ICapabilityNetworkHandler {

    public void sendPacket(int i, NBTTagCompound data);

}
