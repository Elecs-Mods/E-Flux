package elec332.eflux.tileentity.multiblock;

import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Elec332 on 13-9-2015.
 */
public abstract class TileMultiBlockInteraction<M> extends TileMultiBlockTile {

    protected abstract Capability<M> getCapability();

    @SuppressWarnings("unchecked")
    public M getMultiBlockHandler(){
        return getMultiBlockCapability(getCapability(), null);
    }

}
