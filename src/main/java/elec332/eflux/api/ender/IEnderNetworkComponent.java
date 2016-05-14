package elec332.eflux.api.ender;

import elec332.eflux.api.ender.internal.IEnderConnection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Elec332 on 8-5-2016.
 */
public interface IEnderNetworkComponent<T> extends INBTSerializable<NBTTagCompound> {

    @Nonnull
    public Capability<T> getRequiredCapability();

    /**
     * Sets the current frequency, -1 means an invalid frequency (no frequency)
     *
     * @param freq The new frequency, can be -1
     */
    public void setFrequency(int freq);

    public int getFrequency();

    public boolean isItem();

    /**
     * Sets the current network ID, network is cleared when the parameter is null;
     *
     * @param uuid The new network ID, can be null
     */
    public void setUUID(UUID uuid);

    public UUID getUuid();

    public void onConnect(IEnderConnection<T> connection);

    @Nullable
    public IEnderConnection<T> getCurrentConnection();

    @Override
    public NBTTagCompound serializeNBT();

    @Override
    public void deserializeNBT(NBTTagCompound nbt);

}
