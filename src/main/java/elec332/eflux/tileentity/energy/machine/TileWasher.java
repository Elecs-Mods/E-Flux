package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.client.inventory.BaseGuiContainer;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.slot.SlotOutput;
import elec332.core.inventory.widget.FluidTankWidget;
import elec332.core.inventory.widget.WidgetProgressArrow;
import elec332.core.util.PlayerHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.FluidRegister;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.items.ItemEFluxGroundMesh;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.tileentity.TileEntityProcessingMachine;
import elec332.eflux.util.DustPile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.*;

import java.util.List;

/**
 * Created by Elec332 on 13-9-2015.
 */
@RegisterTile(name = "TileEntityEFluxWasher")
public class TileWasher extends TileEntityProcessingMachine implements IFluidHandler {

    public TileWasher(){
        super(2, 0);
        waterTank = new FluidTank(12 * FluidContainerRegistry.BUCKET_VOLUME);
        slibTank = new FluidTank(9 * FluidContainerRegistry.BUCKET_VOLUME);
    }

    private FluidTank waterTank, slibTank;
    private ItemStack gonnaBeOutputted;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
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
        return tagCompound;
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
    public boolean onBlockActivatedSafe(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = ItemStack.copyItemStack(player.inventory.getCurrentItem());
        if (stack != null && FluidContainerRegistry.isBucket(stack)){

            if (FluidContainerRegistry.isFilledContainer(stack)) {
                FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(stack);
                int used = fill(side, fs, false);
                if (used > 0 && used == fs.amount){
                    fill(side, fs, true);
                    if (!PlayerHelper.isPlayerInCreative(player)) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                        ItemStack d = FluidContainerRegistry.drainFluidContainer(stack);
                        if (!player.inventory.addItemStackToInventory(d)) {
                            WorldHelper.dropStack(worldObj, pos.offset(side), d);
                        }
                    }
                }
            } else if (FluidContainerRegistry.isEmptyContainer(stack)){
                int i = FluidContainerRegistry.getContainerCapacity(stack);
                FluidStack fs = drain(side, i, false);
                if (fs != null && fs.amount == i){
                    ItemStack add = FluidContainerRegistry.fillFluidContainer(fs, stack);
                    if (add != null) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                        if (!player.inventory.addItemStackToInventory(add)) {
                            WorldHelper.dropStack(worldObj, pos.offset(side), add);
                        }
                    }
                }
            }
            return !worldObj.isRemote;
        } else {
            return super.onBlockActivatedSafe(player, side, hitX, hitY, hitZ);
        }
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
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return waterTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(slibTank.getFluid())) {
            return null;
        }
        return slibTank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return slibTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid == FluidRegistry.WATER;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return fluid == FluidRegister.slib;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
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
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
        return slot == 0 && ItemEFluxGroundMesh.isValidMesh(stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
        return slot == 1;
    }
}
