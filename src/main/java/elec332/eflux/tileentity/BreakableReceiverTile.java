package elec332.eflux.tileentity;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 16-5-2015.
 */
public abstract class BreakableReceiverTile extends BreakableMachineTile {

    public int getMaxEF(int rp) {
        return (getMaxStoredPower()-storedPower)/rp;
    }

    protected void receivePower(int rp, int ef, ForgeDirection direction) {
        this.storedPower = storedPower+rp*ef;
        if (storedPower > getMaxStoredPower())
            this.storedPower = getMaxStoredPower();
    }

    protected abstract int getMaxStoredPower();

}
