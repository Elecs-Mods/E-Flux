package elec332.eflux.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

/**
 * Created by Elec332 on 13-9-2015.
 */
@Deprecated
public final class MultiBlockInterfaces {

    private MultiBlockInterfaces(){
    }

    public interface IEFluxMultiBlock{
    }

    @Deprecated
    public interface IEFluxMultiBlockFluidHandler extends IEFluxMultiBlock {

        public int fill(FluidStack resource, boolean doFill, BlockPos pos);

        public FluidStack drain(FluidStack resource, boolean doDrain, BlockPos pos);

        public FluidStack drain(int maxDrain, boolean doDrain, BlockPos pos);

        public boolean canFill(Fluid fluid, BlockPos pos);

        public boolean canDrain(Fluid fluid, BlockPos pos);

        public FluidTankInfo[] getTankInfo(BlockPos pos);

    }

    @Deprecated
    public interface IEFluxMultiBlockPowerAcceptor extends IEFluxMultiBlock{

        public int requestedRP();

        public int getRequestedEF(int rp);

        public int receivePower(int rp, int ef);

    }

    @Deprecated
    public interface IEFluxMultiBlockPowerProvider extends IEFluxMultiBlock{

        public int provideEnergy(int rp, boolean execute);

    }

}
