package elec332.eflux.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import elec332.core.baseclasses.tileentity.BaseTileWithInventory;
import elec332.eflux.EFlux;
import elec332.eflux.util.IComparatorOverride;
import elec332.eflux.util.IEFluxMachine;
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
public abstract class BaseMachineTEWithInventory extends BaseTileWithInventory implements IEnergyReceiver, IEFluxTile, IComparatorOverride, IEFluxMachine {

    public void modifyEnergyStored(int energy) {
        storage.modifyEnergyStored(energy);
    }

    private EnergyStorage storage;
    private int comparatorPower;

    public BaseMachineTEWithInventory(int maxEnergy, int maxReceive, int invSize) {
        super(invSize);
        this.storage = new EnergyStorage(maxEnergy);
        this.storage.setMaxReceive(maxReceive);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.timeCheck()){
            int newComp = (int) Math.ceil(((float) this.getEnergyStored(ForgeDirection.UNKNOWN) / (float) this.getMaxEnergyStored(ForgeDirection.UNKNOWN) * 15.0));
            if (this.comparatorPower != newComp){
                this.comparatorPower = newComp;
                this.markDirty();
            }
        }
    }

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
        return this.comparatorPower;
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

    @Override
    public void onWrenched(ForgeDirection forgeDirection) {
        switch (forgeDirection){
            case NORTH:
               this.setBlockMetadataWithNotify(0);
                break;
            case EAST:
                this.setBlockMetadataWithNotify(1);
                break;
            case SOUTH:
                this.setBlockMetadataWithNotify(2);
                break;
            case WEST:
                this.setBlockMetadataWithNotify(3);
                break;
            default:
                break;
        }
        markDirty();
    }

    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ){
        return openGui(player);
    }
}
