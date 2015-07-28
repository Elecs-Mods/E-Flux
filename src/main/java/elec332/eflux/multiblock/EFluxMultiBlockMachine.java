package elec332.eflux.multiblock;

import elec332.core.multiblock.AbstractMultiBlock;
/**
 * Created by Elec332 on 28-7-2015.
 */
public abstract class EFluxMultiBlockMachine extends AbstractMultiBlock{

    public abstract int getMaxStoredPower();

    public abstract float getAcceptance();

    /**
     * The amount returned here is NOT supposed to change, anf if it does,
     * do not forget that it will receive a penalty if the machine is not running at optimum RP
     *
     * @return the amount of requested EF
     */
    public abstract int getEFForOptimalRP();

    /**
     * @return The Redstone Potential at which the machine wishes to operate
     */
    public abstract int requestedRP();

}
