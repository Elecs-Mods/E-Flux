package elec332.eflux.endernetwork.capabilities;

import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import elec332.eflux.api.ender.internal.IEndergyCapability;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 19-5-2016.
 */
public class EFluxCapabilityEndergy extends AbstractEnderCapability<IEndergyCapability> implements IEndergyCapability, ITickable {

    public EFluxCapabilityEndergy(Side side, IEnderNetwork network) {
        super(side, network);
    }

    @CapabilityInject(IEndergyCapability.class)
    private static Capability<IEndergyCapability> CAPABILITY;
    private static final int ENDERGY_CONSUMPTION_TIER = 90;
    private static final int ENDERGY_PRODUCTION_TIER = 10;
    private static final int ENDERGY_MAX_TIER = 40;

    private int tier;
    private int power;
    private int maxPower;

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
    public int getPowerConsumption() {
        return tier * 30;
    }

    @Override
    public void update() {
        float consumption = ENDERGY_CONSUMPTION_TIER * tier * Math.max(((float) (maxPower - power))/(ENDERGY_PRODUCTION_TIER * tier), 1);
        float f = Math.max(network.getStoredPower() / consumption, 1);
        if (f > 0.12f && network.drainPower((int) (consumption * f))){
            power += ENDERGY_PRODUCTION_TIER * f;
            if (power > maxPower){
                power = maxPower;
            }
        }
        if (power > 0){
            power--;
        }
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
        save.setInteger("power", power);
        return save;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.tier = nbt.getInteger("tier");
        this.power = nbt.getInteger("power");
    }

    @Override
    public void deserializeItemStack(ItemStack stack) {
        super.deserializeItemStack(stack);
        this.tier = Math.max(stack.getMetadata(), 4);
        this.maxPower = tier * ENDERGY_MAX_TIER;
    }

    @Override
    public int getStoredEndergy() {
        return power;
    }

    @Override
    public boolean drainEndergy(int endergy) {
        if (power >= endergy){
            power -= endergy;
            return true;
        }
        power = 0;
        return false;
    }

}
