package elec332.eflux.endernetwork.capabilities;

import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.api.ender.internal.IEndergyCapability;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 19-5-2016.
 */
public class EFluxEnderCapabilityEndergy extends AbstractEnderCapability<IEndergyCapability> implements IEndergyCapability {

    public EFluxEnderCapabilityEndergy(Side side, IEnderNetwork network) {
        super(side, network);
    }

    @CapabilityInject(IEndergyCapability.class)
    private static Capability<IEndergyCapability> CAPABILITY;

    private int tier;

    @Nonnull
    @Override
    public Capability<IEndergyCapability> getCapability() {
        return CAPABILITY;
    }

    @Nullable
    @Override
    public IEndergyCapability get() {
        return this;
    }

    @Override
    public int getEndergyConsumption() {
        return tier * 3;
    }

    @Override
    public void addConnection(IStableEnderConnection<IEndergyCapability> connection) {
    }

    @Override
    public void removeConnection(IStableEnderConnection<IEndergyCapability> connection, DisconnectReason reason) {
    }

    @Nonnull
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound save = new NBTTagCompound();
        save.setInteger("tier", tier);
        return save;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.tier = nbt.getInteger("tier");
    }

    @Override
    public void deserializeItemStack(ItemStack stack) {
        super.deserializeItemStack(stack);
        this.tier = Math.max(stack.getMetadata(), 4);
    }

    @Override
    public int getStoredEndergy() {
        return network.getStoredEndergy();
    }

    @Override
    public boolean drainEndergy(int endergy) {
        return network.drainEndergy(endergy) && endergy < (tier * 18);
    }

}
