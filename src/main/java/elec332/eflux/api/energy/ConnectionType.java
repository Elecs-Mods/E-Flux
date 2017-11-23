package elec332.eflux.api.energy;

import elec332.eflux.api.EFluxAPI;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Elec332 on 25-7-2016.
 */
public enum ConnectionType {

    PROVIDER(IEnergySource.class) {

        @Override
        public Capability<IEnergySource> getCapability() {
            return EFluxAPI.GENERATOR_CAPABILITY;
        }

        @Override
        public boolean isProvider() {
            return true;
        }

        @Override
        public boolean isReceiver() {
            return false;
        }

    },
    RECEIVER(IEnergyReceiver.class) {

        @Override
        public Capability<IEnergyReceiver> getCapability() {
            return EFluxAPI.RECEIVER_CAPABILITY;
        }

        @Override
        public boolean isProvider() {
            return false;
        }

        @Override
        public boolean isReceiver() {
            return true;
        }

    },
    TRANSMITTER(IEnergyTransmitter.class) {

        @Override
        public Capability<IEnergyTransmitter> getCapability() {
            return EFluxAPI.TRANSMITTER_CAPABILITY;
        }

        @Override
        public boolean isProvider() {
            return true;
        }

        @Override
        public boolean isReceiver() {
            return true;
        }

    };

    <T extends IEnergyObject> ConnectionType(Class<T> type){
        this.type = type;
    }

    private final Class<? extends IEnergyObject> type;

    public Class<? extends IEnergyObject> getType() {
        return type;
    }

    public abstract Capability<? extends IEnergyObject> getCapability();

    public abstract boolean isProvider();

    public abstract boolean isReceiver();

    static {
        EFluxAPI.dummyLoad();
    }

}
