package elec332.eflux.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import elec332.core.baseclasses.tileentity.BaseTileWithInventory;
import elec332.eflux.EFlux;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 4-4-2015.
 */
public abstract class BaseMachineTEWithInventory extends BaseTileWithInventory implements IEnergyReceiver {

    public void modifyEnergyStored(int energy) {
        storage.modifyEnergyStored(energy);
    }

    private EnergyStorage storage;

    public BaseMachineTEWithInventory(int maxEnergy, int maxReceive, int invSize) {
        super(invSize);
        this.storage = new EnergyStorage(maxEnergy);
        this.storage.setMaxReceive(maxReceive);
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName()?this.customInventoryName: EFlux.ModID+"."+standardInventoryName();
    }

    protected abstract String standardInventoryName();

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return this.storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return this.storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return this.storage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.storage.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        this.storage.writeToNBT(tagCompound);
    }
}
