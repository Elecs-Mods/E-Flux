package elec332.eflux.tileentity.multiblock;

import elec332.core.multiblock.AbstractMultiBlockTile;
import elec332.core.multiblock.IMultiBlock;
import elec332.eflux.EFlux;
import elec332.eflux.multiblock.MultiBlockInterfaces;

/**
 * Created by Elec332 on 13-9-2015.
 */
public abstract class TileMultiBlockInteraction<M extends MultiBlockInterfaces.IEFluxMultiBlock> extends AbstractMultiBlockTile {

    public TileMultiBlockInteraction() {
        super(EFlux.multiBlockRegistry);
    }

    @SuppressWarnings("unchecked")
    public M getMultiBlockHandler(){
        IMultiBlock multiBlock = getMultiBlock();
        if (multiBlock == null)
            return null;
        try {
            return (M) multiBlock;
        } catch (ClassCastException e){
            return null;
        }
    }
}
