package elec332.eflux.grid.power;

import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.grid.WorldRegistry;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class PowerTile {  //Wrapper for TileEntities

    public PowerTile(Object tileEntity, World world, BlockPos pos){
        if (!EnergyAPIHelper.isEnergyTile(tileEntity))
            throw new IllegalArgumentException();
        this.tile = tileEntity;
        this.world = world;
        this.location = new BlockPos(pos);
        this.grids = new EFluxCableGrid[6];
        if (tileEntity instanceof IEnergyTransmitter) {
            //this.grids[0] = newGrid(EnumFacing.UNKNOWN);
            this.connectType = ConnectType.CONNECTOR;
        } else if (tileEntity instanceof IEnergyReceiver && tileEntity instanceof IEnergySource)
            this.connectType = ConnectType.SEND_RECEIVE;
        else if (tileEntity instanceof IEnergyReceiver)
            this.connectType = ConnectType.RECEIVE;
        else if (tileEntity instanceof IEnergySource)
            this.connectType = ConnectType.SEND;
        this.hasInit = true;
    }

    private Object tile;
    private World world;
    private boolean hasInit = false;
    private BlockPos location;
    private EFluxCableGrid[] grids;
    private ConnectType connectType;

    private boolean singleGrid(){
        return this.connectType == ConnectType.CONNECTOR;
    }

    public ConnectType getConnectType() {
        return connectType;
    }

    public BlockPos getLocation() {
        return location;
    }

    public Object getTile() {
        return tile;
    }

    public boolean hasInit() {
        return hasInit;
    }

    public void replaceGrid(EFluxCableGrid old, EFluxCableGrid newGrid){
        if (singleGrid()){
            grids[0] = newGrid;
        } else {
            int q = 0;
            for (EFluxCableGrid grid : grids){
                if (grid != null)
                    q++;
            }
            EFlux.systemPrintDebug("OldSizeBeforeMerge: " + q);
            int i = removeGrid(old);
            EFlux.systemPrintDebug(i);
            grids[i] = newGrid;
            q = 0;
            for (EFluxCableGrid grid : grids){
                if (grid != null)
                    q++;
            }
            EFlux.systemPrintDebug("NewSizeAfterMerge " + q);
            EFlux.systemPrintDebug(grids.length);
        }
    }

    public void resetGrid(EFluxCableGrid grid){
        removeGrid(grid);
        if (singleGrid() && WorldHelper.chunkLoaded(world, location))
            getGrid();
    }

    public int removeGrid(EFluxCableGrid grid){
        if (grids.length == 0)
            throw new RuntimeException();
        for (int i = 0; i < grids.length; i++){
            if (grid.equals(grids[i])){
                grids[i] = null;
                return i;
            }
        }
        return -1;
    }

    public EFluxCableGrid[] getGrids() {
        return grids;
    }

    public EFluxCableGrid getGridFromSide(EnumFacing forgeDirection){
        return singleGrid()?getGrid():getFromSide(forgeDirection);
    }

    private EFluxCableGrid getFromSide(EnumFacing direction){
        EFluxCableGrid grid = grids[direction.ordinal()];
        if (grid == null) {
            grid = newGrid(direction);
            grids[direction.ordinal()] = grid;
        }
        return grid;
    }

    public EFluxCableGrid getGrid(){
        if (!singleGrid())
            throw new UnsupportedOperationException("Request grid when tile has multiple grids");
        EFluxCableGrid grid = grids[0];
        if (grid == null){
            grid = newGrid(null);
            grids[0] = grid;
        }
        return grid;
    }

    private EFluxCableGrid newGrid(EnumFacing direction){
        return WorldRegistry.get(world).getWorldPowerGrid().registerGrid(new EFluxCableGrid(world, this, direction));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PowerTile && location.equals(((PowerTile)obj).getLocation());
    }

    public enum ConnectType{
        CONNECTOR, SEND, RECEIVE, SEND_RECEIVE
    }
}
