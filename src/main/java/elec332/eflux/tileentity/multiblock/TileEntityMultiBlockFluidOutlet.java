package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.world.WorldHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Created by Elec332 on 14-4-2016.
 */
@RegisterTile(name = "TileEntityEFluxMultiBlockFluidOutlet")
public class TileEntityMultiBlockFluidOutlet extends AbstractTileEntityMultiBlockFluidHandler implements ITickable {

    @Override
    protected boolean canFillFrom(EnumFacing facing) {
        return false;
    }

    @Override
    public boolean canFaceUpOrDown() {
        return true;
    }

    @Override
    public void update() {
        if (isRedstonePowered(this)){
            EnumFacing facing = getTileFacing();
            TileEntity tile = WorldHelper.getTileAt(worldObj, getPos().offset(facing));
            if (tile instanceof IFluidHandler){
                FluidStack stack = drain(facing, 100, false);
                int i = ((IFluidHandler)tile).fill(facing.getOpposite(), stack, false);
                if (i > 0){
                    ((IFluidHandler)tile).fill(facing.getOpposite(), drain(facing, i, true), true);
                }
            }
        }
    }

}
