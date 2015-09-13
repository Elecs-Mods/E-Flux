package elec332.eflux.tileentity.multiblock;

import elec332.core.util.BasicInventory;
import elec332.eflux.blocks.BlockMachinePart;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 4-9-2015.
 */
//TODO: Interface
public class TileEntityMultiBlockItemGate extends BlockMachinePart.TileEntityBlockMachine implements ISidedInventory{

    public TileEntityMultiBlockItemGate(){
        super();
        this.inventory = new BasicInventory("itemGate", 3, this);
    }

    private BasicInventory inventory;
    private int mode;

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        tagCompound.setInteger("mode", mode);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        mode = tagCompound.getInteger("mode");
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote && timeCheck() && getMultiBlock() != null){
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                inventory.setInventorySlotContents(i, getMultiBlock().inject(inventory.getStackInSlot(i)));
            }
        }
    }

    @Override
    public void onWrenched(ForgeDirection forgeDirection) {
        if (getMultiBlock() == null) {
            if (forgeDirection == getTileFacing() && mode == 0) {
                mode = 1;
            } else if (mode != 0){
                mode = 0;
            }
            markDirty();
        }
        super.onWrenched(forgeDirection);
        syncData();
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        currentTip.add("Mode: " + (isOutputMode() ? "output" : "input"));
        return currentTip;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return ForgeDirection.getOrientation(side) == getTileFacing() ? allSlots(inventory.getSizeInventory()) : new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return ForgeDirection.getOrientation(side) == getTileFacing() && isInputMode() && isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return ForgeDirection.getOrientation(side) == getTileFacing() && isOutputMode();
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

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public String getInventoryName() {
        return inventory.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return inventory.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public void openInventory() {
        inventory.openInventory();
    }

    @Override
    public void closeInventory() {
        inventory.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

}
