package elec332.eflux.util;

import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.heat.IHeatReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

/**
 * Created by Elec332 on 15-4-2016.
 */
public class HeatHelper {

    @Nullable
    public static IHeatReceiver getHeatReceiver(IBlockAccess iba, BlockPos pos, EnumFacing from){
        TileEntity tile = WorldHelper.getTileAt(iba, pos.offset(from));
        if (tile == null){
            return null;
        }
        from = from.getOpposite();
        if (tile.hasCapability(EFluxAPI.HEAT_CAPABILITY, from)) {
            return tile.getCapability(EFluxAPI.HEAT_CAPABILITY, from);
        }
        if (tile instanceof TileEntityFurnace){
            return new FurnaceHeatReceiver((TileEntityFurnace) tile);
        }
        return null;
    }

    private static final class FurnaceHeatReceiver implements IHeatReceiver {

        private FurnaceHeatReceiver(TileEntityFurnace furnace){
            this.furnace = furnace;
        }

        private final TileEntityFurnace furnace;

        @Override
        public int getHeat() {
            return (int) ((furnace.getField(0) * getHeatRatio()) / 1.2f);
        }

        @Override
        public void addHeat(int heat) {
            int i = furnace.getField(0);
            if (i < getMaxFurnaceHeat()){
                furnace.setField(0, i + Math.max(getMaxFurnaceHeat() - i, (int)((heat / (float)getHeatRatio()) * 1.2f)));
            }
            furnace.setField(1, getMaxFurnaceHeat());
        }

        private int getMaxFurnaceHeat(){
            return getHeatRatio() * 5;
        }

    }

    private static int getHeatRatio(){
        return Config.Machines.Heater.generatedHeatPerTick;
    }

}
