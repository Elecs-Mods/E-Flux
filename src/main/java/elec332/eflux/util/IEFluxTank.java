package elec332.eflux.util;

import elec332.core.multiblock.dynamic.IDynamicMultiBlockTile;
import elec332.eflux.grid.tank.EFluxDynamicTank;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 17-4-2016.
 */
public interface IEFluxTank extends IDynamicMultiBlockTile<EFluxDynamicTank>, IFluidHandler {

    public int getTankSize();

    public void setLastSeenFluid(Fluid fluid);

    public Fluid getLastSeenFluid();

    public void setClientRenderFluid(Fluid fluid);

    @SideOnly(Side.CLIENT)
    public Fluid getClientRenderFluid();

    public void setClientRenderHeight(float height);

    @SideOnly(Side.CLIENT)
    public float getClientRenderHeight();

}
