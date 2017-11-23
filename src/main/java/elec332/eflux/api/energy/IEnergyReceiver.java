package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyReceiver extends IEnergyObject {

    /**
     * Returns the resistance of this machine
     *
     * @return A static resistance value, which is cached in the circuit, so this is only called once.
     */
    public double getResistance();

    //Blow up if voltage is too high or something...
    public void receivePower(double voltage, double amps);

}
