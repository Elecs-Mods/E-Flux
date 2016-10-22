package elec332.eflux.tileentity;

import elec332.core.api.inventory.IDefaultInventory;
import elec332.core.util.BasicInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 13-9-2015.
 */
public abstract class BreakableMachineTileWithSlots extends TileEntityBreakableMachine implements IDefaultInventory {

    public BreakableMachineTileWithSlots(int i){
        inventory = new BasicInventory("name", i, this);
    }

    protected BasicInventory inventory;

    @Nonnull
    @Override
    public BasicInventory getInventory() {
        return inventory;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        return tagCompound;
    }

    @Override
    public void onBlockRemoved() {
        InventoryHelper.dropInventoryItems(worldObj, pos, this);
        super.onBlockRemoved();
    }

}
