package elec332.eflux.inventory;

import com.google.common.collect.Lists;
import elec332.core.inventory.ContainerMachine;
import elec332.core.main.ElecCore;
import elec332.core.util.BasicInventory;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.circuit.IElectricComponent;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.inventory.slot.SlotAssembly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

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
                return CircuitHelper.getCircuit(stack) != null;
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
        ItemStack stack = inv.getStackInSlot(0);
        if (CircuitHelper.isEtchedCircuit(stack)) {
            circuit.setStack(stack);
        }
        addPlayerInventoryToContainer();
        syncSlots();
        if (!player.worldObj.isRemote){
            tickable = new Runnable() {
                @Override
                public void run() {
                    canClick = assemblyTable.getStoredPower() >= 200;
                    for (IContainerListener crafting : listeners){
                        crafting.sendProgressBarUpdate(ContainerAssemblyTable.this, 3, canClick ? 1 : 0);
                    }
                }
            };
            ElecCore.tickHandler.registerTickable(tickable, Side.SERVER);
        }
    }

    private List<SlotAssembly> assemblies = Lists.newArrayList();
    private InventoryCircuit circuit;
    private EnergyContainer assemblyTable;
    private Runnable tickable;

    public boolean canClick = false;

    private void syncSlots(){
        ItemStack stack = getSlot(0).inventory.getStackInSlot(0);
        if (CircuitHelper.getCircuit(stack) != null){
            circuit.setStack(stack);
            int i = CircuitHelper.getCircuitBoardSize(stack);
            for (SlotAssembly assembly : assemblies) {
                assembly.setI(i);
                if (i > assembly.getSlotIndex()) {
                    assembly.validItem = CircuitHelper.getRequiredCircuitComponent(stack, assembly.getSlotIndex());
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
        if (slotID < 10) {
            syncSlots();
            circuit.validate();
            if (slotID > 0){
                canClick = false;
            }
        }
        ItemStack stack = super.slotClick(slotID, dragType, clickTypeIn, player);
        syncSlots();
        return stack;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        getSlot(1).inventory.closeInventory(player);
        getSlot(0).inventory.closeInventory(player);
        if (!player.worldObj.isRemote) {
            ElecCore.tickHandler.removeTickable(tickable);
        }
    }

    private class InventoryCircuit extends BasicInventory {

        private InventoryCircuit(){
            super("NAME", 9);
        }

        public void setStack(ItemStack stack) {
            if (CircuitHelper.getCircuit(stack) == null) {
                throw new IllegalArgumentException("Stack does not have ICircuit capability!");
            }
            this.stack = stack;
            checkOpen();
        }

        public void removeStack(){
            checkClose();
            this.stack = null;
        }

        private ItemStack stack;

        @Override
        public void openInventory(@Nonnull EntityPlayer player) {
            checkOpen();
        }

        @Override
        public void setInventorySlotContents(int slotID, ItemStack stack) {
            if (slotID >= inventoryContents.length){
                return;
            }
            super.setInventorySlotContents(slotID, stack);
        }

        protected void checkOpen(){
            if (stack != null) {
                ICircuit circuit = Objects.requireNonNull(CircuitHelper.getCircuit(stack));
                inventoryContents = circuit.getSolderedComponents();
            }
        }

        @Override
        public void closeInventory(@Nonnull EntityPlayer player) {
            checkClose();
        }

        protected void checkClose(){
            if (stack != null) {
                markDirty();
                inventoryContents = new ItemStack[0];
            }
        }

        @Override
        public void markDirty() {
            if (stack != null) {
                ICircuit circuit = Objects.requireNonNull(CircuitHelper.getCircuit(stack));
                circuit.setSolderedComponents(inventoryContents);;
            }
        }

        public void validate(){
            isValid();
            markDirty();
        }

        private boolean isValid(){
            if (stack != null) {
                ICircuit circuit = Objects.requireNonNull(CircuitHelper.getCircuit(stack));
                circuit.validate();
                return circuit.isValidCircuit();
            }
            return false;
        }

        @Override
        public boolean isItemValidForSlot(int id, ItemStack stack) {
            return stack.getItem() instanceof IElectricComponent;
        }

        @Override
        public int getInventoryStackLimit() {
            return 1;
        }

    }

}
