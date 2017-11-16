package elec332.eflux.api;

import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEndergyCapability;
import elec332.eflux.api.energy.IEnergyGenerator;
import elec332.eflux.api.energy.IEnergyReceiver;
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
    @CapabilityInject(IEnergyGenerator.class)
    public static Capability<IEnergyGenerator> GENERATOR_CAPABILITY;
    @CapabilityInject(IEnergyTransmitter.class)
    public static Capability<IEnergyTransmitter> TRANSMITTER_CAPABILITY;

    @CapabilityInject(IHeatReceiver.class)
    public static Capability<IHeatReceiver> HEAT_CAPABILITY;

    @CapabilityInject(IEnderNetworkComponent.class)
    public static Capability<IEnderNetworkComponent> ENDER_COMPONENT_CAPABILITY;

    @CapabilityInject(IEndergyCapability.class)
    public static Capability<IEndergyCapability> ENDERGY_ENDER_CAPABILITY;

    @CapabilityInject(ICircuit.class)
    public static Capability<ICircuit> EFLUX_CIRCUIT_CAPABILITY;

    public static void dummyLoad(){
    }

    static {
        registerWithoutStorageAndDefaultInstance(IEnergyReceiver.class);
        registerWithoutStorageAndDefaultInstance(IEnergyGenerator.class);
        registerWithoutStorageAndDefaultInstance(IEnergyTransmitter.class);
        registerWithoutStorageAndDefaultInstance(IHeatReceiver.class);
        registerWithoutStorageAndDefaultInstance(IEnderNetworkComponent.class);
        registerWithoutStorageAndDefaultInstance(IEndergyCapability.class);
        registerWithoutStorageAndDefaultInstance(ICircuit.class);
    }

    @SuppressWarnings("all")
    static void registerWithoutStorageAndDefaultInstance(Class clazz){
        CapabilityManager.INSTANCE.register(clazz, new Capability.IStorage() {

            @Override
            public NBTBase writeNBT(Capability capability, Object instance, EnumFacing side) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void readNBT(Capability capability, Object instance, EnumFacing side, NBTBase nbt) {
                throw new UnsupportedOperationException();
            }

        }, new Callable() {

            @Override
            public Object call() throws Exception {
                throw new UnsupportedOperationException();
            }

        });
    }

}
