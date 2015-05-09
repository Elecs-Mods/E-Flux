package elec332.eflux.tileentity;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.api.util.IMultiMeterDataProviderMultiLine;
import elec332.eflux.util.BreakableMachineInventory;
import elec332.eflux.util.CalculationHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 1-5-2015.
 */
public abstract class BreakableMachineTile extends TileBase implements IEnergyReceiver, IMultiMeterDataProviderMultiLine {

    public abstract ItemStack getRandomRepairItem();

    public abstract float getAcceptance();

    public abstract int getEFForOptimalRP();

    public abstract int getMaxEF(int rp);

    public void onBroken(){
        if (!worldObj.isRemote)
            breakableMachineInventory = new BreakableMachineInventory(this, getRandomRepairItem());
        sendPacket(1, new NBTTagCompound());
    }

    private boolean broken = false;
    protected int storedPower = 0;

    public boolean isBroken() {
        return broken;
    }

    private BreakableMachineInventory breakableMachineInventory;

    public BreakableMachineInventory getBreakableMachineInventory() {
        return breakableMachineInventory;
    }

    public void setBroken(boolean broken) {
        if (!broken)
            this.breakableMachineInventory = null;
        else if (!this.broken) onBroken();
        this.broken = broken;
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (broken) return openBrokenGui(player);
        return onBlockActivatedSafe(player, side, hitX, hitY, hitZ);
    }

    public boolean onBlockActivatedSafe(EntityPlayer player, int side, float hitX, float hitY, float hitZ){
        return false;
    }

    private boolean openBrokenGui(EntityPlayer player){
        //if (breakableMachineInventory == null)
        //    breakableMachineInventory = new BreakableMachineInventory(this, getRandomRepairItem());
        player.openGui(EFlux.instance, 1, worldObj, xCoord, yCoord, zCoord);
        return true;
    }

    /**
     * @param rp        The Redstone Potential in the network
     * @param direction The requested direction
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    @Override
    public int getRequestedEF(int rp, ForgeDirection direction) {
        if (rp > requestedRP(direction)*(1+getAcceptance()))
            setBroken(true);
        if (rp < requestedRP(direction)*(1-getAcceptance()) || broken)
            return 0;
        return CalculationHelper.calcRequestedEF(rp, requestedRP(direction), getEFForOptimalRP(), getMaxEF(rp), getAcceptance());
    }

    /**
     * @param direction the direction where the power will be provided to
     * @param rp        the RedstonePotential in the network
     * @param ef        the amount of EnergeticFlux that is being provided
     * @return The amount of EnergeticFlux that wasn't used
     */
    @Override
    public int receivePower(ForgeDirection direction, int rp, int ef) {
        if (!broken) {
            receivePower(rp, ef, direction);
            return 0;
        } else return ef;
    }

    protected abstract void receivePower(int rp, int ef, ForgeDirection direction);

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        this.storedPower = tagCompound.getInteger("power");
        this.broken = tagCompound.getBoolean("broken");
        if (broken) {
            this.breakableMachineInventory = new BreakableMachineInventory(this, ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("itemRep")));
        }
    }

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        tagCompound.setInteger("power", storedPower);
        tagCompound.setBoolean("broken", broken);
        if (broken) {
            NBTTagCompound newTag = new NBTTagCompound();
            breakableMachineInventory.getRepairItem().writeToNBT(newTag);
            tagCompound.setTag("itemRep", newTag);
        }
    }

    @Override
    public void onTileLoaded() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
    }

    @Override
    public void onTileUnloaded() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(this));
    }
}
