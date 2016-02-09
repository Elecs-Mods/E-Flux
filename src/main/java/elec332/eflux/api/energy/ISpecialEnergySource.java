package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 28-4-2015.
 */
public interface ISpecialEnergySource extends IEnergySource {  //Backup, DO NOT USE

    /**
     * @param rp
     *      the RedstonePotential in the network
     * @param reqEF
     *      the requested amount of EnergeticFlux
     *
     * @return The amount of EnergeticFlux the tile will provide for the given Redstone Potential.
     */
    public int provideEnergeticFlux(int rp, int reqEF);
}
