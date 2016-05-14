package elec332.eflux.api;

import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.energy.IEnergyMonitor;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.api.heat.IHeatReceiver;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

/**
 * Created by Elec332 on 4-2-2016.
 */
public class EFluxAPI {

    @CapabilityInject(IEnergyReceiver.class)
    public static Capability<IEnergyReceiver> RECEIVER_CAPABILITY;
    @CapabilityInject(IEnergySource.class)
    public static Capability<IEnergySource> PROVIDER_CAPABILITY;
    @CapabilityInject(IEnergyTransmitter.class)
    public static Capability<IEnergyTransmitter> TRANSMITTER_CAPABILITY;
    @CapabilityInject(IEnergyMonitor.class)
    public static Capability<IEnergyMonitor> MONITOR_CAPABILITY;

    @CapabilityInject(IHeatReceiver.class)
    public static Capability<IHeatReceiver> HEAT_CAPABILITY;

    @CapabilityInject(IEnderNetworkComponent.class)
    public static Capability<IEnderNetworkComponent> ENDER_COMPONENT_CAPABILITY;

    public static void dummyLoad(){
    }

    static {
        registerWithoutStorageAndDefaultInstance(IEnergyReceiver.class);
        registerWithoutStorageAndDefaultInstance(IEnergySource.class);
        registerWithoutStorageAndDefaultInstance(IEnergyTransmitter.class);
        registerWithoutStorageAndDefaultInstance(IEnergyMonitor.class);
        registerWithoutStorageAndDefaultInstance(IHeatReceiver.class);
        registerWithoutStorageAndDefaultInstance(IEnderNetworkComponent.class);
    }

    static void registerWithoutStorageAndDefaultInstance(Class clazz){
        CapabilityManager.INSTANCE.register(clazz, new Capability.IStorage() {

            @Override
            public NBTBase writeNBT(Capability capability, Object instance, EnumFacing side) {
                return null;
            }

            @Override
            public void readNBT(Capability capability, Object instance, EnumFacing side, NBTBase nbt) {

            }

        }, new Callable() {

            @Override
            public Object call() throws Exception {
                throw new UnsupportedOperationException();
            }

        });
    }

}
