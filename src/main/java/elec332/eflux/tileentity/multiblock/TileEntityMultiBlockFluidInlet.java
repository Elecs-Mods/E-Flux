package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.util.FluidHelper;
import elec332.core.util.FluidTankWrapper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Created by Elec332 on 13-9-2015.
 */
@RegisterTile(name = "TileEntityEFluxMultiBlockFluidInlet")
public class TileEntityMultiBlockFluidInlet extends AbstractTileEntityMultiBlockFluidHandler implements ITickable {

    public TileEntityMultiBlockFluidInlet(){
        fluidHandler = new FluidTankWrapper() {

            @Override
            protected IFluidTank getTank() {
                return (IFluidTank) getMultiBlockHandler();
            }

            @Override
            protected boolean canDrain() {
                return false;
            }

        };
    }

    private final IFluidHandler fluidHandler;

    @Override
    protected boolean hasFluidHandler(EnumFacing side, boolean hasMultiBlock) {
        return getMultiBlockHandler() != null;
    }

    @Override
    protected IFluidHandler getFluidHandler(EnumFacing side, boolean hasMultiBlock) {
        return fluidHandler;
    }

    @Override
    public boolean canFaceUpOrDown() {
        return true;
    }

    @Override
    public void update() {
        if (isRedstonePowered(this)) {
            IFluidHandler mb = getMultiBlockHandler();
            if (mb == null){
                return;
            }
            EnumFacing facing = getTileFacing();

            IFluidHandler fluidHandler = FluidHelper.getFluidHandler(worldObj, pos.offset(facing), facing.getOpposite());

            if (fluidHandler == null) {
                return;
            }

            FluidStack stack = fluidHandler.drain(100, false);
            int i = mb.fill(stack, false);
            if (i > 0) {
                mb.fill(fluidHandler.drain(i, true), true);
            }
        }
    }

}
