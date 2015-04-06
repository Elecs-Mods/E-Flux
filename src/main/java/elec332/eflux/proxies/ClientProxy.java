package elec332.eflux.proxies;

import elec332.eflux.tileentity.BaseMachineTEWithInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ClientProxy extends CommonProxy{

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) instanceof BaseMachineTEWithInventory)
            return ((BaseMachineTEWithInventory) world.getTileEntity(x, y, z)).getGuiClient(player);
        else return null;
    }
}
