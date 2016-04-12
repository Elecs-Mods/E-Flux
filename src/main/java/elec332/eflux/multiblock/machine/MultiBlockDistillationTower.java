package elec332.eflux.multiblock.machine;

import elec332.core.multiblock.AbstractMultiBlock;
import elec332.eflux.api.util.CapabilityHelper;
import elec332.eflux.init.FluidRegister;
import elec332.eflux.util.IEFluxFluidHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.*;

/**
 * Created by Elec332 on 1-4-2016.
 */
public class MultiBlockDistillationTower extends AbstractMultiBlock implements IEFluxFluidHandler {

    /**
     * Initialise your multiblock here, all fields provided by @link IMultiblock have already been given a value
     */
    @Override
    public void init() {
        oilTank = CapabilityHelper.forCapacity(6000);
    }

    private CapabilityHelper.FluidHandlerHelper oilTank;

    @Override
    public boolean onAnyBlockActivated(EntityPlayer player) {
        return false;
    }

    /**
     * This gets run server-side only
     */
    @Override
    public void onTick() {
        System.out.println("tick destillation");
    }

    /**
     * Invalidate your multiblock here
     */
    @Override
    public void invalidate() {

    }


    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return canFill(resource.getFluid()) ? fill(doFill, resource) : 0;
    }

    private int fill(boolean doFill, FluidStack stack){
        int ret = oilTank.fill(stack, doFill);
        markDirty();
        return ret;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(Fluid fluid) {
        return fluid == FluidRegister.crudeOil;
    }

    @Override
    public boolean canDrain(Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo() {
        return new FluidTankInfo[0];
    }

}
