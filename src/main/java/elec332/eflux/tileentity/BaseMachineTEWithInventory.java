package elec332.eflux.tileentity;

import elec332.core.baseclasses.tileentity.BaseTileWithInventory;
import elec332.eflux.EFlux;
import elec332.eflux.util.IEFluxMachine;
import elec332.core.baseclasses.tileentity.IInventoryTile;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Elec332 on 4-4-2015.
 */
@Deprecated
public abstract class BaseMachineTEWithInventory extends BaseTileWithInventory implements IEFluxMachine, IInventoryTile {

    public BaseMachineTEWithInventory(int invSize) {
        super(invSize);

    }

    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return openGui(player);
    }

    public boolean openGui(EntityPlayer player){
        player.openGui(EFlux.instance, 0, worldObj, xCoord, yCoord, zCoord);
        return true;
    }
}
