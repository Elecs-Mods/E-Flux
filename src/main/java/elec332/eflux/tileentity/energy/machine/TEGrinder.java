package elec332.eflux.tileentity.energy.machine;

import elec332.core.util.BlockLoc;
import elec332.core.util.DirectionHelper;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.tileentity.BaseMultiBlockMachine;
import elec332.eflux.util.EnumMachines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Elec332 on 3-4-2015.
 */
@Deprecated //But don't delete yet
public class TEGrinder extends BaseMultiBlockMachine implements IEnergyReceiver{

    public TEGrinder() {
        //super(60000, 40, 18);
        super(18);
    }

    private int storedPower = 0;

    public int getStoredPower() {
        return storedPower;
    }

    @Override
    protected String standardInventoryName() {
        return "grinder";
    }

    @Override
    public Container getGuiServer(EntityPlayer player) {
        return null;//new TEGrinderContainer(this, player);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return null;//new GuiInventoryGrinder(this, player);
    }

    @Override
    protected boolean arePlayerConditionsMet(EntityPlayer player) {
        return true;
    }

    @Override
    protected boolean areBlocksAtRightLocations() {
        return getBlockAtOffset(-1, 0, -1) == Blocks.beacon && getBlockAtOffset(-1, 0, 1) == Blocks.beacon && getBlockAtOffset(1, 0, 1) == Blocks.beacon && getBlockAtOffset(1, 0, -1) == Blocks.beacon;
    }

    @Override
    protected List<BlockLoc> getIMultiBlockLocations() {
        ArrayList<BlockLoc> ret = new ArrayList<BlockLoc>();
        ret.add(new BlockLoc(0, 2, 0));
        return ret;
    }

    @Override
    protected void onCreated() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
    }

    @Override
    public void invalidateMultiBlock() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(this));
        super.invalidateMultiBlock();
    }

    @Override
    public void onTileLoaded() {
        super.onTileLoaded();
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
    }

    @Override
    public EnumMachines getMachine() {
        return EnumMachines.GRINDER;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return DirectionHelper.getDirectionFromNumber(getBlockMetadata()) == direction && this.isFormed();
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(ForgeDirection direction) {
        return 30;
    }

    /**
     * @param rp        The Redstone Potential in the network
     * @param direction The requested direction
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    @Override
    public int getRequestedEF(int rp, ForgeDirection direction) {
        if (isSlave())
            return ((TEGrinder)getMaster()).getRequestedEF(rp, direction);
        return isFormed()?Math.min(20, (5000-storedPower)/rp):0;
    }

    /**
     * @param direction the direction where the power will be provided to
     * @param rp        the RedstonePotential in the network
     * @param ef        the amount of EnergeticFlux that is being provided
     * @return The amount of EnergeticFlux that wasn't used
     */
    @Override
    public int receivePower(ForgeDirection direction, int rp, int ef) {
        if (isSlave())
            return ((TEGrinder)getMaster()).receivePower(direction, rp, ef);
        if (isFormed())
            storedPower = storedPower+rp*ef;
        return 0;
    }
}
