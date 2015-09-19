package elec332.eflux.tileentity.energy.machine;

import com.google.common.collect.Lists;
import elec332.core.client.inventory.BaseGuiContainer;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.slot.SlotOutput;
import elec332.core.inventory.widget.FluidTankWidget;
import elec332.core.inventory.widget.WidgetProgressArrow;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.FluidRegister;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.items.GroundMesh;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.tileentity.TileEntityProcessingMachine;
import elec332.eflux.util.DustPile;
import elec332.eflux.util.GrinderRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

/**
 * Created by Elec332 on 13-9-2015.
 */
public class TileWasher extends TileEntityProcessingMachine implements IFluidHandler{

    public TileWasher(){
        super(2, 0);
        waterTank = new FluidTank(12 * FluidContainerRegistry.BUCKET_VOLUME);
        slibTank = new FluidTank(9 * FluidContainerRegistry.BUCKET_VOLUME);
    }

    private FluidTank waterTank, slibTank;
    private ItemStack gonnaBeOutputted;

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagCompound tag = new NBTTagCompound();
        waterTank.writeToNBT(tag);
        tagCompound.setTag("wTank", tag);
        tag = new NBTTagCompound();
        slibTank.writeToNBT(tag);
        tagCompound.setTag("sTank", tag);
        if (gonnaBeOutputted != null){
            tag = new NBTTagCompound();
            gonnaBeOutputted.writeToNBT(tag);
            tagCompound.setTag("GBO", tag);
        }
    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        waterTank.readFromNBT(tagCompound.getCompoundTag("wTank"));
        slibTank.readFromNBT(tagCompound.getCompoundTag("sTank"));
        if (tagCompound.hasKey("GBO"))
            gonnaBeOutputted = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("GBO"));
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Blocks.iron_bars);
    }

    @Override
    public float getAcceptance() {
        return 0.1f;
    }

    @Override
    protected int getMaxStoredPower() {
        return 3000;
    }

    @Override
    public int getEFForOptimalRP() {
        return 14;
    }

    @Override
    public int getRequestedRP() {
        return 7;
    }

    @Override
    public boolean canProcess() {
        if (!((GroundMesh.isValidMesh(getStackInSlot(0)) || gonnaBeOutputted != null) && checkOutput())){
            fluid = false;
            gonnaBeOutputted = null;
            stone = 0;
            return false;
        }
        return true;
    }

    private boolean fluid;
    private int stone;

    private boolean checkFluids(){
        if (!fluid){
             if (checkWater()){
                 fluid = true;
                 return true;
             }
            return false;
        }
        return true;
    }

    private boolean checkWater() {
        int i = stone;
        i *= 200;
        return waterTank.getFluidAmount() >= i && slibTank.getFluidAmount() + i <= slibTank.getCapacity();
    }


    private boolean checkOutput(){
        if (gonnaBeOutputted == null){
            DustPile dustPile = DustPile.fromNBT(getStackInSlot(0).stackTagCompound);
            stone = dustPile.wash();
            ItemStack transformed = new ItemStack(ItemRegister.groundMesh);
            transformed.stackTagCompound = dustPile.toNBT();
            if ((getStackInSlot(1) == null || (ItemStack.areItemStackTagsEqual(getStackInSlot(1), transformed) && getStackInSlot(1).stackSize < getInventoryStackLimit())) && checkFluids()){
                gonnaBeOutputted = transformed;
                decrStackSize(0, 1);
                markDirty();
                return true;
            }
            stone = 0;
            return false;
        }
        return true;
    }

    @Override
    public void onProcessDone() {
        fluid = false;
        int i = stone;
        i *= 200;
        waterTank.drain(i, true);
        slibTank.fill(new FluidStack(FluidRegister.slib, i), true);
        stone = 0;
        if (gonnaBeOutputted.stackTagCompound != null) {
            if (getStackInSlot(1) == null) {
                setInventorySlotContents(1, gonnaBeOutputted.copy());
            } else {
                getStackInSlot(1).stackSize++;
            }
        }
        gonnaBeOutputted = null;
        inventory.markDirty();
    }


    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return direction != getTileFacing();
    }

    @Override
    public void addSlots(BaseContainer baseContainer) {
        baseContainer.addSlotToContainer(new Slot(inventory, 0, 51, 34));
        baseContainer.addSlotToContainer(new SlotOutput(inventory, 1, 109, 34));
        baseContainer.addPlayerInventoryToContainer();
        baseContainer.addWidget(new FluidTankWidget(130, 19, 176, 0, 32, 44, slibTank));
        baseContainer.addWidget(new FluidTankWidget(14, 19, 176, 0, 32, 44, waterTank));
        baseContainer.addWidget(new WidgetProgressArrow(76, 33, this, true));
    }

    @Override
    protected void registerMachineSlots(List<Slot> registerList) {
    }

    @Override
    public int getRequiredPowerPerTick() {
        return 40;
    }

    @Override
    public int getProcessTime() {
        return 200;
    }

    @Override
    public float getProgressScaled(int progress) {
        return progress/200f;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return waterTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(slibTank.getFluid())) {
            return null;
        }
        return slibTank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return slibTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid == FluidRegistry.WATER;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid == FluidRegister.slib;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{waterTank.getInfo(), slibTank.getInfo()};
    }

    @Override
    public EnumRecipeMachine getMachine() {
        return null;
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new BaseGuiContainer(getGuiServer(player)) {
            @Override
            public ResourceLocation getBackgroundImageLocation() {
                return new EFluxResourceLocation("gui/washer.png");
            }
        };
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slot == 0 && GroundMesh.isValidMesh(stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 1;
    }
}
