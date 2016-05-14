package elec332.eflux.api.ender;

import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderConnection;
import elec332.eflux.api.ender.internal.IStableEnderConnection;

/**
 * Created by Elec332 on 8-5-2016.
 */
public interface IEnderNetworkTile<T> extends IEnderNetworkComponent<T> {

    @Override
    default public boolean isItem(){
        return false;
    }

    @Override
    default public void onConnect(IEnderConnection<T> connection){
        onConnect((IStableEnderConnection<T>) connection);
    }

    public void onConnect(IStableEnderConnection<T> connection);

    public void onDisconnect(IStableEnderConnection<T> connection, DisconnectReason reason);

    public IStableEnderConnection<T> getCurrentConnection();

}
