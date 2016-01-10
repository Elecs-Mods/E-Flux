package elec332.eflux.tileentity;

import elec332.core.compat.handlers.WailaCompatHandler;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.api.util.IMultiMeterDataProviderMultiLine;
import elec332.eflux.util.BreakableMachineInventory;
import mcp.mobius.waila.api.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 1-5-2015.
 */
public abstract class BreakableMachineTile extends EnergyTileBase implements IEnergyReceiver, IMultiMeterDataProviderMultiLine, IBreakableMachine, WailaCompatHandler.IWailaInfoTile {

    public BreakableMachineTile(){
        super();
        this.energyContainer = new EnergyContainer(getMaxStoredPower(), getEFForOptimalRP(), getAcceptance(), getRequestedRP(), this);
    }

    private boolean broken = false;
    protected EnergyContainer energyContainer;

    public abstract ItemStack getRandomRepairItem();

    public abstract float getAcceptance();

    protected abstract int getMaxStoredPower();

    /**
     * The amount returned here is NOT supposed to change, anf if it does,
     * do not forget that it will receive a penalty if the machine is not running at optimum RP
     *
     * @return the amount of requested EF
     */
    public abstract int getEFForOptimalRP();

    public abstract int getRequestedRP();

    @Override
    public void onBroken(){
        if (!worldObj.isRemote)
            breakableMachineInventory = new BreakableMachineInventory(this, getRandomRepairItem());
        sendPacket(1, new NBTTagCompound());
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
        notifyNeighboursOfDataChange();
        syncData();
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (broken) return openBrokenGui(player);
        return onBlockActivatedSafe(player, side, hitX, hitY, hitZ);
    }

    public boolean onBlockActivatedSafe(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
        return false;
    }

    private boolean openBrokenGui(EntityPlayer player){
        //if (breakableMachineInventory == null)
        //    breakableMachineInventory = new BreakableMachineInventory(this, getRandomRepairItem());
        player.openGui(EFlux.instance, 1, worldObj, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public int getRequestedEF(int rp, EnumFacing direction) {
        return energyContainer.getRequestedEF(rp, direction);
    }

    @Override
    public int receivePower(EnumFacing direction, int rp, int ef) {
        return energyContainer.receivePower(direction, rp, ef);
    }

    @Override
    public int requestedRP(EnumFacing direction) {
        return energyContainer.requestedRP(direction);
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        energyContainer.readFromNBT(tagCompound);
        this.broken = tagCompound.getBoolean("broken");
        if (broken) {
            this.breakableMachineInventory = new BreakableMachineInventory(this, ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("itemRep")));
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
    public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag != null){
            currentTip.add("Energy: "+tag.getInteger("energy")+"/"+tag.getInteger("maxEnergy"));
            if (tag.getBoolean("broken")){
                currentTip.add(SpecialChars.ALIGNCENTER+SpecialChars.ITALIC+"Broken");
            }
        }
        return currentTip;
    }

    @Override
    public NBTTagCompound getWailaTag(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        tag.setInteger("energy", energyContainer.getStoredPower());
        tag.setInteger("maxEnergy", energyContainer.getMaxStoredEnergy());
        tag.setBoolean("broken", broken);
        return tag;
    }

    @Override
    public String[] getProvidedData() {
        return new String[]{
            "energy: "+energyContainer.getStoredPower(),
                "maxEnergy: "+energyContainer.getMaxStoredEnergy(),
                "broken: "+broken
        };
    }

}
