package elec332.eflux.tileentity;

import elec332.eflux.util.CalculationHelper;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 16-5-2015.
 */
public abstract class BreakableReceiverTile extends BreakableMachineTile {

    @Override
    protected int getRequestedEFSafe(int rp, ForgeDirection direction) {
        return getEFForOptimalRP();
    }

    public int getMaxEF(int rp) {
        return (getMaxStoredPower()-storedPower)/rp;
    }

    protected void receivePower(int rp, int ef, ForgeDirection direction) {
        int calcEF = CalculationHelper.calcRequestedEF(rp, requestedRP(direction), getEFForOptimalRP(), getMaxEF(rp), getAcceptance());
        this.storedPower = storedPower+rp*calcEF;
        if (storedPower > getMaxStoredPower())
            this.storedPower = getMaxStoredPower();
    }

    protected abstract int getMaxStoredPower();

    /**
     * The amount returned here is NOT supposed to change, anf if it does,
     * do not forget that it will receive a penalty if the machine is not running at optimum RP
     *
     * @return the amount of requested EF
     */
    public abstract int getEFForOptimalRP();

}
