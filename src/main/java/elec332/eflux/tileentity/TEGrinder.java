package elec332.eflux.tileentity;

import elec332.eflux.util.Directions;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class TEGrinder extends BaseMachineTEWithInventory{

    public TEGrinder() {
        super(6000, 40, 18);
    }

    @Override
    protected String standardInventoryName() {
        return "Grinder";
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return Directions.getDirectionFromNumber(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) == from;
    }
}
