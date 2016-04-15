package elec332.eflux.tileentity.kinetic;

import com.google.common.collect.Lists;
import elec332.core.util.DirectionHelper;
import elec332.eflux.api.kinetic.IKineticReceiver;
import elec332.eflux.grid.WorldRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 13-2-2016.
 */
public class TileKineticCableConnect extends AbstractKineticTileBase implements IKineticReceiver {

    @Override
    public void onTileLoaded() {
        super.onTileLoaded();
        network = new KineticNetwork(worldObj);
        network.c++;
    }

    @Override
    public void onTileUnloaded() {
        super.onTileUnloaded();
        network.c--;
        network.receivers.remove(this);
    }

    @Override
    protected EnumFacing getOutputFacing() {
        return getTileFacing();
    }

    @Override
    protected void onReceiverChanged() {
        if (receiver != null && !network.receivers.contains(this)){

        }
    }

    private KineticNetwork network;

    @Override
    public boolean canReceiveFrom(TileEntity provider) {
        return !(provider instanceof TileKineticCableConnect);
    }

    @Override
    public void process(int torque, int speed) {
        network.process(torque, speed);
    }

    public boolean connect(TileKineticCableConnect otherTile){
        if (validC(otherTile)) {
            network.c--;
            network = otherTile.network;
            network.c++;
            return true;
        }
        return false;
    }

    private boolean validC(TileKineticCableConnect t) {
        Vec3i offset = DirectionHelper.rotateRight(getTileFacing()).getDirectionVec();
        BlockPos pos = getPos().subtract(t.getPos());
        return !(offset.getX() == 0 && pos.getX() != 0) && !(offset.getZ() == 0 && pos.getZ() != 0);
    }

    private static class KineticNetwork implements ITickable {

        private KineticNetwork(World world){
            this.world = world;
            this.receivers = Lists.newArrayList();
            WorldRegistry.get(world).addTickable(this);
        }

        private World world;
        private List<IKineticReceiver> receivers;
        private int c;

        private int t, s;

        private void process(int torque, int speed){
            int oldP = t * s;
            t = Math.max(t, torque);
            oldP += torque * speed;
            s = oldP / t;
        }

        @Override
        public void update() {
            if (c <= 0){
                WorldRegistry.get(world).removeTickable(this);
            }
            int receivers = this.receivers.size();
            for (IKineticReceiver r : this.receivers){
                r.process(t/receivers, s);
            }
            t = s = 0;
        }

    }

}
