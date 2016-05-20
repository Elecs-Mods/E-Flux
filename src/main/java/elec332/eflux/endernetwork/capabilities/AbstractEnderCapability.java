package elec332.eflux.endernetwork.capabilities;

import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.internal.ICapabilityNetworkHandler;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Elec332 on 11-5-2016.
 */
public abstract class AbstractEnderCapability<T> implements IEnderCapability<T> {

    public AbstractEnderCapability(Side side, IEnderNetwork network){
        this.side = side;
        this.network = network;
    }

    protected final Side side;
    protected final IEnderNetwork network;
    protected ICapabilityNetworkHandler handler;

    protected void sendPoke(int id){
        sendPacket(id, new NBTTagCompound());
    }

    protected void sendPacket(int id, NBTTagCompound tag){
        if (handler != null){
            handler.sendPacket(id, tag);
        }
    }

    @Override
    public void setNetworkHandler(ICapabilityNetworkHandler networkHandler) {
        this.handler = networkHandler;
    }

    @Override
    public void invalidate() {
        handler = null;
    }

}
