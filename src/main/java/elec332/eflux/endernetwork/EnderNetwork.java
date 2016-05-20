package elec332.eflux.endernetwork;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import elec332.core.nbt.NBTMap;
import elec332.core.util.BasicInventory;
import elec332.core.util.NBTHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.*;
import elec332.eflux.api.ender.internal.*;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.ISpecialEnergySource;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.api.energy.container.IEFluxPowerHandler;
import elec332.eflux.multiblock.machine.MultiBlockEnderContainer;
import elec332.eflux.network.PacketSendEnderNetworkData;
import elec332.eflux.network.PacketSyncEnderNetwork;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Elec332 on 18-2-2016.
 */
public final class EnderNetwork implements INBTSerializable<NBTTagCompound>, IEFluxPowerHandler, IEnergyReceiver, ISpecialEnergySource, ITickable, IEnderNetwork {

    @SuppressWarnings("unchecked")
    EnderNetwork(UUID id, Side side){
        this.energyContainer = new EnergyContainer(10000, this);
        this.id = id;
        this.maxID = 5;
        this.side = side;
        this.capabilityMap = NBTMap.newNBTMap(Integer.class, EnderCapabilityWrapper.class, new Function<Integer, EnderCapabilityWrapper>() {
            @Nullable
            @Override
            public EnderCapabilityWrapper apply(@Nullable Integer input) {
                return new EnderCapabilityWrapper(EnderNetwork.this);
            }
        });
        this.capabilities = new IEnderCapability[maxID];
        this.linkedReferences = new WeakReference[maxID];
        this.activeConnections = new List[maxID];
        this.capabilityNetworkHandlers = new CapabilityNetworkHandler[maxID];
        this.tickables = Lists.newArrayList();
        this.upgradeInventory = new BasicInventory("", maxID){

            @Override
            public boolean isItemValidForSlot(int id, ItemStack stack) {
                return stack == null || stack.getItem() instanceof IEnderCapabilityContainingItem;
            }

            @Override
            public int getInventoryStackLimit() {
                return 1;
            }

            @Override
            public void setInventorySlotContents(int slotID, ItemStack stack) {
                super.setInventorySlotContents(slotID, stack);
                if (EnderNetwork.this.side.isServer()) {
                    if (stack != null) {
                        IEnderCapabilityFactory factory = ((IEnderCapabilityContainingItem) stack.getItem()).getCapabilityFactory(stack);
                        setCapability(new EnderCapabilityWrapper(factory, stack, EnderNetwork.this), slotID);
                    } else {
                        setCapability(null, slotID);
                    }
                }
            }

        };
    }

    public static final int INVALID = -1;

    private final Side side;
    private final UUID id;
    private boolean powered;
    private int drain;

