package elec332.eflux.compat.rf;

import cofh.api.energy.IEnergyProvider;
import elec332.core.tile.TileBase;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.util.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 20-7-2015.
 */
public class TileRFConverter extends TileBase implements IEnergyReceiver, IEnergyProvider, ITickable {

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

    @Override
    public void update() {
        EnumFacing tf = getTileFacing();
        TileEntity tile = WorldHelper.getTileAt(getWorld(), pos.offset(tf.getOpposite()));
        if (tile instanceof cofh.api.energy.IEnergyReceiver){
            cofh.api.energy.IEnergyReceiver receiver = (cofh.api.energy.IEnergyReceiver) tile;
            if (receiver.canConnectEnergy(tf)){
                storedPower -= receiver.receiveEnergy(tf, extractEnergy(tf.getOpposite(), 600, true), false);
            }
        }
    }

    /**
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP() {
        return 1; //This is the only machine that doesn't care about the RP of the network
    }

    /**
     * @param rp        The Redstone Potential in the network
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    @Override
    public int getRequestedEF(int rp) {
        return (int) Math.min(400/rp, ((6000-storedPower)/getRFConversion())/rp);
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param ef        the amount of EnergeticFlux that is being provided
     //* @return The amount of EnergeticFlux that wasn't used
     */
    @Override
    public int receivePower(int rp, int ef) {
        storedPower += rp*ef*getRFConversion();
        if (storedPower > 6000)
            storedPower = 6000;
        markDirty();
        return 0;
    }

    /*
     * RF part
     */

    /**
     * Remove energy from an IEnergyGenerator, internal distribution is left entirely to the IEnergyGenerator.
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
            storedPower -= ret*getRFConversion();
        markDirty();
        return ret;
    }

    /**
     * Returns the amount of energy currently stored.
     */
    @Override
    public int getEnergyStored(EnumFacing from) {
        return storedPower;
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     */
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 6000;
    }

    /**
     * Returns TRUE if the TileEntity can connect on a given side.
     */
    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return from == getTileFacing().getOpposite();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return (capability == EFluxAPI.RECEIVER_CAPABILITY && facing == getTileFacing()) || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        return (capability == EFluxAPI.RECEIVER_CAPABILITY && facing == getTileFacing()) ? (T)this : super.getCapability(capability, facing);
    }

    private float getRFConversion(){
        return 1f/Config.RFConversionNumber;
    }

}
