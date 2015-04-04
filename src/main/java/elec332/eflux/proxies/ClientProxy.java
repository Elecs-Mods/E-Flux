package elec332.eflux.proxies;

import elec332.eflux.client.inventory.GuiInventoryGrinder;
import elec332.eflux.tileentity.TEGrinder;
import elec332.eflux.util.GuiID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ClientProxy extends CommonProxy{

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (GuiID.values()[ID]){
            case GRINDER:
                return new GuiInventoryGrinder((TEGrinder)world.getTileEntity(x, y, z), player);
            default:
                return null;
        }
    }
}
