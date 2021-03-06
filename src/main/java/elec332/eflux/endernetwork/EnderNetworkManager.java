package elec332.eflux.endernetwork;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import elec332.core.api.data.IExternalSaveHandler;
import elec332.core.api.network.ElecByteBuf;
import elec332.core.api.network.object.INetworkObject;
import elec332.core.api.network.object.INetworkObjectHandler;
import elec332.core.api.registry.ISingleRegister;
import elec332.core.main.ElecCore;
import elec332.core.nbt.NBTMap;
import elec332.core.util.NBTHelper;
import elec332.core.util.NBTTypes;
import elec332.eflux.EFlux;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Elec332 on 18-2-2016.
 */
public final class EnderNetworkManager implements IExternalSaveHandler, INBTSerializable<NBTTagCompound>, ITickable, INetworkObject {

    private EnderNetworkManager(Side side){
        this.side = side;
        this.networkData = NBTMap.newNBTMap(UUID.class, EnderNetwork.class, new Function<UUID, EnderNetwork>() {

            @Override
            public EnderNetwork apply(UUID input) {
                return new EnderNetwork(input, EnderNetworkManager.this.side);
            }

        });
        this.validNetworks = Sets.newHashSet();
    }

    public static void dummy(){
    }

    public static void registerSaveHandler(ISingleRegister<IExternalSaveHandler> saveHandlerRegistry){
        if (registered){
            throw new IllegalStateException();
        }
        saveHandlerRegistry.register(get(Side.SERVER));
        MinecraftForge.EVENT_BUS.register(new Object(){

            @SubscribeEvent
            @SuppressWarnings("unused")
            public void onTick(TickEvent.ServerTickEvent event){
                if (event.phase == TickEvent.Phase.START){
                    get(Side.SERVER).update();
                }
            }

        });
        registered = true;
    }

    private static boolean registered;

    public static void onPacket(NBTTagCompound received, MessageContext messageContext){
        get(messageContext.side).deserializeNBT(received);
    }

    static void setNetworkData(EnderNetwork network, int i, NBTTagCompound data){
        if (network.getSide().isClient()){
            throw new IllegalArgumentException();
        }
        get(Side.SERVER).sendPacket(0, new NBTHelper().addToTag(network.getNetworkId(), "id").addToTag(i, "nr").addToTag(data, "tag").serializeNBT());
    }

    public static EnderNetworkManager get(World world){
        return get(!Preconditions.checkNotNull(world).isRemote ? Side.SERVER : Side.CLIENT);
    }

    private static EnderNetworkManager get(Side side){
        if (isServer){
            return INSTANCE;
        }
        return INSTANCES.get(side);
    }

    @SideOnly(Side.CLIENT)
    private static Map<Side, EnderNetworkManager> INSTANCES;

    @SideOnly(Side.SERVER)
    private static EnderNetworkManager INSTANCE;

    private static final boolean isServer;

    private NBTMap<UUID, EnderNetwork> networkData;
    private Set<UUID> validNetworks;
    private final Side side;

    @Nullable
    public EnderNetwork get(UUID uuid){
        if (uuid == null){
            return null;
        }
        EnderNetwork ret = networkData.get(uuid);
        if (ret == null){
            if (!validNetworks.contains(uuid)){
                return null;
            }
            throw new RuntimeException();
            // = new EnderNetwork(uuid, side);
            //networkData.put(uuid, ret);
        }
        return ret;
    }

