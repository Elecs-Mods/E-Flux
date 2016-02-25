package elec332.eflux.tileentity.kinetic;

import elec332.eflux.api.EFluxKinetics;
import elec332.eflux.api.kinetic.IKineticReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Elec332 on 13-2-2016.
 */
public class TileKineticBox extends AbstractKineticTileBase implements IKineticReceiver {

    @Override
    protected EnumFacing getOutputFacing() {
        return getTileFacing().getOpposite();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == EFluxKinetics.KINETIC_CAPABILITY && facing == getTileFacing() || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == EFluxKinetics.KINETIC_CAPABILITY && facing == getTileFacing() ? null : super.getCapability(capability, facing);
    }

    @Override
    public boolean canReceiveFrom(TileEntity provider) {
        return true;
    }

    @Override
    public void process(int torque, int speed) {
        if (receiver != null){
            receiver.process(torque, speed);
        }
    }

}
