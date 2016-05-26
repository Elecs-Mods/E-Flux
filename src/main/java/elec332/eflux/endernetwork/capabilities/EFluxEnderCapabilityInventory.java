package elec332.eflux.endernetwork.capabilities;

import elec332.core.util.BasicInventory;
import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import elec332.eflux.util.SafeWrappedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 21-5-2016.
 */
public class EFluxEnderCapabilityInventory extends AbstractEnderCapability<IItemHandler> {

    public EFluxEnderCapabilityInventory(Side side, IEnderNetwork network) {
        super(side, network);
    }

    private static final Capability<IItemHandler> CAPABILITY = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

    private int slots;
    private SafeWrappedInventory inv;

    @Nonnull
    @Override
    public Capability<IItemHandler> getCapability() {
        return CAPABILITY;
    }

    @Nullable
    @Override
    public IItemHandler get() {
        return inv;
    }

    @Override
    public int getEndergyConsumption() {
        return slots;
    }

    @Override
    public void addConnection(IStableEnderConnection<IItemHandler> connection) {
    }

    @Override
    public void removeConnection(IStableEnderConnection<IItemHandler> connection, DisconnectReason reason) {
    }

    @Override
    public void invalidate() {
        super.invalidate();
        inv.clear();
    }

    @Nonnull
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setInteger("slots", slots);
        ret.setTag("contents", CAPABILITY.getStorage().writeNBT(CAPABILITY, inv, null));
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.slots = nbt.getInteger("slots");
        this.inv = SafeWrappedInventory.of(new InvWrapper(new BasicInventory("", slots)));
        CAPABILITY.getStorage().readNBT(CAPABILITY, inv, null, nbt.getTag("contents"));
    }

}
