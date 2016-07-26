package elec332.eflux.energy.grid;

import com.google.common.collect.Sets;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyProvider;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.api.energy.ISpecialEnergyProvider;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

/**
 * Created by Elec332 on 24-7-2016.
 */
public class EnergyGrid {

    protected EnergyGrid(){
        this.allObjects = Sets.newHashSet();
        this.allObjectsImmutable = Collections.unmodifiableSet(allObjects);
        this.allConnections = Sets.newHashSet();
        this.allConnectionsImmutable = Collections.unmodifiableSet(allConnections);
        this.receivers = Sets.newHashSet();
        this.providers = Sets.newHashSet();
        this.specialProviders = Sets.newHashSet();
        this.maxTransfer = -1;
        this.transmitters = Sets.newHashSet();
    }

    private final Set<EFluxEnergyObject> allObjects, allObjectsImmutable, transmitters;
    private final Set<ConnectionData> allConnections, allConnectionsImmutable;
    private final Set<ConnectionData> receivers, providers, specialProviders;

    private int maxTransfer;

    public void tick(){
        debugStuff();
        distributePower();
    }

    protected void merge(EnergyGrid grid){
        for (ConnectionData connectionData : grid.allConnections){
            addObject(connectionData.object, connectionData.facing);
        }
    }

    @Nonnull
    protected Set<EFluxEnergyObject> getConnectedObjects(){
        return allObjectsImmutable;
    }

    @Nonnull
    public Set<ConnectionData> getAllConnections() {
        return allConnectionsImmutable;
    }

    protected void addObject(EFluxEnergyObject o, EnumFacing side){
        o.setGridForFace(this, side);
        allObjects.add(o);
        ConnectionData connectionData = new ConnectionData(o, side);
        allConnections.add(connectionData);
        identifyAdd(connectionData);
    }

    private void identifyAdd(ConnectionData connectionData){
        ICapabilityProvider c = connectionData.object;
        EnumFacing f = connectionData.facing;
        if (GridObjectHandler.isValidProvider(c, f)){
            IEnergyProvider provider = c.getCapability(EFluxAPI.PROVIDER_CAPABILITY, f);
            if (provider instanceof ISpecialEnergyProvider){
                specialProviders.add(connectionData);
            } else {
                providers.add(connectionData);
            }
        }
        if (GridObjectHandler.isValidReceiver(c, f)){
            receivers.add(connectionData);
        }
        if (GridObjectHandler.isValidTransmitter(c, f)){
            IEnergyTransmitter transmitter = c.getCapability(EFluxAPI.TRANSMITTER_CAPABILITY, f);
            int i = transmitter.getMaxEFTransfer();
            checkTransferRate(i);
            transmitters.add(connectionData.object);
        }
    }

    private void checkTransferRate(int newPossibleRate){
        if (newPossibleRate == -1){
            return;
        }
        if (maxTransfer == -1){
            maxTransfer = newPossibleRate;
        } else {
            maxTransfer = Math.min(maxTransfer, newPossibleRate);
        }
    }

    protected void removeObject(EFluxEnergyObject o){
        allObjects.remove(o);
        transmitters.remove(o);
        Set<ConnectionData> remove = Sets.newHashSet();
        for (ConnectionData connectionData : allConnections){
            if (connectionData.object.equals(o)){
                remove.add(connectionData);
            }
        }
        allConnections.removeAll(remove);
        receivers.removeAll(remove);
        providers.removeAll(remove);
        specialProviders.removeAll(remove);
    }

    protected void validate(){

    }

    protected void invalidate(){
        allObjects.clear();
        allConnections.clear();
        receivers.clear();
        providers.clear();
        specialProviders.clear();
        transmitters.clear();
    }

    @SuppressWarnings("all")
    private void distributePower(){
        int requestedPower = 0;
        int rp = 1;
        int totalProvided = 0;
        int specialCanProvide = 0;

        int[] va = new int[this.receivers.size()];
        IEnergyReceiver[] vReceivers = new IEnergyReceiver[this.receivers.size()];

        int[] vs = new int[specialProviders.size()];
        ISpecialEnergyProvider[] vProviders = new ISpecialEnergyProvider[specialProviders.size()];

        int counter = 0;
        for (ConnectionData gridData : this.receivers) {
            IEnergyReceiver receiver = gridData.object.getCapability(EFluxAPI.RECEIVER_CAPABILITY, gridData.facing);
            rp = Math.max(rp, receiver.requestedRP());
            vReceivers[counter] = receiver;
            counter++;
        }
        counter = 0;
        for (ConnectionData gridData : providers) {
            totalProvided += gridData.object.getCapability(EFluxAPI.PROVIDER_CAPABILITY, gridData.facing).provideEnergy(rp, true);
        }
        for (ConnectionData gridData : specialProviders) {
            ISpecialEnergyProvider energyProvider = (ISpecialEnergyProvider) gridData.object.getCapability(EFluxAPI.PROVIDER_CAPABILITY, gridData.facing);
            int e = energyProvider.provideEnergy(rp, false);
            vs[counter] = e;
            specialCanProvide += e;
            vProviders[counter] = energyProvider;
        }
        for (int i = 0; i < vReceivers.length; i++) {
            IEnergyReceiver receiver = vReceivers[i];
            int e = receiver.getRequestedEF(rp);
            va[i] = e;
            requestedPower += e;
        }
        if (maxTransfer > -1) { //TODO: Move up?
            rp = Math.min(rp, maxTransfer);
        }
        if (totalProvided >= requestedPower){
            for (int i = 0; i < vReceivers.length; i++) {
                vReceivers[i].receivePower(rp, va[i]);
            }
        } else if (totalProvided > 0 || specialCanProvide > 0){
            int needed = requestedPower-totalProvided;
            if (specialCanProvide > needed){
                float d = needed/(float)specialCanProvide;
                for (int i = 0; i < vProviders.length; i++) {
                    totalProvided += vProviders[i].provideEnergeticFlux(rp, (int) (vs[i] * d));
                }
            } else {
                for (int i = 0; i < vProviders.length; i++) {
                    totalProvided += vProviders[i].provideEnergeticFlux(rp, vs[i]);
                }
            }
            if (totalProvided >= requestedPower) {
                for (int i = 0; i < vReceivers.length; i++) {
                    vReceivers[i].receivePower(rp, va[i]);
                }
            } else {
                float diff = (float) totalProvided / (float) requestedPower;
                for (int i = 0; i < vReceivers.length; i++) {
                    vReceivers[i].receivePower(rp, (int) (va[i] * diff));
                }
            }
        }
    }

    private void debugStuff(){
        if (GridObjectHandler.shouldDebug()) {
            for (EFluxEnergyObject o : allObjects) {
                System.out.println(o.getPosition().toString());
            }
        }
    }

    public class ConnectionData {

        private ConnectionData(EFluxEnergyObject object, EnumFacing facing){
            this.object = object;
            this.facing = facing;
        }

        protected final EFluxEnergyObject object;
        protected final EnumFacing facing;
/*
        @Override
        public boolean equals(Object obj) {
            return obj instanceof ConnectionData && ((ConnectionData) obj).object.equals(object) && ((ConnectionData) obj).facing == facing;
        }

        @Override
        public int hashCode() {
            return object.hashCode() * 31 + facing.hashCode();
        }*/

    }

}
