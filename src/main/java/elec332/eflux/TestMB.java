package elec332.eflux;

import elec332.eflux.multiblock.IMultiBlock;

/**
 * Created by Elec332 on 27-7-2015.
 */
public class TestMB extends IMultiBlock {

    /**
     * Initialise your multiblock here, all fields provided by @link IMultiblock have already been given a value
     */
    @Override
    public void init() {
        System.out.println("INIT_MB");
    }

    /**
     * This gets run server-side only
     */
    @Override
    public void onTick() {
        System.out.println("TICK_MB");
    }

    /**
     * Invalidate your multiblock here
     */
    @Override
    public void invalidate() {
        System.out.println("GOODBYE_MB");
    }
}
