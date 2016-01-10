package elec332.eflux.compat.rf;

import cofh.nonexistant.api.energy.IEnergyProvider;
import elec332.core.util.DirectionHelper;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.tileentity.EnergyTileBase;
import elec332.eflux.util.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 20-7-2015.
 */
public class TileRFConverter extends EnergyTileBase implements IEnergyReceiver, IEnergyProvider{

    private int storedPower;

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        tagCompound.setInteger("power", storedPower);
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        this.storedPower = tagCompound.getInteger("power");
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
        return direction == DirectionHelper.getDirectionFromNumber(getBlockMetadata());
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(EnumFacing direction) {
        return 0; //This is the only machine that doesn't care about the RP of the network
    }

    /**
     * @param rp        The Redstone Potential in the network
     * @param direction The requested direction
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    @Override
    public int getRequestedEF(int rp, EnumFacing direction) {
        return Math.min(400/rp, (6000-storedPower)/rp);
    }

    /**
     * @param direction the direction where the power will be provided to
     * @param rp        the RedstonePotential in the network
     * @param ef        the amount of EnergeticFlux that is being provided
     //* @return The amount of EnergeticFlux that wasn't used
     */
    @Override
    public int receivePower(EnumFacing direction, int rp, int ef) {
        storedPower += rp*ef;
        if (storedPower > 6000)
            storedPower = 6000;
        return 0;
    }

    /**
     * RF part
     */

    /**
     * Remove energy from an IEnergyProvider, internal distribution is left entirely to the IEnergyProvider.
     *
     * @param from       Orientation the energy is extracted from.
     * @param maxExtract Maximum amount of energy to extract.
     * @param simulate   If TRUE, the extraction will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) extracted.
     */
    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        int i = getEnergyStored(from);
        int ret = Math.min(i, maxExtract);
        if (!simulate)
            storedPower -= ret*Config.RFConversionNumber;
        return ret;
    }

    /**
     * Returns the amount of energy currently stored.
     */
    @Override
    public int getEnergyStored(EnumFacing from) {
        return storedPower/Config.RFConversionNumber;
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     */
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 6000/Config.RFConversionNumber;
    }

    /**
     * Returns TRUE if the TileEntity can connect on a given side.
     */
    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return from == DirectionHelper.getDirectionFromNumber(getBlockMetadata()).getOpposite();
    }
}
