package elec332.eflux.api.ender.internal;

import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.IEnderNetworkTile;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 8-5-2016.
 */
public interface IStableEnderConnection<T> extends IEnderConnection<T> {

    @Nonnull
    @Override
    public IEnderNetworkTile<T> getComponent();

    @Nonnull
    public IEnderCapability<T> getEnderCapability();

    default public void terminateConnection(){
        terminateConnection(DisconnectReason.UNKNOWN);
    }

    public void terminateConnection(DisconnectReason reason);

    @Nonnull
    public IEnderNetwork getEnderNetwork();

    public int getFrequency();

}
