package elec332.eflux.tileentity.multiblock;

import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInformation;
import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.tile.IActivatableMachine;
import elec332.core.util.ItemStackHelper;
import elec332.core.util.WrappedItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 4-9-2015.
 */
@RegisteredTileEntity("TileEntityEFluxMultiBlockItemGate")
public class TileEntityMultiBlockItemGate extends AbstractTileEntityMultiBlockHandler<IItemHandler> implements IActivatableMachine {

    public TileEntityMultiBlockItemGate(){
        super();
        this.itemHandler = new WrappedItemHandler() {

            @Nonnull
            @Override
            protected IItemHandler getItemHandler() {
                return getMultiBlockHandler();
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return isInputMode() ? super.insertItem(slot, stack, simulate) : stack;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return isOutputMode() ? super.extractItem(slot, amount, simulate) : ItemStackHelper.NULL_STACK;
            }

        };
    }

    private IItemHandlerModifiable itemHandler;
    private int mode;
    private boolean hasFilter;

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        tagCompound.setInteger("mode", mode);
        tagCompound.setBoolean("f", hasFilter);
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        mode = tagCompound.getInteger("mode");
        hasFilter = tagCompound.getBoolean("f");
    }

    @Override
    public boolean onWrenched(EnumFacing forgeDirection) {
        if (getMultiBlock() == null) {
            if (forgeDirection == getTileFacing()) {
                if (mode == 0) {
                    mode = 1;
                } else {
                    mode = 0;
                }
            } else {
                setFacing(forgeDirection);
            }
            markDirty();
            syncData();
            reRenderBlock();
            return true;
        }
        return false;
    }

    @Override
    public boolean canFaceUpOrDown() {
        return true;
    }

    @Override
    public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData) {
        information.addInformation("Mode: " + (isOutputMode() ? "output" : "input"));
        super.addInformation(information, hitData);
    }

    public boolean isOutputMode(){
        return mode == 0;
    }

    public boolean isInputMode(){
        return mode == 1;
    }

    @Nullable
    private IItemHandler getInv(EnumFacing side){
        if (side == getTileFacing() && getMultiBlockHandler() != null){
            return itemHandler;
        }
        return null;
    }

    @Override
    public boolean isActive() {
        return isOutputMode();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, boolean hasMultiBlock) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && getInv(facing) != null || super.hasCapability(capability, facing, hasMultiBlock);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, boolean hasMultiBlock) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) getInv(facing) : super.getCapability(capability, facing, hasMultiBlock);
    }

    @Override
    protected Capability<IItemHandler> getCapability() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

}
