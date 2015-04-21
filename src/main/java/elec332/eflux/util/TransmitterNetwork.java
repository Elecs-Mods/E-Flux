package elec332.eflux.util;
/*
import com.google.common.collect.Lists;
import elec332.eflux.api.transmitter.IEnergyReceiver;
import elec332.eflux.handlers.TransmitterNetworkHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Elec332 on 16-4-2015.
 *//*
public abstract class TransmitterNetwork<T extends IEnergyReceiver, I extends T> {

    protected final List<I> conduits = new ArrayList<I>();

    protected final Class<I> implClass;
    protected final Class<T> baseConduitClass;

    protected TransmitterNetwork(Class<I> implClass, Class<T> baseConduitClass) {
        this.implClass = implClass;
        this.baseConduitClass = baseConduitClass;
    }

    public void init(IEnergyReceiver tile, Collection<I> connections, World world) {

        if(world.isRemote) {
            throw new UnsupportedOperationException();
        }

        // Destroy all existing redstone networks around this block
        for (I con : connections) {
            TransmitterNetwork<?, ?> network = con.getNetwork();
            if(network != null) {
                network.destroyNetwork();
            }
        }
        setNetwork(world, tile);
        notifyNetworkOfUpdate();
    }

    public final Class<T> getBaseConduitType() {
        return baseConduitClass;
    }

    protected void setNetwork(World world, IEnergyReceiver tile) {

        T conduit = (T) tile;

        if(conduit != null && implClass.isAssignableFrom(conduit.getClass()) && conduit.setNetwork(this)) {
            addConduit(implClass.cast(conduit));
            TileEntity te = tile.getTile();
            List<T> connections = getConnectedConduits(conduit);
            for (T con : connections) {
                if(con.getNetwork() == null) {
                    setNetwork(world, (IEnergyReceiver)con.getTile());
                } else if(con.getNetwork() != this) {
                    con.getNetwork().destroyNetwork();
                    setNetwork(world, (IEnergyReceiver)con.getTile());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<T> getConnectedConduits(IEnergyReceiver transmitter){
        List<T> ret = Lists.newArrayList();
        if (transmitter != null) {
            for (ForgeDirection direction : transmitter.getConnections()) {
                TileEntity tile = transmitter.getTile();
                TileEntity next = tile.getWorldObj().getTileEntity(tile.xCoord+direction.offsetX, tile.yCoord+direction.offsetY, tile.zCoord+direction.offsetZ);
                if (next instanceof IEnergyReceiver)
                    ret.add((T) next);
            }
        }
        return ret;
    }

    public void addConduit(I con) {
        if(!conduits.contains(con)) {
            if(conduits.isEmpty()) {
                TransmitterNetworkHandler.instance.registerNetwork(this);
            }
            conduits.add(con);
        }
    }

    public void destroyNetwork() {
        for (I con : conduits) {
            con.setNetwork(null);
        }
        conduits.clear();
        TransmitterNetworkHandler.instance.unregisterNetwork(this);
    }

    public List<I> getConduits() {
        return conduits;
    }

    public void notifyNetworkOfUpdate() {
        for (I con : conduits) {
            TileEntity te = con.getTile();
            te.getWorldObj().markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
        }
    }


    public void tickNetwork() {
    }
}
*/