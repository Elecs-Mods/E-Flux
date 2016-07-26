package elec332.eflux.api.energy;

import elec332.eflux.api.EFluxAPI;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Elec332 on 25-7-2016.
 */
public enum ConnectionType {

    PROVIDER(IEnergyProvider.class) {

        @Override
        public Capability<IEnergyProvider> getCapability() {
            return EFluxAPI.PROVIDER_CAPABILITY;
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

    <T extends IEnergyTile> ConnectionType(Class<T> type){
        this.type = type;
    }

    private final Class<? extends IEnergyTile> type;

    public Class<? extends IEnergyTile> getType() {
        return type;
    }

    public abstract Capability<? extends IEnergyTile> getCapability();

    public abstract boolean isProvider();

    public abstract boolean isReceiver();

    static {
        EFluxAPI.dummyLoad();
    }

}
