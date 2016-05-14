package elec332.eflux.api.ender.internal;

/**
 * Created by Elec332 on 8-5-2016.
 */
public interface IWeakEnderConnection<T> extends IEnderConnection<T> {

    public boolean isValid();

}
