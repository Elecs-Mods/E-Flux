package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergySource extends IEnergyObject, IBreakableMachine {

    public float getVariance();

    //Volts
    public int getCurrentAverageEF();

    //Amps
    public float getMaxRP();

}
