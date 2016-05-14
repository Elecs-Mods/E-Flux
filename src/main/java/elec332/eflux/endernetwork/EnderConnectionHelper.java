package elec332.eflux.endernetwork;

import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEnderConnection;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * Created by Elec332 on 9-5-2016.
 */
public final class EnderConnectionHelper {

    @Nullable
    public static IEnderNetworkComponent getClearedComponent(@Nullable ICapabilityProvider capabilityProvider, @Nullable EnumFacing side) {
        return clearComponent(getComponent(capabilityProvider, side));
    }

    @Nullable
    public static <T> IEnderNetworkComponent<T> clearComponent(IEnderNetworkComponent<T> component){
        if (component != null) {
            IEnderConnection connection = component.getCurrentConnection();
            if (connection != null) {
                if (connection instanceof IStableEnderConnection) {
                    ((IStableEnderConnection) connection).terminateConnection();
                } else if (connection instanceof ItemConnection) {
                    ((ItemConnection) connection).invalidate();
                } else {
                    throw new RuntimeException("Invalid connection type: " + connection);
                }
            }
            return component;
        }
        return null;
    }

    @Nullable
    public static IEnderNetworkComponent getComponent(@Nullable ICapabilityProvider capabilityProvider, @Nullable EnumFacing side){
        if (capabilityProvider != null && capabilityProvider.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, side)){
            return capabilityProvider.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, side);
        }
        return null;
    }

}