    public UUID generateNewKey(){
        if (!side.isServer()){
            throw new IllegalAccessError();
        }
        UUID uuid = UUID.randomUUID();
        while (networkData.keySet().contains(uuid)){
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

    public EnderNetwork createNetwork(@Nonnull UUID uuid, @Nullable NBTTagCompound tag){
        if (!side.isServer()){
            throw new IllegalAccessError();
        }
        EnderNetwork ret = createNetwork_(uuid, tag);
        boolean hasTag = tag != null;
        ElecByteBuf byteBuf = sender.createByteBuf().writeUuid(uuid).writeBoolean(hasTag);
        if (hasTag){
            byteBuf.writeNBTTagCompoundToBuffer(tag);
        }
        sender.sendToAll(1, byteBuf);
        return ret;
    }

    private EnderNetwork createNetwork_(@Nonnull UUID uuid, @Nullable NBTTagCompound tag){
        if (networkData.get(uuid) != null){
            throw new IllegalStateException();
        }
        EnderNetwork network = new EnderNetwork(uuid, side);
        networkData.put(uuid, network); //Generate the network
        if (tag != null){
            network.deserializeNBT(tag);
        }
        validNetworks.add(uuid);
        return network;
    }

    @Nonnull
    public NBTTagCompound removeNetwork(UUID uuid){
        if (side.isServer() && uuid != null && networkData.containsKey(uuid)) {
            sender.sendToAll(2, sender.createByteBuf().writeUuid(uuid));
            return removeNetwork_(uuid).serializeNBT();
        }
        throw new IllegalArgumentException();
    }

    @Nonnull
    private EnderNetwork removeNetwork_(@Nonnull UUID uuid) {
        EnderNetwork ret = networkData.remove(uuid);
        if (ret != null) {
            validNetworks.remove(uuid);
            return ret;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void update() {
        for (EnderNetwork network : networkData.values()){
            network.update();
        }
    }

    //IO

    @Override
    public String getName() {
        return "EFluxEnderNetwork";
    }

    @Override
    public void load(ISaveHandler iSaveHandler, WorldInfo worldInfo, NBTTagCompound nbtTagCompound) {
        deserializeNBT(nbtTagCompound);
    }

    @Nullable
    @Override
    public NBTTagCompound save(ISaveHandler iSaveHandler, WorldInfo worldInfo) {
        return serializeNBT();
    }

    @Override
    public void nullifyData() {
        networkData.clear();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTHelper().addToTag(networkData.serializeNBT(), "lEN").addToTag(serializeKeys(), "Ukeys").serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        networkData.deserializeNBT(nbt.getTagList("lEN", 10));
        deserializeKeys(nbt.getTagList("Ukeys", NBTTypes.STRING.getID()));
    }

    private NBTTagList serializeKeys(){
        NBTTagList ret = new NBTTagList();
        for (UUID uuid : validNetworks){
            ret.appendTag(new NBTTagString(uuid.toString()));
        }
        return ret;
    }

    private void deserializeKeys(NBTTagList tagList) {
        Set<UUID> ret = Sets.newHashSet();
        for (int i = 0; i < tagList.tagCount(); i++) {
            ret.add(UUID.fromString(tagList.getStringTagAt(i)));
        }
        this.validNetworks = ret;
    }

    //Network

    private INetworkObjectHandler sender;

    @Override
    public void setNetworkObjectHandler(INetworkObjectHandler handler) {
        if (side.isServer()){
            sender = handler;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onPacket(int id, ElecByteBuf data) {
        switch (id){
            case 0:
                onNetworkPacket(data.readNBTTagCompoundFromBuffer());
                break;
            case 1:
                createNetwork_(data.readUuid(), data.readBoolean() ? data.readNBTTagCompoundFromBuffer() : null);
                break;
            case 2:
                removeNetwork_(data.readUuid());
                break;
        }
    }

    @SuppressWarnings("all")
    private void onNetworkPacket(NBTTagCompound tag){
        NBTHelper nbt = new NBTHelper(tag);
        EnderNetwork network = EnderNetworkManager.get(EFlux.proxy.getClientWorld()).get(nbt.getUUID("id"));
        network.onPacket(nbt.getInteger("nr"), nbt.getCompoundTag("tag"));
    }

    private void sendPacket(int i, NBTTagCompound tag){
        sender.sendToAll(i, tag);
    }

    static {
        Side side = FMLCommonHandler.instance().getSide();
        switch (side){
            case CLIENT:
                INSTANCES = Maps.newEnumMap(Side.class);
                for (Side sideT : Side.values()){
                    INSTANCES.put(sideT, new EnderNetworkManager(sideT));
                }
                ElecCore.networkHandler.registerNetworkObject(INSTANCES.get(Side.CLIENT), INSTANCES.get(Side.SERVER));
                break;
            case SERVER:
                INSTANCE = new EnderNetworkManager(Side.SERVER);
                ElecCore.networkHandler.registerNetworkObject(null, INSTANCE);
                break;
        }
        isServer = side.isServer();
    }

}
