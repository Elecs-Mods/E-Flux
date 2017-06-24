package elec332.eflux.tileentity.multiblock;

import elec332.eflux.util.capability.IRedstoneUpgradable;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Created by Elec332 on 14-4-2016.
 */
public abstract class AbstractTileEntityMultiBlockFluidHandler extends AbstractTileEntityMultiBlockHandler<IFluidHandler> implements IRedstoneUpgradable {

    protected abstract IFluidHandler getFluidHandler(EnumFacing side, boolean hasMultiBlock);

    protected abstract boolean hasFluidHandler(EnumFacing side, boolean hasMultiBlock);

    @Override
    protected Capability<IFluidHandler> getCapability() {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, boolean hasMultiBlock) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasFluidHandler(facing, hasMultiBlock) || super.hasCapability(capability, facing, hasMultiBlock);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, boolean hasMultiBlock) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T) getFluidHandler(facing, hasMultiBlock) : super.getCapability(capability, facing, hasMultiBlock);
    }

}
