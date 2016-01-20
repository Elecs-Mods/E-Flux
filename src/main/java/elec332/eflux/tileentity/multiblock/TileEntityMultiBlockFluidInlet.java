package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.eflux.multiblock.MultiBlockInterfaces;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Created by Elec332 on 13-9-2015.
 */
@RegisterTile(name = "TileEntityEFluxMultiBlockFluidInlet")
public class TileEntityMultiBlockFluidInlet extends TileMultiBlockInteraction<MultiBlockInterfaces.IEFluxMultiBlockFluidHandler> implements IFluidHandler{

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return getMultiBlockHandler() == null ? 0 : getMultiBlockHandler().fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return getMultiBlockHandler() != null && getMultiBlockHandler().canFill(fluid);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return getMultiBlockHandler() == null ? new FluidTankInfo[0] : getMultiBlockHandler().getTankInfo();
    }

}
