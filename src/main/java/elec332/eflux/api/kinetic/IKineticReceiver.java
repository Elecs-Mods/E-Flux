package elec332.eflux.api.kinetic;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 13-2-2016.
 */
public interface IKineticReceiver {

    public boolean canReceiveFrom(TileEntity provider);

    public void process(int torque, int speed);

}
