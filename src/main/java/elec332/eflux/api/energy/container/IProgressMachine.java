package elec332.eflux.api.energy.container;

/**
 * Created by Elec332 on 24-8-2015.
 */
public interface IProgressMachine {

    public int getRequiredPowerPerTick();

    public int getProcessTime();

    public boolean canProcess();

    public void onProcessDone();

}
