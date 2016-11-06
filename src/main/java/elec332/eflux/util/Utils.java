package elec332.eflux.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elec332 on 3-5-2015.
 */
public class Utils {

    public static AxisAlignedBB getAABBAroundBlock(BlockPos loc, int xOffsetWest, int yOffsetDown, int zOffsetNorth, int xOffsetEast, int yOffsetUp, int zOffsetSouth){
        return new AxisAlignedBB(loc.getX() - xOffsetWest, loc.getY() - yOffsetDown, loc.getZ() - zOffsetNorth, loc.getX() + xOffsetEast + 1, loc.getY() + yOffsetUp + 1, loc.getZ() + zOffsetSouth + 1);
    }

}
