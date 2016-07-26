package elec332.eflux.energy.grid;

import com.google.common.collect.*;
import elec332.core.world.DimensionCoordinate;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.energy.IEFluxEnergyObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Elec332 on 23-7-2016.
 */
public class EFluxEnergyObject implements IEFluxEnergyObject {

    protected EFluxEnergyObject(TileEntity tile){
        this.tile = tile;
        this.coord = DimensionCoordinate.fromTileEntity(tile);
        this.capData = newMap();
        this.grids = new EnergyGrid[SIDES];
        this.endPoint = -1;
        this.endPoints = false;
        this.uniqueGridsImmutable = ImmutableSet.of();
    }

    private final TileEntity tile;
    private final DimensionCoordinate coord;
    private Map<Capability, EnumSet<EnumFacing>> capData;
    private EnumSet<EnumFacing> connectors;
    private boolean endPoints;

    private Set<EnergyGrid> uniqueGridsImmutable;
    private EnergyGrid[] grids;
    private int endPoint;

    protected boolean checkCapabilities(){
        boolean ret = false;
        Map<Capability, EnumSet<EnumFacing>> newMap = newMap();
        for (Capability cap : ENERGY_CAPABILITIES){
            EnumSet<EnumFacing> stored = capData.get(cap), now = newMap.get(cap);
            check(cap, now);

            if (!ret && !stored.equals(now)){
                ret = true;
            }
        }
        this.connectors = newMap.get(EFluxAPI.TRANSMITTER_CAPABILITY);
        //this.endPoints = connectors.size() > 1;
        this.capData.clear();
        this.capData = newMap;
        return ret;
    }

    private void check(Capability cap, EnumSet<EnumFacing> set){
        //TileEntity tile = this.tile.get();
        set.clear();
        if (tile == null){
            return;
        }
        for (EnumFacing facing : EnumFacing.VALUES){
            if (tile.hasCapability(cap, facing)){
                set.add(facing);
            }
        }
    }

    protected void removedFromGrid(EnergyGrid grid){
        for (int i = 0; i < grids.length; i++) {
            EnergyGrid grid1 = grids[i];
            if (grid.equals(grid1)){
                grids[i] = null;
            }
        }
        checkGridStuff();
    }

    protected void setGridForFace(EnergyGrid grid, EnumFacing facing){
        if (connectors.contains(facing)) {
            for (EnumFacing facing1 : connectors){
                grids[facing1.ordinal()] = grid;
            }
        } else {
            grids[facing.ordinal()] = grid;
        }
        checkGridStuff();
    }

    private void checkGridStuff(){
        Set<EnergyGrid> g = Sets.newHashSet();
        uniqueGridsImmutable = Collections.unmodifiableSet(g);
        int i = 0, h = 0;
        boolean d = false;
        for (int j = 0; j < grids.length; j++) {
            EnergyGrid grid = grids[j];
            if (grid != null){
                if (g.add(grid)){
                    i++;
                    h = j;
                } else {
                    d = true;
                }
            }
        }
        if (d){
            endPoint = -1;
            endPoints = false;
        } else {
            if (i == 1){
                endPoint = h;
                endPoints = true;
            } else {
                endPoint = -1;
                endPoints = i > 1;
            }
        }
    }

    protected EnergyGrid getGrid(EnumFacing side){
        return grids[side.ordinal()];
    }

    @Nullable
    @Override
    public TileEntity getTileEntity() {
        return tile;//coord.isLoaded() ? tile.get() : null;
    }

    @Override
    @Nonnull
    public DimensionCoordinate getPosition(){
        return coord;
    }

    protected boolean multipleEndpoints(){
        return endPoints;
    }

    protected Set<EnergyGrid> getGrids(){
        return uniqueGridsImmutable;
    }

    protected EnergyGrid getEndPoint(){
        if (!isEndPoint()){
            throw new RuntimeException();
        }
        return grids[endPoint];
    }

    protected boolean isEndPoint(){
        return endPoint != -1;
    }

    protected boolean validTile(){
        return coord.isLoaded();//tile.get() != null;
    }

    protected void clearTile(){
        //tile.clear();
    }

    private Map<Capability, EnumSet<EnumFacing>> newMap(){
        Map<Capability, EnumSet<EnumFacing>> ret = Maps.newHashMap();
        initMap(ret);
        return ret;
    }

    private void initMap(Map<Capability, EnumSet<EnumFacing>> map){
        for (Capability cap : ENERGY_CAPABILITIES){
            map.put(cap, EnumSet.noneOf(EnumFacing.class));
        }
    }

    private static final Capability[] ENERGY_CAPABILITIES;
    private static final int SIDES;

    static {
        SIDES = EnumFacing.VALUES.length;
        ENERGY_CAPABILITIES = new Capability[4];
        ENERGY_CAPABILITIES[0] = EFluxAPI.RECEIVER_CAPABILITY;
        ENERGY_CAPABILITIES[1] = EFluxAPI.PROVIDER_CAPABILITY;
        ENERGY_CAPABILITIES[2] = EFluxAPI.TRANSMITTER_CAPABILITY;
        ENERGY_CAPABILITIES[3] = EFluxAPI.MONITOR_CAPABILITY;
    }

    //Capability tile link-through

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        EnumSet<EnumFacing> set = capData.get(capability); //Cached data first
        if (set != null){
            return set.contains(facing);
        }
        if (!coord.isLoaded()){
            return false;
        }
        //TileEntity tile = this.tile.get();
        return tile != null && tile.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (!coord.isLoaded()){
            return null;
        }
        //TileEntity tile = this.tile.get();
        if (tile == null){
            return null;
        }
        return tile.getCapability(capability, facing);
    }

}
