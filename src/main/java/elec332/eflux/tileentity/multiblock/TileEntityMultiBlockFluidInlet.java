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
public class TileEntityMultiBlockFluidInlet extends AbstractTileEntityMultiBlockFluidHandler {

    @Override
    protected boolean canDrainFrom(EnumFacing facing) {
        return false;
    }

    @Override
    public boolean canFaceUpOrDown() {
        return true;
    }

}
