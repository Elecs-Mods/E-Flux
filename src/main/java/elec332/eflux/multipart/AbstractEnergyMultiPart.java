package elec332.eflux.multipart;

import elec332.core.multipart.AbstractMultiPart;
import elec332.eflux.api.energy.EnergyAPIHelper;

/**
 * Created by Elec332 on 9-2-2016.
 */
public abstract class AbstractEnergyMultiPart extends AbstractMultiPart {

    @Override
    public void onPartValidated() {
        if (!getWorld().isRemote) {
            EnergyAPIHelper.postLoadEvent(getTile());
        }
    }

    @Override
    public void onPartInvalidated() {
        if (!getWorld().isRemote) {
            EnergyAPIHelper.postUnloadEvent(getTile());
        }
    }

}
