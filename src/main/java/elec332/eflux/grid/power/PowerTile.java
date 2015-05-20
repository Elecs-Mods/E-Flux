package elec332.eflux.grid.power;

import elec332.core.util.BlockLoc;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.grid.WorldRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class PowerTile {  //Wrapper for TileEntities
    public PowerTile(TileEntity tileEntity){
        if (!EnergyAPIHelper.isEnergyTile(tileEntity))
            throw new IllegalArgumentException();
        this.tile = tileEntity;
        this.location = new BlockLoc(tileEntity);
        this.grids = new EFluxCableGrid[6];
        if (tileEntity instanceof IEnergyTransmitter) {
            this.grids[0] = newGrid(ForgeDirection.UNKNOWN);
            this.connectType = ConnectType.CONNECTOR;
        } else if (tileEntity instanceof IEnergyReceiver && tileEntity instanceof IEnergySource)
            this.connectType = ConnectType.SEND_RECEIVE;
        else if (tileEntity instanceof IEnergyReceiver)
            this.connectType = ConnectType.RECEIVE;
        else if (tileEntity instanceof IEnergySource)
            this.connectType = ConnectType.SEND;
        this.hasInit = true;
    }

    private TileEntity tile;
    private boolean hasInit = false;
    private BlockLoc location;
    private EFluxCableGrid[] grids;
    public int toGo;
    private ConnectType connectType;

    private boolean singleGrid(){
        return this.connectType == ConnectType.CONNECTOR;
    }

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
        if (singleGrid()){
            grids[0] = newGrid;
        } else {
            int q = 0;
            for (EFluxCableGrid grid : grids){
                if (grid != null)
                    q++;
            }
            System.out.println("OldSizeBeforeMerge: "+q);
            int i = removeGrid(old);
            System.out.println(i);
            grids[i] = newGrid;
            q = 0;
            for (EFluxCableGrid grid : grids){
                if (grid != null)
                    q++;
            }
            System.out.println("NewSizeAfterMerge "+q);
            System.out.println(grids.length);
        }
    }

    public void resetGrid(EFluxCableGrid grid){
        removeGrid(grid);
        if (singleGrid())
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

    public EFluxCableGrid getGridFromSide(ForgeDirection forgeDirection){
        return singleGrid()?getGrid():getFromSide(forgeDirection);
    }

    private EFluxCableGrid getFromSide(ForgeDirection direction){
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
            grid = newGrid(ForgeDirection.UNKNOWN);
            grids[0] = grid;
        }
        return grid;
    }

    private EFluxCableGrid newGrid(ForgeDirection direction){
        return WorldRegistry.get(tile.getWorldObj()).getWorldPowerGrid().registerGrid(new EFluxCableGrid(tile.getWorldObj(), this, direction));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PowerTile && location.equals(((PowerTile)obj).getLocation());
    }

    public enum ConnectType{
        CONNECTOR, SEND, RECEIVE, SEND_RECEIVE
    }
}
