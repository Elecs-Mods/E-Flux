package elec332.eflux.energy.grid;

import com.google.common.collect.Sets;
import elec332.core.main.ElecCore;
import elec332.core.world.DimensionCoordinate;
import elec332.core.world.PositionedObjectHolder;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.energy.IEnergyProvider;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.util.Config;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import java.util.Set;

import static elec332.eflux.api.EFluxAPI.*;
import static elec332.eflux.api.energy.ConnectionType.*;

/**
 * Created by Elec332 on 24-7-2016.
 */
public enum GridObjectHandler {

    INSTANCE;

    @SuppressWarnings("all")
    GridObjectHandler(){
        this.objects = new Int2ObjectArrayMap<PositionedObjectHolder<EFluxEnergyObject>>();
        this.extraUnload = Sets.newHashSet();
        this.changeCheck = Sets.newHashSet();
        this.add = Sets.newHashSet();
        this.grids = Sets.newHashSet();
    }

    private final Int2ObjectMap<PositionedObjectHolder<EFluxEnergyObject>> objects;
    private final Set<DimensionCoordinate> extraUnload, changeCheck, add;

    private final Set<EnergyGrid> grids;

    @SuppressWarnings("all") //Manual switch
    private static boolean forceDebug = ElecCore.developmentEnvironment && false;

    protected void tickEnd(){
        int i = 1;
        for (EnergyGrid grid : grids){
            systemPrintDebug("TickStart "+ i);
            grid.tick();
            systemPrintDebug("Tick end");
            i++;
        }
        if (shouldDebug()) {
            int i3 = 0;
            for (int i2 : objects.keySet()) {
                for (ChunkPos chunkPos : objects.get(i2).getChunks())
                    i3 += objects.get(i2).getObjectsInChunk(chunkPos).size();
            }
            System.out.println("Total objects: " + i3);
        }
    }

    //Block changed
    @SuppressWarnings("all")
    protected final void checkNotifyStuff(Set<DimensionCoordinate> updates){
        for (DimensionCoordinate dimCoord : updates) {
            EFluxEnergyObject o = getObject(dimCoord);
            if (o == null){
                continue;
            }
            TileEntity tile = dimCoord.getTileEntity();
            if (tile == null) {
                if (o != null) {
                    //o.clearTile(); //Just in case
                    extraUnload.add(dimCoord);
                }
                //Nothing to do, no tile and no energy object.
                return;
            } else {
                if (!o.validTile()){
                    throw new IllegalStateException(); //Something has gone terribly wrong somewhere...
                }
                if (o.checkCapabilities()){
                    changeCheck.add(dimCoord);
                    return;
                }
                //Insignificant change, nothing more to do.
            }
        }
    }

    //Block added/removed (most likely atleast)
    protected void checkBlockUpdates(Set<DimensionCoordinate> updates){
        for (DimensionCoordinate dimCoord : updates){
            TileEntity tile = dimCoord.getTileEntity();
            EFluxEnergyObject o = getObject(dimCoord);
            if (o == null && tile == null){
                return;
            }
            if (o == null && isEnergyObject(tile)){
                add.add(dimCoord);
            }
            if (o != null && tile == null){
                extraUnload.add(dimCoord);
            }
            if (o != null && isEnergyObject(tile)){
                if (o.checkCapabilities()){
                    changeCheck.add(dimCoord);
                }
            }
        }
    }

    protected void worldUnload(World world){
        PositionedObjectHolder<EFluxEnergyObject> worldObjects = objects.get(WorldHelper.getDimID(world));
        if (worldObjects != null) {
            Set<DimensionCoordinate> unload = Sets.newHashSet();
            for (ChunkPos chunkPos : worldObjects.getChunks()) {
                for (EFluxEnergyObject o : worldObjects.getObjectsInChunk(chunkPos).values()) {
                    unload.add(o.getPosition());
                }
            }
            unloadObjects_Internal(unload);
        }
    }

    protected void checkChunkUnload(Set<DimensionCoordinate> updates){
        updates.addAll(extraUnload);
        updates.addAll(changeCheck);
        unloadObjects_Internal(updates);
        extraUnload.clear();
    }

    private void unloadObjects_Internal(Set<DimensionCoordinate> updates){
        for (DimensionCoordinate dimCoord : updates){
            EFluxEnergyObject o = getObject(dimCoord);
            if (o == null){
                System.out.println("????_-3"); //???
                continue;
            }
            if (o.isEndPoint()){ //Easier & lightweight
                EnergyGrid grid = o.getEndPoint();
                grid.removeObject(o);
                o.removedFromGrid(grid);
            } else { //Shame, but 1 more chance
                Set<EnergyGrid> grids = o.getGrids();
                if (o.multipleEndpoints()){
                    for (EnergyGrid grid : grids){
                        if (grid != null){
                            grid.removeObject(o);
                        }
                        o.removedFromGrid(grid);
                    }
                } else { //Nope, gotta do it the hard way
                    for (EnergyGrid grid : grids){
                        if (grid != null) {
                            for (EnergyGrid.ConnectionData o2 : grid.getAllConnections()) {
                                o2.object.setGridForFace(null, o2.facing);
                                if (!updates.contains(o2.object.getPosition()) && !o.equals(o2.object)) {
                                    add.add(o2.object.getPosition());
                                }
                            }
                        }
                        o.removedFromGrid(grid);
                        removeGrid(grid);
                    }
                }
            }
            removeObject(dimCoord);
        }
    }

