package elec332.eflux.tileentity;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.tile.TileBase;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.BreakReasons;
import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.energy.IEnergySource;
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
 * Created by Elec332 on 9-11-2017.
 */
@RegisteredTileEntity("houje***benbezig")
public class TileGeneratorHVOutput extends TileBase implements ICircuitInput {

	private IEnergySource gen = new IEnergySource() {
		@Override
		public float getVariance() {
			return 0;
		}

		@Nonnull
		@Override
		public EnergyType getEnergyType(int post) {
			return EnergyType.AC;
		}

		@Nonnull
		@Override
		public ConnectionPoint getConnectionPoint(int post) {
			return post == 0 ? new ConnectionPoint(pos, world, EnumFacing.UP, 0) : new ConnectionPoint(pos, world, EnumFacing.DOWN, 0);
		}

		@Override
		public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec) {
			return side == EnumFacing.UP ? new ConnectionPoint(pos, world, EnumFacing.UP, 0) : (side == EnumFacing.DOWN ? new ConnectionPoint(pos, world, EnumFacing.DOWN, 0) : null);
		}

		@Override
		public int getCurrentAverageEF() {
			return 100;
		}

		@Override
		public float getMaxRP() {
			return 1000000000;
		}

		@Override
		public void breakMachine(BreakReasons reason) {
			System.out.println("Break: "+reason);
		}

	};

	@Override
	public int getConnectionLocationFromHitVec(EnumFacing side, Vec3d hit) {
		if (side == EnumFacing.UP){
			return 1;
		} else if (side == EnumFacing.DOWN){
			return 2;
		}
		return -1;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		//WireGridHandler.INSTANCE.onActivated(this, player, hand, side, new Vec3d(hitX, hitY, hitZ));
		return super.onBlockActivated(state, player, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockRemoved() {
		//WireGridHandler.INSTANCE.removeBlock(this);
		super.onBlockRemoved();
	}

	@Override
	public IEnergySource getGenerator() {
		return gen;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == EFluxAPI.ENERGY_CAP || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == EFluxAPI.ENERGY_CAP ? (T) gen :super.getCapability(capability, facing);
	}

}
