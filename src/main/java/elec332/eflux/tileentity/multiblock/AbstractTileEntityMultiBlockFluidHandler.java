package elec332.eflux.tileentity.multiblock;

import elec332.eflux.util.IEFluxFluidHandler;
import elec332.eflux.util.capability.IRedstoneUpgradable;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Created by Elec332 on 14-4-2016.
 */
public abstract class AbstractTileEntityMultiBlockFluidHandler extends AbstractTileEntityMultiBlockHandler<IEFluxFluidHandler> implements IFluidHandler, IRedstoneUpgradable {

    @CapabilityInject(IEFluxFluidHandler.class)
    private static Capability<IEFluxFluidHandler> CAPABILITY;

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (!canFillFrom(from)){
            return 0;
        }
        IEFluxFluidHandler mb = getMultiBlockHandler();
        return mb == null ? 0 : mb.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (!canDrainFrom(from)){
            return null;
        }
        IEFluxFluidHandler mb = getMultiBlockHandler();
        return mb == null ? null : mb.drain(resource, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        if (!canDrainFrom(from)){
            return null;
        }
        IEFluxFluidHandler mb = getMultiBlockHandler();
        return mb == null ? null : mb.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        if (!canFillFrom(from)){
            return false;
        }
        IEFluxFluidHandler mb = getMultiBlockHandler();
        return mb != null && mb.canFill(fluid);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        if (!canDrainFrom(from)){
            return false;
        }
        IEFluxFluidHandler mb = getMultiBlockHandler();
        return mb != null && mb.canDrain(fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        IEFluxFluidHandler mb = getMultiBlockHandler();
        return mb == null ? new FluidTankInfo[0] : mb.getTankInfo();
    }

    protected boolean canFillFrom(EnumFacing facing){
        return true;
    }

    protected boolean canDrainFrom(EnumFacing facing){
        return true;
    }

    @Override
    protected Capability<IEFluxFluidHandler> getCapability() {
        return CAPABILITY;
    }

}
