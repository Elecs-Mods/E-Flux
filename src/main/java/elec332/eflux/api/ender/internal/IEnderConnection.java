package elec332.eflux.api.ender.internal;

import elec332.eflux.api.ender.IEnderNetworkComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 8-5-2016.
 */
public interface IEnderConnection<T> {

    @Nullable
    public T get();

    @Nonnull
    public IEnderNetworkComponent<T> getComponent();

}
