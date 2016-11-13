package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.client.inventory.BaseGuiContainer;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.slot.SlotOutput;
import elec332.core.inventory.widget.FluidTankWidget;
import elec332.core.inventory.widget.WidgetProgressArrow;
import elec332.core.util.FluidHelper;
import elec332.core.util.FluidTankWrapper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.FluidRegister;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.items.ItemEFluxGroundMesh;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.tileentity.TileEntityProcessingMachine;
import elec332.eflux.util.DustPile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 13-9-2015.
 */
@RegisteredTileEntity("TileEntityEFluxWasher")
public class TileEntityWasher extends TileEntityProcessingMachine implements ISidedInventory {

    public TileEntityWasher(){
        super(2, 0);
        waterTank = new FluidTank(12 * Fluid.BUCKET_VOLUME) {

            @Override
            public boolean canFillFluidType(FluidStack fluidStack) {
                return fluidStack != null && fluidStack.getFluid() == FluidRegistry.WATER;
            }

            @Override
            public boolean canDrainFluidType(FluidStack fluidStack) {
                return fluidStack == null || fluidStack.getFluid() == FluidRegistry.WATER;
            }
        };
        slibTank = new FluidTank(9 * Fluid.BUCKET_VOLUME) {

            @Override
            public boolean canFillFluidType(FluidStack fluidStack) {
                return fluidStack != null && fluidStack.getFluid() == FluidRegister.slib;
            }

            @Override
            public boolean canDrainFluidType(FluidStack fluidStack) {
                return fluidStack == null || fluidStack.getFluid() == FluidRegister.slib;
            }

        };
        wTnk = new FluidTankWrapper() {

            @Override
            protected IFluidTank getTank() {
                return waterTank;
            }

            @Override
            protected boolean canDrain() {
                return false;
            }

            @Override
            protected boolean canFillFluidType(FluidStack fluidStack) {
                return waterTank.canDrainFluidType(fluidStack);
            }

        };
        sTnk = new FluidTankWrapper() {

            @Override
            protected IFluidTank getTank() {
                return slibTank;
            }

            @Override
            protected boolean canFill() {
                return false;
            }

            @Override
            protected boolean canDrainFluidType(FluidStack fluidStack) {
                return slibTank.canDrainFluidType(fluidStack);
            }

        };


        capability = new FluidHandlerFluidMap();
        capability.addHandler(FluidRegistry.WATER, waterTank);
        capability.addHandler(FluidRegister.slib, slibTank);
    }

    private FluidTank waterTank, slibTank;
    private FluidTankWrapper wTnk, sTnk;
    private FluidHandlerFluidMap capability;
    private ItemStack gonnaBeOutputted;

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("wTank", wTnk.serializeNBT());
        tagCompound.setTag("sTank", sTnk.serializeNBT());
        if (gonnaBeOutputted != null){
            NBTTagCompound tag = new NBTTagCompound();
            gonnaBeOutputted.writeToNBT(tag);
            tagCompound.setTag("GBO", tag);
        }
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        wTnk.deserializeNBT(tagCompound.getCompoundTag("wTank"));
        sTnk.deserializeNBT(tagCompound.getCompoundTag("sTank"));
        if (tagCompound.hasKey("GBO"))
            gonnaBeOutputted = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("GBO"));
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Blocks.IRON_BARS);
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
        if (!((ItemEFluxGroundMesh.isValidMesh(getStackInSlot(0)) || gonnaBeOutputted != null) && checkOutput())){
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
            DustPile dustPile = DustPile.fromNBT(getStackInSlot(0).getTagCompound());
            if (!dustPile.clean)
                return false;
            stone = dustPile.wash();
            ItemStack transformed = new ItemStack(ItemRegister.groundMesh);
            transformed.setTagCompound(dustPile.toNBT());
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
        if (gonnaBeOutputted.getTagCompound() != null) {
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
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
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
    public boolean onBlockActivatedSafe(IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        return FluidHelper.tryDrainItem(player, hand, wTnk) || FluidHelper.tryFillItem(player, hand, sTnk) || super.onBlockActivatedSafe(state, player, hand, stack, side, hitX, hitY, hitZ);
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
    @Nonnull
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int slot, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
        return slot == 0 && ItemEFluxGroundMesh.isValidMesh(stack);
    }

    @Override
    public boolean canExtractItem(int slot, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
        return slot == 1;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T) this.capability :super.getCapability(capability, facing);
    }

}
