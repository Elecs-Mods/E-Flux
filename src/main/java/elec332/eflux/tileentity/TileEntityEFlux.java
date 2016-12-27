package elec332.eflux.tileentity;

import elec332.core.tile.TileBase;
import elec332.eflux.EFlux;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Elec332 on 9-10-2016.
 */
public class TileEntityEFlux extends TileBase {

    public boolean openLocalWindow(EntityPlayer player){
        return openWindow(player, EFlux.proxy, 0);
    }

}
