package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.world.WorldHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Created by Elec332 on 13-9-2015.
 */
@RegisterTile(name = "TileEntityEFluxMultiBlockFluidInlet")
public class TileEntityMultiBlockFluidInlet extends AbstractTileEntityMultiBlockFluidHandler implements ITickable {

    @Override
    protected boolean canDrainFrom(EnumFacing facing) {
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
                FluidStack stack = ((IFluidHandler) tile).drain(facing.getOpposite(), 100, false);
                int i = fill(facing, stack, false);
                if (i > 0){
                    fill(facing, ((IFluidHandler) tile).drain(facing.getOpposite(), i, true), true);
                }
            }
        }
    }

}
