package elec332.eflux.grid.power;

import com.google.common.collect.Lists;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.api.energy.ISpecialEnergySource;
import elec332.eflux.grid.WorldRegistry;
import elec332.eflux.multipart.cable.PartAbstractCable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 15-4-2015.
 */
public class EFluxCableGrid {

    public EFluxCableGrid(PowerTile p, EnumFacing direction){
        acceptors = Lists.newArrayList();
        providers = Lists.newArrayList();
        locations = Lists.newArrayList();
        specialProviders = Lists.newArrayList();
        monitors = Lists.newArrayList();
        identifier = UUID.randomUUID();
        this.world = p.getWorld();
        locations.add(p.getLocation());
        if (p.isProvider(direction)) {
            if (!(p.getProvider(direction) instanceof ISpecialEnergySource)) {
                providers.add(new GridData(p.getLocation(), direction));
            } else {
                specialProviders.add(new GridData(p.getLocation(), direction));
            }
        }
        if (p.isReceiver(direction)) {
            acceptors.add(new GridData(p.getLocation(), direction));
        }
        if (p.isMonitor(direction)){
            monitors.add(new GridData(p.getLocation(), direction));
        }
        if (p.isTransmitter(direction)) {
            IEnergyTransmitter transmitter = p.getTransmitter(direction);
            maxTransfer = transmitter.getMaxRPTransfer();
            if (transmitter instanceof PartAbstractCable) {
                ((PartAbstractCable) transmitter).setGridIdentifier(identifier);
            }
        } else {
            maxTransfer = -1;
        }
        getWorldHolder().fillSurroundings(locations);
    }

    private UUID identifier;
    private World world;
    private List<GridData> acceptors;
    private List<GridData> providers;
    private List<GridData> specialProviders;
    private List<GridData> monitors;
    private List<BlockPos> locations;
    private int maxTransfer;

    public List<BlockPos> getLocations(){
        return locations;
    }

    public EFluxCableGrid mergeGrids(EFluxCableGrid grid){
        if (WorldHelper.getDimID(world) != WorldHelper.getDimID(grid.world))
            throw new RuntimeException();
        if (this.equals(grid))
            return this;
        getWorldHolder().removeGrid(grid);
        this.maxTransfer = Math.max(maxTransfer, grid.maxTransfer);
        this.locations.addAll(grid.locations);
        this.acceptors.addAll(grid.acceptors);
        this.providers.addAll(grid.providers);
        this.specialProviders.addAll(grid.specialProviders);
        this.monitors.addAll(grid.monitors);
        for (BlockPos vec : grid.locations){
            PowerTile powerTile = getWorldHolder().getPowerTile(vec);
            if (powerTile != null) {
                powerTile.replaceGrid(grid, this);
            } else {
                throw new RuntimeException();
            }
        }
        grid.invalidate();
        getWorldHolder().fillSurroundings(locations);
        EFlux.systemPrintDebug("MERGED");
        return this;
    }

    public void onTick(){
        EFlux.systemPrintDebug("START");
        for (BlockPos vec : locations)
            EFlux.systemPrintDebug(vec.toString());
        EFlux.systemPrintDebug("Locations: " + locations.size());
        EFlux.systemPrintDebug("Acceptors: " + acceptors.size());
        EFlux.systemPrintDebug("Providers " + (providers.size() + specialProviders.size()));
        EFlux.systemPrintDebug("MaxTransfer: " + maxTransfer);
        EFlux.systemPrintDebug("UUID: " + identifier);
        EFlux.systemPrintDebug("STOP");
        processPower();
    }


    public void processPower() {
        int requestedPower = 0;
        int rp = 1;
        int totalProvided = 0;
        int specialCanProvide = 0;
        int[] va = new int[acceptors.size()];
        int[] vs = new int[specialProviders.size()];
        for (GridData gridData : acceptors) {
            rp = Math.max(rp, getPowerTile(gridData).getReceiver(gridData.getDirection()).requestedRP());
        }
        for (GridData gridData : providers) {
            totalProvided += getPowerTile(gridData).getProvider(gridData.getDirection()).provideEnergy(rp, true);
        }
        for (GridData gridData : specialProviders) {
            int s = getPowerTile(gridData).getProvider(gridData.getDirection()).provideEnergy(rp, false);
            vs[specialProviders.indexOf(gridData)] = s;
            specialCanProvide = specialCanProvide + s;
        }
        for (GridData gridData : acceptors) {
            int e = getPowerTile(gridData).getReceiver(gridData.getDirection()).getRequestedEF(rp);
            va[acceptors.indexOf(gridData)] = e;
            requestedPower = requestedPower + e;
        }
        if (maxTransfer > -1) {
            rp = Math.min(rp, maxTransfer);
        }
        if (totalProvided >= requestedPower){
            for (GridData gridData : acceptors) {
                getPowerTile(gridData).getReceiver(gridData.getDirection()).receivePower(rp, va[acceptors.indexOf(gridData)]);
            }
        } else if (totalProvided > 0 || specialCanProvide > 0){
            int needed = requestedPower-totalProvided;
            if (specialCanProvide > needed){
                float d = needed/(float)specialCanProvide;
                for (GridData gridData : specialProviders) {
                    totalProvided = totalProvided + ((ISpecialEnergySource) getPowerTile(gridData).getProvider(gridData.getDirection())).provideEnergeticFlux(rp, (int) (vs[specialProviders.indexOf(gridData)] * d));
                }
            } else {
                for (GridData gridData : specialProviders) {
                    totalProvided = totalProvided + ((ISpecialEnergySource) getPowerTile(gridData).getProvider(gridData.getDirection())).provideEnergeticFlux(rp, vs[specialProviders.indexOf(gridData)]);
                }
            }
            if (totalProvided > requestedPower) {
                totalProvided = requestedPower;
            }
            float diff = (float)totalProvided/(float)requestedPower;
            for (GridData gridData : acceptors) {
                getPowerTile(gridData).getReceiver(gridData.getDirection()).receivePower(rp, (int) (va[acceptors.indexOf(gridData)] * diff));
            }
        }
        for (GridData gridData : monitors){
            getPowerTile(gridData).getMonitor(gridData.getDirection()).onEnergyTick(rp, totalProvided);
        }
    }

    private PowerTile getPowerTile(GridData gridData){
        return getWorldHolder().getPowerTile(gridData.getLoc());
    }

    private WorldGridHolder getWorldHolder(){
        return WorldRegistry.get(world).getWorldPowerGrid();
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

    public class GridData {

        public GridData(BlockPos blockLoc, EnumFacing direction){
            this.loc = blockLoc;
            this.direction = direction;
        }

        private BlockPos loc;
        private EnumFacing direction;

        public BlockPos getLoc() {
            return loc;
        }

        public EnumFacing getDirection() {
            return direction;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof GridData && ((GridData) obj).loc.equals(loc) && ((GridData) obj).direction.equals(direction);
        }

    }

}
