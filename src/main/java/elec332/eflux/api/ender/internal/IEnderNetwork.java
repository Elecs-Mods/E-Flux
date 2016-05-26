package elec332.eflux.api.ender.internal;

import elec332.eflux.api.ender.IEnderNetworkComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Created by Elec332 on 8-5-2016.
 */
public interface IEnderNetwork {

    public UUID getNetworkId();

    public int[] getFrequencies(Capability capability);

    public boolean connect(@Nonnull IEnderNetworkComponent component);

    public boolean drainEndergy(int endergy);

    public int getStoredEndergy();

    public void syncToClient();

}
