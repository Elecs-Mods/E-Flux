package elec332.eflux.tileentity;

import elec332.core.util.NBTTypes;
import elec332.core.world.WorldHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 13-9-2015.
 */
public abstract class BreakableMachineTileWithSlots extends TileEntityBreakableMachine {

    public BreakableMachineTileWithSlots(IItemHandlerModifiable i){
        inventory = i;
    }

    protected IItemHandlerModifiable inventory;

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inventory, null, tagCompound.getTagList("Items", NBTTypes.COMPOUND.getID()));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("Items", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inventory, null));
        return tagCompound;
    }

    @Override
    public void onBlockRemoved() {
        WorldHelper.dropInventoryItems(getWorld(), pos, inventory);
        super.onBlockRemoved();
    }

}
