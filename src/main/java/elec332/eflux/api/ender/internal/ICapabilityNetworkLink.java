package elec332.eflux.api.ender.internal;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Elec332 on 11-5-2016.
 */
public interface ICapabilityNetworkLink {

    public void sendPacket(int id, NBTTagCompound data);

}
