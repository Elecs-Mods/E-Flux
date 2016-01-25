package elec332.eflux.api.energy;

import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Elec332 on 16-5-2015.
 */
public class EnergyAPIHelper {

    public static void postLoadEvent(TileEntity tile){
        postLoadEvent(tile.getWorld(), tile.getPos(), tile);
    }

    public static void postUnloadEvent(TileEntity tile){
        postUnloadEvent(tile.getWorld(), tile.getPos(), tile);
    }

    public static void postLoadEvent(World world, BlockPos pos, Object tile){
        if (tile instanceof IEnergyTile)
            MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(world, pos, tile));
    }

    public static void postUnloadEvent(World world, BlockPos pos, Object tile){
        if (tile instanceof IEnergyTile)
            MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(world, pos, tile));
    }

    public static boolean isEnergyTile(Object tile){
        return tile instanceof IEnergyTile;
    }

    public static void checkValidity(Object tile){
        if (!isEnergyTile(tile))
            throw new IllegalArgumentException("TileEntity isn't instanceof IEnergyTile");
    }
}
