package elec332.eflux.util;

import cpw.mods.fml.relauncher.Side;
import elec332.core.helper.ItemHelper;
import elec332.core.inventory.BaseContainer;
import elec332.core.main.ElecCore;
import elec332.core.util.IRunOnce;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.client.inventory.GuiStandardFormat;
import elec332.eflux.inventory.ContainerSingleSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 1-5-2015.
 */
public class BreakableMachineInventory implements IInventory{

    public BreakableMachineInventory(IBreakableMachine tile, ItemStack s){
        this.i = tile;
        this.repairItem = s;
    }

    private ItemStack[] inventoryContent = new ItemStack[1];
    private IBreakableMachine i;
    private ItemStack repairItem;

    public ItemStack getRepairItem() {
        return repairItem;
    }

    public Object brokenGui(Side side, EntityPlayer player){
        BaseContainer container = new ContainerSingleSlot(this, player){
            @Override
            public ItemStack slotClick(int p_75144_1_, int p_75144_2_, int p_75144_3_, final EntityPlayer p_75144_4_) {
                ElecCore.tickHandler.registerCall(new IRunOnce() {
                    @Override
                    public void run() {
                        if (canFix())
                            p_75144_4_.closeScreen();
                    }
                });
                return super.slotClick(p_75144_1_, p_75144_2_, p_75144_3_, p_75144_4_);
            }

        };
        if (side==Side.CLIENT)
            return new GuiStandardFormat(container, new ResourceLocation("nope.png"));
        return container;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventoryContent[slot];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if(this.inventoryContent[p_70298_1_] != null) {
            ItemStack itemstack;
            if(this.inventoryContent[p_70298_1_].stackSize <= p_70298_2_) {
                itemstack = this.inventoryContent[p_70298_1_];
                this.inventoryContent[p_70298_1_] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.inventoryContent[p_70298_1_].splitStack(p_70298_2_);
                if(this.inventoryContent[p_70298_1_].stackSize == 0) {
                    this.inventoryContent[p_70298_1_] = null;
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if(this.inventoryContent[p_70304_1_] != null) {
            ItemStack itemstack = this.inventoryContent[p_70304_1_];
            this.inventoryContent[p_70304_1_] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.inventoryContent[p_70299_1_] = p_70299_2_;
        if(p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit()) {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "BrokenMachine";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return i.isBroken();
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {
        canFix();
    }

    public boolean canFix(){
        if (ItemHelper.areItemsEqual(inventoryContent[0], repairItem)) {
            i.setBroken(false);
            inventoryContent[0] = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return ItemHelper.areItemsEqual(p_94041_2_, repairItem);
    }
}
