package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 23-2-2016.
 */
public interface IEnergyMonitor extends IEnergyTile {

    /**
     * This gets called every time power is processed,
     * it gives the monitor info about the RP in the network and the processed EF.
     *
     * @param rp The RP value of the grid.
     * @param ef The processed EF that tick.
     */
    public void onEnergyTick(int rp, int ef);

}
