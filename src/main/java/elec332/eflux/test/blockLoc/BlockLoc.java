package elec332.eflux.test.blockLoc;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class BlockLoc {

    public BlockLoc(TileEntity tileEntity){
        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
    }

    public int xCoord;
    public int yCoord;
    public int zCoord;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlockLoc && ((BlockLoc)obj).zCoord == zCoord && ((BlockLoc)obj).yCoord == yCoord && ((BlockLoc)obj).xCoord == xCoord;
    }

    @Override
    public String toString() {
        return "("+xCoord+","+yCoord+","+zCoord+")";
    }

    @Override
    public int hashCode() {
        return xCoord+yCoord+zCoord;
    }
}