    protected void checkChunkLoad(Set<DimensionCoordinate> updates){
        Set<DimensionCoordinate> oldUpdates = Sets.newHashSet(updates);
        updates.addAll(add);
        updates.addAll(changeCheck);
        for (DimensionCoordinate dimCoord : updates){
            TileEntity tile = dimCoord.getTileEntity();
            if (tile == null || !isEnergyObject(tile)){
                continue;
            }
            EFluxEnergyObject o = getObject(dimCoord);
            if (o != null){
                if (oldUpdates.contains(dimCoord)) {
                    throw new IllegalStateException();
                }
            } else {
                o = createNewObject(tile);
                o.checkCapabilities(); //Set initial data
            }
            internalAdd(o);
        }
        add.clear();
        changeCheck.clear();
    }

    private void internalAdd(EFluxEnergyObject o){ //Separated to make code more readable
        DimensionCoordinate dimCoord = o.getPosition();
        World world = dimCoord.getWorld();
        if (world == null){
            throw new IllegalStateException();
        }
        BlockPos pos = dimCoord.getPos();
        for (EnumFacing facing : EnumFacing.VALUES){
            BlockPos offset = pos.offset(facing);
            DimensionCoordinate offsetDimCoord = new DimensionCoordinate(dimCoord.getDimension(), offset);
            EFluxEnergyObject offsetObject = getObject(offsetDimCoord);
            if (offsetObject == null){
                continue;
            }
            if (!canConnect(o, facing, offsetObject)){
                continue;
            }
            EnergyGrid oGrid = o.getGrid(facing), offsetGrid = offsetObject.getGrid(facing.getOpposite());
            if (oGrid == null){
                if (offsetGrid == null){
                    EnergyGrid newGrid = addGrid();
                    newGrid.addObject(o, facing);
                    newGrid.addObject(offsetObject, facing.getOpposite());
                } else {
                    offsetGrid.addObject(o, facing);
                }
            } else {
                if (offsetGrid == null){
                    oGrid.addObject(offsetObject, facing.getOpposite());
                } else {
                    if (oGrid != offsetGrid) {
                        oGrid.merge(offsetGrid);
                        removeGrid(offsetGrid);
                    }
                    oGrid.addObject(o, facing);
                    oGrid.addObject(offsetObject, facing.getOpposite());
                }
            }
        }
    }

    @SuppressWarnings("all")
    private boolean canConnect(EFluxEnergyObject o1, EnumFacing facing, EFluxEnergyObject o2){
        EnumFacing opp = facing.getOpposite();
        TileEntity o1Tile = o1.getTileEntity(), o2Tile = o2.getTileEntity();
        if (o1 == null || o2 == null || o1Tile == null || o2Tile == null){
            throw new IllegalStateException();
        }
        if (providerReceiver(o1, facing, o2)){
            return true;
        }
        if (providerReceiver(o2, opp, o1)){
            return true;
        }
        if (o1.hasCapability(TRANSMITTER_CAPABILITY, facing)){
            final IEnergyTransmitter o1T = o1.getCapability(TRANSMITTER_CAPABILITY, facing);
            if (o1T != null) {
                if (o2.hasCapability(TRANSMITTER_CAPABILITY, opp)) {
                    IEnergyTransmitter o2T = o2.getCapability(TRANSMITTER_CAPABILITY, opp);
                    if (o2T != null && o1T.canConnectTo(TRANSMITTER, o2Tile, TRANSMITTER, o2T) && o2T.canConnectTo(TRANSMITTER, o1Tile, TRANSMITTER, o1T)) {
                        return true;
                    }
                }
                if (transmitterConnect(o1, o1T, facing, o2)) {
                    return true;
                }
            }
        }
        if (o2.hasCapability(TRANSMITTER_CAPABILITY, opp)){
            final IEnergyTransmitter o2T = o2.getCapability(TRANSMITTER_CAPABILITY, opp);
            if (o2T != null && transmitterConnect(o2, o2T, opp, o1)){
                return true;
            }
        }
        return false;
    }

