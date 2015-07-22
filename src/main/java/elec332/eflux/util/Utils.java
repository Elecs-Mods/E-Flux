package elec332.eflux.util;

import elec332.core.util.BlockLoc;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elec332 on 3-5-2015.
 */
public class Utils {

    public static AxisAlignedBB getAABBAroundBlock(BlockLoc loc, int xOffsetWest, int yOffsetDown, int zOffsetNorth, int xOffsetEast, int yOffsetUp, int zOffsetSouth){
        return AxisAlignedBB.getBoundingBox(loc.xCoord - xOffsetWest, loc.yCoord - yOffsetDown, loc.zCoord - zOffsetNorth, loc.xCoord + xOffsetEast + 1, loc.yCoord + yOffsetUp + 1, loc.zCoord + zOffsetSouth + 1);
    }

    public static <E> List<E> copyOf(List<E> original){
        List<E> ret = new ArrayList<E>();
        for (E e : original)
            ret.add(e);
        return ret;
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] toArray(List<E> list){
        return (E[])list.toArray();
    }
}
