package elec332.eflux.api.ender;

import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.ICapabilityNetworkHandler;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Elec332 on 4-5-2016.
 */
public interface IEnderCapability<T> extends INBTSerializable<NBTTagCompound> {

    @Nonnull
    public Capability<T> getCapability();

    @Nullable
    public T get();

    public int getEndergyConsumption();

    default public boolean canConnect(IEnderNetworkComponent<T> component){
        return true;
    }

    public void addConnection(IStableEnderConnection<T> connection);

    public void removeConnection(IStableEnderConnection<T> connection, DisconnectReason reason);

    default public void setNetworkHandler(ICapabilityNetworkHandler networkHandler){
    }

    default public void onDataPacket(int id, NBTTagCompound data){
    }

    default public void validate(){
    }

    default public void invalidate(){
    }

    default public void addInformation(List<String> list){
    }

    default public String getInformation(){
        return getEndergyConsumption()+" EF";
    }

    default public boolean hasConfigGUI(){
        return false;
    }

    default public Object getGuiObject(boolean client){
        return null;
    }

    @Override
    @Nonnull
    public NBTTagCompound serializeNBT();

    @Override
    public void deserializeNBT(NBTTagCompound nbt);

    /**
     * If this capability is created from an ItemStack, this method will be called
     *
     * @param stack The ItemStack
     */
    default public void deserializeItemStack(ItemStack stack){
        if (stack.hasTagCompound()){
            deserializeNBT(stack.getTagCompound());
        }
    }

}
