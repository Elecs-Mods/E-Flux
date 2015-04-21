package elec332.eflux.test;
/*
import elec332.core.baseclasses.tileentity.TileBase;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.event.PowerTransmitterEvent;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.transmitter.IPowerTransmitter;
import elec332.eflux.util.TransmitterNetwork;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elec332 on 15-4-2015.
 *//*
public class TileCable extends TileBase implements IEnergyReceiver, IPowerTransmitter{

    private EFluxCableGrid cableGrid;

    public void onPlacement(){
        //MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
        List<TileCable> cables = new ArrayList<TileCable>();
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS){
            TileEntity tile = worldObj.getTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
            if (tile instanceof TileCable){
                cables.add((TileCable) tile);
            }
        }
        if (cables.size() == 0)
            cableGrid = new EFluxCableGrid();
        else if (cables.size() == 1)
            cables.get(0).getCableGrid().addCableToGrid(this);
        else {
            this.mergeAllGrids(cables).addCableToGrid(this);
        }
    }

    //public EFluxCableGrid getCableGrid(){
    //    return cableGrid;
    //}

    public EFluxCableGrid setGrid(EFluxCableGrid grid){
        this.cableGrid = grid;
        return this.cableGrid;
    }

    private EFluxCableGrid mergeAllGrids(List<TileCable> cables){
        int i = cables.size();
        if (i == 2){
            cables.get(0).getCableGrid().mergeGrids(cables.get(1).getCableGrid());
        }
        //ToDo: 3,4,5,6
        
        return this.cableGrid;
    }

    @Override
    public int maxEFReceive() {
        return 0;
    }

    @Override
    public int maxRPReceive() {
        return 0;
    }

    @Override
    public boolean canReceiveEnergy(ForgeDirection side) {
        return true;
    }

    @Override
    public float receiveEnergy(float amount) {
        return 0;
    }

    @Override
    public TransmitterNetwork<?, ?> getNetwork() {
        return null;
    }

    @Override
    public TileEntity getTile() {
        return null;
    }

    @Override
    public boolean setNetwork(TransmitterNetwork<?, ?> network) {
        return false;
    }

    @Override
    public List<ForgeDirection> getConnections() {
        return null;
    }
}
*/