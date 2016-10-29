package elec332.eflux.multiblock;

import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInformation;
import elec332.core.multiblock.AbstractMultiBlock;
import elec332.core.util.InventoryHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.api.energy.container.IEFluxPowerHandler;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockItemGate;
import elec332.eflux.util.BreakableMachineInventory;
import elec332.eflux.util.MultiBlockLogic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 28-7-2015.
 */
public abstract class EFluxMultiBlockMachine extends AbstractMultiBlock implements IBreakableMachine, IEFluxPowerHandler {

    public EFluxMultiBlockMachine(){
        super();
        energyContainer = new EnergyContainer(getMaxStoredPower(), this, this);
        this.broken = false;
    }

    @CapabilityInject(IEnergyReceiver.class)
    private static Capability<IEnergyReceiver> CAPABILITY;

    private EnergyContainer energyContainer;
    private BreakableMachineInventory breakableMachineInventory;
    private boolean broken;
    private TileEntityMultiBlockItemGate gate;

    protected void setItemOutput(int length, int width, int height){
        this.gate = (TileEntityMultiBlockItemGate) getTileAtTranslatedPos(length, width, height);
    }

    @Override
    public void init() {
    }

    @Override
    public void markObjectDirty() {
        markDirty();
    }

    @Override
    public void onTick() {
        if (getWorldObj().getTotalWorldTime() % 32L == 0L) {
            markDirty();
        }
    }

    public ItemStack inject(ItemStack stack){
        return stack;
    }

    public boolean canAddToOutput(ItemStack stack){
        return InventoryHelper.addItemToInventory(gate, stack);
    }

    public void ejectStack(ItemStack stack){
        if (!canAddToOutput(stack)){
            MultiBlockLogic.dropStack(getWorldObj(), gate.getPos(), gate.getTileFacing(), stack);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        energyContainer.writeToNBT(tagCompound);
        tagCompound.setBoolean("broken", broken);
        if (broken) {
            NBTTagCompound newTag = new NBTTagCompound();
            breakableMachineInventory.getRepairItem().writeToNBT(newTag);
            tagCompound.setTag("itemRep", newTag);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        energyContainer.readFromNBT(tagCompound);
        this.broken = tagCompound.getBoolean("broken");
        if (broken) {
            this.breakableMachineInventory = new BreakableMachineInventory(this, ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("itemRep")));
        }
    }

    @Override
    public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData) {
        information.add("Power: "+energyContainer.getStoredPower());
        information.add("Broken: "+broken);
        super.addInformation(information, hitData);
    }

    public int getStoredPower(){
        return energyContainer.getStoredPower();
    }

    @Override
    public boolean isBroken() {
        return broken;
    }

    @Override
    public void setBroken(boolean broken) {
        if (!broken)
            this.breakableMachineInventory = null;
        this.broken = broken;
        markDirty();
    }

    @Override
    public void onBroken() {
        if (!getWorldObj().isRemote) {
            breakableMachineInventory = new BreakableMachineInventory(this, getRandomRepairItem());
            getSaveDelegate().syncData();
        }
    }

    @Override
    public void invalidate() {
    }

    @Override
    public boolean onAnyBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, BlockPos pos, IBlockState state) {
        if (broken)
            return openGui(player);
        return onAnyBlockActivatedSafe(player, hand, stack, pos, state);
    }

    public boolean onAnyBlockActivatedSafe(EntityPlayer player, EnumHand hand, ItemStack stack, BlockPos pos, IBlockState state){
        return false;
    }

    @Override
    public final Object getGui(EntityPlayer player, boolean client) {
        if (broken)
            return breakableMachineInventory.brokenGui(client ? Side.CLIENT : Side.SERVER, player);
        return getMachineGui(player, client);
    }

    @SuppressWarnings("unused")
    public Object getMachineGui(EntityPlayer player, boolean client){
        return null;
    }

    protected final EnergyContainer getEnergyContainer(){
        return this.energyContainer;
    }

    public boolean openGui(EntityPlayer player){
        return openGui(player, EFlux.instance);
    }

    protected abstract int getMaxStoredPower();

    public abstract ItemStack getRandomRepairItem();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSpecialCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        return capability == CAPABILITY ? (T) energyContainer : super.getCapability(capability, facing, pos);
    }

}
