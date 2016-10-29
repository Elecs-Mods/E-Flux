package elec332.eflux.grid.tank;

import com.google.common.collect.Sets;
import elec332.core.grid.AbstractGridHandler;
import elec332.core.grid.DefaultTileEntityLink;
import elec332.core.world.DimensionCoordinate;
import elec332.eflux.util.IEFluxTank;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by Elec332 on 9-10-2016.
 */
public class EFluxTankHandler extends AbstractGridHandler<EFluxTankHandler.TileLink> {

    public EFluxTankHandler(){
        this.grids = Sets.newHashSet();
    }

    private final Set<EFluxDynamicTankGrid> grids;

    @Override
    protected void onObjectRemoved(TileLink o, Set<DimensionCoordinate> set) {
        EFluxDynamicTankGrid grid = o.getGrid();
        if (grid == null) {
            return;
        }
        for (TileLink o2 : grid.getConnections()) {
            grid.onRemoved(o2);
            if (!set.contains(o2.getPosition()) && o != o2) {
                add.add(o2.getPosition());
            }
        }
        grid.invalidate();
        grids.remove(grid);
    }

    @Override
    @SuppressWarnings("all")
    protected void internalAdd(TileLink o) {
        DimensionCoordinate coord = o.getPosition();
        BlockPos pos = coord.getPos();
        o.setGrid(newGrid(o));
        for (EnumFacing facing : EnumFacing.VALUES){
            TileLink ttl = getDim(coord).get(pos.offset(facing));
            if (ttl != null){
                EFluxDynamicTankGrid grid = ttl.getGrid();
                if (grid != null && o.getGrid().canMerge(grid)){
                    o.getGrid().mergeWith(grid);
                    grid.invalidate();
                    grids.remove(grid);
                }
            }
        }
    }

    @Override
    public void tick() {
        for (EFluxDynamicTankGrid grid : grids){
            grid.tick();
        }
    }

    @Override
    public boolean isValidObject(TileEntity tileEntity) {
        return tileEntity instanceof IEFluxTank;
    }

    @Override
    protected TileLink createNewObject(TileEntity tileEntity) {
        return new TileLink(tileEntity);
    }

    private EFluxDynamicTankGrid newGrid(TileLink tile){
        EFluxDynamicTankGrid ret = new EFluxDynamicTankGrid(tile);
        grids.add(ret);
        return ret;
    }

    class TileLink extends DefaultTileEntityLink {

        private TileLink(TileEntity tile) {
            super(tile);
        }

        private EFluxDynamicTankGrid grid;

        @Nullable
        public EFluxDynamicTankGrid getGrid(){
            return grid;
        }

        @SuppressWarnings("all")
        protected void setGrid(EFluxDynamicTankGrid grid){
            this.grid = grid;
            getTank().setTankGrid(grid);
        }

        @Nullable
        public IEFluxTank getTank(){
            return (IEFluxTank) tile;
        }

    }

}
