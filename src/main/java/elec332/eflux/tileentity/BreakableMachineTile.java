package elec332.eflux.tileentity;

import elec332.core.compat.handlers.WailaCompatHandler;
import elec332.core.server.ServerHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.api.energy.container.IEFluxPowerHandler;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.api.util.IMultiMeterDataProviderMultiLine;
import elec332.eflux.util.BreakableMachineInventory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
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
public abstract class BreakableMachineTile extends EnergyTileBase implements IEnergyReceiver, IMultiMeterDataProviderMultiLine, IBreakableMachine, WailaCompatHandler.IWailaInfoTile, IEFluxPowerHandler {

    public BreakableMachineTile(){
        super();
        this.energyContainer = new EnergyContainer(getMaxStoredPower(), this, this);
    }

    private boolean broken = false;
    protected EnergyContainer energyContainer;

    public abstract ItemStack getRandomRepairItem();

    protected abstract int getMaxStoredPower();

    @Override
    public int getOptimalRP() {
        return getRequestedRP();
    }

    public abstract int getRequestedRP();

    @Override
    public void onBroken(){
        if (!worldObj.isRemote) {
            breakableMachineInventory = new BreakableMachineInventory(this, getRandomRepairItem());
            for (EntityPlayerMP playerMP : ServerHelper.instance.getAllPlayersWatchingBlock(worldObj, pos)){
                playerMP.playerNetServerHandler.sendPacket(getDescriptionPacket());
            }
        }
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
                "broken: "+broken,
                "facing: "+getTileFacing()
        };
    }

}
