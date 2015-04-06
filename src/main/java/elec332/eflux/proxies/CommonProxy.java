package elec332.eflux.proxies;

import cpw.mods.fml.common.network.IGuiHandler;
import elec332.eflux.tileentity.BaseMachineTEWithInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 24-2-2015.
 */
public abstract class CommonProxy implements IGuiHandler{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) instanceof BaseMachineTEWithInventory)
            return ((BaseMachineTEWithInventory) world.getTileEntity(x, y, z)).getGuiServer(player);
        else return null;
    }
}
