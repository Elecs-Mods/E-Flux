package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyReceiver extends IEnergyTile {

    //volts
    public int getRequestedEF();

    public int getResistance();

    //Blow up if voltage is too high or something... (or AC instead of DC, if machine has no rectifier)
    public void receivePower(int ef, int rp, EnergyType type);

}
