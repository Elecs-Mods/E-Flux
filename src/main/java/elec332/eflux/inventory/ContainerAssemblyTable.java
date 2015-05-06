package elec332.eflux.inventory;

import com.google.common.collect.Lists;
import elec332.core.helper.ItemHelper;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.circuit.IElectricComponent;
import elec332.eflux.inventory.slot.SlotAssembly;
import elec332.eflux.util.BasicInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class ContainerAssemblyTable extends BaseContainer {
    public ContainerAssemblyTable(EntityPlayer player, IInventory inv) {
        super(player);
        this.addSlotToContainer(new Slot(inv, 0, 124, 35));
        inv.openInventory();
        this.circuit = new InventoryCircuit();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                assemblies.add((SlotAssembly) this.addSlotToContainer(new SlotAssembly(circuit, j + i * 3, 30 + j * 18, 17 + i * 18)));
            }
        }
        circuit.openInventory();
        if (inv.getStackInSlot(0) != null && inv.getStackInSlot(0).getItem() instanceof ICircuit)
            circuit.setStack(inv.getStackInSlot(0));
        addPlayerInventoryToContainer();
        syncSlots();
    }

    private List<SlotAssembly> assemblies = Lists.newArrayList();
    private InventoryCircuit circuit;

    private void syncSlots(){
        ItemStack stack = getSlot(0).inventory.getStackInSlot(0);
        if (stack!= null && stack.getItem() instanceof ICircuit){
            circuit.setStack(stack);
            int i = ((ICircuit)stack.getItem()).boardSize(stack);
            for (SlotAssembly assembly : assemblies) {
                assembly.setI(i);
                if (i > assembly.getSlotIndex())
                    assembly.validItem = ((ICircuit)stack.getItem()).getRequiredComponent(assembly.getSlotIndex(), stack);
            }
        } else {
            for (SlotAssembly assembly : assemblies) {
                assembly.setI(0);
                assembly.validItem = null;
            }
            circuit.removeStack();
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        circuit.validate();
        syncSlots();
    }

    @Override
    public ItemStack slotClick(int slotID, int var2, int var3, EntityPlayer player) {
        if (slotID  < 10) {

            syncSlots();
        }
        return super.slotClick(slotID, var2, var3, player);
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        getSlot(1).inventory.closeInventory();
        getSlot(0).inventory.closeInventory();
    }

    private class InventoryCircuit extends BasicInventory implements IInventory{
        private InventoryCircuit(){
            super("NAME", 9);
        }

        public void setStack(ItemStack stack) {
            if (stack == null || !(stack.getItem() instanceof ICircuit))
                throw new IllegalArgumentException("Item not instance of ICircuit");
            this.stack = stack;
            openInventory();
        }

        public void removeStack(){
            closeInventory();
            this.stack = null;
        }

        private ItemStack stack;
        private boolean formed = false;

        @Override
        public void openInventory() {
            if (stack != null)
                readFromNBT(stack.getTagCompound());
        }

        @Override
        public void closeInventory() {
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                writeToNBT(tag);
                stack.setTagCompound(tag);
                inventoryContents = new ItemStack[getSizeInventory()];
            }
        }

        @Override
        public void markDirty() {
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                writeToNBT(tag);
                stack.setTagCompound(tag);
            }
        }

        public void validate(){
            if (stack != null) {
                formed = isValid();
                markDirty();
            }
        }

        private boolean isValid(){
            ICircuit circuit = (ICircuit) stack.getItem();
            for (int i = 0; i < circuit.boardSize(stack); i++) {
                if (!ItemHelper.areItemsEqual(circuit.getRequiredComponent(i, stack), getStackInSlot(i)))
                    return false;
            }
            return true;
        }

        @Override
        public boolean isItemValidForSlot(int id, ItemStack stack) {
            return stack.getItem() instanceof IElectricComponent;
        }

        @Override
        public int getInventoryStackLimit() {
            return 1;
        }

        @Override
        public void writeToNBT(NBTTagCompound compound) {
            NBTTagList nbttaglist = new NBTTagList();
            for(int i = 0; i < this.inventoryContents.length; ++i) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte)i);
                if (this.inventoryContents[i] != null)
                    this.inventoryContents[i].writeToNBT(tag);
                nbttaglist.appendTag(tag);
            }
            compound.setTag("Items", nbttaglist);
            compound.setBoolean("valid", formed);
        }

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            this.inventoryContents = new ItemStack[this.getSizeInventory()];
            if (stack.hasTagCompound()) {
                NBTTagList nbttaglist = compound.getTagList("Items", 10);
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    NBTTagCompound tag = nbttaglist.getCompoundTagAt(i);
                    int j = tag.getByte("Slot") & 255;
                    if (j >= 0 && j < this.inventoryContents.length) {
                        this.inventoryContents[j] = ItemStack.loadItemStackFromNBT(tag);
                    }
                }
                this.formed = compound.getBoolean("valid");
            }
        }
    }
}
