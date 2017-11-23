package elec332.eflux.tileentity;

import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInfoProvider;
import elec332.core.api.info.IInformation;
import elec332.core.server.ServerHelper;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.api.energy.container.IEFluxPowerHandler;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.util.BreakableMachineInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

/**
 * Created by Elec332 on 1-5-2015.
 */
public abstract class TileEntityBreakableMachine extends TileEntityEFlux implements IBreakableMachine, IInfoProvider, IEFluxPowerHandler {

    public TileEntityBreakableMachine(){
        super();
        this.energyContainer = new EnergyContainer(this, this);
    }

    private boolean broken = false;
    protected EnergyContainer energyContainer;

    public abstract ItemStack getRandomRepairItem();

    @Override
    public void onBroken(){
        if (!getWorld().isRemote) {
            breakableMachineInventory = new BreakableMachineInventory(this, getRandomRepairItem());
            for (EntityPlayerMP playerMP : ServerHelper.instance.getAllPlayersWatchingBlock(getWorld(), pos)){
                playerMP.connection.sendPacket(getUpdatePacket());
            }
        }
    }

    @Override
    public void markObjectDirty() {
        markDirty();
    }

    @Override
    public boolean isBroken() {
        return broken;
    }

    private BreakableMachineInventory breakableMachineInventory;

    public BreakableMachineInventory getBreakableMachineInventory() {
        return breakableMachineInventory;
    }

    @Override
    public void setBroken(boolean broken) {
        if (!broken)
            this.breakableMachineInventory = null;
        this.broken = broken;
        notifyNeighborsOfChange();
        syncData();
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        createConnectionPoints();
    }

    @Override
    public void onLoad() {
        createConnectionPoints();
    }

    protected void createConnectionPoints(){
    }

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (broken) return openBrokenGui(player);
        return onBlockActivatedSafe(state, player, hand, side, hitX, hitY, hitZ);
    }

    public boolean onBlockActivatedSafe(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZZ){
        return false;
    }

    private boolean openBrokenGui(EntityPlayer player){
        openWindow(player, EFlux.proxy, 1);
        return true;
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        energyContainer.readFromNBT(tagCompound);
        this.broken = tagCompound.getBoolean("broken");
        if (broken) {
            this.breakableMachineInventory = new BreakableMachineInventory(this, ItemStackHelper.loadItemStackFromNBT(tagCompound.getCompoundTag("itemRep")));
        }
    }

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        energyContainer.writeToNBT(tagCompound);
        tagCompound.setBoolean("broken", broken);
        if (broken) {
            NBTTagCompound newTag = new NBTTagCompound();
            breakableMachineInventory.getRepairItem().writeToNBT(newTag);
            tagCompound.setTag("itemRep", newTag);
        }
    }

    @Override
    public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData) {
        NBTTagCompound tag = hitData.getData();
        DecimalFormat df = new DecimalFormat("#.##");
        information.add("Voltage: "+df.format(tag.getDouble("ef")));
        information.add("Amps: "+df.format(tag.getDouble("rp")));
        information.add("Resistance: "+df.format(getResistance())+" ohms");//"\u2126"));
        //information.addInformation("Energy: "+tag.getInteger("energy")+"/"+tag.getInteger("maxEnergy"));
    }

    @Nonnull
    @Override
    public NBTTagCompound getInfoNBTData(@Nonnull NBTTagCompound tag, TileEntity tile, @Nonnull EntityPlayerMP player, @Nonnull IInfoDataAccessorBlock hitData) {
        tag.setInteger("energy", energyContainer.getStoredPower());
        tag.setInteger("maxEnergy", energyContainer.getMaxStoredEnergy());
        tag.setDouble("ef", energyContainer.lastEf);
        tag.setDouble("rp", energyContainer.lastRP);
        return tag;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == EFluxAPI.ENERGY_CAP || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        return capability == EFluxAPI.ENERGY_CAP ? (T) energyContainer : super.getCapability(capability, facing);
    }

}
