package elec332.eflux.api.energy.container;

import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.util.ConnectionPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 21-1-2016.
 */
public interface IEFluxPowerHandler {

    /**
     * The amount returned here is NOT supposed to change, anf if it does,
     * do not forget that it will receive a penalty if the machine is not running at optimum RP
     *
     * @return the amount of requested EF
     */
    public int getWorkingVoltage();

    public float getAcceptance();

    public int getMaxRP();

    public double getResistance();

    default public EnergyType getEnergyType(){
        return EnergyType.AC;
    }
    @Nonnull
    public ConnectionPoint getConnectionPoint(int post);

    @Nullable
    public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec);


    public void markObjectDirty();

}
