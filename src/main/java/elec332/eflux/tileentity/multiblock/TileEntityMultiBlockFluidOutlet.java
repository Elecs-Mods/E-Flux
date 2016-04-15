package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
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
        if (hasRedstone()){
            EnumFacing facing = getTileFacing().getOpposite();
            TileEntity tile = worldObj.getTileEntity(getPos().offset(facing.getOpposite()));
            if (tile instanceof IFluidHandler){
                FluidStack stack = ((IFluidHandler)tile).drain(facing, 100, false);
                int i = fill(facing, stack, false);
                if (i > 0){
                    fill(facing, ((IFluidHandler) tile).drain(facing, i, true), true);
                }
            }
        }
    }

}
