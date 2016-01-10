package elec332.eflux.inventory.slot;

import elec332.core.util.ItemHelper;
import elec332.eflux.api.circuit.IElectricComponent;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class SlotAssembly extends Slot {
    public SlotAssembly(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    public void setI(int i){
        this.i = i;
    }
    private int i = 0;
    public ItemStack validItem = null;

    @Override
    public boolean isItemValid(ItemStack stack) {
        return i > getSlotIndex() && stack.getItem() instanceof IElectricComponent && ItemHelper.areItemsEqual(stack, validItem);
    }
}
