package elec332.eflux.tileentity;

import elec332.core.baseclasses.tileentity.BaseTileWithInventory;
import elec332.eflux.EFlux;
import elec332.eflux.util.IEFluxMachine;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Elec332 on 4-4-2015.
 */
public abstract class BaseMachineTEWithInventory extends BaseTileWithInventory implements IEFluxMachine {

    public BaseMachineTEWithInventory(int invSize) {
        super(invSize);

    }

    public abstract Object getGuiServer(EntityPlayer player);

    public abstract Object getGuiClient(EntityPlayer player);

    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return openGui(player);
    }

    public boolean openGui(EntityPlayer player){
        player.openGui(EFlux.instance, 0, worldObj, xCoord, yCoord, zCoord);
        return true;
    }
}
