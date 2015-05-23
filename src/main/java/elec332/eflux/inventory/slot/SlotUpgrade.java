package elec332.eflux.inventory.slot;

import elec332.eflux.api.circuit.ICircuit;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 23-5-2015.
 */
public class SlotUpgrade extends Slot{
    public SlotUpgrade(IInventory inventory, int slotID, int x, int z) {
        super(inventory, slotID, x, z);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ICircuit;
    }
}
