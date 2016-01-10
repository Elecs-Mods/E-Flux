package elec332.eflux.api.energy;

import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergySource extends IEnergyTile{

    /**
     * @param direction
     *      the direction from which a connection is requested
     *
     * @return weather the tile can connect and provide power to the given side
     */
    public boolean canProvidePowerTo(EnumFacing direction);

    /**
     * @param rp
     *      the RedstonePotential in the network
     * @param direction
     *      the direction where the power will be provided to
     * @param execute
     *      weather the power is actually drawn from the tile,
     *      this flag is always true for IEnergySource.
     *
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    public int provideEnergy(int rp, EnumFacing direction, boolean execute);

}
