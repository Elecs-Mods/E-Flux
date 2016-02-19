package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyReceiver extends IEnergyTile {

    /**
     *
     * @return The Redstone Potential at which the machine wishes to operate
     */
    public int requestedRP();

    /**
     *
     * @param rp
     *      The Redstone Potential in the network
     *
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    public int getRequestedEF(int rp);

    /**
     * @param rp
     *      the RedstonePotential in the network
     * @param ef
     *      the amount of EnergeticFlux that is being provided
     *
     * @return The amount of EnergeticFlux that wasn't used
     */
    public int receivePower(int rp, int ef);

}
