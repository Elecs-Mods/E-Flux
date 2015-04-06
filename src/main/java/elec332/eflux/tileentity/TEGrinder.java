package elec332.eflux.tileentity;

import elec332.eflux.client.inventory.GuiInventoryGrinder;
import elec332.eflux.inventory.TEGrinderContainer;
import elec332.eflux.util.Directions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;


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
        return Directions.getDirectionFromNumber(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) == from && this.isFormed();
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!isFormed())
            return tryToForm();
        else return openGui(player);
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
    protected boolean areBlocksAtRightLocations() {
        return getBlockAtOffset(-1, 0, -1) == Blocks.beacon && getBlockAtOffset(-1, 0, 1) == Blocks.beacon && getBlockAtOffset(1, 0, 1) == Blocks.beacon && getBlockAtOffset(1, 0, -1) == Blocks.beacon;
    }

    @Override
    protected void onCreated() {
    }
}
