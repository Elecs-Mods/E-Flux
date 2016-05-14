package elec332.eflux.endernetwork.util;

import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEnderConnection;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.api.ender.internal.IWeakEnderConnection;
import elec332.eflux.endernetwork.EnderNetworkManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Elec332 on 14-5-2016.
 */
public class DefaultEnderConnectableItem<T> implements IEnderNetworkComponent<T> {

    public DefaultEnderConnectableItem(Capability<T> capability){
        this.capability = capability;
    }

    private final Capability<T> capability;
    private UUID network;
    private int freq;
    private IEnderConnection<T> connection;

    public boolean connect(World world) {
        IEnderNetwork network = EnderNetworkManager.get(world).get(this.network);
        return network != null && network.connect(this);
    }

    @Nonnull
    @Override
    public Capability<T> getRequiredCapability() {
        return capability;
    }

    @Override
    public void setFrequency(int freq) {
        if (freq != this.freq){
            connection = null;
        }
        this.freq = freq;
    }

    @Override
    public int getFrequency() {
        return freq;
    }

    @Override
    public boolean isItem() {
        return true;
    }

    @Override
    public void setUUID(UUID uuid) {
        if (network != null && network.equals(uuid)){
            connection = null;
        }
        this.network = uuid;
    }

    @Override
    public UUID getUuid() {
        return network;
    }

    @Override
    public void onConnect(IEnderConnection<T> connection) {
        this.connection = connection;
    }

    @Nullable
    @Override
    public IEnderConnection<T> getCurrentConnection() {
        if (connection != null && !((IWeakEnderConnection<T>)connection).isValid()){
            connection = null;
        }
        return connection;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound ret = new NBTTagCompound();
        if (network != null){
            ret.setString("uuid", network.toString());
        }
        ret.setInteger("i", freq);
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("uuid")){
            network = UUID.fromString(nbt.getString("uuid"));
        } else {
            network = null;
        }
        freq = nbt.getInteger("i");
    }

}
