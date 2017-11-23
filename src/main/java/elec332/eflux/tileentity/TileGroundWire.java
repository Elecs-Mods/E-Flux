package elec332.eflux.tileentity;

import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.ref.MCMPCapabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 16-11-2017.
 */
public class TileGroundWire extends TileEntity implements IMultipartTile {


	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == MCMPCapabilities.MULTIPART_TILE || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	@SuppressWarnings("all")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == MCMPCapabilities.MULTIPART_TILE ? (T) this : super.getCapability(capability, facing);
	}

}
