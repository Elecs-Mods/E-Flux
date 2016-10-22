package elec332.eflux.util;

import elec332.eflux.grid.tank.EFluxDynamicTankGrid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 17-4-2016.
 */
public interface IEFluxTank {

    public void setTankGrid(EFluxDynamicTankGrid grid);

    public EFluxDynamicTankGrid getTankGrid();

    public void setSaveData(NBTTagCompound var1);

    public NBTTagCompound getSaveData();

    ///////////////////////////////////////////////////////////

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
