package elec332.eflux.api;

import elec332.eflux.api.kinetic.IKineticReceiver;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * Created by Elec332 on 13-2-2016.
 */
public class EFluxKinetics {

    @CapabilityInject(IKineticReceiver.class)
    public static Capability<IKineticReceiver> KINETIC_CAPABILITY;

    static {
        EFluxAPI.registerWithoutStorageAndDefaultInstance(IKineticReceiver.class);
    }

}
