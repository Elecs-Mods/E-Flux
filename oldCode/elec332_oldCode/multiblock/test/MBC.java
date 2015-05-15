package elec332_oldCode.multiblock.test;

import elec332.core.util.BlockLoc;
import elec332.core.world.WorldHelper;
import elec332_oldCode.multiblock.IMultiBlockMainTile;
import elec332_oldCode.multiblock.IMultiBlockPart;
import elec332_oldCode.multiblock.MultiBlockControllerBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 15-5-2015.
 */
public class MBC extends MultiBlockControllerBase {
    public MBC(IMultiBlockMainTile tile) {
        super(tile);
    }

    @Override
    public boolean checkMultiBlockLocations() {
        BlockLoc loc = new BlockLoc(getTile()).atSide(ForgeDirection.UP);
        TileEntity tile = WorldHelper.getTileAt(getTile().getWorldObj(), loc);
        if (tile instanceof IMultiBlockPart) {
            return getMainTile().getLocList().locations.contains(((IMultiBlockPart) tile));
        }
        return false;
    }

    @Override
    public void tickServer() {
        System.out.println("Complete & Tick!");
    }

    @Override
    public void tickClient() {

    }
}
