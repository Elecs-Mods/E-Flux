package elec332.eflux.test.power;

import cofh.api.energy.IEnergyReceiver;
import elec332.eflux.api.transmitter.IEnergySource;
import elec332.eflux.api.transmitter.IEnergyTile;
import elec332.eflux.api.transmitter.IPowerTransmitter;
import elec332.eflux.test.WorldRegistry;
import elec332.eflux.test.blockLoc.BlockLoc;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Elec332 on 15-4-2015.
 */
public class EFluxCableGrid {

    public EFluxCableGrid(World world, PowerTile p){
        //cables = new ArrayList<TileCable>();
        //providers = new ArrayList<IEnergyProvider>();
        acceptors = new ArrayList<BlockLoc>();
        providers = new ArrayList<BlockLoc>();
        locations = new ArrayList<BlockLoc>();
        this.world = world;
        if (!addToGrid((IEnergyTile)p.getTile(), p.getLocation()))
            throw new RuntimeException();
        identifier = UUID.randomUUID();
    }

    private UUID identifier;

    private World world;
    //private List<TileCable> cables;
    //private List<IEnergyProvider> providers;
    private List<BlockLoc> acceptors;
    private List<BlockLoc> providers;
    private List<BlockLoc> locations;

    public boolean addToGrid(IEnergyTile tile, BlockLoc loc){
        if (locations.contains(loc))
            return false;
        if (tile instanceof IPowerTransmitter){
            //acceptors.add(loc);
            //providers.add(loc);
            locations.add(loc);
            return true;
        }
        if (tile instanceof IEnergyReceiver){
            acceptors.add(loc);
            locations.add(loc);
            return true;
        } //else
        if (tile instanceof IEnergySource){
            providers.add(loc);
            //if (!locations.contains(loc))
            locations.add(loc);
            return true;
        } //else throw new RuntimeException();
        return true;
    }

    public boolean canAddReceiver(BlockLoc vec){
        return providers.contains(vec);
    }

    public boolean canAddProvider(BlockLoc vec){
        return acceptors.contains(vec);
    }


    //workaround
    public boolean hasTile(Vec3 vec){
        //WorldRegistryPowerNetwork.get(world).sm(""+locations.contains(vec));
        //return locations.contains(vec);
        for (BlockLoc vec0: locations){
            if (vec0.toString().equalsIgnoreCase(vec.toString())) {
                WorldRegistry.get(world).sm("ttrr");
                return true;
            }
        }
        WorldRegistry.get(world).sm("ffff");
        return false;
    }

    public List<BlockLoc> getLocations(){
        return locations;
    }


    public EFluxCableGrid mergeGrids(EFluxCableGrid grid){
        if (this.world.provider.dimensionId != grid.world.provider.dimensionId)
            throw new RuntimeException();
        WorldRegistry.get(grid.world).getWorldPowerGrid().removeGrid(grid);
        this.locations.addAll(grid.locations);
        this.acceptors.addAll(grid.acceptors);
        this.providers.addAll(grid.providers);
        for (BlockLoc vec : locations){
            Map<BlockLoc, PowerTile> blockLocPowerTileMap = WorldRegistry.get(world).getWorldPowerGrid().getRegisteredTiles();
            PowerTile powerTile = blockLocPowerTileMap.get(vec);
            powerTile.replaceGrid(grid, this);
        }
        return this;
    }

    public void onTick(){
        System.out.println("START");
        for (BlockLoc vec:locations)
            System.out.println(vec.toString());
        System.out.println("STOP");
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EFluxCableGrid && ((EFluxCableGrid)obj).identifier.equals(identifier);
    }
}
