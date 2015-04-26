package elec332.eflux.test.power;

import cpw.mods.fml.common.gameevent.TickEvent;
import elec332.eflux.EFlux;
import elec332.eflux.api.transmitter.IEnergyReceiver;
import elec332.eflux.api.transmitter.IEnergySource;
import elec332.eflux.api.transmitter.IEnergyTile;
import elec332.eflux.api.transmitter.IPowerTransmitter;
import elec332.eflux.test.blockLoc.BlockLoc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class WorldGridHolder {

    public WorldGridHolder(World world){
        this.world = world;
        this.grids = new ArrayList<EFluxCableGrid>();
        this.registeredTiles = new HashMap<BlockLoc, PowerTile>();
        this.pending = new ArrayList<PowerTile>();
        this.pendingRemovals = new ArrayList<PowerTile>();
        this.oldInt = 0;
    }


    private World world;  //Dunno why I have this here (yet)
    //private List<EFluxCableGrid> grids;
    //private WeakHashMap<EFluxCableGrid, Object> grids;
    private List<EFluxCableGrid> grids;
    private List<PowerTile> pending;
    private List<PowerTile> pendingRemovals;
    //private boolean q = true;
    private Map<BlockLoc, PowerTile> registeredTiles;
    private int oldInt;
    //private boolean r = true;

    public EFluxCableGrid registerGrid(EFluxCableGrid grid){
        this.grids.add(grid);
        return grid;
    }

    public NBTTagCompound toNBT(NBTTagCompound tagCompound){
        return tagCompound;
    }

    public Map<BlockLoc, PowerTile> getRegisteredTiles() {
        return registeredTiles;
    }

    /*public EFluxCableGrid genNewPowerGrid(IEnergyTile base){
        EFluxCableGrid grid = new EFluxCableGrid(world);
        grid.addToGrid(base, genCoords((TileEntity)base));
        //grids.put(grid, null);
        grids.add(grid);
        return grid;
    }
*/
    protected void removeGrid(EFluxCableGrid grid){
        grids.remove(grid);
        /*try{
        for (EFluxCableGrid grid1 : grids){
            if (grid.equals(grid1))
                grids.remove(grid1);
        }}catch (Exception e){
            e.printStackTrace();
        }*/
    }

    public void addTile(IEnergyTile tile){
        TileEntity theTile = (TileEntity) tile;
        PowerTile powerTile = new PowerTile(theTile, this);
        /*if (theTile instanceof IEnergyReceiver || theTile instanceof IEnergySource){
            for (int i = 0; i < 5; i++){
                powerTile.getGrids().add(registerGrid(new EFluxCableGrid(world, powerTile)));
            }
        }*/
        registeredTiles.put(genCoords(theTile), powerTile);
        //pending.add(powerTile);
        addTile(powerTile);
        EFlux.logger.info("Tile placed at "+genCoords(theTile).toString());
       // r = true;
    }

    public void addTile(PowerTile powerTile){
        if(!world.isRemote) {
            //try {
            EFlux.logger.info("Processing tile at "+powerTile.getLocation().toString());
                TileEntity theTile = powerTile.getTile();
                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                    EFlux.logger.info("Processing tile at "+powerTile.getLocation().toString() + " for side "+direction.toString());
                    TileEntity possTile = world.getTileEntity(theTile.xCoord + direction.offsetX, theTile.yCoord + direction.offsetY, theTile.zCoord + direction.offsetZ);
                    if (possTile != null && possTile instanceof IEnergyTile) {
                        PowerTile powerTile1 = getPowerTile(genCoords(possTile));
                        if (powerTile1 == null || !powerTile1.hasInit()) {
                            pending.add(powerTile);
                            break;
                        }
                        if (canConnect(powerTile, direction, powerTile1)) {
                            if (possTile instanceof IPowerTransmitter) {
                                EFluxCableGrid intGrid = powerTile.getGridFromSide(direction);
                                EFluxCableGrid grid = powerTile1.getGrid();
                                if (!grid.equals(intGrid))
                                    grid.mergeGrids(intGrid);
                            } else {
                                /*if (possTile instanceof IEnergySource) {
                                    //IEnergySource energySource = (IEnergySource) possTile;
                                    //if (energySource.canProvidePowerTo(direction.getOpposite())) {
                                       // if (!(powerTile.getTile() instanceof IEnergySource)) {
                                            EFluxCableGrid grid = powerTile1.getGridFromSide(direction.getOpposite());
                                            powerTile.getGridFromSide(direction).mergeGrids(grid);
                                        //}
                                    //}
                                }
                                if (possTile instanceof IEnergyReceiver) {
                                    //IEnergyReceiver energyReceiver = (IEnergyReceiver) possTile;
                                    //if (energyReceiver.canAcceptEnergyFrom(direction.getOpposite())) {
                                        //if (!(powerTile.getTile() instanceof IEnergyReceiver)) {
                                            EFluxCableGrid grid = powerTile1.getGridFromSide(direction.getOpposite());
                                            powerTile.getGridFromSide(direction).mergeGrids(grid);
                                        //}
                                    //}
                                }*/
                                EFluxCableGrid grid = powerTile1.getGridFromSide(direction.getOpposite());
                                powerTile.getGridFromSide(direction).mergeGrids(grid);
                            }
                        }
                    } else {
                        EFlux.logger.info("There is no tile at side "+direction.toString() + " that is valid for connection");
                    }
                }

            //} catch (Exception e){}
        }
    }

    private boolean canConnect(PowerTile powerTile1, ForgeDirection direction, PowerTile powerTile2){
        TileEntity mainTile = powerTile1.getTile();
        //TileEntity secondTile = powerTile2.getTile();
        boolean flag1 = false;
        boolean flag2 = false;
        if (powerTile1.getConnectType() == powerTile2.getConnectType() && (powerTile1.getConnectType() == PowerTile.ConnectType.SEND || powerTile1.getConnectType() == PowerTile.ConnectType.RECEIVE))
            return false; //We don't want to receivers or 2 sources connecting, do we?
        if (mainTile instanceof IPowerTransmitter){
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

    private boolean canConnectFromSide(ForgeDirection direction, PowerTile powerTile2){
        TileEntity secondTile = powerTile2.getTile();
        boolean flag1 = false;
        boolean flag2 = false;
        if (secondTile instanceof IPowerTransmitter)
            return true;
        if (secondTile instanceof IEnergyReceiver)
            flag1 = ((IEnergyReceiver) secondTile).canAcceptEnergyFrom(direction);
        if (secondTile instanceof IEnergySource)
            flag2 = ((IEnergySource)secondTile).canProvidePowerTo(direction);
        return flag1 || flag2;
    }

    public void removeTile(IEnergyTile tile){
        PowerTile powerTile = getPowerTile(genCoords((TileEntity) tile));
        powerTile.toGo = 3;
        pendingRemovals.add(powerTile);
        //q = true;
    }

    public void removeTile(PowerTile tile){
        PowerTile powerTile = tile; // = registeredTiles.get(genCoords((TileEntity) tile));
        //powerTile = getPowerTile(genCoords((TileEntity)tile));
        //try {
        if (powerTile != null)
            for (EFluxCableGrid grid : powerTile.getGrids()) {
                if(grid != null) {
                    EFlux.logger.info("Removing tile at " + powerTile.getLocation().toString());
                    List<BlockLoc> vec3List = new ArrayList<BlockLoc>();
                    vec3List.addAll(grid.getLocations());
                    vec3List.remove(powerTile.getLocation());
                    EFlux.logger.info(registeredTiles.keySet().size());
                    registeredTiles.remove(powerTile.getLocation());
                    EFlux.logger.info(registeredTiles.keySet().size());
                    this.grids.remove(grid);
                    List<BlockLoc> vec3List2 = new ArrayList<BlockLoc>();
                    for (BlockLoc vec : vec3List) {
                        if (!vec.equals(powerTile.getLocation())) {
                            getPowerTile(vec).resetGrid(grid);
                            vec3List2.add(vec);
                        }
                        //registeredTiles.remove(vec);
                    }
                    for (BlockLoc vec : vec3List2){
                        EFlux.logger.info("Re-adding tile at " + vec.toString());
                        TileEntity tileEntity1 = getTile(vec);
                        if (tileEntity1 instanceof IEnergyTile)
                            if (getPowerTile(vec) != null)
                                addTile(getPowerTile(vec));
                    }
                }
            }
        //} catch (NullPointerException e){e.printStackTrace();}
        /*EFluxCableGrid[] grids = getGrid(genCoords((TileEntity) tile));
        if (grids == null || grids.length == 0)
            return;
        for (EFluxCableGrid grid : grids) {
            List<Vec3> vec3List = new ArrayList<Vec3>();
            vec3List.addAll(grid.getLocations());
            this.grids.remove(grid);
            for (Vec3 vec : vec3List) {
                TileEntity tileEntity1 = getTile(vec);
                if (tileEntity1 instanceof IEnergyTile)
                    MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent((IEnergyTile) tileEntity1));
            }
        }*/
    }

    private void remTile(BlockLoc loc){
        for (BlockLoc loc1 : registeredTiles.keySet()){
            if (loc.equals(loc1))
                registeredTiles.remove(loc1);
        }
    }

    private EFluxCableGrid[] getGrid(Vec3 vec){
        List<EFluxCableGrid> ret = new ArrayList<EFluxCableGrid>();
        if (vec != null){
            for (EFluxCableGrid grid : grids){
                if (grid.hasTile(vec))
                    ret.add(grid);
            }
        }
        return ret.toArray(new EFluxCableGrid[ret.size()]);
    }

    private EFluxCableGrid getFirstGrid(Vec3 vec){
        //if (vec != null){
        for (EFluxCableGrid grid : grids){
            if (grid.hasTile(vec))
                return grid;
        }
        //}
        return null;
    }

    protected void onServerTickInternal(TickEvent event){
        if (event.phase == TickEvent.Phase.START) {
            EFlux.logger.info("Tick!");
            boolean r = false;
            boolean q = false;
            if (pending.size() == oldInt && oldInt > 0 && !r) {
                List<PowerTile> tr = new ArrayList<PowerTile>();
                for (int i = 0; i < pending.size(); i++)
                    tr.add(pending.get(i));
                for (PowerTile powerTile : tr) {
                    addTile(powerTile);
                }
                pending.removeAll(tr);
                EFlux.logger.info("TickStuffPendingDone");
            }
            if (!q && !pendingRemovals.isEmpty()){
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
            }

            this.oldInt = pending.size();
            //r = false;
            //q = false;
            /*int i = 0;
            try {
                for (EFluxCableGrid grid : grids) {
                    i++;
                    grid.onTick();
                    EFlux.logger.info(i);
                }
            } catch (ConcurrentModificationException e){

            }*/
            for (int i = 0; i < grids.size(); i++){
                try {
                    grids.get(i).onTick();
                } catch (Throwable t){
                    throw new RuntimeException(t);
                }
                EFlux.logger.info(i);
            }
        }
    }

    public PowerTile getPowerTile(BlockLoc loc){
        return registeredTiles.get(loc);
        /*for (BlockLoc blockLoc : registeredTiles.keySet()){
            if (blockLoc.equals(loc))
                return registeredTiles.get(blockLoc);
        }
        return null;*/
    }


    private BlockLoc genCoords(TileEntity tileEntity){
        return new BlockLoc(tileEntity);
    }

    private TileEntity getTile(BlockLoc vec){
        return world.getTileEntity(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    /*public static void onServerTick(TickEvent.ServerTickEvent event){
        for (World world : registeredWorlds){
            get(world).onServerTickInternal(event);
        }
    }*/
}
