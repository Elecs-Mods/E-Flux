package elec332.eflux.tileentity.multiblock;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Elec332 on 13-9-2015.
 */
public abstract class AbstractTileEntityMultiBlockHandler<M> extends AbstractTileEntityMultiBlock {

    private boolean redstone = false, check = true;

    protected abstract Capability<M> getCapability();

    @SuppressWarnings("unchecked")
    public M getMultiBlockHandler(){
        return getMultiBlockCapability(getCapability(), null);
    }

    @Override
    public void onNeighborBlockChange(Block block) {
        super.onNeighborBlockChange(block);
        check = true;
    }

    @Override
    public void onNeighborTileChange(BlockPos neighbor) {
        super.onNeighborTileChange(neighbor);
        check = true;
    }

    public boolean hasRedstone(){
        if (check){
            redstone = worldObj.isBlockPowered(getPos());
            check = false;
        }
        return redstone;
    }

}
