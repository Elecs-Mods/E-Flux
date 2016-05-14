package elec332.eflux.api.ender.internal;

import elec332.eflux.api.ender.IEnderNetworkComponent;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Elec332 on 8-5-2016.
 */
public interface IEnderNetwork {

    public int[] getFrequencies(Capability capability);

    public boolean connect(IEnderNetworkComponent component);

    public boolean drainPower(int power);

    public void syncToClient();

}
