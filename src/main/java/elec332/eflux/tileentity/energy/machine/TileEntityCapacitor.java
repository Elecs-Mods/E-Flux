package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.eflux.api.energy.IEnergyProvider;
import elec332.eflux.tileentity.TileEntityBreakableMachine;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 30-4-2015.
 */
@RegisteredTileEntity("TileEntityEFluxCapacitor")
public class TileEntityCapacitor extends TileEntityBreakableMachine implements IEnergyProvider {

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.STICK);
    }

    @Override
    public float getAcceptance() {
        return 0.15f;
    }

    @Override
    protected int getMaxStoredPower() {
        return 3000;
    }

    @Override
    public int getEFForOptimalRP() {
        return 20;
    }

    /*@Override
    public int getMaxEF(int rp) {
        return Math.min(20, (300-storedPower)/rp);
    }

    @Override
    protected void receivePower(int rp, int ef, EnumFacing direction) {
        storedPower = storedPower+rp*ef;
    }*/

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
        return true;
    }

    @Override
    public int getRequestedRP() {
        return 20;
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param reqEF     the requested amount of EnergeticFlux
     * @return The amount of EnergeticFlux the tile will provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergeticFlux(int rp, int reqEF) {
        if (reqEF > energyContainer.getStoredPower()/rp)
            reqEF = energyContainer.getStoredPower()/rp;
        return Math.min(reqEF, 400/rp);
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param execute   weather the power is actually drawn from the tile,
     *                  this flag is always true for IEnergySource.
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, boolean execute) {
        if (!execute)
            return Math.min(400/rp, energyContainer.getStoredPower()/rp);
        return 0;
    }
}
