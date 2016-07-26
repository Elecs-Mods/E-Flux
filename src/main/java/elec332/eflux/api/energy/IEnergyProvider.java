package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyProvider extends IEnergyTile {

    /**
     * @param rp
     *      the RedstonePotential in the network
     * @param execute
     *      weather the power is actually drawn from the tile,
     *      this flag is always true for IEnergySource.
     *
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    public int provideEnergy(int rp, boolean execute);

}
