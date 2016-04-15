package elec332.eflux.grid.power;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import elec332.core.world.PositionedObjectHolder;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergyTransmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class WorldGridHolder {

    public WorldGridHolder(World world){
        this.world = world;
        this.grids = Lists.newArrayList();
        this.registeredTiles = new PositionedObjectHolder<PowerTile>();
        this.surroundingData = new PositionedObjectHolder<SurroundingData>();
        this.pending = new ArrayDeque<PowerTile>();
        this.oldInt = 0;
    }

    private World world;  //Dunno why I have this here (yet)
    private List<EFluxCableGrid> grids;
    private Queue<PowerTile> pending;
    private PositionedObjectHolder<PowerTile> registeredTiles;
    private PositionedObjectHolder<SurroundingData> surroundingData;
    private int oldInt;

    public EFluxCableGrid registerGrid(EFluxCableGrid grid){
        this.grids.add(grid);
        return grid;
    }

    protected void removeGrid(EFluxCableGrid grid){
        grids.remove(grid);
    }

    public void onBlockChange(final BlockPos pos, final Set<EnumFacing> notifiedSides) {
        EFlux.systemPrintDebug("blockChange on world: " + WorldHelper.getDimID(world) + " " + pos);
        for (EnumFacing facing : notifiedSides) {
            BlockPos newPos = pos.offset(facing);
            PowerTile pt = getPowerTile(newPos);
            if (pt != null) {
                EFlux.systemPrintDebug("checking surroundings of : " + newPos);
                SurroundingData s = new SurroundingData(newPos);
                SurroundingData c = surroundingData.get(newPos);
                if (!c.equals(s)) {
                    if (c.isBlockChange(s)) {
                        EFlux.systemPrintDebug("BChenge");
                        continue;
                    } else {
                        EFlux.systemPrintDebug("RBuild");
                        rebuild(newPos);
                        return;
                    }
                }
                EFlux.systemPrintDebug("checked surroundings");
            }
        }
    }

    private void rebuild(BlockPos rPos){
        EFlux.systemPrintDebug("rebuidling");
        Set<EFluxCableGrid> grids = getPowerTile(rPos).getGrids();
        for (EFluxCableGrid grid : grids) {
            if (grid != null) {
                this.grids.remove(grid);
                List<BlockPos> copy = Lists.newArrayList(grid.getLocations());
                for (BlockPos pos : copy) {
                    registeredTiles.remove(pos);
                    surroundingData.remove(pos);
                }
                for (BlockPos pos : copy) {
                    if (WorldHelper.chunkLoaded(world, pos)) {
                        TileEntity tile = WorldHelper.getTileAt(world, pos);
                        if (tile != null) {
                            addTile(tile);
                        }
                    }
                }
            }
        }
    }

    public void addTile(TileEntity tile){
        if (!world.isRemote) {
            if (WorldHelper.getDimID(world) != WorldHelper.getDimID(this.world)) {
                throw new IllegalArgumentException();
            }
            if (!WorldHelper.chunkLoaded(world, tile.getPos()) || !EnergyAPIHelper.isEnergyTile(tile)) {
                return;
            }
            if (getPowerTile(tile.getPos()) != null) {
                //if (!(tile instanceof IMultipartContainer)) {
               //    throw new IllegalStateException();
                //} else {
                    getPowerTile(tile.getPos()).checkConnector();
                    surroundingData.put(new SurroundingData(tile.getPos()), tile.getPos());
                    for (EnumFacing facing : EnumFacing.VALUES) {
                        BlockPos o = tile.getPos().offset(facing);
                        if (getPowerTile(o) != null) {
                            getPowerTile(o).checkConnector();
                            surroundingData.put(new SurroundingData(o), o);
                        }
                    }
                    return;
                //}
            }
            PowerTile powerTile = new PowerTile(tile);
            registeredTiles.put(powerTile, tile.getPos());
            surroundingData.put(new SurroundingData(tile.getPos()), tile.getPos());
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos o = tile.getPos().offset(facing);
                if (getPowerTile(o) != null) {
                    surroundingData.put(new SurroundingData(o), o);
                }
            }
            addTile(powerTile);
        }
    }

    private void addTile(PowerTile powerTile){
        if(!world.isRemote) {
            if (!WorldHelper.chunkLoaded(world, powerTile.getLocation())) {
                return;
            }
            EFlux.systemPrintDebug("Processing tile at " + powerTile.getLocation().toString());
            for (EnumFacing direction : EnumFacing.VALUES) {
                EFlux.systemPrintDebug("Processing tile at " + powerTile.getLocation().toString() + " for side " + direction.toString());
                BlockPos checkedPos = powerTile.getLocation().offset(direction);
                if (WorldHelper.chunkLoaded(world, checkedPos)) {
                    TileEntity tile = WorldHelper.getTileAt(world, checkedPos);
                    if (tile != null && EnergyAPIHelper.isEnergyTile(tile)) {
                        PowerTile powerTile1 = getPowerTile(checkedPos);
                        EFlux.systemPrintDebug("CP");
                        if (powerTile1 == null || !powerTile1.hasInit()) {
                            pending.add(powerTile);
                            break;
                        }
                        EFlux.systemPrintDebug("G_Check");
                        if (canConnect(powerTile, direction, powerTile1)) {
                            EFlux.systemPrintDebug("CC");
                            EFluxCableGrid grid = powerTile1.getGridFromSide(direction.getOpposite());
                            powerTile.getGridFromSide(direction).mergeGrids(grid);
                        }
                    } else {
                        EFlux.systemPrintDebug("There is no tile at side " + direction.toString() + " that is valid for connection");
                    }
                }
            }
        }
    }

    private boolean canConnect(PowerTile powerTile1, EnumFacing direction, PowerTile powerTile2){
        if (!EnergyAPIHelper.canHandleEnergy(powerTile1.getTile(), direction) || !EnergyAPIHelper.canHandleEnergy(powerTile2.getTile(), direction.getOpposite())){
            EFlux.systemPrintDebug("Snap1");
            return false; //One (or both) cannot connect.
        }
        if (powerTile1.canReceive(direction) != powerTile2.canProvide(direction.getOpposite()) && powerTile1.canProvide(direction) != powerTile2.canReceive(direction.getOpposite())) {
            EFlux.systemPrintDebug("Snap2");
            return false; //We don't want to receivers or 2 sources connecting, do we?
        }
        if (powerTile1.isTransmitter(direction) && powerTile2.isTransmitter(direction.getOpposite())) {
            EFlux.systemPrintDebug("Snap3");
            IEnergyTransmitter transmitter2 = powerTile2.getTransmitter(direction.getOpposite());
            IEnergyTransmitter transmitter1 = powerTile1.getTransmitter(direction);
            return transmitter2.canConnectTo(transmitter1) && transmitter1.canConnectTo(transmitter2);
        } else if (powerTile1.canProvide(direction) && powerTile2.canReceive(direction.getOpposite())){
            EFlux.systemPrintDebug("Snap4");
            return true;
        } else if (powerTile1.canReceive(direction)){
            EFlux.systemPrintDebug("Snap5");
            return powerTile2.canProvide(direction.getOpposite());
        }
        EFlux.systemPrintDebug("Snap6");
        return false;
    }

    public void removeTile(TileEntity tile){
        if (!world.isRemote) {
            if (WorldHelper.getDimID(world) != WorldHelper.getDimID(this.world)) {
                throw new IllegalArgumentException();
            }/*
            if (WorldHelper.chunkLoaded(world, tile.getPos()) && WorldHelper.getTileAt(world, tile.getPos()) == tile && tile instanceof IMultipartContainer && EnergyAPIHelper.isEnergyTile(tile)) {
                getPowerTile(tile.getPos()).checkConnector();
                onBlockChange(tile.getPos(), EnumSet.allOf(EnumFacing.class));
                surroundingData.put(new SurroundingData(tile.getPos()), tile.getPos());
                return;
            }*/
            EFlux.systemPrintDebug("RemTile");
            removeTile(getPowerTile(tile.getPos()), true);
        }
    }

    void removeTile(PowerTile powerTile, boolean reAdd){
        if (!world.isRemote && powerTile != null) {
            surroundingData.remove(powerTile.getLocation());
            registeredTiles.remove(powerTile.getLocation());
            for (EFluxCableGrid grid : powerTile.getGrids()) {
                if (grid != null) {
                    List<BlockPos> vec3List = Lists.newArrayList();
                    vec3List.addAll(grid.getLocations());
                    vec3List.remove(powerTile.getLocation());
                    //registeredTiles.remove(powerTile.getLocation());
                    this.grids.remove(grid);
                    List<BlockPos> vec3List2 = Lists.newArrayList();
                    for (BlockPos vec : vec3List) {
                        if (!vec.equals(powerTile.getLocation())) {
                            PowerTile pt = getPowerTile(vec);
                            if (pt != null) {
                                pt.removeGrid(grid);
                                vec3List2.add(vec);
                            }
                        }
                    }
                    if (reAdd) {
                        for (BlockPos vec : vec3List2) {
                            if (getPowerTile(vec) != null) {
                                addTile(getPowerTile(vec));
                            }
                        }
                    }
                }
            }
        } else {
            throw new IllegalStateException();
        }
    }

    public void onServerTickInternal(){
        if (!pending.isEmpty() && pending.size() == oldInt) {
            for (PowerTile powerTile = pending.poll(); powerTile != null; powerTile = pending.poll()){
                addTile(powerTile);
            }
            pending.clear();
        }
        this.oldInt = pending.size();
        int size = grids.size();
        for (int i = 0; i < size; i++) {
            try {
                grids.get(i).onTick();
            } catch (Throwable t) {
                EFlux.logger.error(t);
            }
            EFlux.systemPrintDebug((i+1)+"/"+size);
        }
    }

    public PowerTile getPowerTile(BlockPos loc){
        return registeredTiles.get(loc);
    }

    public void fillSurroundings(List<BlockPos> list){
        for (BlockPos pos : list){
            surroundingData.put(new SurroundingData(pos), pos);
        }
    }

    private class SurroundingData {

        private SurroundingData(BlockPos pos){
            data = Maps.newEnumMap(EnumFacing.class);
            for (EnumFacing facing : EnumFacing.VALUES){
                FacedSurroundingData s = new FacedSurroundingData(pos, facing);
                data.put(facing, s);
                hash += facing.ordinal() * s.hashCode();
            }
        }

        private final Map<EnumFacing, FacedSurroundingData> data;
        private int hash;

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof SurroundingData && equal((SurroundingData) obj);
        }

        private boolean equal(SurroundingData surroundingData){
            for (EnumFacing facing : EnumFacing.VALUES){
                if (!data.get(facing).equals(surroundingData.data.get(facing))){
                    return false;
                }
            }
            return true;
        }

        private boolean isBlockChange(SurroundingData surroundingData){
            for (EnumFacing facing : EnumFacing.VALUES){
                if (data.get(facing).hasBlock != surroundingData.data.get(facing).hasBlock){
                    return true;
                }
            }
            return false;
        }

    }

    private class FacedSurroundingData {

        private FacedSurroundingData(BlockPos pos, EnumFacing side){
            PowerTile thisTile = getPowerTile(pos);
            PowerTile otherTile = getPowerTile(pos.offset(side));
            //if (otherTile != null && otherTile.getTile() != (WorldHelper.chunkLoaded(world, pos.offset(side)) ? WorldHelper.getTileAt(world, pos.offset(side)) : null)){
             //   otherTile = null;
            //}
            if (thisTile == null){
                throw new RuntimeException();
            }
            if (otherTile != null){
                transmitter = otherTile.isTransmitter(side.getOpposite());
                receive = otherTile.isReceiver(side.getOpposite());
                send = otherTile.isProvider(side.getOpposite());
                if (transmitter && thisTile.isTransmitter(side)){
                    IEnergyTransmitter transmitter2 = otherTile.getTransmitter(side.getOpposite());
                    IEnergyTransmitter transmitter1 = thisTile.getTransmitter(side);
                    //EFlux.systemPrintDebug("ThisN: " + (transmitter1 == null) + ",  OtherN: " + (transmitter2 == null));
                    transmitterConnect = transmitter1 != null && transmitter2 != null && transmitter2.canConnectTo(transmitter1) && transmitter1.canConnectTo(transmitter2);
                } else {
                    transmitterConnect = false;
                }
            } else {
                transmitter = receive = send = transmitterConnect = false;
            }
            hasBlock = otherTile != null;
        }

        private final boolean transmitter, receive, send, transmitterConnect, hasBlock;

        @Override
        public int hashCode() {
            return (transmitter ? 1 : 0) * 31 + (receive ? 1 : 0) * 43 + (send ? 1 : 0) * 57 + (transmitterConnect ? 1 : 0) * 69;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof FacedSurroundingData && equal((FacedSurroundingData) obj);
        }

        private boolean equal(FacedSurroundingData obj){
            return transmitter == obj.transmitter && receive == obj.receive && send == obj.send && transmitterConnect == obj.transmitterConnect;
        }

    }

}
