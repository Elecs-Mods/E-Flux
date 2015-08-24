package elec332.eflux.api.util;

/**
 * Created by Elec332 on 24-8-2015.
 */
public interface IBreakableMachine {

    public boolean isBroken();

    public void setBroken(boolean broken);

    public void onBroken();

}
