package elec332.eflux.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

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
    public boolean canProvidePowerTo(ForgeDirection direction);

    /**
     * @param rp
     *      the RedstonePotential in the network
     * @param direction
     *      the direction where the power will be provided to
     *
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    public int provideEnergy(int rp, ForgeDirection direction);

}
