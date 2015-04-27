package elec332.eflux.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergySource extends IEnergyTile{

    /**
     * @return weather the tile can connect and provide power to the given side
     */
    public boolean canProvidePowerTo(ForgeDirection direction);

    /**
     * @param rp
     *      the RedstonePotential in the network
     * @param direction
     *      the direction where the power will be provided to
     * @param execute
     *      weather the energy is actually drawn from the tile, or if its just to check how much power the tile
     *      is going to provide. If true, the power is drawn from the tile, if false, its just to check.
     *
     * @return The amount of EnergeticFlux the tile will provide for the given Redstone Potential.
     */
    public int getProvidedEFForRP(int rp, ForgeDirection direction, boolean execute);

}
