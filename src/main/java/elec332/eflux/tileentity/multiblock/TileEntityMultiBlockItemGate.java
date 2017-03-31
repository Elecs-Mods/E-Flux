package elec332.eflux.tileentity.multiblock;

import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInformation;
import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.tile.IActivatableMachine;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.WrappedItemHandler;
import elec332.core.world.WorldHelper;
import elec332.eflux.multiblock.EFluxMultiBlockMachine;
import elec332.eflux.tileentity.basic.TileEntityBlockMachine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 4-9-2015.
 */
//TODO: Interface
@RegisteredTileEntity("TileEntityEFluxMultiBlockItemGate")
public class TileEntityMultiBlockItemGate extends TileEntityBlockMachine implements IActivatableMachine, ITickable {

    public TileEntityMultiBlockItemGate(){
        super();
        this.inventory = new BasicItemHandler(3);
        in = WrappedItemHandler.wrap(inventory, true, false);
        out = WrappedItemHandler.wrap(inventory, false, true);
    }

    private BasicItemHandler inventory;
    private IItemHandler in, out;
    private int mode;
    private boolean hasFilter;

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.deserializeNBT(tagCompound);
    }

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
    public void onBlockRemoved() {
        WorldHelper.dropInventoryItems(getWorld(), pos, inventory);
        super.onBlockRemoved();
    }

    @SuppressWarnings("unused")
    public boolean hasFilter() {
        return hasFilter;
    }

    @Override
    public void update() {
        if (!getWorld().isRemote && isInputMode() && timeCheck() && getMultiBlock() != null){
            for (int i = 0; i < inventory.getSlots(); i++) {
                inventory.setStackInSlot(i, (((EFluxMultiBlockMachine)getMultiBlock())).inject(inventory.getStackInSlot(i)));
            }
        }
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

    private int[] allSlots(int i){
        int[] ret = new int[i];
        for (int j = 0; j < i; j++) {
            ret[j] = j;
        }
        return ret;
    }

    public boolean isOutputMode(){
        return mode == 0;
    }

    public boolean isInputMode(){
        return mode == 1;
    }

    @Nullable
    private IItemHandler getInv(EnumFacing side){
        if (side == getTileFacing()){
            if (isInputMode()){
                return in;
            }
            return out;
        }
        return null;
    }

    @Override
    public boolean isActive() {
        return isInputMode();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, boolean hasMultiBlock) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing, hasMultiBlock);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, boolean hasMultiBlock) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) getInv(facing) : super.getCapability(capability, facing, hasMultiBlock);
    }

}
