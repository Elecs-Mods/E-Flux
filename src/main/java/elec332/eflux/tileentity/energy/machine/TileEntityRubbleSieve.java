package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.ItemStackHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.energy.container.IProgressMachine;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.tileentity.TileEntityBreakableMachine;
import elec332.eflux.util.DustPile;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 17-10-2015.
 */
@RegisteredTileEntity("TileEntityEFluxRubbleSieve")
public class TileEntityRubbleSieve extends TileEntityBreakableMachine implements IProgressMachine, ITickable {

    public TileEntityRubbleSieve() {
        super();
        input = new BasicItemHandler(2){

            @Override
            public boolean canInsert(int slot, @Nonnull ItemStack stack) {
                if (!ItemStackHelper.isStackValid(stack) || stack.getItem() != ItemRegister.groundMesh || stack.getTagCompound() == null) {
                    return false;
                }
                DustPile dustPile = DustPile.fromNBT(stack.getTagCompound());
                return dustPile != null && dustPile.scanned;
            }

        };
        output = new BasicItemHandler(2);
        energyContainer.setProgressMachine(this);
    }

    private static final int rubble_slot = 0;
    private static final int normal_output_slot = 1;

    private final BasicItemHandler input, output;

    private ItemStack sieving, tbo;
    private int r;

    @Override
    public void update() {
        if (!getWorld().isRemote){
            energyContainer.tick();
        }
    }

    @Override
    public void onBlockRemoved() {
        WorldHelper.dropInventoryItems(getWorld(), pos, input);
        WorldHelper.dropInventoryItems(getWorld(), pos, output);
        super.onBlockRemoved();
    }

    @Override
    public int getRequiredPowerPerTick() {
        return 50;
    }

    @Override
    public int getProcessTime() {
        return 90;
    }

    @Override
    public boolean canProcess() {
        if (sieving == null){
            int slot = 0;
            ItemStack stack = input.getStackInSlot(0);
            if (!ItemStackHelper.isStackValid(stack)){
                stack = input.getStackInSlot(1);
                slot = 1;
            }
            if (!ItemStackHelper.isStackValid(stack)) {
                return false;
            }
            ItemStack copy = stack.copy();
            copy.stackSize = 1;
            DustPile dustPile = DustPile.fromNBT(copy.getTagCompound());
            r += dustPile.sieve();
            ItemStack rubble = null;
            if (r >= 9){
                rubble = ItemRegister.scrap.copy();
                rubble.stackSize = 0;
                while (r >= 9 && rubble.stackSize < 64){
                    r -= 9;
                    rubble.stackSize += 1;
                }
            }
            ItemStack sis = output.getStackInSlot(rubble_slot);
            if (rubble != null && ItemStackHelper.isStackValid(sis) && sis.stackSize + rubble.stackSize > 64) {
                return false;
            }
            if (dustPile.getSize() > 0) { //Check whether pile is empty
                sis = output.getStackInSlot(normal_output_slot);
                copy.setTagCompound(dustPile.toNBT()); //Returned tag can not be null (check above)
                if (ItemStackHelper.isStackValid(sis)&& (!ItemStack.areItemStackTagsEqual(sis, copy) || !(copy.stackSize + sis.stackSize > 64))) {
                    return false;
                }
                sieving = copy;
            } else {
                sieving = null;
            }
            input.extractItem(slot, 1, true);
            tbo = rubble;
            markDirty();
        }
        return true;
    }

    @Override
    public void onProcessDone() {
        ItemStack stack;
        if (tbo != null) {
            stack = tbo.copy();
            if (ItemStackHelper.isStackValid(output.getStackInSlot(rubble_slot))) {
                stack.stackSize += output.getStackInSlot(rubble_slot).stackSize;
            }
            output.setStackInSlot(rubble_slot, stack);
        }
        if (sieving != null) {
            stack = sieving.copy();
            if (ItemStackHelper.isStackValid(output.getStackInSlot(normal_output_slot))) {
                stack.stackSize += output.getStackInSlot(normal_output_slot).stackSize;
            }
            output.setStackInSlot(normal_output_slot, stack);
        }
        tbo = null;
        sieving = null;
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (sieving != null){
            NBTTagCompound tag = new NBTTagCompound();
            sieving.writeToNBT(tag);
            tagCompound.setTag("sievingStack", tag);
        }
        if (tbo != null){
            NBTTagCompound tag = new NBTTagCompound();
            tbo.writeToNBT(tag);
            tagCompound.setTag("tbo", tag);
        }
        tagCompound.setInteger("sA", r);
        tagCompound.setTag("input", input.serializeNBT());
        tagCompound.setTag("output", output.serializeNBT());
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("sievingStack")){
            sieving = ItemStackHelper.loadItemStackFromNBT(tagCompound.getCompoundTag("sievingStack"));
        }
        if (tagCompound.hasKey("tbo")){
            tbo = ItemStackHelper.loadItemStackFromNBT(tagCompound.getCompoundTag("tbo"));
        }
        input.deserializeNBT(tagCompound.getCompoundTag("input"));
        output.deserializeNBT(tagCompound.getCompoundTag("output"));
        r = tagCompound.getInteger("sA");
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.IRON_INGOT);
    }

    @Override
    public int getWorkingVoltage() {
        return 25;
    }

    @Override
    public float getAcceptance() {
        return 0.2f;
    }

    @Override
    public int getMaxRP() {
        return 12;
    }

    @Override
    public double getResistance() {
        return 2;
    }

    @Nonnull
    @Override
    public ConnectionPoint getConnectionPoint(int post) {
        return post == 0 ? cp1 : cp2;
    }

    @Nullable
    @Override
    public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec) {
        return side != getTileFacing().getOpposite() ? null : (hitVec.y > 0.5 ? cp2 : cp1);
    }

    @Override
    protected void createConnectionPoints() {
        cp1 = new ConnectionPoint(pos, world, getTileFacing().getOpposite(), 1);
        cp2 = new ConnectionPoint(pos, world, getTileFacing().getOpposite(), 2);
    }

    private ConnectionPoint cp1, cp2;
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return isItemSide(capability, facing) ? (T) (facing == EnumFacing.UP ? input : output) : super.getCapability(capability, facing);
    }

    private boolean isItemSide(Capability<?> capability, EnumFacing facing){
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (facing == EnumFacing.UP || facing == EnumFacing.DOWN);
    }

}
