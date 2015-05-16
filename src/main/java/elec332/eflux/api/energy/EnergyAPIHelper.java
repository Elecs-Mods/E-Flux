package elec332.eflux.api.energy;

import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Elec332 on 16-5-2015.
 */
public class EnergyAPIHelper {

    public static void postLoadEvent(TileEntity tile){
        if (tile instanceof IEnergyTile)
            MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(tile));
    }

    public static void postUnloadEvent(TileEntity tile){
        if (tile instanceof IEnergyTile)
            MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(tile));
    }

    public static boolean isEnergyTile(TileEntity tile){
        return tile instanceof IEnergyTile;
    }

    public static void checkValidity(TileEntity tile){
        if (!isEnergyTile(tile))
            throw new IllegalArgumentException("TileEntity isn't instanceof IEnergyTile");
    }
}
