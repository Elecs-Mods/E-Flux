package elec332.eflux.test.power;

import cpw.mods.fml.common.gameevent.TickEvent;
import elec332.eflux.EFlux;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.transmitter.IEnergyReceiver;
import elec332.eflux.api.transmitter.IEnergySource;
import elec332.eflux.api.transmitter.IEnergyTile;
import elec332.eflux.api.transmitter.IPowerTransmitter;
import elec332.eflux.test.blockLoc.BlockLoc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

/**
 * Created by Elec332 on 23-4-2015.
 */
public class WorldGridHolder {

    public WorldGridHolder(World world){
        this.world = world;
        this.grids = new HashSet<EFluxCableGrid>();
        this.registeredTiles = new HashMap<BlockLoc, PowerTile>();
    }


    private World world;  //Dunno why I have this here (yet)
    //private List<EFluxCableGrid> grids;
    //private WeakHashMap<EFluxCableGrid, Object> grids;
    private Set<EFluxCableGrid> grids;
    private Map<BlockLoc, PowerTile> registeredTiles;

    public EFluxCableGrid registerGrid(EFluxCableGrid grid){
        this.grids.add(grid);
        return grid;
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
        //grids.remove(grid);
        try{
        for (EFluxCableGrid grid1 : grids){
            if (grid.equals(grid1))
                grids.remove(grid1);
        }}catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addTile(IEnergyTile tile){
        if(!world.isRemote) {
            //try {
                TileEntity theTile = (TileEntity) tile;
                PowerTile powerTile = new PowerTile(theTile, this);
                registeredTiles.put(genCoords(theTile), powerTile);
                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                    TileEntity possTile = world.getTileEntity(theTile.xCoord + direction.offsetX, theTile.yCoord + direction.offsetY, theTile.zCoord + direction.offsetZ);
                    if (possTile != null && possTile instanceof IEnergyTile) {
                        if (possTile instanceof IPowerTransmitter) {
                            PowerTile powerTile1 = getPowerTile(genCoords(possTile));//registeredTiles.get(genCoords(possTile));
                            EFluxCableGrid intGrid = powerTile.getGrid();
                            if (powerTile1 != null && powerTile1.hasInit()) {
                                EFluxCableGrid grid = powerTile1.getGrid();
                                if (!grid.equals(intGrid))
                                    grid.mergeGrids(intGrid);
                            }
                        }
                    }
                }

            //} catch (Exception e){}
        }
    }

    public void removeTile(IEnergyTile tile){
        PowerTile powerTile = registeredTiles.get(genCoords((TileEntity) tile));
        try {
            for (EFluxCableGrid grid : powerTile.getGrids()) {
                List<BlockLoc> vec3List = new ArrayList<BlockLoc>();
                vec3List.addAll(grid.getLocations());
                vec3List.remove(powerTile.getLocation());
                registeredTiles.remove(powerTile.getLocation());
                this.grids.remove(grid);
                for (BlockLoc vec : vec3List) {
                    registeredTiles.remove(vec);
                    TileEntity tileEntity1 = getTile(vec);
                    if (tileEntity1 instanceof IEnergyTile)
                        MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent((IEnergyTile) tileEntity1));
                }
            }
        } catch (NullPointerException e){e.printStackTrace();}
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
            int i = 0;
            try {
                for (EFluxCableGrid grid : grids) {
                    i++;
                    grid.onTick();
                    EFlux.logger.info(i);
                }
            } catch (ConcurrentModificationException e){

            }
        }
    }

    public PowerTile getPowerTile(BlockLoc loc){
        for (BlockLoc blockLoc : registeredTiles.keySet()){
            if (blockLoc.equals(loc))
                return registeredTiles.get(blockLoc);
        }
        return null;
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
