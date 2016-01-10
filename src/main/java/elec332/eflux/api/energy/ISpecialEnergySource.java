package elec332.eflux.api.energy;

import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 28-4-2015.
 */
public interface ISpecialEnergySource extends IEnergySource{  //Backup, DO NOT USE

    /**
     * @param rp
     *      the RedstonePotential in the network
     * @param direction
     *      the direction where the power will be provided to
     * @param reqEF
     *      the requested amount of EnergeticFlux
     *
     * @return The amount of EnergeticFlux the tile will provide for the given Redstone Potential.
     */
    public int provideEnergeticFlux(int rp, EnumFacing direction, int reqEF);
}
