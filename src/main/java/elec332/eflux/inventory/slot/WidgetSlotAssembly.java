package elec332.eflux.inventory.slot;

import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.util.InventoryHelper;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.api.circuit.IElectricComponent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Elec332 on 17-12-2016.
 */
public class WidgetSlotAssembly extends WidgetSlot {

    public WidgetSlotAssembly(IItemHandler inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public void setI(int i){
        this.i = i;
    }

    private int i = 0;
    public ItemStack validItem = ItemStackHelper.NULL_STACK;

    @Override
    public boolean isItemValid(ItemStack stack) {
        return i > getSlotIndex() && ItemStackHelper.isStackValid(validItem) && stack.getItem() instanceof IElectricComponent && InventoryHelper.areEqualNoSizeNoNBT(stack, validItem);
    }

}
