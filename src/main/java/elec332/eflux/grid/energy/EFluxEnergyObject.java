package elec332.eflux.grid.energy;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import elec332.core.grid.v2.DefaultTileEntityLink;
import elec332.eflux.api.EFluxAPI;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Elec332 on 23-7-2016.
 */
public class EFluxEnergyObject extends DefaultTileEntityLink implements IEnergyGridInformation {

    protected EFluxEnergyObject(TileEntity tile){
        super(tile);
        this.capData = newMap();
        this.grids = new EFluxEnergyGrid[SIDES];
        this.endPoint = -1;
        this.endPoints = false;
        this.uniqueGridsImmutable = ImmutableSet.of();
    }

    private Map<Capability, EnumSet<EnumFacing>> capData;
    private Set<EnumFacing> connectors;
    private boolean endPoints;

    private Set<EFluxEnergyGrid> uniqueGridsImmutable;
    private EFluxEnergyGrid[] grids;
    private int uniqueGrids;
    private int endPoint;

    @Override
    public boolean hasChanged() {
        boolean ret = false;
        Map<Capability, EnumSet<EnumFacing>> newMap = newMap();
        for (Capability cap : ENERGY_CAPABILITIES){
            EnumSet<EnumFacing> stored = capData.get(cap), now = newMap.get(cap);
            check(cap, now);

            if (!ret && !stored.equals(now)){
                ret = true;
            }
        }
        setConnectors(newMap.get(EFluxAPI.TRANSMITTER_CAPABILITY));
        //this.endPoints = connectors.size() > 1;
        this.capData.clear();
        this.capData = newMap;
        return ret;
    }

    @Override
    public int getActiveConnections() {
        return uniqueGrids;
    }

    @Nullable
    @Override
    public Class getInformationType() {
        return IEnergyGridInformation.class;
    }

    @Nullable
    @Override
    public Object getInformation() {
        return this;
    }

    private void setConnectors(EnumSet<EnumFacing> s){
        connectors = Collections.unmodifiableSet(s);
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

    protected void removedFromGrid(EFluxEnergyGrid grid){
        for (int i = 0; i < grids.length; i++) {
            EFluxEnergyGrid grid1 = grids[i];
            if (grid.equals(grid1)){
                grids[i] = null;
            }
        }
        checkGridStuff();
    }

    protected void setGridForFace(EFluxEnergyGrid grid, EnumFacing facing){
        if (facing == null || connectors.contains(facing)){
            for (EnumFacing facing1 : connectors){
                grids[facing1.ordinal()] = grid;
            }
        } else {
            grids[facing.ordinal()] = grid;
        }
        checkGridStuff();
    }

    protected Set<EnumFacing> getConnectorSides(){
        return connectors;
    }

    private void checkGridStuff(){
        Set<EFluxEnergyGrid> g = Sets.newHashSet();
        uniqueGridsImmutable = Collections.unmodifiableSet(g);
        int i = 0, h = 0;
        boolean d = false;
        for (int j = 0; j < grids.length; j++) {
            EFluxEnergyGrid grid = grids[j];
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
        uniqueGrids = uniqueGridsImmutable.size();
    }

    protected EFluxEnergyGrid getGrid(EnumFacing side){
        return grids[side.ordinal()];
    }

    protected boolean multipleEndpoints(){
        return endPoints;
    }

    protected Set<EFluxEnergyGrid> getGrids(){
        return uniqueGridsImmutable;
    }

    protected EFluxEnergyGrid getEndPoint(){
        if (!isEndPoint()){
            throw new RuntimeException();
        }
        return grids[endPoint];
    }

    protected boolean isEndPoint(){
        return endPoint != -1;
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

    @Override
    protected boolean hasCachedCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        EnumSet<EnumFacing> set = capData.get(capability);
        if (set != null) {
            return set.contains(facing);
        }
        return super.hasCachedCapability(capability, facing);
    }

}
