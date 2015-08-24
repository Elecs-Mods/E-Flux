package elec332.eflux.tileentity.energy.machine;

import elec332.core.util.DirectionHelper;
import elec332.eflux.api.energy.ISpecialEnergySource;
import elec332.eflux.tileentity.BreakableReceiverTile;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 30-4-2015.
 */
public class Capacitor extends BreakableReceiverTile implements ISpecialEnergySource{

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.stick);
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
    protected void receivePower(int rp, int ef, ForgeDirection direction) {
        storedPower = storedPower+rp*ef;
    }*/

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return DirectionHelper.getDirectionFromNumber(getBlockMetadata()) == direction;
    }

    @Override
    public int getRequestedRP() {
        return 20;
    }

    @Override
    public String[] getProvidedData() {
        return new String[]{"Stored Energy: "+energyContainer.getStoredPower(), "Broken: "+isBroken()};
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param direction the direction where the power will be provided to
     * @param reqEF     the requested amount of EnergeticFlux
     * @return The amount of EnergeticFlux the tile will provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergeticFlux(int rp, ForgeDirection direction, int reqEF) {
        return 0;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and provide power to the given side
     */
    @Override
    public boolean canProvidePowerTo(ForgeDirection direction) {
        return false;
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param direction the direction where the power will be provided to
     * @param execute   weather the power is actually drawn from the tile,
     *                  this flag is always true for IEnergySource.
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, ForgeDirection direction, boolean execute) {
        return 0;
    }
}
