package elec332.eflux.energy;

import elec332.core.world.DimensionCoordinate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 24-7-2016.
 */
public interface IEFluxEnergyObject extends ICapabilityProvider {

    @Nullable
    public TileEntity getTileEntity();

    @Nonnull
    public DimensionCoordinate getPosition();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing);

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing);

}
