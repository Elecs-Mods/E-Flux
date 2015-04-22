package elec332.eflux.test;

import cofh.api.energy.IEnergyReceiver;
import elec332.eflux.api.transmitter.IEnergySource;
import elec332.eflux.api.transmitter.IEnergyTile;
import elec332.eflux.api.transmitter.IPowerTransmitter;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elec332 on 15-4-2015.
 */
public class EFluxCableGrid {

    public EFluxCableGrid(World world){
        //cables = new ArrayList<TileCable>();
        //providers = new ArrayList<IEnergyProvider>();
        acceptors = new ArrayList<Vec3>();
        providers = new ArrayList<Vec3>();
        locations = new ArrayList<Vec3>();
        this.world = world;
    }
    private World world;
    //private List<TileCable> cables;
    //private List<IEnergyProvider> providers;
    private List<Vec3> acceptors;
    private List<Vec3> providers;
    private List<Vec3> locations;

    public boolean addToGrid(IEnergyTile tile, Vec3 loc){
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

    public boolean canAddReceiver(Vec3 vec){
        return providers.contains(vec);
    }

    public boolean canAddProvider(Vec3 vec){
        return acceptors.contains(vec);
    }


    //workaround
    public boolean hasTile(Vec3 vec){
        //WorldRegistryPowerNetwork.get(world).sm(""+locations.contains(vec));
        //return locations.contains(vec);
        for (Vec3 vec0: locations){
            if (vec0.toString().equalsIgnoreCase(vec.toString())) {
                WorldRegistryPowerNetwork.get(world).sm("ttrr");
                return true;
            }
        }
        WorldRegistryPowerNetwork.get(world).sm("ffff");
        return false;
    }

    public List<Vec3> getLocations(){
        return locations;
    }


    public EFluxCableGrid mergeGrids(EFluxCableGrid grid){
        if (this.world.provider.dimensionId != grid.world.provider.dimensionId)
            throw new RuntimeException();
        WorldRegistryPowerNetwork.get(grid.world).removeGrid(grid);
        this.locations.addAll(grid.locations);
        this.acceptors.addAll(grid.acceptors);
        this.providers.addAll(grid.providers);
        return this;
    }

    public void onTick(){
        System.out.println("START");
        for (Vec3 vec:locations)
            System.out.println(vec.toString());
        System.out.println("STOP");
    }
}
