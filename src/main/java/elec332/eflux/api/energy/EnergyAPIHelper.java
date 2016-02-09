package elec332.eflux.api.energy;

import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.event.PowerTransmitterEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * Created by Elec332 on 16-5-2015.
 */
public class EnergyAPIHelper {

    public static boolean canProvide(ICapabilityProvider provider, EnumFacing side){
        return isProvider(provider, side) || isTransmitter(provider, side);
    }

    public static boolean canReceive(ICapabilityProvider provider, EnumFacing side){
        return isReceiver(provider, side) || isTransmitter(provider, side);
    }

    public static boolean isReceiver(ICapabilityProvider provider, EnumFacing side){
        return provider.hasCapability(EFluxAPI.RECEIVER_CAPABILITY, side);
    }

    public static boolean isProvider(ICapabilityProvider provider, EnumFacing side){
        return provider.hasCapability(EFluxAPI.PROVIDER_CAPABILITY, side);
    }

    public static boolean isTransmitter(ICapabilityProvider provider, EnumFacing side){
        return provider.hasCapability(EFluxAPI.TRANSMITTER_CAPABILITY, side);
    }

    public static boolean canHandleEnergy(TileEntity tile, EnumFacing side){
        return isReceiver(tile, side) || isProvider(tile, side) || isTransmitter(tile, side);
    }

    public static void postLoadEvent(TileEntity tile){
        postIfValid(tile, new PowerTransmitterEvent.Load(tile));
    }

    public static void postUnloadEvent(TileEntity tile){
        postIfValid(tile, new PowerTransmitterEvent.UnLoad(tile));
    }

    private static void postIfValid(TileEntity tile, PowerTransmitterEvent event){
        if (isEnergyTile(tile)){
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    public static boolean isEnergyTile(TileEntity tile){
        for (EnumFacing facing : EnumFacing.VALUES){
            if (canHandleEnergy(tile, facing)){
                return true;
            }
        }
        return false;
    }

    public static void checkValidity(TileEntity tile){
        if (!isEnergyTile(tile))
            throw new IllegalArgumentException("TileEntity isn't instanceof IEnergyTile");
    }
}