    private boolean transmitterConnect(EFluxEnergyObject o1, @Nonnull IEnergyTransmitter o1T, EnumFacing facing, EFluxEnergyObject o2){
        EnumFacing opp = facing.getOpposite();
        @Nonnull
        @SuppressWarnings("all")
        TileEntity o1Tile = o1.getTileEntity(), o2Tile = o2.getTileEntity();
        if (isReceiver(o2, opp)){
            IEnergyReceiver receiver = getReceiver(o2, opp);
            if (receiver != null && receiver.canConnectTo(RECEIVER, o1Tile, TRANSMITTER, o1T) && o1T.canConnectTo(TRANSMITTER, o2Tile, RECEIVER, receiver)){
                return true;
            }
        }
        if (isProvider(o2, opp)){
            IEnergyProvider source = getProvider(o2, opp);
            if (source != null && source.canConnectTo(PROVIDER, o1Tile, TRANSMITTER, o1T) && o1T.canConnectTo(TRANSMITTER, o2Tile, PROVIDER, source)){
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("all")
    private boolean providerReceiver(EFluxEnergyObject o1, EnumFacing facing, EFluxEnergyObject o2){
        EnumFacing opp = facing.getOpposite();
        if (isProvider(o1, facing) && isReceiver(o2, opp)){
            IEnergyProvider source = getProvider(o1, facing);
            IEnergyReceiver receiver = getReceiver(o2, opp);
            return source != null && receiver != null && source.canConnectTo(PROVIDER, o2.getTileEntity(), RECEIVER, receiver) && receiver.canConnectTo(RECEIVER, o1.getTileEntity(), PROVIDER, source);
        }
        return false;
    }

    private IEnergyReceiver getReceiver(ICapabilityProvider capabilityProvider, EnumFacing facing){
        return capabilityProvider.getCapability(RECEIVER_CAPABILITY, facing);
    }

    private IEnergyProvider getProvider(ICapabilityProvider capabilityProvider, EnumFacing facing){
        return capabilityProvider.getCapability(PROVIDER_CAPABILITY, facing);
    }

    private boolean isReceiver(ICapabilityProvider capabilityProvider, EnumFacing facing){
        return capabilityProvider.hasCapability(RECEIVER_CAPABILITY, facing);
    }

    private boolean isProvider(ICapabilityProvider capabilityProvider, EnumFacing facing){
        return capabilityProvider.hasCapability(PROVIDER_CAPABILITY, facing);
    }

    protected static boolean isValidReceiver(ICapabilityProvider capabilityProvider, EnumFacing facing){
        return hasValidCapability(capabilityProvider, facing, RECEIVER_CAPABILITY);
    }

    protected static boolean isValidProvider(ICapabilityProvider capabilityProvider, EnumFacing facing){
        return hasValidCapability(capabilityProvider, facing, PROVIDER_CAPABILITY);
    }

    protected static boolean isValidTransmitter(ICapabilityProvider capabilityProvider, EnumFacing facing){
        return hasValidCapability(capabilityProvider, facing, TRANSMITTER_CAPABILITY);
    }

    private static boolean hasValidCapability(ICapabilityProvider capabilityProvider, EnumFacing facing, Capability capability){
        return capabilityProvider.hasCapability(capability, facing) && capabilityProvider.getCapability(capability, facing) != null;
    }

    private EnergyGrid addGrid(){
        EnergyGrid newGrid = new EnergyGrid();
        grids.add(newGrid);
        newGrid.validate();
        return newGrid;
    }

    private void removeGrid(EnergyGrid grid){
        grid.invalidate();
        grids.remove(grid);
    }

    private static boolean isEnergyObject(ICapabilityProvider capabilityProvider){
        return GridEventInputHandler.isEnergyObject(capabilityProvider);
    }

    private EFluxEnergyObject createNewObject(TileEntity tile){
        EFluxEnergyObject o = new EFluxEnergyObject(tile);
        objects.get(WorldHelper.getDimID(tile.getWorld())).put(o, tile.getPos());
        return o;
    }

    private void removeObject(DimensionCoordinate dimensionCoordinate){
        getDim(dimensionCoordinate).remove(dimensionCoordinate.getPos());
    }

    private EFluxEnergyObject getObject(DimensionCoordinate dimensionCoordinate){
        return getDim(dimensionCoordinate).get(dimensionCoordinate.getPos());
    }

    private PositionedObjectHolder<EFluxEnergyObject> getDim(DimensionCoordinate dimensionCoordinate){
        PositionedObjectHolder<EFluxEnergyObject> ret = objects.get(dimensionCoordinate.getDimension());
        if (ret == null){
            ret = new PositionedObjectHolder<>();
            objects.put(dimensionCoordinate.getDimension(), ret);
        }
        return ret;
    }

    protected static boolean shouldDebug(){
        return forceDebug || Config.debugLog;
    }

    private static void systemPrintDebug(Object s){
        if (shouldDebug()) {
            System.out.println(s);
        }
    }

}
