package elec332.eflux.inventory;

import com.google.common.collect.Lists;
import elec332.core.inventory.ContainerMachine;
import elec332.core.util.BasicInventory;
import elec332.core.util.InventoryHelper;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.circuit.IElectricComponent;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.grid.WorldRegistry;
import elec332.eflux.inventory.slot.SlotAssembly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;

import java.util.List;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class ContainerAssemblyTable extends ContainerMachine {

    public ContainerAssemblyTable(EntityPlayer player, IInventory inv, final EnergyContainer assemblyTable) {
        super(null, player, 0);
        this.assemblyTable = assemblyTable;
        this.addSlotToContainer(new Slot(inv, 0, 124, 35){

            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack != null && stack.getItem() instanceof ICircuit;
            }

        });
        inv.openInventory(player);
        this.circuit = new InventoryCircuit();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                assemblies.add((SlotAssembly) this.addSlotToContainer(new SlotAssembly(circuit, j + i * 3, 30 + j * 18, 17 + i * 18)));
            }
        }
        circuit.openInventory(player);
        if (inv.getStackInSlot(0) != null && inv.getStackInSlot(0).getItem() instanceof ICircuit && ((ICircuit)inv.getStackInSlot(0).getItem()).isCircuit(inv.getStackInSlot(0))) {
            circuit.setStack(inv.getStackInSlot(0));
        }
        addPlayerInventoryToContainer();
        syncSlots();
        if (!player.worldObj.isRemote){
            tickable = new ITickable() {
                @Override
                public void update() {
                    canClick = assemblyTable.getStoredPower() >= 200;
                    for (IContainerListener crafting : listeners){
                        crafting.sendProgressBarUpdate(ContainerAssemblyTable.this, 3, canClick ? 1 : 0);
                    }
                }
            };
            WorldRegistry.get(player.worldObj).addTickable(tickable);
        }
    }

    private List<SlotAssembly> assemblies = Lists.newArrayList();
    private InventoryCircuit circuit;
    private EnergyContainer assemblyTable;
    private ITickable tickable;

    public boolean canClick = false;

    private void syncSlots(){
        ItemStack stack = getSlot(0).inventory.getStackInSlot(0);
        if (stack!= null && stack.getItem() instanceof ICircuit && ((ICircuit)stack.getItem()).isCircuit(stack)){
            circuit.setStack(stack);
            int i = ((ICircuit)stack.getItem()).boardSize(stack);
            for (SlotAssembly assembly : assemblies) {
                assembly.setI(i);
                if (i > assembly.getSlotIndex()) {
                    assembly.validItem = ((ICircuit) stack.getItem()).getRequiredComponent(stack, assembly.getSlotIndex());
                }
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
        for (IContainerListener crafting : listeners){
            crafting.sendProgressBarUpdate(this, 3, canClick ? 1 : 0);
        }
    }

    @Override
    public void updateProgressBar(int id, int value) {
        if (id == 3){
            canClick = value == 1;
            return;
        }
        super.updateProgressBar(id, value);
    }

    @Override
    public ItemStack slotClick(int slotID, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if (slotID > 0 && slotID < 10 && (player.worldObj.isRemote || !assemblyTable.drainPower(200)) && !canClick) {
            detectAndSendChanges();
            return null;
        }
        if (slotID  < 10) {
            syncSlots();
            circuit.validate();
            if (slotID > 0){
                canClick = false;
            }
        }
        return super.slotClick(slotID, dragType, clickTypeIn, player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        getSlot(1).inventory.closeInventory(player);
        getSlot(0).inventory.closeInventory(player);
        if (!player.worldObj.isRemote) {
            WorldRegistry.get(player.worldObj).removeTickable(tickable);
        }
    }

    private class InventoryCircuit extends BasicInventory implements IInventory {

        private InventoryCircuit(){
            super("NAME", 9);
        }

        public void setStack(ItemStack stack) {
            if (stack == null || !(stack.getItem() instanceof ICircuit)) {
                throw new IllegalArgumentException("Item not instance of ICircuit");
            }
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            this.stack = stack;
            checkOpen();
        }

        public void removeStack(){
            checkClose();
            this.stack = null;
        }

        private ItemStack stack;
        private boolean formed = false;

        @Override
        public void openInventory(EntityPlayer player) {
            checkOpen();
        }

        protected void checkOpen(){
            if (stack != null) {
                readFromNBT(stack.getTagCompound());
            }
        }

        @Override
        public void closeInventory(EntityPlayer player) {
            checkClose();
        }

        protected void checkClose(){
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
                if (!InventoryHelper.areEqualNoSizeNoNBT(circuit.getRequiredComponent(stack, i), getStackInSlot(i))) {
                    return false;
                }
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
                if (this.inventoryContents[i] != null) {
                    this.inventoryContents[i].writeToNBT(tag);
                }
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
