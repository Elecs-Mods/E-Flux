package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.eflux.util.IEFluxFluidHandler;
import net.minecraft.util.EnumFacing;
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
public class TileEntityMultiBlockFluidInlet extends TileMultiBlockInteraction<IEFluxFluidHandler> implements IFluidHandler {

    @CapabilityInject(IEFluxFluidHandler.class)
    private static Capability<IEFluxFluidHandler> CAPABILITY;

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        IEFluxFluidHandler mb = getMultiBlockHandler();
        return mb == null ? 0 : mb.fill(resource, doFill);
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
        IEFluxFluidHandler mb = getMultiBlockHandler();
        return mb != null && mb.canFill(fluid);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        IEFluxFluidHandler mb = getMultiBlockHandler();
        return mb == null ? new FluidTankInfo[0] : mb.getTankInfo();
    }

    @Override
    protected Capability<IEFluxFluidHandler> getCapability() {
        return CAPABILITY;
    }

}
