package elec332.eflux.tileentity.energy.machine;

import elec332.eflux.api.energy.container.IProgressMachine;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.tileentity.BreakableMachineTileWithSlots;
import elec332.eflux.util.DustPile;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 17-10-2015.
 */
public class TileRubbleSieve extends BreakableMachineTileWithSlots implements IProgressMachine{

    public TileRubbleSieve() {
        super(4);
        energyContainer.setProgressMachine(this);
    }

    private static final int rubble_slot = 2;
    private static final int normal_output_slot = 3;

    private ItemStack sieving, tbo;

    @Override
    public void updateEntity() {
        super.updateEntity();
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
            ItemStack stack = getStackInSlot(0);
            if (stack == null){
                stack = getStackInSlot(1);
            }
            if (stack == null)
                return false;
            ItemStack copy = stack.copy();
            copy.stackSize = 1;
            DustPile dustPile = DustPile.fromNBT(copy.stackTagCompound);
            ItemStack rubble = dustPile.sieve();
            ItemStack sis = getStackInSlot(rubble_slot);
            if (rubble != null && sis != null && sis.stackSize + rubble.stackSize > getInventoryStackLimit())
                return false;
            sis = getStackInSlot(normal_output_slot);
            copy.stackTagCompound = dustPile.toNBT();
            if (sis != null && (!ItemStack.areItemStackTagsEqual(sis, copy) || !(copy.stackSize + sis.stackSize > getInventoryStackLimit())))
                return false;
            sieving = copy;
            tbo = rubble;
        }
        return true;
    }

    @Override
    public void onProcessDone() {
        ItemStack stack = tbo.copy();
        if (getStackInSlot(rubble_slot) != null){
            stack.stackSize += getStackInSlot(rubble_slot).stackSize;
        }
        setInventorySlotContents(rubble_slot, stack);
        stack = sieving.copy();
        if (getStackInSlot(normal_output_slot) != null){
            stack.stackSize += getStackInSlot(normal_output_slot).stackSize;
        }
        setInventorySlotContents(normal_output_slot, stack);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (sieving != null){
            NBTTagCompound tag = new NBTTagCompound();
            sieving.writeToNBT(tag);
            tagCompound.setTag("sievingStack", tag);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("sievingStack")){
            sieving = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("sievingStack"));
        } else {
            sieving = null;
        }
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.iron_ingot);
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
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return direction != ForgeDirection.UP && direction != ForgeDirection.DOWN;
    }

    @Override
    public String[] getProvidedData() {
        return new String[0];
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        switch (direction){
            case UP:
                return new int[]{0, 1};
            case DOWN:
                return new int[]{rubble_slot, normal_output_slot};
            default:
                return new int[0];
        }
    }

    private boolean isInput(int side, int slot){
        int[] i = getAccessibleSlotsFromSide(side);
        return i.length != 0 && i[0] == 0 && slot >= 0 && slot <= 1;
    }

    private boolean isOutput(int side, int slot){
        int[] i = getAccessibleSlotsFromSide(side);
        return i.length != 0 && i[0] == rubble_slot && slot >= 2 && slot <= 3;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return isInput(side, slot) && !(stack == null || stack.getItem() != ItemRegister.groundMesh || stack.stackTagCompound == null);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return isOutput(side, slot);
    }

}