    private EnergyContainer energyContainer;
    private int maxID;
    private final NBTMap<Integer, EnderCapabilityWrapper> capabilityMap;
    private IEnderCapability[] capabilities;
    private WeakReference<IEnderCapability>[] linkedReferences;
    List<IStableEnderConnection>[] activeConnections;
    private CapabilityNetworkHandler[] capabilityNetworkHandlers;
    private List<ITickable> tickables;
    private BasicInventory upgradeInventory;

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound saveTag = new NBTTagCompound();
        energyContainer.writeToNBT(saveTag);
        saveTag.setTag("capsE", capabilityMap.serializeNBT());
        saveTag.setInteger("mxI", maxID);
        upgradeInventory.writeToNBT(saveTag);
        saveTag.setBoolean("powered", powered);
        return saveTag;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deserializeNBT(NBTTagCompound nbt) {
        disconnectAll(DisconnectReason.UNKNOWN);
        for (CapabilityNetworkHandler networkHandler : capabilityNetworkHandlers){
            if (networkHandler != null){
                networkHandler.invalidate();
            }
        }
        energyContainer.readFromNBT(nbt);
        capabilityMap.deserializeNBT(nbt.getTagList("capsE", 10));
        capabilities = new IEnderCapability[maxID];
        linkedReferences = new WeakReference[maxID];
        activeConnections = new List[maxID];
        capabilityNetworkHandlers = new CapabilityNetworkHandler[maxID];
        tickables.clear();
        for (Map.Entry<Integer, EnderCapabilityWrapper> entry : capabilityMap.entrySet()){
            IEnderCapability cap = entry.getValue().getCapability();
            setCapabilityInArray(entry.getKey(), cap);
            Object o = cap.get();
            if (o instanceof ITickable){
                tickables.add((ITickable) o);
            }
        }
        powered = nbt.getBoolean("powered");
        upgradeInventory.readFromNBT(nbt);
        calculatePower();
        maxID = nbt.getInteger("mxI");
        if (side.isServer()){
            syncToClient();
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <T> IEnderCapability<T> getCapability(Capability<T> capability, int id){
        if (!isValidFrequency(id) || !powered){
            return null;
        }
        IEnderCapability cap = capabilities[id];
        if (cap != null && cap.getCapability() == capability){
            return cap;
        }
        return null;
    }

    public void getStackTooltip(List<String> list, int slot){
        if (!isValidFrequency(slot)){
            return;
        }
        IEnderCapability cap = capabilities[slot];
        if (cap != null){
            cap.addInformation(list);
        }
    }

    public String getCapabilityInformation(int slot){
        if (!isValidFrequency(slot)){
            return "";
        }
        IEnderCapability cap = capabilities[slot];
        if (cap != null){
            return cap.getInformation();
        }
        return "";
    }

    public boolean isPowered() {
        return powered;
    }

    private void setCapability(EnderCapabilityWrapper capabilityWrapper, int id){
        if (!isValidFrequency(id)){
            return;
        }
        //System.out.println("setCap: "+(capabilityWrapper != null ? capabilityWrapper.getCapability().getCapability().getName() : "null") + "  "+id+"    "+side);
        EnderCapabilityWrapper old = capabilityMap.get(id);
        if (old != null) {
            IEnderCapability oldCap = old.getCapability();
            if (oldCap instanceof ITickable) {
                tickables.remove(oldCap);
            }
        }
        capabilityMap.remove(id);
        setCapabilityInArray(id, null);
        if (capabilityWrapper != null) {
            capabilityMap.put(id, capabilityWrapper);
            IEnderCapability capability = capabilityWrapper.getCapability();
            setCapabilityInArray(id, capability);
            if (capability instanceof ITickable) {
                tickables.add((ITickable) capability);
            }
        }
        calculatePower();
        if (side.isServer()) {
            syncToClient();
        }
    }

    private boolean isValidFrequency(int freq){
        return freq >= 0 && freq < maxID;
    }

    public int getMaxID() {
        return maxID;
    }

    public BasicInventory getUpgradeInventory() {
        return upgradeInventory;
    }

    @Override
    public void syncToClient(){
        EFlux.networkHandler.getNetworkWrapper().sendToAll(new PacketSyncEnderNetwork(this));
        //System.out.println("synctoclient");
    }

    public void syncToClient(EntityPlayerMP player){
        EFlux.networkHandler.getNetworkWrapper().sendTo(new PacketSyncEnderNetwork(this), player);
        //System.out.println("synctoclient");
    }

    @Override
    public UUID getNetworkId() {
        return id;
    }

    public Side getSide() {
        return side;
    }

    private void setCapabilityInArray(int i, IEnderCapability capability){
        IEnderCapability old = capabilities[i];
        if (old != null){
            old.invalidate();
            if (side.isServer()) {
                capabilityNetworkHandlers[i].invalidate();
            }
        }
        capabilities[i] = capability;
        WeakReference reference = linkedReferences[i];
        if (reference != null){
            reference.clear();
        }
        List<IStableEnderConnection> list = activeConnections[i];
        if (list != null){
            for (IStableEnderConnection connection : list){
                disconnect_internal(connection, DisconnectReason.CAPABILITY_REMOVED);
            }
        }
        activeConnections[i] = null;
        if (capability != null) {
            linkedReferences[i] = new WeakReference<IEnderCapability>(capability);
            activeConnections[i] = Lists.newArrayList();
            if (side.isServer()) {
                capabilityNetworkHandlers[i] = new CapabilityNetworkHandler(i, this);
                capability.setNetworkHandler(capabilityNetworkHandlers[i]);
            }
            capability.validate();
        }
    }

    private void calculatePower(){
        int power = 0;
        for (IEnderCapability capability : capabilities){
            if (capability != null) {
                power += capability.getPowerConsumption();
            }
        }
        this.drain = power;
    }

    public EnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public void update() {
        setPowered(energyContainer.drainPower(drain));
        if (powered) {
            for (ITickable tickable : tickables) {
                tickable.update();
            }
        }
    }

    private void setPowered(boolean powered){
        if (powered != this.powered){ //Wow....
            this.powered = powered;
            if (side.isServer()) {
                sendPacket(0, new NBTHelper().addToTag(powered, "powered").serializeNBT());
            }
            if (!powered){
                disconnectAll(DisconnectReason.OUT_OF_POWER);
                for (int i = 0; i < capabilities.length; i++) {
                    linkedReferences[i] = new WeakReference<IEnderCapability>(capabilities[i]);
                }
                for (IEnderCapability capability : capabilities){
                    if (capability != null){
                        capability.invalidate();
                    }
                }
            } else {
                for (IEnderCapability capability : capabilities){
                    if (capability != null){
                        capability.validate();
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void disconnectAll(DisconnectReason reason){
        for (WeakReference reference : linkedReferences){
            if (reference != null){
                reference.clear();
            }
        }
        for (List<IStableEnderConnection> connections : activeConnections){
            if (connections != null){
                for (IStableEnderConnection connection : connections){
                    disconnect_internal(connection, reason);
                }
            }
        }
        linkedReferences = new WeakReference[maxID];
        activeConnections = new List[maxID];
    }

    /**
     * The amount returned here is NOT supposed to change, anf if it does,
     * do not forget that it will receive a penalty if the machine is not running at optimum RP
     *
     * @return the amount of requested EF
     */
    @Override
    public int getEFForOptimalRP() {
        return (int) (drain * 1.2);
    }

    @Override
    public float getAcceptance() {
        return 0.2f;
    }

    @Override
    public int getOptimalRP() {
        return MultiBlockEnderContainer.ENDER_RP_REQ;
    }

    @Override
    public void markObjectDirty() {
        //syncToClient(); Gets called too often
    }

    /**
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP() {
        return energyContainer.requestedRP();
    }

    /**
     * @param rp The Redstone Potential in the network
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    @Override
    public int getRequestedEF(int rp) {
        return energyContainer.getRequestedEF(rp);
    }

    /**
     * @param rp the RedstonePotential in the network
     * @param ef the amount of EnergeticFlux that is being provided
     * @return The amount of EnergeticFlux that wasn't used
     */
    @Override
    public int receivePower(int rp, int ef) {
        return energyContainer.receivePower(rp, ef);
    }

    /**
     * @param rp    the RedstonePotential in the network
     * @param reqEF the requested amount of EnergeticFlux
     * @return The amount of EnergeticFlux the tile will provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergeticFlux(int rp, int reqEF) {
        return rp * reqEF > mpe() ? provideEnergy(rp, true) : (energyContainer.drainPower(rp*reqEF) ? reqEF : 0);
    }

    /**
     * @param rp      the RedstonePotential in the network
     * @param execute weather the power is actually drawn from the tile,
     *                this flag is always true for IEnergySource.
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, boolean execute) {
        int ret = mpe()/rp;
        if (execute){
            energyContainer.drainPower(ret);
        }
        return ret;
    }

    private int mpe(){
        return Math.min(1000, energyContainer.getStoredPower());
    }

    @Override
    public int[] getFrequencies(Capability capability) {
        int[] ret = new int[0];
        if (!powered){
            return ret;
        }
        for (int i = 0; i < capabilities.length; i++) {
            IEnderCapability enderCapability = capabilities[i];
            if (enderCapability != null && enderCapability.getCapability() == capability){
                int len = ret.length;
                ret = Arrays.copyOf(ret, len + 1);
                ret[len] = i;
            }
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean connect(@Nonnull IEnderNetworkComponent component) {
        if (!id.equals(component.getUuid())){
            throw new IllegalArgumentException();
        }
        int id = component.getFrequency();
        IEnderCapability capability = getCapability(component.getRequiredCapability(), id);
        if (capability == null){
            return false;
        }
        if (component instanceof IEnderNetworkTile) {
            IStableEnderConnection connection = (IStableEnderConnection) component.getCurrentConnection();
            if (connection != null) {
                connection.terminateConnection();
            }
            IEnderNetworkTile tile = (IEnderNetworkTile) component;
            StableConnection.makeConnection(this, capability, tile);
            if (side.isServer()) {
                TileEntity tileEntity = tile.getTile();
                sendPacket(2, new NBTHelper().addToTag(tileEntity.getPos()).addToTag(WorldHelper.getDimID(tileEntity.getWorld()), "dim").serializeNBT());
            }
            return true;
        } else {
            IEnderConnection connection = component.getCurrentConnection();
            if (connection != null){
                ((ItemConnection)connection).invalidate();
            }
            component.onConnect(new ItemConnection<>(component, linkedReferences[id]));
            return true;
        }
    }

    @Override
    public boolean drainPower(int power) {
        return powered && energyContainer.drainPower(power);
    }

    @Override
    public int getStoredPower() {
        return powered ? energyContainer.getStoredPower() : 0;
    }

    private void sendPacket(int i, NBTTagCompound data){
        EFlux.networkHandler.getNetworkWrapper().sendToAll(new PacketSendEnderNetworkData(this, i, data));
    }

    void sendCapabilityPacket(int cap, int id, NBTTagCompound data){
        sendPacket(1, new NBTHelper().addToTag(cap, "cap").addToTag(id, "id").addToTag(data, "data").serializeNBT());
    }

    public void onPacket(int id, NBTTagCompound data){
        switch (id){
            case 0:
                setPowered(data.getBoolean("powered"));
                break;
            case 1:
                IEnderCapability capability = capabilities[data.getInteger("cap")];
                if (capability != null){
                    capability.onDataPacket(data.getInteger("id"), data.getCompoundTag("data"));
                }
                break;
            case 2:
                World world = EFlux.proxy.getClientWorld();
                NBTHelper nbt = new NBTHelper(data);
                if (WorldHelper.getDimID(world) == nbt.getInteger("dim")){
                    TileEntity tile = WorldHelper.getTileAt(world, nbt.getPos());
                    if (tile != null && tile.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)){
                        IEnderNetworkTile ent = ((IEnderNetworkTile)tile.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null));
                        if (ent != null) {
                            connect(ent);
                        }
                    }
                }
                break;
        }
    }

    static <T> void disconnect_internal(IStableEnderConnection<T> connection, DisconnectReason reason){
        if (connection.getComponent().getCurrentConnection() != connection){
            throw new IllegalStateException();
        }
        connection.getComponent().onDisconnect(connection, reason);
        connection.getEnderCapability().removeConnection(connection, reason);
    }

}
