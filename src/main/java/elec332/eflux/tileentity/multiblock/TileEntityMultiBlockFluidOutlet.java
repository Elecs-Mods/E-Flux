package elec332.eflux.tileentity.multiblock;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.util.FluidHelper;
import elec332.core.util.FluidTankWrapper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Created by Elec332 on 14-4-2016.
 */
@RegisteredTileEntity("TileEntityEFluxMultiBlockFluidOutlet")
public class TileEntityMultiBlockFluidOutlet extends AbstractTileEntityMultiBlockFluidHandler implements ITickable {

    public TileEntityMultiBlockFluidOutlet(){
        fluidHandler = new FluidTankWrapper() {

            @Override
            protected IFluidTank getTank() {
                return (IFluidTank) getMultiBlockHandler();
            }

            @Override
            protected boolean canFill() {
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

            FluidStack stack = mb.drain(100, false);
            int i = fluidHandler.fill(stack, false);
            if (i > 0) {
                fluidHandler.fill(mb.drain(i, true), true);
            }
        }
    }

}
