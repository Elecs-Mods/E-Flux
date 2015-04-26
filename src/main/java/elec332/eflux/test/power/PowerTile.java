package elec332.eflux.test.power;

import elec332.eflux.api.transmitter.IEnergyReceiver;
import elec332.eflux.api.transmitter.IEnergySource;
import elec332.eflux.api.transmitter.IEnergyTile;
import elec332.eflux.api.transmitter.IPowerTransmitter;
import elec332.eflux.test.WorldRegistry;
import elec332.eflux.test.blockLoc.BlockLoc;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class PowerTile {  //Wrapper for TileEntities
    public PowerTile(TileEntity tileEntity, WorldGridHolder gridHolder){
        if (!(tileEntity instanceof IEnergyTile))
            throw new IllegalArgumentException();
        this.tile = tileEntity;
        this.location = new BlockLoc(tileEntity);
        this.grids = new EFluxCableGrid[6];
        if (tileEntity instanceof IPowerTransmitter) {
            this.grids[0] = (gridHolder.registerGrid(new EFluxCableGrid(tileEntity.getWorldObj(), this)));
            this.connectType = ConnectType.CONNECTOR;
        }
        if (tileEntity instanceof IEnergyReceiver && tileEntity instanceof IEnergySource)
            this.connectType = ConnectType.SEND_RECEIVE;
        else if (tileEntity instanceof IEnergyReceiver)
            this.connectType = ConnectType.RECEIVE;
        else if (tileEntity instanceof  IEnergySource)
            this.connectType = ConnectType.RECEIVE;
        this.singleGrid = this.connectType == ConnectType.CONNECTOR;
        this.hasInit = true;
    }

    private TileEntity tile;
    //private WorldGridHolder gridHolder;
    private boolean hasInit = false;
    private BlockLoc location;
    private boolean singleGrid;
    private EFluxCableGrid[] grids;
    public int toGo;
    private ConnectType connectType;

    public ConnectType getConnectType() {
        return connectType;
    }

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
        if (singleGrid){
            grids[0] = (newGrid);
        } else {
            int i = removeGrid(old);
            System.out.println(i);
            //if (i != 0) {
                //grids.set(i, newGrid);
            //} else grids.add(newGrid);
            grids[i] = newGrid;
            //grids.add(newGrid);
            System.out.println(grids.length);
        }
    }

    public int removeGrid(EFluxCableGrid grid){
        if (grids.length == 0)
            throw new RuntimeException();
        for (int i = 0; i < grids.length; i++){
            if (grid.equals(grids[i])){
                //int i = grids.indexOf(grid1);
                grids[i] = null;
                return i;
            }
        }
        return -1;
    }

    public EFluxCableGrid[] getGrids() {
        return grids;
    }

    public EFluxCableGrid getGridFromSide(ForgeDirection forgeDirection){
        return singleGrid?getGrid():getFromSide(forgeDirection.ordinal());
    }

    private EFluxCableGrid getFromSide(int i){
        EFluxCableGrid grid = grids[i];
        if (grid == null) {
            grid = WorldRegistry.get(tile.getWorldObj()).getWorldPowerGrid().registerGrid(new EFluxCableGrid(tile.getWorldObj(), this));
            grids[i] = grid;
        }

        /*try {
            grid = grids.get(i);
        } catch (IndexOutOfBoundsException e){
            grid = WorldRegistry.get(tile.getWorldObj()).getWorldPowerGrid().registerGrid(new EFluxCableGrid(tile.getWorldObj(), this));
            grids.add(i, grid);
        }
        /*if (grid == null){
            grid = WorldRegistry.get(tile.getWorldObj()).getWorldPowerGrid().registerGrid(new EFluxCableGrid(tile.getWorldObj(), this));
            grids.add(i, grid);
        }*/
        return grid;
    }

    public EFluxCableGrid getGrid(){
        if (!singleGrid)
            throw new UnsupportedOperationException("Request grid when tile has multiple grids");
        return grids[0];
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PowerTile && location.equals(((PowerTile)obj).getLocation());
    }

    public enum ConnectType{
        CONNECTOR, SEND, RECEIVE, SEND_RECEIVE
    }
}
