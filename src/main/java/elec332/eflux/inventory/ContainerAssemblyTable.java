package elec332.eflux.inventory;

import com.google.common.collect.Lists;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.circuit.IElectricComponent;
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
        //if (inv.getStackInSlot(0) != null && inv.getStackInSlot(0).getItem() instanceof ICircuit) {
        this.circuit = new getInv();
        //int a = ((ICircuit)inv.getStackInSlot(0).getItem()).boardSize(inv.getStackInSlot(0));
        //int q = 0;
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
    private getInv circuit;

    private void syncSlots(){
        ItemStack stack = getSlot(0).inventory.getStackInSlot(0);
        if (stack!= null && stack.getItem() instanceof ICircuit){
            circuit.setStack(stack);
            int i = ((ICircuit)stack.getItem()).boardSize(stack);
            //for (int j = 1; j < 10; j++) {
            for (SlotAssembly assembly : assemblies) {
                assembly.setI(i);
            }
                //Slot s = getSlot(j);
                //((SlotAssembly)s).setI(i);

            //}
        } else circuit.removeStack();
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        syncSlots();
    }

    @Override
    public ItemStack slotClick(int p_75144_1_, int p_75144_2_, int p_75144_3_, EntityPlayer p_75144_4_) {
        if (p_75144_1_ == 0)
            syncSlots();
        return super.slotClick(p_75144_1_, p_75144_2_, p_75144_3_, p_75144_4_);
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        getSlot(1).inventory.closeInventory();
        getSlot(0).inventory.closeInventory();
    }


    private class getInv extends BasicInventory implements IInventory{
        private getInv(){
            super("NAME", 9);//((ICircuit) stack.getItem()).boardSize(stack));
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

        @Override
        public boolean isItemValidForSlot(int id, ItemStack stack) {
            return stack.getItem() instanceof IElectricComponent;
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
