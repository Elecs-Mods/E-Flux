package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.annotations.RegisterTile;
import elec332.eflux.api.energy.container.IProgressMachine;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.tileentity.BreakableMachineTileWithSlots;
import elec332.eflux.util.DustPile;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * Created by Elec332 on 17-10-2015.
 */
@RegisterTile(name = "TileEntityEFluxRubbleSieve")
public class TileEntityRubbleSieve extends BreakableMachineTileWithSlots implements IProgressMachine, ITickable, ISidedInventory {

    public TileEntityRubbleSieve() {
        super(4);
        energyContainer.setProgressMachine(this);
    }

    private static final int rubble_slot = 2;
    private static final int normal_output_slot = 3;

    private ItemStack sieving, tbo;
    private int r;

    @Override
    public void update() {
        if (!worldObj.isRemote){
            energyContainer.tick();
        }
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
            ItemStack stack = getStackInSlot(0);
            if (stack == null){
                stack = getStackInSlot(1);
                slot = 1;
            }
            if (stack == null) {
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
            ItemStack sis = getStackInSlot(rubble_slot);
            if (rubble != null && sis != null && sis.stackSize + rubble.stackSize > getInventoryStackLimit()) {
                return false;
            }
            if (dustPile.getSize() > 0) { //Check whether pile is empty
                sis = getStackInSlot(normal_output_slot);
                copy.setTagCompound(dustPile.toNBT()); //Returned tag can not be null (check above)
                if (sis != null && (!ItemStack.areItemStackTagsEqual(sis, copy) || !(copy.stackSize + sis.stackSize > getInventoryStackLimit()))) {
                    return false;
                }
                sieving = copy;
            } else {
                sieving = null;
            }
            decrStackSize(slot, 1);
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
            if (getStackInSlot(rubble_slot) != null) {
                stack.stackSize += getStackInSlot(rubble_slot).stackSize;
            }
            setInventorySlotContents(rubble_slot, stack);
        }
        if (sieving != null) {
            stack = sieving.copy();
            if (getStackInSlot(normal_output_slot) != null) {
                stack.stackSize += getStackInSlot(normal_output_slot).stackSize;
            }
            setInventorySlotContents(normal_output_slot, stack);
        }
        tbo = null;
        sieving = null;
    }

    @Override
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
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("sievingStack")){
            sieving = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("sievingStack"));
        }
        if (tagCompound.hasKey("tbo")){
            tbo = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("tbo"));
        }
        r = tagCompound.getInteger("sA");
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.IRON_INGOT);
    }

    @Override
    public float getAcceptance() {
        return 0.2f;
    }

    @Override
    protected int getMaxStoredPower() {
        return 300;
    }

    @Override
    public int getEFForOptimalRP() {
        return 12;
    }

    @Override
    public int getRequestedRP() {
        return 5;
    }

    @Override
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
        return direction != EnumFacing.UP && direction != EnumFacing.DOWN;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        switch (side){
            case UP:
                return new int[]{0, 1};
            case DOWN:
                return new int[]{rubble_slot, normal_output_slot};
            default:
                return new int[0];
        }
    }

    private boolean isInput(EnumFacing side, int slot){
        int[] i = getSlotsForFace(side);
        return i.length != 0 && i[0] == 0 && slot >= 0 && slot <= 1;
    }

    private boolean isOutput(EnumFacing side, int slot){
        int[] i = getSlotsForFace(side);
        return i.length != 0 && i[0] == rubble_slot && slot >= 2 && slot <= 3;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
        if (!isInput(side, slot) || stack == null || stack.getItem() != ItemRegister.groundMesh || stack.getTagCompound() == null) {
            return false;
        }
        DustPile dustPile = DustPile.fromNBT(stack.getTagCompound());
        return dustPile != null && dustPile.scanned;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
        return isOutput(side, slot);
    }

}
