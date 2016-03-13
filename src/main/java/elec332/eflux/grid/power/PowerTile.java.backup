package elec332.eflux.grid.power;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.*;
import elec332.eflux.grid.WorldRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class PowerTile {  //Wrapper for TileEntities, prevents the loading of chunks when working with grid data.

    public PowerTile(TileEntity tileEntity){
        if (!EnergyAPIHelper.isEnergyTile(tileEntity))
            throw new IllegalArgumentException();
        this.tile = tileEntity;
        this.world = tileEntity.getWorld();
        this.location = new BlockPos(tileEntity.getPos());
        this.grids = new EFluxCableGrid[6];
        connectorSides = EnumSet.noneOf(EnumFacing.class);
        checkConnector();
        this.hasInit = true;
    }

    private TileEntity tile;
    private World world;
    private boolean hasInit = false;
    private BlockPos location;
    private EFluxCableGrid[] grids;
    private Set<EnumFacing> connectorSides;

    public void checkConnector(){
        for (EnumFacing facing : EnumFacing.VALUES){
            if (EnergyAPIHelper.isTransmitter(tile, facing)){
                connectorSides.add(facing);
            }
        }
    }

    public BlockPos getLocation() {
        return location;
    }

    public TileEntity getTile() {
        return tile;
    }

    public World getWorld(){
        return world;
    }

    public boolean isReceiver(EnumFacing side){
        return EnergyAPIHelper.isReceiver(tile, side);
    }

    public boolean isProvider(EnumFacing side){
        return EnergyAPIHelper.isProvider(tile, side);
    }

    public boolean isTransmitter(EnumFacing side){
        return EnergyAPIHelper.isTransmitter(tile, side);//connectorSides.contains(side);
    }

    public boolean isMonitor(EnumFacing side){
        return EnergyAPIHelper.isMonitor(tile, side);
    }

    public boolean canProvide(EnumFacing side){
        return EnergyAPIHelper.canProvide(tile, side);
    }

    public boolean canReceive(EnumFacing side){
        return EnergyAPIHelper.canReceive(tile, side);
    }

    public IEnergyReceiver getReceiver(EnumFacing side){
        return tile.getCapability(EFluxAPI.RECEIVER_CAPABILITY, side);
    }

    public IEnergySource getProvider(EnumFacing side){
        return tile.getCapability(EFluxAPI.PROVIDER_CAPABILITY, side);
    }

    public IEnergyTransmitter getTransmitter(EnumFacing side){
        return tile.getCapability(EFluxAPI.TRANSMITTER_CAPABILITY, side);
    }

    public IEnergyMonitor getMonitor(EnumFacing side){
        return tile.getCapability(EFluxAPI.MONITOR_CAPABILITY, side);
    }

    public boolean hasInit() {
        return hasInit;
    }

    public void replaceGrid(EFluxCableGrid old, EFluxCableGrid newGrid) {
        List<Integer> i = removeGrid(old);
        if (i != null) {
            for (Integer j : i) {
                grids[j] = newGrid;
            }
        } else {
            EFlux.systemPrintDebug("Replace null");
            throw new IllegalStateException();
        }
    }

    public List<Integer> removeGrid(EFluxCableGrid grid){
        if (grids.length == 0)
            throw new RuntimeException();
        List<Integer> ret = Lists.newArrayList();
        for (int i = 0; i < grids.length; i++){
            if (grid.equals(grids[i])){
                grids[i] = null;
                ret.add(i);
            }
        }
        return ret.isEmpty() ? null : ret;
    }

    public Set<EFluxCableGrid> getGrids() {
        return Sets.newHashSet(grids);
    }

    public EFluxCableGrid getGridFromSide(EnumFacing forgeDirection){
        return getFromSide(forgeDirection);
    }

    private EFluxCableGrid getFromSide(EnumFacing direction){
        checkConnector();
        if (connectorSides.size() > 0) {
            Set<EFluxCableGrid> gridL = Sets.newHashSet();
            boolean doStuff = false;
            for (EnumFacing facing : connectorSides) {
                EFluxCableGrid grid = grids[facing.ordinal()];
                if (grid != null) {
                    gridL.add(grid);
                } else {
                    doStuff = true;
                }
            }
            if (gridL.size() > 1) {
                throw new RuntimeException();
            }
            if (doStuff) {
                EFluxCableGrid newGrid;
                if (gridL.size() == 0) {
                    newGrid = newGrid(direction);
                } else {
                    newGrid = gridL.iterator().next();
                }
                for (EnumFacing facing : connectorSides) {
                    grids[facing.ordinal()] = newGrid;
                }
                return newGrid;
            }
        }
        EFluxCableGrid grid = grids[direction.ordinal()];
        if (grid == null) {
            grid = newGrid(direction);
            grids[direction.ordinal()] = grid;
        }
        return grid;
    }

    private EFluxCableGrid newGrid(EnumFacing direction){
        return WorldRegistry.get(world).getWorldPowerGrid().registerGrid(new EFluxCableGrid(this, direction));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PowerTile && location.equals(((PowerTile)obj).getLocation());
    }

}
