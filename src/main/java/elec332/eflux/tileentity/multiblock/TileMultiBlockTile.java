package elec332.eflux.tileentity.multiblock;

import elec332.core.multiblock.AbstractMultiBlock;
import elec332.core.multiblock.AbstractMultiBlockTile;
import elec332.eflux.EFlux;
import elec332.eflux.multiblock.EFluxMultiBlockMachine;

/**
 * Created by Elec332 on 28-8-2015.
 */
public abstract class TileMultiBlockTile extends AbstractMultiBlockTile {

    public TileMultiBlockTile() {
        super(EFlux.multiBlockRegistry);
    }

    @Override
    public EFluxMultiBlockMachine getMultiBlock() {
        return (EFluxMultiBlockMachine) super.getMultiBlock();
    }
}
