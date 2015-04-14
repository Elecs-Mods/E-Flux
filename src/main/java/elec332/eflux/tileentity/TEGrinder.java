package elec332.eflux.tileentity;

import elec332.core.util.DirectionHelper;
import elec332.eflux.client.inventory.GuiInventoryGrinder;
import elec332.eflux.inventory.TEGrinderContainer;
import elec332.eflux.util.EnumMachines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Elec332 on 3-4-2015.
 */
public class TEGrinder extends BaseMultiBlockMachine{

    public TEGrinder() {
        super(60000, 40, 18);
    }

    @Override
    protected String standardInventoryName() {
        return "grinder";
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return DirectionHelper.getDirectionFromNumber(this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) == from && this.isFormed();
    }

    @Override
    public Object getGuiServer(EntityPlayer player) {
        return new TEGrinderContainer(this, player);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new GuiInventoryGrinder(this, player);
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
    protected List<Vec3> getIMultiBlockLocations() {
        ArrayList<Vec3> ret = new ArrayList<Vec3>();
        ret.add(Vec3.createVectorHelper(0, 2, 0));
        return ret;
    }

    @Override
    protected void onCreated() {
    }

    @Override
    public EnumMachines getMachine() {
        return EnumMachines.GRINDER;
    }
}
