package elec332.eflux.inventory;

import com.google.common.collect.Lists;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.window.IWindowListener;
import elec332.core.inventory.window.Window;
import elec332.core.main.ElecCore;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.circuit.IElectricComponent;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.inventory.slot.WidgetSlotAssembly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * Created by Elec332 on 17-12-2016.
 */
public class WindowAssemblyTable extends Window {

    public WindowAssemblyTable(IItemHandler inv, final EnergyContainer assemblyTable) {
        super();
        this.assemblyTable = assemblyTable;
        this.circuitItem = inv;
    }

    private List<WidgetSlotAssembly> assemblies = Lists.newArrayList();
    private InventoryCircuit circuit;
    private IItemHandler circuitItem;
    private EnergyContainer assemblyTable;
    private Runnable tickable;

    private boolean canClick = false;

    @Override
    protected void initWindow() {
        this.addWidget(new WidgetSlot(circuitItem, 0, 124, 35){

            @Override
            public boolean isItemValid(ItemStack stack) {
                return CircuitHelper.getCircuit(stack) != null;
            }
            
        });
        this.circuit = new InventoryCircuit();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                assemblies.add(this.addWidget(new WidgetSlotAssembly(circuit, j + i * 3, 30 + j * 18, 17 + i * 18)));
            }
        }
        circuit.checkOpen();
        ItemStack stack = circuitItem.getStackInSlot(0);
        if (CircuitHelper.isEtchedCircuit(stack)) {
            circuit.setStack(stack);
        }
        addPlayerInventoryToContainer();
        syncSlots();
        if (!getPlayer().getEntityWorld().isRemote){
            tickable = new Runnable() {

                @Override
                public void run() {
                    canClick = assemblyTable.getStoredPower() >= 200;
                    for (IWindowListener crafting : getListeners()){
                        crafting.sendProgressBarUpdate(3, canClick ? 1 : 0);
                    }
                }

            };
            ElecCore.tickHandler.registerTickable(tickable, Side.SERVER);
        }
    }

    private void syncSlots(){
        ItemStack stack = circuitItem.getStackInSlot(0);
        if (CircuitHelper.getCircuit(stack) != null){
            circuit.setStack(stack);
            int i = CircuitHelper.getCircuitBoardSize(stack);
            for (WidgetSlotAssembly assembly : assemblies) {
                assembly.setI(i);
                if (i > assembly.getSlotIndex()) {
                    assembly.validItem = CircuitHelper.getRequiredCircuitComponent(stack, assembly.getSlotIndex());
                }
            }
        } else {
            for (WidgetSlotAssembly assembly : assemblies) {
                assembly.setI(0);
                assembly.validItem = null;
            }
            circuit.removeStack();
        }
    }

    @Override
    public void detectAndSendChanges(Container from) {
        super.detectAndSendChanges(from);
        circuit.validate();
        syncSlots();
        for (IWindowListener crafting : getListeners()){
            crafting.sendProgressBarUpdate(3, canClick ? 1 : 0);
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
    @Nonnull
    public ItemStack slotClick(int slotID, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if (slotID > 0 && slotID < 10 && (player.getEntityWorld().isRemote || !assemblyTable.drainPower(200)) && !canClick) {
            detectAndSendChanges();
            return ItemStackHelper.NULL_STACK;
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
    public void onWindowClosed(EntityPlayer player) {
        super.onWindowClosed(player);
        if (!player.getEntityWorld().isRemote) {
            ElecCore.tickHandler.removeTickable(tickable);
        }
        circuit.checkClose();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void handleMouseClick(WidgetSlot slotIn, int slotId, int mouseButton, @Nonnull ClickType type) {
        if (!canClick && slotId > 0 && slotId < 10) {
            return;
        }
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
    }

    private class InventoryCircuit extends BasicItemHandler {

        private InventoryCircuit(){
            super(9);
        }

        public void setStack(ItemStack stack) {
            if (CircuitHelper.getCircuit(stack) == null) {
                throw new IllegalArgumentException("Stack does not have ICircuit capability!");
            }
            this.stack = stack;
            checkOpen();
        }

        private void removeStack(){
            checkClose();
            this.stack = null;
        }

        private ItemStack stack;

        private void checkOpen(){
            if (ItemStackHelper.isStackValid(stack)) {
                ICircuit circuit = Objects.requireNonNull(CircuitHelper.getCircuit(stack));
                stacks = (NonNullList<ItemStack>) circuit.getSolderedComponents();
            }
        }

        private void checkClose(){
            if (ItemStackHelper.isStackValid(stack)) {
                onContentsChanged(-1);
                stacks = NonNullList.withSize(0, ItemStackHelper.NULL_STACK);
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (ItemStackHelper.isStackValid(stack)) {
                ICircuit circuit = Objects.requireNonNull(CircuitHelper.getCircuit(stack));
                circuit.setSolderedComponents(stacks);
            }
        }

        public void validate(){
            isValid();
            onContentsChanged(-1);
        }

        private boolean isValid(){
            if (ItemStackHelper.isStackValid(stack)) {
                ICircuit circuit = Objects.requireNonNull(CircuitHelper.getCircuit(stack));
                circuit.validate();
                return circuit.isValidCircuit();
            }
            return false;
        }

        @Override
        public boolean isStackValidForSlot(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() instanceof IElectricComponent;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

    }

}
