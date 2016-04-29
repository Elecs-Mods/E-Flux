package elec332.eflux.tileentity.multiblock;

import elec332.core.multiblock.AbstractMultiBlockTile;
import elec332.eflux.EFlux;

/**
 * Created by Elec332 on 28-8-2015.
 */
public abstract class AbstractTileEntityMultiBlock extends AbstractMultiBlockTile {

    public AbstractTileEntityMultiBlock() {
        super(EFlux.multiBlockRegistry);
    }

}
