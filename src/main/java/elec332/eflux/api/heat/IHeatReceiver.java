package elec332.eflux.api.heat;

/**
 * Created by Elec332 on 2-4-2016.
 *
 * Heat is not in degrees, but in "energy".
 */
public interface IHeatReceiver {

    public int getHeat();

    public void addHeat(int heat);

}
