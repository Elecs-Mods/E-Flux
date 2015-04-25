package elec332.eflux.test.power;

import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;
import elec332.eflux.api.transmitter.IEnergyTile;
import elec332.eflux.test.blockLoc.BlockLoc;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class PowerTile {  //Wrapper for TileEntities
    public PowerTile(TileEntity tileEntity, WorldGridHolder gridHolder){
        if (!(tileEntity instanceof IEnergyTile))
            throw new IllegalArgumentException();
        this.tile = tileEntity;
        this.location = new BlockLoc(tileEntity);
        this.grids = new ArrayList<EFluxCableGrid>();
        this.grids.add(gridHolder.registerGrid(new EFluxCableGrid(tileEntity.getWorldObj(), this)));
        this.singleGrid = true;
        this.hasInit = true;
    }

    private TileEntity tile;
    private boolean hasInit = false;
    private BlockLoc location;
    private List<EFluxCableGrid> grids;
    private boolean singleGrid;
    public int toGo;

    public BlockLoc getLocation() {
        return location;
    }

    public TileEntity getTile() {
        return tile;
    }

    public boolean hasInit() {
        return hasInit;
    }

    public void replaceGrid(EFluxCableGrid old, EFluxCableGrid newGrid){
        int i = removeGrid(old);
        System.out.println(i);
        if (i != 0) {
            grids.set(i, newGrid);
        } else grids.add(newGrid);
        //grids.add(newGrid);
        System.out.println(grids.size());
    }

    private int removeGrid(EFluxCableGrid grid){
        for (EFluxCableGrid grid1 : grids){
            if (grid.equals(grid1)){
                int i = grids.indexOf(grid1);
                grids.remove(grid1);
                return i;
            }
        }
        return -1;
    }

    public List<EFluxCableGrid> getGrids() {
        return grids;
    }

    public EFluxCableGrid getGrid(){
        if (!singleGrid)
            throw new ActionNotSupportedException("Request grid when tile has multiple grids");
        return grids.get(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PowerTile && location.equals(((PowerTile)obj).getLocation());
    }
}
