package elec332.eflux.grid.tank;

import elec332.core.multiblock.dynamic.AbstractDynamicMultiBlockWorldHolder;
import elec332.eflux.util.IEFluxTank;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by Elec332 on 16-4-2016.
 */
public class EFluxDynamicTankWorldHolder extends AbstractDynamicMultiBlockWorldHolder<EFluxDynamicTankWorldHolder, EFluxDynamicTank> {

    public EFluxDynamicTankWorldHolder(World world) {
        super(world);
    }

    @Override
    public boolean isTileValid(TileEntity tileEntity) {
        return tileEntity instanceof IEFluxTank;
    }

    @Override
    public boolean canConnect(TileEntity tileEntity, EnumFacing enumFacing, TileEntity tileEntity1) {
        Fluid f1 = ((IEFluxTank)tileEntity).getLastSeenFluid();
        Fluid f2 = ((IEFluxTank)tileEntity1).getLastSeenFluid();
        return f1 == null || f2 == null || f1 == f2;
    }

    @Override
    public EFluxDynamicTank newMultiBlock(TileEntity tileEntity) {
        return new EFluxDynamicTank((IEFluxTank)tileEntity, this);
    }

}
