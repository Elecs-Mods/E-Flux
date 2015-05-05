package elec332.eflux.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class BasicInventory implements IInventory {

    private String inventoryTitle;
    private int slotsCount;
    protected ItemStack[] inventoryContents;

    public BasicInventory(String p_i1561_1_, int p_i1561_3_) {
        this.inventoryTitle = p_i1561_1_;
        this.slotsCount = p_i1561_3_;
        this.inventoryContents = new ItemStack[p_i1561_3_];
    }

    public ItemStack getStackInSlot(int slotID) {
        return slotID >= 0 && slotID < this.inventoryContents.length ? this.inventoryContents[slotID] : null;
    }

    public ItemStack decrStackSize(int slotID, int size) {
        if (this.inventoryContents[slotID] != null) {
            ItemStack itemstack;
            if (this.inventoryContents[slotID].stackSize <= size) {
                itemstack = this.inventoryContents[slotID];
                this.inventoryContents[slotID] = null;
                this.markDirty();
                return itemstack;
            }
            else {
                itemstack = this.inventoryContents[slotID].splitStack(size);
                if (this.inventoryContents[slotID].stackSize == 0) {
                    this.inventoryContents[slotID] = null;
                }
                this.markDirty();
                return itemstack;
            }
        }
        else {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int slotID) {
        //if (this.inventoryContents[slotID] != null) {
        //    ItemStack itemstack = this.inventoryContents[slotID];
        //    this.inventoryContents[slotID] = null;
        ///   return itemstack;
        //}
        //else {
        return null;
        //
    }

    public void setInventorySlotContents(int slotID, ItemStack stack) {
        this.inventoryContents[slotID] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    public int getSizeInventory() {
        return this.slotsCount;
    }

    public String getInventoryName() {
        return this.inventoryTitle;
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void markDirty() {}

    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    public void openInventory() {}

    public void closeInventory() {}

    public boolean isItemValidForSlot(int id, ItemStack stack) {
        return true;
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.inventoryContents = new ItemStack[this.getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound tag = nbttaglist.getCompoundTagAt(i);
            int j = tag.getByte("Slot") & 255;
            if(j >= 0 && j < this.inventoryContents.length) {
                this.inventoryContents[j] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound) {
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < this.inventoryContents.length; ++i) {
            if(this.inventoryContents[i] != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte)i);
                this.inventoryContents[i].writeToNBT(tag);
                nbttaglist.appendTag(tag);
            }
        }
        compound.setTag("Items", nbttaglist);
    }
}
