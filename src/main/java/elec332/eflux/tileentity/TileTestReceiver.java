package elec332.eflux.tileentity;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.tile.TileBase;
import elec332.core.util.PlayerHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.util.ConnectionPoint;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 13-11-2017.
 */
@RegisteredTileEntity("ttr")
public class TileTestReceiver extends TileBase implements IEnergyReceiver{


	@Override
	public void receivePower(double ef, double rp) {
		this.ef = ef;
		this.rp = rp;
	}

	public double ef, rp;

	@Override
	public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
			PlayerHelper.sendMessageToPlayer(player, "V: " + ef + "   A: " + rp);
		}
		return super.onBlockActivated(state, player, hand, side, hitX, hitY, hitZ);
	}

	@Nonnull
	@Override
	public EnergyType getEnergyType(int post) {
		return EnergyType.AC;
	}

	ConnectionPoint p1 = new ConnectionPoint(pos, world, EnumFacing.EAST, 0), p2 = new ConnectionPoint(pos, world, EnumFacing.WEST, 0);

	@Nonnull
	@Override
	public ConnectionPoint getConnectionPoint(int post) {
		return post == 0 ? new ConnectionPoint(pos, world, EnumFacing.EAST, 0) : new ConnectionPoint(pos, world, EnumFacing.WEST, 0);
	}

	@Nullable
	@Override
	public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec) {
		return side == EnumFacing.EAST ? new ConnectionPoint(pos, world, EnumFacing.EAST, 0) : (side == EnumFacing.WEST ? new ConnectionPoint(pos, world, EnumFacing.WEST, 0) : null);
	}

	@Override
	public double getResistance() {
		return 4;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == EFluxAPI.ENERGY_CAP || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == EFluxAPI.ENERGY_CAP ? (T) this : super.getCapability(capability, facing);
	}

}
