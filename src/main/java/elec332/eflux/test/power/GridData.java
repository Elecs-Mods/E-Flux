package elec332.eflux.test.power;

import elec332.eflux.test.blockLoc.BlockLoc;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 27-4-2015.
 */
public class GridData {
    public GridData(BlockLoc blockLoc, ForgeDirection direction){
        this.loc = blockLoc;
        this.direction = direction;
    }

    private BlockLoc loc;
    private ForgeDirection direction;

    public BlockLoc getLoc() {
        return loc;
    }

    public ForgeDirection getDirection() {
        return direction;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GridData && ((GridData) obj).loc.equals(loc) && ((GridData) obj).direction == direction;
    }
}
