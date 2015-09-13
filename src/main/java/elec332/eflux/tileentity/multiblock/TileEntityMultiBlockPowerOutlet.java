package elec332.eflux.tileentity.multiblock;

import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.multiblock.MultiBlockInterfaces;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 13-9-2015.
 */
public class TileEntityMultiBlockPowerOutlet extends TileMultiBlockInteraction<MultiBlockInterfaces.IEFluxMultiBlockPowerProvider> implements IEnergySource {

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
     * @return weather the tile can connect and provide power to the given side
     */
    @Override
    public boolean canProvidePowerTo(ForgeDirection direction) {
        return direction == getTileFacing();
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param direction the direction where the power will be provided to
     * @param execute   weather the power is actually drawn from the tile,
     *                  this flag is always true for IEnergySource.
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, ForgeDirection direction, boolean execute) {
        return getMultiBlockHandler() == null ? 0 : getMultiBlockHandler().provideEnergy(rp, execute);
    }

}
