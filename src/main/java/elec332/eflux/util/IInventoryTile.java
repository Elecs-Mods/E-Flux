package elec332.eflux.util;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Elec332 on 1-5-2015.
 */
public interface IInventoryTile {

    public Object getGuiServer(EntityPlayer player);


    public Object getGuiClient(EntityPlayer player);
}
