package elec332.eflux.tileentity.multiblock;

import elec332.core.multiblock.AbstractMultiBlock;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Elec332 on 13-9-2015.
 */
public abstract class AbstractTileEntityMultiBlockHandler<M> extends AbstractTileEntityMultiBlock {

    protected abstract Capability<M> getCapability();

    @SuppressWarnings("unchecked")
    public M getMultiBlockHandler(){
        AbstractMultiBlock mb = getMultiBlock();
        return mb == null ? null : mb.getSpecialCapability(getCapability(), null, pos);
    }

}
