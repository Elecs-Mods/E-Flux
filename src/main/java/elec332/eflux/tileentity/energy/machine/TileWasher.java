package elec332.eflux.tileentity.energy.machine;

import com.google.common.collect.Lists;
import elec332.core.client.inventory.BaseGuiContainer;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.FluidRegister;
import elec332.eflux.items.GroundMesh;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.tileentity.TileEntityProcessingMachine;
import elec332.eflux.util.DustPile;
import elec332.eflux.util.GrinderRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
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
        if (!(GroundMesh.isValidMesh(getStackInSlot(0)) && checkFluids() && checkOutput())){
            fluid = false;
            output = false;
            return false;
        }
        return true;
    }

    private boolean fluid, output;

    private boolean checkFluids(){
        if (!fluid){
             if (checkWater()){
                 fluid = true;
                 return true;
             }
        }
        return true;
    }

    private boolean checkWater() {
        int i = 0;
        DustPile dustPile = DustPile.fromNBT(getStackInSlot(0).stackTagCompound);
        if (!dustPile.clean)
            return false;
        for (DustPile.DustPart dustPart : dustPile.getContent()) {
            if (dustPart.getContent().equals(GrinderRecipes.stoneDust)) {
                i += dustPart.getNuggetAmount();
            }
        }
        i *= 200;
        return waterTank.getFluidAmount() >= i && slibTank.fill(new FluidStack(FluidRegister.slib, i), false) == i;
    }


    private boolean checkOutput(){
        if (!output){
            if (getStackInSlot(1) == null){
                output = true;
                return true;
            }
            if (!GroundMesh.isValidMesh(getStackInSlot(1)))
                return false;
            if (DustPile.fromNBT(getStackInSlot(0).stackTagCompound).sameContents(DustPile.fromNBT(getStackInSlot(1).stackTagCompound))){
                output = true;
                return true;
            }
        }
        return true;
    }

     @Override
     public void onProcessDone() {
         System.out.println("Process");
        if (getStackInSlot(0) == null)
            return;
         System.out.println("Process continue");
        fluid = false;
        output = false;
        int i = 0;
        ItemStack copy = inventory.getStackInSlot(0).copy();
        DustPile dustPile = DustPile.fromNBT(copy.stackTagCompound);
        List<DustPile.DustPart> toRemove = Lists.newArrayList();
        for (DustPile.DustPart dustPart : dustPile.getContent()) {
            if (dustPart.getContent().equals(GrinderRecipes.stoneDust)) {
                i += dustPart.getNuggetAmount();
                toRemove.add(dustPart);
            }
        }
        i *= 200;
        waterTank.drain(i, true);
        slibTank.fill(new FluidStack(FluidRegister.slib, i), true);
        dustPile.pure = true;
        dustPile.getContent().removeAll(toRemove);
        inventory.decrStackSize(0, 1);
        if (getStackInSlot(1) == null){
            copy.stackSize = 1;
            setInventorySlotContents(1, copy);
        }
        getStackInSlot(1).stackSize++;
        inventory.markDirty();
    }

    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return direction.getOpposite() == getTileFacing();
    }

    @Override
    protected void registerMachineSlots(List<Slot> registerList) {
        oneInputSlot(registerList);
        oneOutPutSlot(registerList);
    }

    @Override
    public int getRequiredPowerPerTick() {
        return 40;
    }

    @Override
    public int getProcessTime() {
        return 2;
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
                return new EFluxResourceLocation("washer");
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
