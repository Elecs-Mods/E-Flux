package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 28-4-2015.
 */
public interface IEnergyProvider extends IEnergyTile {

    public int providePower(int EF, boolean simulate);

}
