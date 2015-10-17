package elec332.eflux.grid.power;

import elec332.core.util.BlockLoc;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.api.energy.ISpecialEnergySource;
import elec332.eflux.grid.WorldRegistry;
import elec332.eflux.tileentity.energy.cable.AbstractCable;
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
        acceptors = new ArrayList<GridData>();
        providers = new ArrayList<GridData>();
        locations = new ArrayList<BlockLoc>();
        specialProviders = new ArrayList<GridData>();
        identifier = UUID.randomUUID();
        this.world = world;
        locations.add(p.getLocation());
        if (p.getTile() instanceof IEnergySource && ((IEnergySource) p.getTile()).canProvidePowerTo(direction)) {
            if (!(p.getTile() instanceof ISpecialEnergySource))
                providers.add(new GridData(p.getLocation(), direction));
            else specialProviders.add(new GridData(p.getLocation(), direction));
        }
        if (p.getTile() instanceof IEnergyReceiver && ((IEnergyReceiver) p.getTile()).canAcceptEnergyFrom(direction))
            acceptors.add(new GridData(p.getLocation(), direction));
        if (p.getTile() instanceof IEnergyTransmitter) {
            maxTransfer = ((IEnergyTransmitter) p.getTile()).getMaxRPTransfer();
            if (p.getTile() instanceof AbstractCable)
                ((AbstractCable) p.getTile()).setGridIdentifier(identifier);
        }
        else maxTransfer = -1;
    }

    private UUID identifier;
    private World world;
    private List<GridData> acceptors;
    private List<GridData> providers;
    private List<GridData> specialProviders;
    private List<BlockLoc> locations;
    private int maxTransfer;

    public List<BlockLoc> getLocations(){
        return locations;
    }

    public EFluxCableGrid mergeGrids(EFluxCableGrid grid){
        if (this.world.provider.dimensionId != grid.world.provider.dimensionId)
            throw new RuntimeException();
        if (this.equals(grid))
            return this;
        WorldRegistry.get(grid.world).getWorldPowerGrid().removeGrid(grid);
        //if (grid.maxTransfer > -1)
        this.maxTransfer = Math.max(maxTransfer, grid.maxTransfer);
        this.locations.addAll(grid.locations);
        this.acceptors.addAll(grid.acceptors);
        this.providers.addAll(grid.providers);
        this.specialProviders.addAll(grid.specialProviders);
        for (BlockLoc vec : grid.locations){
            PowerTile powerTile = getWorldHolder().getPowerTile(vec);
            if (powerTile != null) {
                powerTile.replaceGrid(grid, this);
            } else {
                throw new RuntimeException();
            }
        }
        grid.invalidate();
        EFlux.systemPrintDebug("MERGED");
        return this;
    }

    public void onTick(){
        EFlux.systemPrintDebug("START");
        for (BlockLoc vec:locations)
            EFlux.systemPrintDebug(vec.toString());
        EFlux.systemPrintDebug("Locations: " + locations.size());
        EFlux.systemPrintDebug("Acceptors: " + acceptors.size());
        EFlux.systemPrintDebug("Providers " + (providers.size() + specialProviders.size()));
        EFlux.systemPrintDebug("MaxTransfer: " + maxTransfer);
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
        for (GridData gridData : acceptors)
            rp = Math.max(rp, ((IEnergyReceiver) getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).requestedRP(gridData.getDirection()));
        for (GridData gridData : providers)
            totalProvided = totalProvided + ((IEnergySource) getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).provideEnergy(rp, gridData.getDirection(), true);
        for (GridData gridData : specialProviders) {
            int s = ((ISpecialEnergySource) getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).provideEnergy(rp, gridData.getDirection(), false);
            vs[specialProviders.indexOf(gridData)] = s;
            specialCanProvide = specialCanProvide + s;
        }
        for (GridData gridData : acceptors) {
            int e = ((IEnergyReceiver) getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).getRequestedEF(rp, gridData.getDirection());
            va[acceptors.indexOf(gridData)] = e;
            requestedPower = requestedPower + e;
        }
        if (maxTransfer > -1)
            rp = Math.min(rp, maxTransfer);
        if (totalProvided >= requestedPower){
            for (GridData gridData : acceptors)
                ((IEnergyReceiver) getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).receivePower(gridData.getDirection(), rp, va[acceptors.indexOf(gridData)]);
        }else if (totalProvided > 0 || specialCanProvide > 0){
            int needed = requestedPower-totalProvided;
            if (specialCanProvide > needed){
                float d = needed/(float)specialCanProvide;
                for (GridData gridData : specialProviders)
                    totalProvided = totalProvided+((ISpecialEnergySource) getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).provideEnergeticFlux(rp, gridData.getDirection(), (int)(vs[specialProviders.indexOf(gridData)] * d));
            } else {
                for (GridData gridData : specialProviders)
                    totalProvided = totalProvided+((ISpecialEnergySource) getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).provideEnergeticFlux(rp, gridData.getDirection(), vs[specialProviders.indexOf(gridData)]);
            }
            if (totalProvided > requestedPower)
                totalProvided = requestedPower;
            float diff = (float)totalProvided/(float)requestedPower;
            for (GridData gridData : acceptors)
                ((IEnergyReceiver) getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).receivePower(gridData.getDirection(), rp, (int)(va[acceptors.indexOf(gridData)] * diff));
        }
        /*for (GridData gridData: acceptors) {
            int i = ((IEnergyReceiver) getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).requestedRP(gridData.getDirection());
            print("Tile at "+gridData.getLoc().toString()+" requests "+i+" rp from side "+gridData.getDirection());
            rp = Math.max(rp, i);
        }
        for (GridData gridData : acceptors){
            int i = requestedPower;
            int e = ((IEnergyReceiver)getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).getRequestedEF(rp, gridData.getDirection());
            requestedPower = i+e;
            print("Tile at "+gridData.getLoc().toString()+" requests "+i+" ef from side "+gridData.getDirection()+" from an RP of" + rp);

        }
        for (GridData gridData : providers){
            int i = totalProvided;
            int e = ((IEnergySource)getWorldHolder().getPowerTile(gridData.getLoc()).getTile()).getMaxEFForRP(rp, gridData.getDirection());
            totalProvided = i+e;
            print("Tile at "+gridData.getLoc().toString()+" can provide "+i+" ef from side "+gridData.getDirection()+" from an RP of" + rp);
        }
        print("RP in network: "+rp);
        print("Requested network: "+requestedPower);
        print("Able to provide: "+totalProvided);
        if (totalProvided > requestedPower){
            int totalDrawn = 0;
            for (GridData gridData : acceptors){
                IEnergyReceiver receiver = (IEnergyReceiver) getWorldHolder().getPowerTile(gridData.getLoc()).getTile();
                int i = receiver.getRequestedEF(rp, gridData.getDirection());
                int q = totalDrawn;
                receiver.receivePower(gridData.getDirection(), rp, i);
                totalDrawn = i+q;
            }
            for (GridData gridData : providers){
                IEnergySource source = (IEnergySource) getWorldHolder().getPowerTile(gridData.getLoc()).getTile();
                int q = totalDrawn;
                if (totalDrawn > 0)
                    totalDrawn = q-source.provideEnergeticFlux(rp, gridData.getDirection(), totalDrawn);
            }
        } else {
            System.out.println("More power requested than able to provide!");
            int total_goingToProvide = 0;
            for (GridData gridData : providers){
                IEnergySource source = (IEnergySource) getWorldHolder().getPowerTile(gridData.getLoc()).getTile();
                int q = total_goingToProvide;
                total_goingToProvide = q+source.provideEnergeticFlux(rp, gridData.getDirection(), source.getMaxEFForRP(rp, gridData.getDirection()));
            }
            print("Network is able to provide "+total_goingToProvide+" EF at "+rp+" RP");
            if (total_goingToProvide > 0) {
                int tg = acceptors.size();
                for (GridData gridData : acceptors) {
                    int gt = total_goingToProvide / tg;
                    IEnergyReceiver receiver = (IEnergyReceiver) getWorldHolder().getPowerTile(gridData.getLoc()).getTile();
                    print("Tile at "+gridData.getLoc().toString()+" will receive "+gt+" ef from side "+gridData.getDirection()+" from an RP of" + rp);
                    int nu = receiver.receivePower(gridData.getDirection(), rp, gt);
                    gt = gt - nu;
                    total_goingToProvide = total_goingToProvide - gt;
                    tg--;
                }
                this.unUsed = total_goingToProvide;
                System.out.println( unUsed+" Left over power");
            }
        }*/
    }

    /*private void print(String s){
        EFlux.logger.info(s);
    }*/


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
        public GridData(BlockLoc blockLoc, ForgeDirection direction){
            this.loc = blockLoc;
            this.direction = direction;
        }

        private BlockLoc loc;
        private ForgeDirection direction;

        public BlockLoc getLoc() {
            return loc;
        }

        public ForgeDirection getDirection() {
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
