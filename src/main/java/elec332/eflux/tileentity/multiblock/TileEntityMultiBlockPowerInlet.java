package elec332.eflux.tileentity.multiblock;

import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.multiblock.MultiBlockInterfaces;
import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 28-7-2015.
 */
public class TileEntityMultiBlockPowerInlet extends TileMultiBlockInteraction<MultiBlockInterfaces.IEFluxMultiBlockPowerAcceptor> implements IEnergyReceiver {

    @Override
    public void onTileLoaded() {
        if (!worldObj.isRemote)
            EnergyAPIHelper.postLoadEvent(this);
    }

    @Override
    public void onTileUnloaded() {
        if (!worldObj.isRemote)
            EnergyAPIHelper.postUnloadEvent(this);
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
        return direction == getTileFacing();
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(EnumFacing direction) {
        return getMultiBlockHandler() == null ? 0 : getMultiBlockHandler().requestedRP();
    }

    /**
     * @param rp        The Redstone Potential in the network
     * @param direction The requested direction
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    @Override
    public int getRequestedEF(int rp, EnumFacing direction) {
        return getMultiBlockHandler() == null ? 0 : getMultiBlockHandler().getRequestedEF(rp);
    }

    /**
     * @param direction the direction where the power will be provided to
     * @param rp        the RedstonePotential in the network
     * @param ef        the amount of EnergeticFlux that is being provided
     * @return The amount of EnergeticFlux that wasn't used
     */
    @Override
    public int receivePower(EnumFacing direction, int rp, int ef) {
        return getMultiBlockHandler() == null ? ef : getMultiBlockHandler().receivePower(rp, ef);
    }

}
