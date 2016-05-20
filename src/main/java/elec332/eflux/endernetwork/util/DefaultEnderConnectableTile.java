package elec332.eflux.endernetwork.util;

import com.google.common.collect.Lists;
import elec332.eflux.api.ender.IEnderNetworkTile;
import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 9-5-2016.
 */
public class DefaultEnderConnectableTile<T> implements IEnderNetworkTile<T> {

    public DefaultEnderConnectableTile(Capability<T> capability, TileEntity tile){
        this.capability = capability;
        this.listeners = Lists.newArrayList();
        this.tile = tile;
    }

    private final Capability<T> capability;
    private final List<IConnectionListener> listeners;
    private int frequency;
    private UUID uuid;
    private IStableEnderConnection<T> currentConnection;
    private TileEntity tile;

    public DefaultEnderConnectableTile<T> addListener(IConnectionListener listener){
        listeners.add(listener);
        return this;
    }

    public DefaultEnderConnectableTile<T> removeListener(IConnectionListener listener){
        listeners.remove(listener);
        return this;
    }

    @Override
    public void onConnect(IStableEnderConnection<T> connection) {
        this.currentConnection = connection;
        for (IConnectionListener listener : listeners){
            listener.onConnect();
        }
    }

    @Override
    public void onDisconnect(IStableEnderConnection<T> connection, DisconnectReason reason) {
        this.currentConnection = null;
        for (IConnectionListener listener : listeners){
            listener.onDisconnect(reason);
        }
    }

    @Nonnull
    @Override
    public Capability<T> getRequiredCapability() {
        return this.capability;
    }

    @Override
    public TileEntity getTile() {
        return tile;
    }

    @Override
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int getFrequency() {
        return this.frequency;
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public IStableEnderConnection<T> getCurrentConnection() {
        return currentConnection;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound ret = new NBTTagCompound();
        if (uuid != null){
            ret.setString("u", uuid.toString());
        }
        ret.setInteger("i", frequency);
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        uuid = null;
        if (nbt.hasKey("u")){
            uuid = UUID.fromString(nbt.getString("u"));
        }
        frequency = nbt.getInteger("i");
    }

    public interface IConnectionListener {

        public void onConnect();

        public void onDisconnect(DisconnectReason reason);

    }

}
