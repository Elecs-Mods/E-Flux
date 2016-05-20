package elec332.eflux.api.ender.internal;

/**
 * Created by Elec332 on 19-5-2016.
 */
public interface IEndergyCapability {

    public int getStoredEndergy();

    public boolean drainEndergy(int endergy);

}
