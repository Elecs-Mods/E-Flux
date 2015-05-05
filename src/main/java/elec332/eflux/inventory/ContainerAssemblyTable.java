package elec332.eflux.inventory;

import com.google.common.collect.Lists;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.items.circuits.CircuitBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class ContainerAssemblyTable extends BaseContainer {
    public ContainerAssemblyTable(EntityPlayer player, IInventory inv) {
        super(player);
        this.addSlotToContainer(new Slot(inv, 0, 124, 35));
        inv.openInventory();
        if (inv.getStackInSlot(0) != null && inv.getStackInSlot(0).getItem() instanceof ICircuit) {
            IInventory circuit = CircuitBase.get(inv.getStackInSlot(0));
            int a = ((ICircuit)inv.getStackInSlot(0).getItem()).boardSize(inv.getStackInSlot(0));
            int q = 0;
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    assemblies.add((SlotAssembly) this.addSlotToContainer(new SlotAssembly(circuit, j + i * 3, 30 + j * 18, 17 + i * 18)));
                }
            }
            circuit.openInventory();
        }
        addPlayerInventoryToContainer();
        syncSlots();
    }

    private List<SlotAssembly> assemblies = Lists.newArrayList();

    private void syncSlots(){
        ItemStack stack = getSlot(0).inventory.getStackInSlot(0);
        if (stack!= null && stack.getItem() instanceof ICircuit){
            int i = ((ICircuit)stack.getItem()).boardSize(stack);
            //for (int j = 1; j < 10; j++) {
            for (SlotAssembly assembly : assemblies)
                assembly.setI(i);
                //Slot s = getSlot(j);
                //((SlotAssembly)s).setI(i);

            //}
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        syncSlots();
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        getSlot(1).inventory.closeInventory();
        getSlot(0).inventory.closeInventory();
    }
}
