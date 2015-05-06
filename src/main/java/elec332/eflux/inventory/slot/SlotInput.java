package elec332.eflux.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 5-5-2015.
 */
public abstract class SlotInput extends Slot {
    public SlotInput(IInventory inventory, int index, int x, int z) {
        super(inventory, index, x, z);
    }

    @Override
    public abstract boolean isItemValid(ItemStack itemStack);

    public abstract ItemStack getOutput();

    public abstract void consumeOnProcess();
}
