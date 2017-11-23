package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyGenerator extends IEnergyObject {

    public float getVariance();

    //AC/DC
    public EnergyType getGeneratedEnergyType();

    //Volts
    public int getCurrentAverageEF();

    //Amps
    public int getMaxRP();

}
