package elec332.eflux.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import elec332.core.baseclasses.tileentity.BaseTileWithInventory;
import elec332.eflux.EFlux;
import elec332.eflux.util.IComparatorOverride;
import elec332.eflux.util.IEFluxTile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 4-4-2015.
 */
public abstract class BaseMachineTEWithInventory extends BaseTileWithInventory implements IEnergyReceiver, IEFluxTile, IComparatorOverride {

    public void modifyEnergyStored(int energy) {
        storage.modifyEnergyStored(energy);
    }

    private EnergyStorage storage;

    public BaseMachineTEWithInventory(int maxEnergy, int maxReceive, int invSize) {
        super(invSize);
        this.storage = new EnergyStorage(maxEnergy);
        this.storage.setMaxReceive(maxReceive);
    }

    public boolean timeCheck() {
        return this.worldObj.getTotalWorldTime() % 32L == 0L;
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

    public boolean openGui(EntityPlayer player){
        player.openGui(EFlux.instance, 0, worldObj, xCoord, yCoord, zCoord);
        return true;
    }

    public abstract Object getGuiServer(EntityPlayer player);

    public abstract Object getGuiClient(EntityPlayer player);

    @Override
    public int getComparatorInputOverride(int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        return (int) Math.ceil(((float) this.getEnergyStored(dir) / (float) this.getMaxEnergyStored(dir) * 15.0));
    }

    @Override
    public int getLightOpacity() {
        return 0;
    }

    @Override
    public int getLightValue() {
        return 0;
    }

    @Override
    public void onBlockRemoved() {
    }

    @Override
    public void onBlockAdded() {
    }

    @Override
    public void onBlockPlacedBy(EntityLivingBase entityLiving, ItemStack stack) {
    }

    @Override
    public void onNeighborBlockChange(Block block) {
    }

    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ){
        return false;
    }
}
