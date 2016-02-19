package elec332.eflux.tileentity.kinetic;

import elec332.core.tile.TileBase;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxKinetics;
import elec332.eflux.api.kinetic.IKineticReceiver;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * Created by Elec332 on 13-2-2016.
 */
public abstract class AbstractKineticTileBase extends TileBase {

    @Nullable
    protected IKineticReceiver receiver;

    @Override
    public void onNeighborBlockChange(Block block) {
        reCheckConnections();
    }

    @Override
    public void onNeighborTileChange(BlockPos neighbor) {
        reCheckConnections();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        reCheckConnections();
    }

    protected void reCheckConnections(){
        System.out.println("Recheck");
        EnumFacing out = getOutputFacing();
        BlockPos pos = getPos().offset(out);
        if (WorldHelper.chunkLoaded(worldObj, pos)) {
            TileEntity tile = WorldHelper.getTileAt(worldObj, pos);
            if (tile.hasCapability(EFluxKinetics.KINETIC_CAPABILITY, out.getOpposite())) {
                IKineticReceiver receiver = tile.getCapability(EFluxKinetics.KINETIC_CAPABILITY, out.getOpposite());
                if (receiver.canReceiveFrom(this)) {
                    IKineticReceiver oldReceiver = this.receiver;
                    this.receiver = receiver;
                    if (oldReceiver != receiver) {
                        onReceiverChanged();
                    }
                }
            } else if (receiver != null){
                this.receiver = null;
                onReceiverChanged();
            }
        } else if (receiver != null){
            this.receiver = null;
            onReceiverChanged();
        }
    }

    protected void onReceiverChanged(){
    }

    protected abstract EnumFacing getOutputFacing();

}
