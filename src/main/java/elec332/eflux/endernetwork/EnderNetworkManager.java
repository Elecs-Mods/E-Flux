package elec332.eflux.endernetwork;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import elec332.core.api.data.IExternalSaveHandler;
import elec332.core.api.network.ElecByteBuf;
import elec332.core.api.network.object.INetworkObject;
import elec332.core.api.network.object.INetworkObjectHandler;
import elec332.core.main.ElecCore;
import elec332.core.nbt.NBTMap;
import elec332.core.util.NBTHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import net.minecraft.nbt.NBTTagCompound;
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
import java.util.UUID;

/**
 * Created by Elec332 on 18-2-2016.
 */
public final class EnderNetworkManager implements IExternalSaveHandler, INBTSerializable<NBTTagCompound>, ITickable, INetworkObject {

    private EnderNetworkManager(Side side){
        this.side = side;
        networkData = NBTMap.newNBTMap(UUID.class, EnderNetwork.class, new Function<UUID, EnderNetwork>() {

            @Override
            public EnderNetwork apply(UUID input) {
                return new EnderNetwork(input, EnderNetworkManager.this.side);
            }

        });
    }

    public static void registerSaveHandler(){
        MinecraftForge.EVENT_BUS.register(new Object(){

            @SubscribeEvent
            public void onTick(TickEvent.ServerTickEvent event){
                if (event.phase == TickEvent.Phase.START){
                    get(Side.SERVER).update();
                }
            }

        });
    }

    public static int[] getValidFequencies(IEnderNetworkComponent component){
        return get(FMLCommonHandler.instance().getEffectiveSide()).get(component.getUuid()).getFrequencies(component.getRequiredCapability());
    }

    public static void onPacket(NBTTagCompound received, MessageContext messageContext){
        get(messageContext.side).deserializeNBT(received);
    }

    static void setNetworkData(EnderNetwork network, int i, NBTTagCompound data){
        if (network.getSide().isClient()){
            throw new IllegalArgumentException();
        }
        get(Side.SERVER).sendPacket(0, new NBTHelper().addToTag(network.getNetworkId(), "id").addToTag(i, "nr").addToTag(data, "tag").serializeNBT());
    }

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
                break;
            case 2:
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

    public static EnderNetworkManager get(World world){
        return get(!world.isRemote ? Side.SERVER : Side.CLIENT);
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
    private final Side side;
    private INetworkObjectHandler sender;

    @Nullable
    public EnderNetwork get(UUID uuid){
        if (uuid == null){
            return null;
        }
        EnderNetwork ret = networkData.get(uuid);
        if (ret == null){
            if (side.isServer()){
                return null;
            }
            ret = new EnderNetwork(uuid, side);
            networkData.put(uuid, ret);
        }
        return ret;
    }

    public UUID generateNew(@Nullable NBTTagCompound tag){
        if (!side.isServer()){
            throw new IllegalAccessError();
        }
        UUID uuid = UUID.randomUUID();
        while (networkData.keySet().contains(uuid)){
            uuid = UUID.randomUUID();
        }
        EnderNetwork network = new EnderNetwork(uuid, side);
        networkData.put(uuid, network); //Generate the network
        if (tag != null){
            network.deserializeNBT(tag);
        }
        return uuid;
    }

    @Nonnull
    public NBTTagCompound removeNetwork(UUID uuid){
        if (uuid != null) {
            EnderNetwork ret = networkData.remove(uuid);
            if (ret != null){
                return ret.serializeNBT();
            }
            return new NBTTagCompound();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void update() {
        for (EnderNetwork network : networkData.values()){
            network.update();
        }
    }

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
        return new NBTHelper().addToTag(networkData.serializeNBT(), "lEN").serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        networkData.deserializeNBT(nbt.getTagList("lEN", 10));
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
