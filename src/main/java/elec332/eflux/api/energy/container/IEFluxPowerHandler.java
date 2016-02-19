package elec332.eflux.api.energy.container;

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
    public int getEFForOptimalRP();

    public float getAcceptance();

    public int getOptimalRP();

    public void markObjectDirty();

}
