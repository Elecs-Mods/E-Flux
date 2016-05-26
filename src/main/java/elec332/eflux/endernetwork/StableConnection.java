package elec332.eflux.endernetwork;

import elec332.core.world.WorldHelper;
import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.IEnderNetworkTile;
import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 8-5-2016.
 */
public final class StableConnection<T> implements IStableEnderConnection<T> {

    static <T> IStableEnderConnection<T> makeConnection(IEnderNetwork network, IEnderCapability<T> capability, IEnderNetworkTile<T> component){
        return new StableConnection<T>(network, capability, component);
    }

    private StableConnection(IEnderNetwork network, IEnderCapability<T> capability, IEnderNetworkTile<T> component){
        this.network = network;
        this.capability = capability;
        this.component = component;
        this.freq = component.getFrequency();
        this.capability.addConnection(this);
        this.component.onConnect(this);
        ((EnderNetwork)this.network).activeConnections[freq].add(this);
        TileEntity tile = component.getTile();
        tile.markDirty();
        WorldHelper.markBlockForUpdate(tile.getWorld(), tile.getPos());
    }

    private final IEnderNetwork network;
    private final IEnderCapability<T> capability;
    private final IEnderNetworkTile<T> component;
    private final int freq;

    @Override
    public T get() {
        return capability.get();
    }

    @Nonnull
    @Override
    public IEnderNetworkTile<T> getComponent() {
        return component;
    }

    @Override
    @Nonnull
    public IEnderCapability<T> getEnderCapability() {
        return capability;
    }

    @Override
    public void terminateConnection(DisconnectReason reason) {
        EnderNetwork.disconnect_internal(this, reason);
        ((EnderNetwork)network).activeConnections[freq].remove(this);
    }

    @Override
    @Nonnull
    public IEnderNetwork getEnderNetwork() {
        return network;
    }

    @Override
    public int getFrequency() {
        return freq;
    }

}
