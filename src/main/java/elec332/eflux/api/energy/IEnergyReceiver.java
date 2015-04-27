package elec332.eflux.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyReceiver extends IEnergyTile{

    /**
     * @return weather the tile can connect and accept power from the given side
     */
    public boolean canAcceptEnergyFrom(ForgeDirection direction);

    /**
     *
     * @param rp
     *      The Redstone Potential in the network
     * @param direction
     *      The requested direction
     *
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    public int getRequestedEF(int rp, ForgeDirection direction);

    /**
     * @param direction
     *      The requested direction
     *      
     * @return The Redstone Potential at which the machine wishes to operate
     */
    public int requestedRP(ForgeDirection direction);

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
    public int receivePower(ForgeDirection direction, int rp, int ef);


}
