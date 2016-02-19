package elec332.eflux.endernetwork;

import com.google.common.base.Function;
import elec332.core.nbt.NBTMap;
import elec332.core.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

/**
 * Created by Elec332 on 18-2-2016.
 */
public final class EnderNetworkManager implements INBTSerializable<NBTTagCompound> {

    public static EnderNetworkManager instance;
    private EnderNetworkManager(){
        networkData = NBTMap.newNBTMap(UUID.class, EnderNetwork.class, new Function<UUID, EnderNetwork>() {
            @Override
            public EnderNetwork apply(UUID input) {
                return new EnderNetwork();
            }
        });
    }

    private NBTMap<UUID, EnderNetwork> networkData;

    public static EnderNetwork get(UUID uuid){
        EnderNetwork ret = instance.networkData.get(uuid);
        if (ret == null){
            ret = new EnderNetwork();
            instance.networkData.put(uuid, ret);
        }
        return ret;
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
        instance = new EnderNetworkManager();
    }

}
