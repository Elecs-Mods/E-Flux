package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.eflux.util.IEFluxFluidHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
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
        if (hasRedstone()){
            EnumFacing facing = getTileFacing().getOpposite();
            TileEntity tile = worldObj.getTileEntity(getPos().offset(facing.getOpposite()));
            if (tile instanceof IFluidHandler){
                FluidStack stack = drain(facing, 100, false);
                int i = ((IFluidHandler) tile).fill(facing, stack, false);
                if (i > 0){
                    ((IFluidHandler) tile).fill(facing, drain(facing, i, true), true);
                }
            }
        }
    }

}
