package elec332.eflux.grid.energy;

import com.google.common.collect.Sets;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyGenerator;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.api.energy.IEnergyProvider;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Created by Elec332 on 24-7-2016.
 */
public class EFluxEnergyGrid {

    protected EFluxEnergyGrid(){
        this.allObjects = Sets.newHashSet();
        this.allObjectsImmutable = Collections.unmodifiableSet(allObjects);
        this.allConnections = Sets.newHashSet();
        this.allConnectionsImmutable = Collections.unmodifiableSet(allConnections);
        this.receivers = Sets.newHashSet();
        this.providers = Sets.newHashSet();
        this.specialProviders = Sets.newHashSet();
        this.maxEF = maxRP = maxPow = -1;
        this.transmitters = Sets.newHashSet();
        this.random = new Random();
        this.uuid = UUID.randomUUID();
    }

    private final Set<EFluxEnergyObject> allObjects, allObjectsImmutable, transmitters;
    private final Set<ConnectionData> allConnections, allConnectionsImmutable;
    private final Set<ConnectionData> receivers, providers, specialProviders;
    private final Random random;
    private final UUID uuid;

    private int maxEF, maxRP, maxPow;

    int processedEF, processedRP;

    public void tick(){
        distributePower();
        //debugStuff();
    }

    protected void merge(EFluxEnergyGrid grid){
        for (ConnectionData connectionData : grid.allConnections){
            for (EnumFacing facing : connectionData.facing) {
                addObject(connectionData.object, facing);
            }
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
        if (side == null){
            throw new IllegalArgumentException();
        }
        o.setGridForFace(this, side);
        ConnectionData connectionData;
        if (allObjects.add(o)) {
            connectionData = new ConnectionData(o, side);
        } else {
            Set<EnumFacing> conn = o.getConnectorSides();
            Set<ConnectionData> c = getConnections(o);
            if (conn.isEmpty() || !conn.contains(side)){
                for (ConnectionData cd : c){
                    if (cd.connectedFacing == side){
                        return;
                    }
                }
                connectionData = new ConnectionData(o, side);
            } else {
                EnumSet<EnumFacing> rC = EnumSet.noneOf(EnumFacing.class);
                Set<ConnectionData> c2 = Sets.newHashSet();
                c.add(new ConnectionData(o, side));
                for (ConnectionData d : c) {
                    for (EnumFacing facing : d.facing) {
                        if (conn.contains(facing)) {
                            c2.add(d);
                            rC.addAll(d.facing);
                            break;
                        }
                    }
                }
                removeConnections(c2);
                connectionData = new ConnectionData(o, rC);
            }
        }
        identifyAdd(connectionData, side);
    }

    private void identifyAdd(ConnectionData connectionData, EnumFacing backup){
        ICapabilityProvider c = connectionData.object;
        EnumFacing f = connectionData.connectedFacing;
        boolean b = false;
        if (EFluxGridHandler.isValidProvider(c, f)){
            IEnergyGenerator provider = c.getCapability(EFluxAPI.GENERATOR_CAPABILITY, f);
            if (provider instanceof IEnergyProvider){
                specialProviders.add(connectionData);
                b = true;
            } else {
                providers.add(connectionData);
                b = true;
            }
        }
        if (EFluxGridHandler.isValidReceiver(c, f)){
            receivers.add(connectionData);
            b = true;
        }
        if (EFluxGridHandler.isValidTransmitter(c, backup)){
            IEnergyTransmitter transmitter = c.getCapability(EFluxAPI.TRANSMITTER_CAPABILITY, backup);
            checkTransferRate(transmitter);
            transmitters.add(connectionData.object);
            b = true;
        }
        if (b){
            allConnections.add(connectionData);
        } else {
            System.out.println("conn: "+connectionData.connectedFacing+" r:"+backup + "    "+connectionData.object.getTileEntity()+"  "+connectionData.facing);
            throw new RuntimeException();
        }
    }

    private void checkTransferRate(IEnergyTransmitter transmitter){
        if (transmitter == null){
            return;
        }
        if (maxEF == -1){
            maxEF = transmitter.getMaxEFTransfer();
        } else if (transmitter.getMaxEFTransfer() != -1){
            maxEF = Math.min(maxEF, transmitter.getMaxEFTransfer());
        }
        if (maxRP == -1){
            maxRP = transmitter.getMaxRPTransfer();
        } else if (transmitter.getMaxRPTransfer() != -1){
            maxRP = Math.min(maxRP, transmitter.getMaxRPTransfer());
        }
        if (maxPow == -1){
            maxPow = transmitter.getMaxPowerTransfer();
        } else if (transmitter.getMaxPowerTransfer() != -1){
            maxPow = Math.min(maxPow, transmitter.getMaxPowerTransfer());
        }
    }

    protected void removeObject(EFluxEnergyObject o){
        allObjects.remove(o);
        transmitters.remove(o);
        removeConnections(getConnections(o));
    }

    private Set<ConnectionData> removeConnections(Set<ConnectionData> remove){
        allConnections.removeAll(remove);
        receivers.removeAll(remove);
        providers.removeAll(remove);
        specialProviders.removeAll(remove);
        return remove;
    }

    private Set<ConnectionData> getConnections(EFluxEnergyObject o){
        Set<ConnectionData> remove = Sets.newHashSet();
        for (ConnectionData connectionData : allConnections){
            if (connectionData.object.equals(o)){
                remove.add(connectionData);
            }
        }
        return remove;
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
        int rp = 0;
        int ef = 0;
        int resistance = 0;
        int reqPow = 0;
/*
        int[] va = new int[this.receivers.size()];
        IEnergyReceiver[] vReceivers = new IEnergyReceiver[this.receivers.size()];

        int[] vs = new int[specialProviders.size()];
        IEnergyProvider[] vProviders = new IEnergyProvider[specialProviders.size()];

        int counter = 0;
        for (ConnectionData gridData : this.receivers) {
            IEnergyReceiver receiver = gridData.object.getCapability(EFluxAPI.RECEIVER_CAPABILITY, gridData.connectedFacing);
            resistance += 1f/receiver.getResistance();
            reqPow = (receiver.getRequestedEF() * receiver.getRequestedEF())/receiver.getResistance();
            vReceivers[counter] = receiver;
            counter++;
        }
        counter = 0;

        for (ConnectionData gridData : providers) {
            IEnergyGenerator provider = gridData.object.getCapability(EFluxAPI.GENERATOR_CAPABILITY, gridData.connectedFacing);
            float calcVar = (random.nextFloat() * (provider.getVariance() * 2)) - provider.getVariance();
            int efC = provider.getCurrentAverageEF();

        }

/*


        if (maxTransfer > -1) {
            rp = Math.min(rp, maxTransfer);
        }

        for (ConnectionData gridData : specialProviders) {
            IEnergyProvider energyProvider = (IEnergyProvider) gridData.object.getCapability(EFluxAPI.GENERATOR_CAPABILITY, gridData.connectedFacing);
            int e = energyProvider.provideEnergy(rp, false);
            vs[counter] = e;
            specialCanProvide += e;
            vProviders[counter] = energyProvider;
            counter++;
        }
        for (int i = 0; i < vReceivers.length; i++) {
            IEnergyReceiver receiver = vReceivers[i];
            int e = receiver.getRequestedEF(rp);
            va[i] = e;
            requestedPower += e;
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
        processedRP = rp;
        processedEF = totalProvided;*/
    }

    private void debugStuff(){
        if (EFluxGridHandler.shouldDebug()) {
            for (EFluxEnergyObject o : allObjects) {
                System.out.println(o.getPosition().toString());
            }
        }
        System.out.println("start");
        Set<BlockPos> s = Sets.newHashSet();
        for (ConnectionData connectionData : allConnectionsImmutable){
            System.out.println(connectionData.object.getPosition()+"   "+connectionData.object+"   "+connectionData.object.getTileEntity()+"   "+ connectionData.facing);
            if (!s.add(connectionData.object.getPosition().getPos())){
                System.out.println("Double pos: "+connectionData.object.getPosition());
            }
        }
        System.out.println("stop");
    }

    public class ConnectionData {

        private ConnectionData(EFluxEnergyObject object, EnumFacing facing){
            this.object = object;
            this.facing = EnumSet.of(facing);
            this.connectedFacing = facing;
        }

        private ConnectionData(EFluxEnergyObject object, EnumSet<EnumFacing> facing){
            this.object = object;
            this.facing = facing;
            this.connectedFacing = null;
        }

        protected final EFluxEnergyObject object;
        protected final EnumSet<EnumFacing> facing;
        protected final EnumFacing connectedFacing;

    }

}
