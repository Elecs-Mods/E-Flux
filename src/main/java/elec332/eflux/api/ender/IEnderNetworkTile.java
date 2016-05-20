package elec332.eflux.api.ender;

import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderConnection;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 8-5-2016.
 */
public interface IEnderNetworkTile<T> extends IEnderNetworkComponent<T> {

    @Override
    default public boolean isItem(){
        return false;
    }

    public TileEntity getTile();

    @Override
    default public void onConnect(IEnderConnection<T> connection){
        onConnect((IStableEnderConnection<T>) connection);
    }

    public void onConnect(IStableEnderConnection<T> connection);

    public void onDisconnect(IStableEnderConnection<T> connection, DisconnectReason reason);

    public IStableEnderConnection<T> getCurrentConnection();

}
