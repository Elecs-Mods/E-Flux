package elec332.eflux.tileentity.multiblock;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.util.IElecItemHandler;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.multiblock.machine.MultiBlockGrinder;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 10-9-2015.
 */
@RegisteredTileEntity("TileEntityEFluxDustStorage")
public class TileEntityMultiBlockDustStorage extends AbstractTileEntityMultiBlock implements IElecItemHandler {

    @Nonnull
    private ItemStack stored = ItemStackHelper.NULL_STACK;
    private boolean redstone;

    @Override
    public void onNeighborBlockChange(Block block) {
        if (!getWorld().isRemote) {
            boolean b = getWorld().isBlockIndirectlyGettingPowered(pos) != 0;
            if (redstone != b) {
                if (b) {
                    pulse();
                }
                redstone = b;
            }
        }
    }

    private void pulse(){
        if (!ItemStackHelper.isStackValid(stored) && getMultiBlock() != null){
            stored = ((MultiBlockGrinder)getMultiBlock()).extractItem();
            markDirty();
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (stored != null) {
            NBTTagCompound tag = new NBTTagCompound();
            stored.writeToNBT(tag);
            tag.setTag("storedDust", tag);
        }
        tagCompound.setBoolean("lastRedstone", redstone);
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("storedDust")){
            stored = ItemStackHelper.loadItemStackFromNBT(tagCompound.getCompoundTag("storedDust"));
        }
        redstone = tagCompound.getBoolean("lastRedstone");
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == 0){
            return stored;
        }
        return ItemStackHelper.NULL_STACK;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack; //No insertion possible
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == 0 && amount > 0 && ItemStackHelper.isStackValid(stored)){
            ItemStack ret = ItemStackHelper.copyItemStack(stored);
            if (ret.stackSize <= amount){
                this.stored = ItemStackHelper.NULL_STACK;
                return ret;
            } else {
                ret.stackSize = amount;
                this.stored.stackSize -= amount;
                return ret;
            }
        }
        return ItemStackHelper.NULL_STACK;
    }

    @Override
    public int getSlotLimit(int i) {
        return 64;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot != 0){
            throw new IllegalArgumentException();
        }
        stored = stack;
    }

}
