package elec332.eflux.test.power;

import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;
import elec332.eflux.api.transmitter.IEnergyTile;
import elec332.eflux.test.blockLoc.BlockLoc;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

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
    }

    private TileEntity tile;
    private BlockLoc location;
    private List<EFluxCableGrid> grids;
    private boolean singleGrid;

    public BlockLoc getLocation() {
        return location;
    }

    public TileEntity getTile() {
        return tile;
    }

    public void replaceGrid(EFluxCableGrid old, EFluxCableGrid newGrid){
        int i = grids.indexOf(old);
        System.out.println(i);
        grids.remove(old);
        //grids.set(i, newGrid);
        grids.add(newGrid);
        System.out.println(grids.size());
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
