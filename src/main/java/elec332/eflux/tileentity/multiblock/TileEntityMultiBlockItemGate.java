package elec332.eflux.tileentity.multiblock;

import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInformation;
import elec332.core.api.inventory.IDefaultInventory;
import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.tile.IActivatableMachine;
import elec332.core.util.BasicInventory;
import elec332.eflux.multiblock.EFluxMultiBlockMachine;
import elec332.eflux.tileentity.basic.TileEntityBlockMachine;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 4-9-2015.
 */
//TODO: Interface
@RegisteredTileEntity("TileEntityEFluxMultiBlockItemGate")
public class TileEntityMultiBlockItemGate extends TileEntityBlockMachine implements ISidedInventory, IDefaultInventory, IActivatableMachine, ITickable {

    public TileEntityMultiBlockItemGate(){
        super();
        this.inventory = new BasicInventory("itemGate", 3, this);
    }

    private BasicInventory inventory;
    private int mode;
    private boolean hasFilter;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
    }

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        tagCompound.setInteger("mode", mode);
        tagCompound.setBoolean("f", hasFilter);
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        mode = tagCompound.getInteger("mode");
        hasFilter = tagCompound.getBoolean("f");
    }

    @Override
    public void onBlockRemoved() {
        InventoryHelper.dropInventoryItems(getWorld(), pos, this);
        super.onBlockRemoved();
    }

    @SuppressWarnings("unused")
    public boolean hasFilter() {
        return hasFilter;
    }

    @Override
    public void update() {
        if (!getWorld().isRemote && isInputMode() && timeCheck() && getMultiBlock() != null){
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                inventory.setInventorySlotContents(i, (((EFluxMultiBlockMachine)getMultiBlock())).inject(inventory.getStackInSlot(i)));
            }
        }
    }

    @Override
    public boolean onWrenched(EnumFacing forgeDirection) {
        if (getMultiBlock() == null) {
            if (forgeDirection == getTileFacing()) {
                if (mode == 0) {
                    mode = 1;
                } else {
                    mode = 0;
                }
            } else {
                setFacing(forgeDirection);
            }
            markDirty();
            syncData();
            reRenderBlock();
            return true;
        }
        return false;
    }

    @Override
    public boolean canFaceUpOrDown() {
        return true;
    }

    @Override
    public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData) {
        information.addInformation("Mode: " + (isOutputMode() ? "output" : "input"));
        super.addInformation(information, hitData);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == getTileFacing() ? allSlots(inventory.getSizeInventory()) : new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
        return side == getTileFacing() && isInputMode() && isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
        return side == getTileFacing() && isOutputMode();
    }

    private int[] allSlots(int i){
        int[] ret = new int[i];
        for (int j = 0; j < i; j++) {
            ret[j] = j;
        }
        return ret;
    }

    public boolean isOutputMode(){
        return mode == 0;
    }

    public boolean isInputMode(){
        return mode == 1;
    }

    @Nonnull
    @Override
    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isActive() {
        return isInputMode();
    }

}
