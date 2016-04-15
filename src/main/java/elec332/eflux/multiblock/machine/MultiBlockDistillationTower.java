package elec332.eflux.multiblock.machine;

import elec332.core.multiblock.AbstractMultiBlock;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.util.CapabilityHelper;
import elec332.eflux.util.IEFluxFluidHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import static elec332.eflux.init.FluidRegister.*;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 1-4-2016.
 */
public class MultiBlockDistillationTower extends AbstractMultiBlock {

    @CapabilityInject(IEFluxFluidHandler.class)
    private static Capability<IEFluxFluidHandler> CAPABILITY;

    /**
     * Initialise your multiblock here, all fields provided by @link IMultiblock have already been given a value
     */
    @Override
    public void init() {
        oilTank = CapabilityHelper.forCapacity(6000);   // Input Tank
        setPositions();
        lubeTank = CapabilityHelper.forCapacity(500);  // 2%
        fuelTank = CapabilityHelper.forCapacity(5000);  // 49%
        dieselTank = CapabilityHelper.forCapacity(4000);// 30%
        petrolTank = CapabilityHelper.forCapacity(2000);// 12%
        gasTank = CapabilityHelper.forCapacity(1000);   // 7%
    }

    private void setPositions(){
        oilIn1 = getBlockLocAtTranslatedPos(0, 0, 1);
        oilIn2 = getBlockLocAtTranslatedPos(2, 0, 1);
        lubeOut = getBlockLocAtTranslatedPos(1, 2, 0);
        gasOut = getBlockLocAtTranslatedPos(1, 2, 6);
        BlockPos lmb = getBlockLocAtTranslatedPos(1, 3, 0);
        petrolOut = lmb.up(5);
        dieselOut = lmb.up(3);
        fuelOut = lmb.up();
    }

    private CapabilityHelper.FluidHandlerHelper oilTank;
    private CapabilityHelper.FluidHandlerHelper lubeTank, fuelTank, dieselTank, petrolTank, gasTank;
    private int heat; //TODO
    private BlockPos oilIn1, oilIn2, lubeOut, gasOut, petrolOut, fuelOut, dieselOut;

    @Override
    public boolean onAnyBlockActivated(EntityPlayer player) {
        return false;
    }

    /**
     * This gets run server-side only
     */
    @Override
    public void onTick() {
        if (getWorldObj().getWorldTime() % 20 == 0){ //TODO: Impl heat
            FluidStack stack = oilTank.drain(100, false);
            if (stack == null || stack.amount != 100){
                return;
            }
            //TODO: decrease heat
            oilTank.drain(100, true);
            attemptFill(lubeTank, 2, lubicrant);
            attemptFill(fuelTank, 49, fuel);
            attemptFill(dieselTank, 30, diesel);
            attemptFill(petrolTank, 12, petrol);
            attemptFill(gasTank, 7, gas);
        }
    }

    private void attemptFill(CapabilityHelper.FluidHandlerHelper tank, int fill, Fluid fluid){
        int i = tank.fill(new FluidStack(fluid, fill), true);
        if (i != fill){
            BlockPos boomPos = getBlockLocAtTranslatedPos(1, 2, 5);
            WorldHelper.spawnExplosion(getWorldObj(), boomPos.getX(), boomPos.getY(), boomPos.getZ(), 5f);
        }
    }

    /**
     * Invalidate your multiblock here
     */
    @Override
    public void invalidate() {

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        if (capability == CAPABILITY){
            if (pos.equals(oilIn1) || pos.equals(oilIn2)){
                return (T)oilTank;
            } else if (pos.equals(fuelOut)){
                return (T)fuelTank;
            } else if (pos.equals(dieselOut)){
                return (T) dieselTank;
            } else if (pos.equals(petrolOut)){
                return (T) petrolTank;
            } else if (pos.equals(gasOut)){
                return (T) gasTank;
            } else if (pos.equals(lubeOut)){
                return (T) lubeTank;
            }
            return null;
        }
        return super.getCapability(capability, facing, pos);
    }

}
