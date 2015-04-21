package elec332.eflux.test;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import elec332.eflux.api.transmitter.IEnergyReceiver;
import elec332.eflux.api.transmitter.IEnergySource;
import elec332.eflux.api.transmitter.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class WorldRegistryPowerNetwork {

    private static Map<World, WorldRegistryPowerNetwork> mappings = new WeakHashMap<World, WorldRegistryPowerNetwork>();

    public static WorldRegistryPowerNetwork get(World world){
        if (world == null)
            throw new IllegalArgumentException();
        WorldRegistryPowerNetwork ret = mappings.get(world);
        if (ret == null){
            ret = new WorldRegistryPowerNetwork(world);
            mappings.put(world, ret);
        }
        return ret;
    }
    //////////////////////////////////////////////////////

    private WorldRegistryPowerNetwork(World world){
        this.world = world;
        this.grids = new WeakHashMap<EFluxCableGrid, Object>();
        FMLCommonHandler.instance().bus().register(this);
    }

    private World world;  //Dunno why I have this here (yet)
    //private List<EFluxCableGrid> grids;
    private WeakHashMap<EFluxCableGrid, Object> grids;

    public EFluxCableGrid genNewPowerGrid(){
        EFluxCableGrid grid = new EFluxCableGrid(world);
        grids.put(grid, null);
        return grid;
    }

    protected void removeGrid(EFluxCableGrid grid){
        grids.remove(grid);
    }

    public void addTile(IEnergyTile tile){
        TileEntity theTile = ((TileEntity) tile);
        Vec3 tileCoords = genCoords(theTile);
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
            TileEntity possTile = theTile.getWorldObj().getTileEntity(theTile.xCoord+direction.offsetX, theTile.yCoord+direction.offsetY, theTile.zCoord+direction.offsetZ);
            if (possTile != null && possTile instanceof IEnergyTile){
                if (possTile instanceof IEnergyReceiver && ((IEnergyReceiver)possTile).canAcceptEnergyFrom(direction.getOpposite())){
                    EFluxCableGrid grid = genNewPowerGrid();
                    grid.addToGrid(tile, genCoords(theTile));
                    grid.addToGrid((IEnergyTile) possTile, genCoords(possTile));
                    /*Vec3 loc = genCoords(possTile);
                    for (EFluxCableGrid grid : grids.keySet()){
                        if (grid.canAddReceiver(loc))
                            grid.addToGrid(tile, genCoords(theTile));
                    }*/
                }
                if (possTile instanceof IEnergySource && ((IEnergySource)possTile).canProvidePowerTo(direction.getOpposite())){
                    if (getGrid(tileCoords) == null){
                        getGrid(genCoords(possTile)).addToGrid(tile, tileCoords);
                    } else getGrid(genCoords(possTile)).mergeGrids(getGrid(tileCoords));

                    /*Vec3 loc = genCoords(possTile);
                    for (EFluxCableGrid grid : grids.keySet()){
                        if (grid.canAddProvider(loc))
                            grid.addToGrid(tile, genCoords(theTile));
                    }*/
                }
            }
        }

        /*
        for (EFluxCableGrid grid : grids.keySet()){
            if (grid.addToGrid(tile))
                break;
        }*/
    }

    private EFluxCableGrid getGrid(Vec3 vec){
        if (vec != null){
            for (EFluxCableGrid grid : grids.keySet()){
                if (grid.hasTile(vec))
                    return grid;
            }
        }
        return null;
    }

    @SubscribeEvent
    private void onServerTick(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.START)
            for (EFluxCableGrid grid : grids.keySet())
                grid.onTick();
    }

    private Vec3 genCoords(TileEntity tileEntity){
        return Vec3.createVectorHelper(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }
}
