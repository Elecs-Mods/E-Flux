package elec332.eflux.endernetwork;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import elec332.core.nbt.NBTMap;
import elec332.core.server.ServerHelper;
import elec332.core.util.NBT;
import elec332.core.util.NBTHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.network.PacketSendValidNetworkKeys;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by Elec332 on 18-2-2016.
 */
public final class EnderNetworkManager implements INBTSerializable<NBTTagCompound>, ITickable {

    private EnderNetworkManager(Side side){
        this.side = side;
        networkData = NBTMap.newNBTMap(UUID.class, EnderNetwork.class, new Function<UUID, EnderNetwork>() {
            @Override
            public EnderNetwork apply(UUID input) {
                return new EnderNetwork(input, EnderNetworkManager.this.side);
            }
        });
        validKeys = Lists.newArrayList();
    }

    public static void registerSaveHandler(){
        ServerHelper.instance.registerExtendedProperties("EFluxEnderNetwork", new Callable<INBTSerializable>() {

            @Override
            public INBTSerializable<NBTTagCompound> call() throws Exception {
                return get(Side.SERVER);
            }

        });
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
    private List<UUID> validKeys;
    private final Side side;

    public EnderNetwork get(UUID uuid){
        if (uuid == null){
            return null;
        }
        EnderNetwork ret = networkData.get(uuid);
        if (ret == null /*&& validKeys.contains(uuid)*/){
            ret = new EnderNetwork(uuid, side);
            networkData.put(uuid, ret);
        }
        return ret;
    }

    public UUID generateNew(){
        UUID uuid = UUID.randomUUID();
        while (networkData.keySet().contains(uuid)){
            uuid = UUID.randomUUID();
        }
        validKeys.add(uuid);
        networkData.put(uuid, new EnderNetwork(uuid, side)); //Generate the network
        sendKeyData();
        return uuid;
    }

    public void removeNetwork(UUID uuid){
        if (uuid != null) {
            networkData.remove(uuid);
            //validKeys.remove(uuid);
            sendKeyData();
        }
    }

    public void deleteNetwork(UUID uuid){
        if (uuid != null) {
            removeNetwork(uuid);
            validKeys.remove(uuid);
        }
    }

    @Override
    public void update() {
        for (EnderNetwork network : networkData.values()){
            network.update();
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTHelper().addToTag(networkData.serializeNBT(), "lEN").addToTag(getValidKeys(), "U_keys").serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        networkData.deserializeNBT(nbt.getTagList("lEN", 10));
        deserializekeys(nbt.getTagList("U_keys", NBT.NBTData.STRING.getID()));
    }

    private void sendKeyData(){
        EFlux.networkHandler.getNetworkWrapper().sendToAll(new PacketSendValidNetworkKeys(getValidKeys()));
    }

    private NBTTagList getValidKeys(){
        NBTTagList tagList = new NBTTagList();
        for (UUID uuid : validKeys){
            tagList.appendTag(new NBTTagString(uuid.toString()));
        }
        return tagList;
    }

    public void deserializekeys(NBTTagList tagList){
        validKeys.clear();
        for (int i = 0; i < tagList.tagCount(); i++) {
            validKeys.add(UUID.fromString(tagList.getStringTagAt(i)));
        }
    }

    static {
        Side side = FMLCommonHandler.instance().getSide();
        switch (side){
            case CLIENT:
                INSTANCES = Maps.newEnumMap(Side.class);
                for (Side sideT : Side.values()){
                    INSTANCES.put(sideT, new EnderNetworkManager(sideT));
                }
                break;
            case SERVER:
                INSTANCE = new EnderNetworkManager(Side.SERVER);
        }
        isServer = side.isServer();
    }

}
