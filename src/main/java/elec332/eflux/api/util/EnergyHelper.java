package elec332.eflux.api.util;
/*
import elec332.eflux.api.energy.IEnergyProvider;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.util.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elec332 on 15-4-2015.
 *//*
public class EnergyHelper {

    public static void emit(IEnergyProvider provider){
        int toEmitTotal = Math.min(provider.getEnergy(), provider.getMaxOutput());
        if (toEmitTotal > 0){
            List<ForgeDirection> outputSides = getValidOutputs(provider);
            if (outputSides.size() > 0){
                float toSent = (float)toEmitTotal/outputSides.size();
                for (ForgeDirection direction : outputSides){
                    tryToEmitToSide(direction, toSent, provider);
                }
            }
        }
    }

    private static void tryToEmitToSide(ForgeDirection direction, float baseSent, IEnergyProvider iEnergyProvider){
        TileEntity provider = ((TileEntity) iEnergyProvider);
        TileEntity possibleReceiver = provider.getWorldObj().getTileEntity(provider.xCoord+direction.offsetX, provider.yCoord+direction.offsetY, provider.zCoord+direction.offsetZ);
        if (possibleReceiver != null) {
            if (possibleReceiver instanceof IEnergyReceiver && ((IEnergyReceiver) possibleReceiver).canReceiveEnergy(direction.getOpposite())){
                int used = (int)(baseSent - ((IEnergyReceiver) possibleReceiver).receiveEnergy(baseSent));
                iEnergyProvider.setEnergy(iEnergyProvider.getEnergy()-used);
            } else if (Config.RFCompatibility && possibleReceiver instanceof cofh.api.energy.IEnergyReceiver){
                int used = (int) baseSent - ((cofh.api.energy.IEnergyReceiver) possibleReceiver).receiveEnergy(direction.getOpposite(), (int)baseSent, false);
                iEnergyProvider.setEnergy(iEnergyProvider.getEnergy()-used);
            }
        }
    }

    public static List<ForgeDirection> getValidOutputs(IEnergyProvider provider){
        List<ForgeDirection> possibleDirections = new ArrayList<ForgeDirection>();
        for (ForgeDirection forgeDirection : ForgeDirection.VALID_DIRECTIONS){
            if (provider.canProvideTo(forgeDirection))
                possibleDirections.add(forgeDirection);
        }
        return possibleDirections;
    }
}
*/