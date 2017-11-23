package elec332.eflux.endernetwork.capabilities;

import com.google.common.collect.Lists;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.IEnderNetworkTile;
import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import elec332.eflux.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 21-5-2016.
 */
public class EFluxEnderCapabilityEnergy extends AbstractEnderCapability<IEnergyReceiver>{//} implements IEnergyReceiver {

    public EFluxEnderCapabilityEnergy(Side side, IEnderNetwork network) {
        super(side, network);
        this.activeConnections = Lists.newArrayList();
    }

    private final List<IStableEnderConnection<IEnergyReceiver>> activeConnections;

    @Nonnull
    @Override
    public Capability<IEnergyReceiver> getCapability() {
        return EFluxAPI.RECEIVER_CAPABILITY;
    }

    @Nullable
    @Override
    public IEnergyReceiver get() {
        return null;//this;
    }

    @Override
    public int getEndergyConsumption() {
        return 39;
    }

    @Override
    public boolean canConnect(IEnderNetworkComponent<IEnergyReceiver> component) {
        return !(component instanceof IEnderNetworkTile) || ((IEnderNetworkTile) component).getTile().hasCapability(EFluxAPI.RECEIVER_CAPABILITY, null);
    }

    @Override
    public void addConnection(IStableEnderConnection<IEnergyReceiver> connection) {
        this.activeConnections.add(connection);
    }

    @Override
    public void removeConnection(IStableEnderConnection<IEnergyReceiver> connection, DisconnectReason reason) {
        this.activeConnections.remove(connection);
    }

    @Nonnull
    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
    }

    /**
     * @return The Redstone Potential at which the machine wishes to operate
     *
    @Override
    public int requestedRP() {
        int rp = 0;
        for (IEnergyReceiver receiver : getAllReceivers()){
            rp = Math.max(rp, receiver.requestedRP());
        }
        return rp;
    }

    /**
     * @param rp The Redstone Potential in the network
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     *
    @Override
    public int getRequestedEF(int rp) {
        int ef = 0;
        for (IEnergyReceiver receiver : getAllReceivers()){
            ef += receiver.getRequestedEF(rp);
        }
        return (int) (ef / 0.9f);
    }


    /**
     * @param rp the RedstonePotential in the network
     * @param ef the amount of EnergeticFlux that is being provided
     * @return The amount of EnergeticFlux that wasn't used
     *
    @Override
    public int receivePower(int rp, int ef) {
        ef = (int) (ef * 0.9f);
        List<IEnergyReceiver> receivers = getAllReceivers();
        int reqEF = 0;
        @SuppressWarnings("unchecked")
        Pair<IEnergyReceiver, Integer>[] data = new Pair[receivers.size()];
        for (int i = 0; i < data.length; i++) {
            IEnergyReceiver receiver = receivers.get(i);
            int e = receiver.getRequestedEF(rp);
            reqEF += e;
            data[i] = Pair.of(receiver, e);
        }
        int d = ef - reqEF;
        if (d > -3){
            d = 0;
        }
        float diff = d == 0 ? 1 : (float) ef / (float) reqEF;
        for (Pair<IEnergyReceiver, Integer> obj : data){
            int dec = (int) (obj.getRight() * diff);
            obj.getKey().receivePower(rp, dec);
            ef -= dec;
        }
        return Math.max(ef, 0);
    }

    private List<IEnergyReceiver> getAllReceivers(){
        return activeConnections.stream()
                .map(connection -> connection.getComponent().getTile())
                .filter(tile -> WorldHelper.chunkLoaded(tile.getWorld(), tile.getPos()) && tile.hasCapability(EFluxAPI.RECEIVER_CAPABILITY, null))
                .map(tile -> tile.getCapability(EFluxAPI.RECEIVER_CAPABILITY, null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }*/

}
