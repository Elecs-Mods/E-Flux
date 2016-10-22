package elec332.eflux.grid.energy;

import com.google.common.collect.Sets;
import elec332.core.grid.v2.AbstractGridHandler;
import elec332.core.main.ElecCore;
import elec332.core.world.DimensionCoordinate;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyProvider;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.util.Config;
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
 * Created by Elec332 on 1-8-2016.
 */
public final class EFluxGridHandler extends AbstractGridHandler<EFluxEnergyObject> {

    public EFluxGridHandler(){
        super();
        this.grids = Sets.newHashSet();
    }

    private final Set<EFluxEnergyGrid> grids;

    @SuppressWarnings("all") //Manual switch
    private static boolean forceDebug = ElecCore.developmentEnvironment && false;

    @Override
    public void tick() {
        int i = 1;
        for (EFluxEnergyGrid grid : grids){
            systemPrintDebug("Start "+i);
            grid.tick();
            systemPrintDebug("End");
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

    @Override
    protected void onObjectRemoved(EFluxEnergyObject o, Set<DimensionCoordinate> updates) {
        if (o.isEndPoint()){ //Easier & lightweight
            EFluxEnergyGrid grid = o.getEndPoint();
            grid.removeObject(o);
            o.removedFromGrid(grid);
        } else { //Shame, but 1 more chance
            Set<EFluxEnergyGrid> grids = o.getGrids();
            if (o.multipleEndpoints()){
                for (EFluxEnergyGrid grid : grids){
                    if (grid != null){
                        grid.removeObject(o);
                    }
                    o.removedFromGrid(grid);
                }
            } else { //Nope, gotta do it the hard way
                for (EFluxEnergyGrid grid : grids){
                    if (grid != null) {
                        for (EFluxEnergyGrid.ConnectionData o2 : grid.getAllConnections()) {
                            o2.object.setGridForFace(null, o2.connectedFacing);
                            //o2.object.removedFromGrid(grid);
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
    }

    @Override
    protected void internalAdd(EFluxEnergyObject o){ //Separated to make code more readable
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
            EFluxEnergyGrid oGrid = o.getGrid(facing), offsetGrid = offsetObject.getGrid(facing.getOpposite());
            if (oGrid == null){
                if (offsetGrid == null){
                    EFluxEnergyGrid newGrid = addGrid();
                    newGrid.addObject(o, facing);
                    newGrid.addObject(offsetObject, facing.getOpposite());
                } else {
                    offsetGrid.addObject(o, facing);
                    //Added
                    offsetGrid.addObject(offsetObject, facing.getOpposite());
                }
            } else {
                if (offsetGrid == null){
                    oGrid.addObject(offsetObject, facing.getOpposite());
                    //Added
                    oGrid.addObject(o, facing);
                } else {
                    if (oGrid != offsetGrid) {
                        oGrid.merge(offsetGrid);System.out.println("merge: "+oGrid+"  "+offsetGrid);
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

    private EFluxEnergyGrid addGrid(){
        EFluxEnergyGrid newGrid = new EFluxEnergyGrid();
        grids.add(newGrid);
        newGrid.validate();
        return newGrid;
    }

    private void removeGrid(EFluxEnergyGrid grid){
        grid.invalidate();
        grids.remove(grid);
    }

    @Override
    public boolean isValidObject(TileEntity tile) {
        if (tile == null){
            return false;
        }
        for (EnumFacing facing : EnumFacing.VALUES){
            if (tile.hasCapability(EFluxAPI.RECEIVER_CAPABILITY, facing) || tile.hasCapability(EFluxAPI.PROVIDER_CAPABILITY, facing) || tile.hasCapability(EFluxAPI.TRANSMITTER_CAPABILITY, facing)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected EFluxEnergyObject createNewObject(TileEntity tile) {
        return new EFluxEnergyObject(tile);
    }

    @SuppressWarnings("all")
    protected static boolean shouldDebug(){
        return forceDebug || Config.debugLog;
    }

    @SuppressWarnings("all")
    protected static void systemPrintDebug(Object s){
        if (shouldDebug()) {
            System.out.println(s);
        }
    }

}
