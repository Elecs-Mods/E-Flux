package elec332.eflux.endernetwork;

import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IWeakEnderConnection;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;

/**
 * Created by Elec332 on 8-5-2016.
 */
public final class ItemConnection<T> implements IWeakEnderConnection<T> {

    ItemConnection(IEnderNetworkComponent<T> component, WeakReference<IEnderCapability> capability){
        this.capability = capability;
        this.component = component;
        this.valid = true;
    }

    private final WeakReference<IEnderCapability> capability;
    private final IEnderNetworkComponent<T> component;
    private boolean valid;

    public void invalidate(){
        valid = false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        if (!valid){
            return null;
        }
        IEnderCapability cap =  capability.get();
        return cap == null ? null : (T) cap.get();
    }

    @Nonnull
    @Override
    public IEnderNetworkComponent<T> getComponent() {
        return component;
    }

    @Override
    public boolean isValid() {
        return valid && capability.get() != null;
    }

}
