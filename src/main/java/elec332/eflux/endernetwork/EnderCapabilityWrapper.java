package elec332.eflux.endernetwork;

import elec332.core.nbt.NBTMap;
import elec332.eflux.EFlux;
import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 4-5-2016.
 */
@SuppressWarnings("unused")
public final class EnderCapabilityWrapper implements INBTSerializable<NBTTagCompound>, NBTMap.Nullifyable {

    EnderCapabilityWrapper(EnderNetwork network){
        if (network == null){
            throw new IllegalArgumentException(new NullPointerException());
        }
        this.side = network.getSide();
        this.network = network;
        this.nullify = false;
    }

    EnderCapabilityWrapper(IEnderCapabilityFactory capabilityFactory, ItemStack stack, EnderNetwork network){
        this(network);
        if (capabilityFactory == null){
            throw new IllegalArgumentException(new NullPointerException());
        }
        this.capabilityFactory = capabilityFactory;
        this.capability = capabilityFactory.createNewCapability(this.side, this.network);
        this.capability.deserializeItemStack(stack);
    }

    private final Side side;
    private final IEnderNetwork network;
    private IEnderCapabilityFactory capabilityFactory;
    private IEnderCapability capability;
    private boolean nullify;

    @Nonnull
    public final IEnderCapabilityFactory getCapabilityfactory() {
        return capabilityFactory;
    }

    @Nonnull
    public final IEnderCapability getCapability() {
        return capability;
    }

    @Override
    public final boolean shouldNullify() {
        return nullify;
    }

    @Override
    public final NBTTagCompound serializeNBT() {
        NBTTagCompound tag = capability.serializeNBT();
        if (!EFlux.enderCapabilityRegistry.containsKey(capabilityFactory.getRegistryName())){
            throw new IllegalStateException();
        }
        tag.setString("wcapCls", capabilityFactory.getRegistryName().toString());
        return tag;
    }

    @Override
    public final void deserializeNBT(NBTTagCompound nbt) {
        capabilityFactory = EFlux.enderCapabilityRegistry.getValue(new ResourceLocation(nbt.getString("wcapCls")));
        nbt.removeTag("wcapCls");
        if (capabilityFactory == null){
            nullify = true;
        } else {
            capability = capabilityFactory.createNewCapability(side, network);
            capability.deserializeNBT(nbt);
        }
    }

}
