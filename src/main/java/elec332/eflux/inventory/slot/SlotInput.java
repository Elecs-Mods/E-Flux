package elec332.eflux.inventory.slot;

import elec332.eflux.tileentity.TileEntityProcessingMachine;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 5-5-2015.
 */
public abstract class SlotInput extends Slot {
    public SlotInput(TileEntityProcessingMachine tile, int index, int x, int z) {
        super(tile.getInventory(), index, x, z);
        this.tile = tile;
    }

    protected TileEntityProcessingMachine tile;

    @Override
    public abstract boolean isItemValid(ItemStack itemStack);

    public abstract ItemStack getOutput();

    public abstract void consumeOnProcess();
}
