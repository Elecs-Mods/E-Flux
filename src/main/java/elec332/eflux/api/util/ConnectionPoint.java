package elec332.eflux.api.util;

import com.google.common.base.Preconditions;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 12-11-2017.
 */
public class ConnectionPoint {

	public ConnectionPoint(BlockPos pos, World world, EnumFacing side, int sideNumber){
		this(pos, Preconditions.checkNotNull(world, "Cannot fetch dimID from null world.").provider.getDimension(), side, sideNumber);
	}

	public ConnectionPoint(BlockPos pos, int world, EnumFacing side, int sideNumber){
		this.pos = Preconditions.checkNotNull(pos.toImmutable());
		this.side = Preconditions.checkNotNull(side);
		this.sideNumber = sideNumber;
		this.world = world;
	}

	private BlockPos pos;
	private EnumFacing side;
	private int sideNumber;
	private int world;

	public BlockPos getPos() {
		return pos;
	}

	public EnumFacing getSide() {
		return side;
	}

	public int getSideNumber() {
		return sideNumber;
	}

	public int getWorld() {
		return world;
	}

	public static final ConnectionPoint NULL_POINT = new ConnectionPoint(new BlockPos(-1, -1, -1), 0, EnumFacing.DOWN, -1);

	public ConnectionPoint copy(){
		return new ConnectionPoint(pos, world, side, sideNumber);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ConnectionPoint && eq((ConnectionPoint) obj);
	}

	private boolean eq(ConnectionPoint cp){
		return pos.equals(cp.pos) && side == cp.side && sideNumber == cp.sideNumber && world == cp.world;
	}

	@Override
	public int hashCode() {
		return pos.hashCode() * 75 + sideNumber * 31 + (side == null ? 1 : 27 * side.hashCode() + world * 98);
	}

	@Override
	public String toString() {
		return "Pos: "+pos+" world: "+world+" side: "+side+" num: "+sideNumber;
	}

}
