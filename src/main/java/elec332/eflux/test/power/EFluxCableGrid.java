package elec332.eflux.test.power;

import cpw.mods.fml.common.FMLCommonHandler;
import elec332.eflux.EFlux;
import elec332.eflux.api.transmitter.IEnergyReceiver;
import elec332.eflux.api.transmitter.IEnergySource;
import elec332.eflux.test.WorldRegistry;
import elec332.eflux.test.blockLoc.BlockLoc;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 15-4-2015.
 */
public class EFluxCableGrid {

    public EFluxCableGrid(World world, PowerTile p, ForgeDirection direction){
        acceptors = new ArrayList<BlockLoc>();
        providers = new ArrayList<BlockLoc>();
        locations = new ArrayList<BlockLoc>();
        this.world = world;
        locations.add(p.getLocation());
        if (p.getTile() instanceof IEnergySource && ((IEnergySource) p.getTile()).canProvidePowerTo(direction))
            providers.add(p.getLocation());
        if (p.getTile() instanceof IEnergyReceiver && ((IEnergyReceiver) p.getTile()).canAcceptEnergyFrom(direction))
            acceptors.add(p.getLocation());
        FMLCommonHandler.instance().bus().register(this);
        identifier = UUID.randomUUID();
    }

    private UUID identifier;
    private World world;
    private List<BlockLoc> acceptors;
    private List<BlockLoc> providers;
    private List<BlockLoc> locations;

    public List<BlockLoc> getLocations(){
        return locations;
    }

    public EFluxCableGrid mergeGrids(EFluxCableGrid grid){
        if (this.world.provider.dimensionId != grid.world.provider.dimensionId)
            throw new RuntimeException();
        if (this.equals(grid))
            return this;
        WorldRegistry.get(grid.world).getWorldPowerGrid().removeGrid(grid);
        this.locations.addAll(grid.locations);
        this.acceptors.addAll(grid.acceptors);
        this.providers.addAll(grid.providers);
        for (BlockLoc vec : grid.locations){
            PowerTile powerTile = WorldRegistry.get(world).getWorldPowerGrid().getPowerTile(vec);
            if (powerTile != null)
                powerTile.replaceGrid(grid, this);
        }
        grid.invalidate();
        EFlux.logger.info("MERGED");
        return this;
    }

    public void onTick(){
        System.out.println("START");
        for (BlockLoc vec:locations)
            System.out.println(vec.toString());
        System.out.println("Locations: "+locations.size());
        System.out.println("Acceptors: "+acceptors.size());
        System.out.println("Providers "+providers.size());
        System.out.println("STOP");
    }

    private void invalidate(){
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
