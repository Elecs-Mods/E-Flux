package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.tile.IActivatableMachine;
import elec332.core.util.BasicInventory;
import elec332.eflux.tileentity.basic.TileEntityBlockMachine;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

/**
 * Created by Elec332 on 4-9-2015.
 */
//TODO: Interface
@RegisterTile(name = "TileEntityEFluxMultiBlockItemGate")
public class TileEntityMultiBlockItemGate extends TileEntityBlockMachine implements ISidedInventory, IActivatableMachine {

    public TileEntityMultiBlockItemGate(){
        super();
        this.inventory = new BasicInventory("itemGate", 3, this);
    }

    private BasicInventory inventory;
    private int mode;
    private boolean hasFilter;

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
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
        InventoryHelper.dropInventoryItems(worldObj, pos, this);
        super.onBlockRemoved();
    }

    @SuppressWarnings("unused")
    public boolean hasFilter() {
        return hasFilter;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void update() {
        super.update();
        if (!worldObj.isRemote && isInputMode() && timeCheck() && getMultiBlock() != null){
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                inventory.setInventorySlotContents(i, (getMultiBlock()).inject(inventory.getStackInSlot(i)));
            }
        }
    }

    @Override
    public void onWrenched(EnumFacing forgeDirection) {
        if (getMultiBlock() == null && forgeDirection == getTileFacing()) {
            if (mode == 0) {
                mode = 1;
            } else {
                mode = 0;
            }
            markDirty();
        } else {
            super.onWrenched(forgeDirection);
        }
        syncData();
        reRenderBlock();
    }

    @Override
    public boolean canFaceUpOrDown() {
        return true;
    }

    @Override
    public boolean onBlockActivatedBy(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldObj.isRemote){
            player.addChatComponentMessage(new TextComponentString("Mode: " + (isOutputMode() ? "output" : "input")));
        }
        return super.onBlockActivatedBy(player, side, hitX, hitY, hitZ);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        currentTip.add("Mode: " + (isOutputMode() ? "output" : "input"));
        return currentTip;
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
    public ItemStack removeStackFromSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return inventory.hasCustomName();
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
    public ITextComponent getDisplayName() {
        return null;
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
    public void openInventory(EntityPlayer player) {
        inventory.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        inventory.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isActive() {
        return isInputMode();
    }
}
