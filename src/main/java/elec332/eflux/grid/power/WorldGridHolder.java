package elec332.eflux.grid.power;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.compat.Compat;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.PartSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class WorldGridHolder {

    public WorldGridHolder(World world){
        this.world = world;
        this.grids = Lists.newArrayList();
        this.registeredTiles = Maps.newHashMap();
        this.pending = new ArrayDeque<PowerTile>();
        this.oldInt = 0;
    }

    private World world;  //Dunno why I have this here (yet)
    private List<EFluxCableGrid> grids;
    private Queue<PowerTile> pending;
    private Map<BlockPos, PowerTile> registeredTiles;
    private int oldInt;

    public EFluxCableGrid registerGrid(EFluxCableGrid grid){
        this.grids.add(grid);
        return grid;
    }

    protected void removeGrid(EFluxCableGrid grid){
        grids.remove(grid);
    }

    public void addTile(Object tile, World world, BlockPos pos){
        EnergyAPIHelper.checkValidity(tile);
        if (WorldHelper.getDimID(world) != WorldHelper.getDimID(this.world)){
            throw new IllegalArgumentException();
        }
        PowerTile powerTile = new PowerTile(tile, world, pos);
        registeredTiles.put(pos, powerTile);
        addTile(powerTile);
        EFlux.systemPrintDebug("Tile placed at " + pos);
    }

    private void addTile(PowerTile powerTile){
        if(!world.isRemote) {
            EFlux.systemPrintDebug("Processing tile at " + powerTile.getLocation().toString());
            for (EnumFacing direction : EnumFacing.VALUES) {
                EFlux.systemPrintDebug("Processing tile at " + powerTile.getLocation().toString() + " for side " + direction.toString());
                BlockPos checkedPos = powerTile.getLocation().offset(direction);
                Object possTile = getEnergyObjectAt(world, checkedPos);
                if (possTile != null && EnergyAPIHelper.isEnergyTile(possTile)) {
                    PowerTile powerTile1 = getPowerTile(checkedPos);
                    if (powerTile1 == null || !powerTile1.hasInit()) {
                        pending.add(powerTile);
                        break;
                    }
                    if (canConnect(powerTile, direction, powerTile1)) {
                        EFluxCableGrid grid = powerTile1.getGridFromSide(direction.getOpposite());
                        powerTile.getGridFromSide(direction).mergeGrids(grid);
                    }
                } else {
                    EFlux.systemPrintDebug("There is no tile at side " + direction.toString() + " that is valid for connection");
                }
            }
        }
    }

    private Object getEnergyObjectAt(World world, BlockPos pos){
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (EnergyAPIHelper.isEnergyTile(tile)){
            return tile;
        } else if (Compat.MCMP && tile instanceof IMultipartContainer){
            IMultipart part = ((IMultipartContainer) tile).getPartInSlot(PartSlot.CENTER);
            if (EnergyAPIHelper.isEnergyTile(part)){
                return part;
            }
            //for (IMultipart multipart : ((IMultipartContainer) tile).getParts()){
            //    if ()
            //}
        }
        return null;
    }

    private boolean canConnect(PowerTile powerTile1, EnumFacing direction, PowerTile powerTile2){
        Object mainTile = powerTile1.getTile();
        boolean flag1 = false;
        boolean flag2 = false;
        if (powerTile1.getConnectType() == powerTile2.getConnectType() && (powerTile1.getConnectType() == PowerTile.ConnectType.SEND || powerTile1.getConnectType() == PowerTile.ConnectType.RECEIVE))
            return false; //We don't want to receivers or 2 sources connecting, do we?
        if (powerTile1.getConnectType() == PowerTile.ConnectType.CONNECTOR && powerTile2.getConnectType() == PowerTile.ConnectType.CONNECTOR){
            return ((IEnergyTransmitter)mainTile).getUniqueIdentifier().equals(((IEnergyTransmitter)powerTile2.getTile()).getUniqueIdentifier());
        } else if (powerTile1.getConnectType() == PowerTile.ConnectType.CONNECTOR){
            return canConnectFromSide(direction.getOpposite(), powerTile2);
        } else {
            if (powerTile1.getConnectType() == PowerTile.ConnectType.SEND_RECEIVE){
                if (((IEnergySource) mainTile).canProvidePowerTo(direction))
                    flag1 = canConnectFromSide(direction.getOpposite(), powerTile2);
                if (((IEnergyReceiver)mainTile).canAcceptEnergyFrom(direction))
                    flag2 = canConnectFromSide(direction.getOpposite(), powerTile2);
                return flag1 || flag2;
            } else if (powerTile1.getConnectType() == PowerTile.ConnectType.RECEIVE){
                if (((IEnergyReceiver) mainTile).canAcceptEnergyFrom(direction))
                    return canConnectFromSide(direction.getOpposite(), powerTile2);
            } else if (powerTile1.getConnectType() == PowerTile.ConnectType.SEND){
                if (((IEnergySource) mainTile).canProvidePowerTo(direction))
                    return canConnectFromSide(direction.getOpposite(), powerTile2);
            }
            return false;
        }
    }

    private boolean canConnectFromSide(EnumFacing direction, PowerTile powerTile2){
        Object secondTile = powerTile2.getTile();
        boolean flag1 = false;
        boolean flag2 = false;
        if (secondTile instanceof IEnergyTransmitter)
            return true;
        if (secondTile instanceof IEnergyReceiver)
            flag1 = ((IEnergyReceiver) secondTile).canAcceptEnergyFrom(direction);
        if (secondTile instanceof IEnergySource)
            flag2 = ((IEnergySource)secondTile).canProvidePowerTo(direction);
        return flag1 || flag2;
    }

    public void removeTile(Object tile, World world, BlockPos pos){
        EnergyAPIHelper.checkValidity(tile);
        if (WorldHelper.getDimID(world) != WorldHelper.getDimID(this.world)){
            throw new IllegalArgumentException();
        }
        removeTile(getPowerTile(pos));
    }

    private void removeTile(PowerTile powerTile){
        if (powerTile != null) {
            for (EFluxCableGrid grid : powerTile.getGrids()) {
                if (grid != null) {
                    EFlux.systemPrintDebug("Removing tile at " + powerTile.getLocation().toString());
                    List<BlockPos> vec3List = Lists.newArrayList();
                    vec3List.addAll(grid.getLocations());
                    vec3List.remove(powerTile.getLocation());
                    EFlux.systemPrintDebug(registeredTiles.keySet().size());
                    registeredTiles.remove(powerTile.getLocation());
                    EFlux.systemPrintDebug(registeredTiles.keySet().size());
                    EFlux.systemPrintDebug(grids.size());
                    this.grids.remove(grid);
                    EFlux.systemPrintDebug(grids.size());
                    List<BlockPos> vec3List2 = Lists.newArrayList();
                    for (BlockPos vec : vec3List) {
                        if (!vec.equals(powerTile.getLocation())) {
                            PowerTile pt = getPowerTile(vec);
                            if (pt != null) {
                                pt.resetGrid(grid);
                                vec3List2.add(vec);
                            }
                        }
                    }
                    for (BlockPos vec : vec3List2) {
                        EFlux.systemPrintDebug("Re-adding tile at " + vec.toString());
                        //TileEntity tileEntity1 = null;//getTile(vec);

                        //if (EnergyAPIHelper.isEnergyTile(tileEntity1))
                            if (getPowerTile(vec) != null)
                                addTile(getPowerTile(vec));
                    }
                }
            }
        }
    }

    public void onServerTickInternal(){
       // EFlux.systemPrintDebug("Tick! " + world.provider.dimensionId);
        if (!pending.isEmpty() && pending.size() == oldInt) {
               /*List<PowerTile> tr = new ArrayList<PowerTile>();
               for (PowerTile powerTile : pending)
                    tr.add(powerTile);
                for (PowerTile powerTile : tr)
                    addTile(powerTile);
                pending.removeAll(tr);
                EFlux.logger.info("TickStuffPendingDone");*/
                for (PowerTile powerTile = pending.poll(); powerTile != null; powerTile = pending.poll()){
                    addTile(powerTile);
                }
                pending.clear();
            }
            /*if (!pendingRemovals.isEmpty()){
                List<PowerTile> tr = new ArrayList<PowerTile>();
                for (PowerTile powerTile : pendingRemovals){
                    powerTile.toGo--;
                    if (getTile(powerTile.getLocation()) == null){
                        powerTile.toGo = 0;
                        if (!tr.contains(powerTile))
                            removeTile(powerTile);
                    }
                    if (powerTile.toGo <= 0)
                        tr.add(powerTile);
                }
                pendingRemovals.removeAll(tr);
            }*/
            this.oldInt = pending.size();
            for (int i = 0; i < grids.size(); i++){
                try {
                    grids.get(i).onTick();
                } catch (Throwable t){
                    //throw new RuntimeException(t);
                }
                EFlux.systemPrintDebug(i);
            }

    }

    public PowerTile getPowerTile(BlockPos loc){
        return registeredTiles.get(loc);
    }

    //private TileEntity getTile(BlockLoc vec){
    //    return world.getTileEntity(vec.xCoord, vec.yCoord, vec.zCoord);
    //}
}
