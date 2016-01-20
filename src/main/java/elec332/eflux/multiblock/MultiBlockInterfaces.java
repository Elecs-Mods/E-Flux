package elec332.eflux.multiblock;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

/**
 * Created by Elec332 on 13-9-2015.
 */
public final class MultiBlockInterfaces {

    private MultiBlockInterfaces(){
    }

    public interface IEFluxMultiBlock{
    }

    public interface IEFluxMultiBlockFluidHandler extends IEFluxMultiBlock {

        public int fill(FluidStack resource, boolean doFill);

        public FluidStack drain(FluidStack resource, boolean doDrain);

        public FluidStack drain(int maxDrain, boolean doDrain);

        public boolean canFill(Fluid fluid);

        public boolean canDrain(Fluid fluid);

        public FluidTankInfo[] getTankInfo();

    }

    public interface IEFluxMultiBlockPowerAcceptor extends IEFluxMultiBlock{

        public int requestedRP();

        public int getRequestedEF(int rp);

        public int receivePower(int rp, int ef);

    }

    public interface IEFluxMultiBlockPowerProvider extends IEFluxMultiBlock{

        public int provideEnergy(int rp, boolean execute);

    }

}
