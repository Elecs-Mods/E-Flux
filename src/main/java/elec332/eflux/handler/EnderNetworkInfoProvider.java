package elec332.eflux.handler;

import elec332.core.api.info.AbstractInfoProviderCapability;
import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInformation;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 25-10-2016.
 */
public class EnderNetworkInfoProvider extends AbstractInfoProviderCapability<IEnderNetworkComponent> {

    public EnderNetworkInfoProvider() {
        super(EFluxAPI.ENDER_COMPONENT_CAPABILITY);
    }

    @Override
    public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData, IEnderNetworkComponent capability) {
        NBTTagCompound tag = hitData.getData();
        information.addInformation("Network ID: " + tag.getString("u"));
        information.addInformation("Frequency: " + tag.getInteger("i"));
        information.addInformation("Has connection: " + tag.getBoolean("b"));
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(@Nonnull NBTTagCompound tag, TileEntity tile, IEnderNetworkComponent capability, @Nonnull EntityPlayerMP player, @Nonnull IInfoDataAccessorBlock hitData) {
        tag.setString("u", String.valueOf(capability.getUuid()));
        tag.setInteger("i", capability.getFrequency());
        tag.setBoolean("b", capability.getCurrentConnection() != null);
        return tag;
    }

}
