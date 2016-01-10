package elec332.eflux.api.energy;

import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyReceiver extends IEnergyTile{

    /**
     * @param direction
     *      the direction from which a connection is requested
     *
     * @return weather the tile can connect and accept power from the given side
     */
    public boolean canAcceptEnergyFrom(EnumFacing direction);

    /**
     * @param direction
     *      The requested direction
     *
     * @return The Redstone Potential at which the machine wishes to operate
     */
    public int requestedRP(EnumFacing direction);

    /**
     *
     * @param rp
     *      The Redstone Potential in the network
     * @param direction
     *      The requested direction
     *
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    public int getRequestedEF(int rp, EnumFacing direction);

    /**
     * @param rp
     *      the RedstonePotential in the network
     * @param direction
     *      the direction where the power will be provided to
     * @param ef
     *      the amount of EnergeticFlux that is being provided
     *
     * @return The amount of EnergeticFlux that wasn't used
     */
    public int receivePower(EnumFacing direction, int rp, int ef);

}
