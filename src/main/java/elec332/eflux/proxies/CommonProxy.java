package elec332.eflux.proxies;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import elec332.eflux.tileentity.BreakableMachineTile;
import elec332.eflux.util.IInventoryTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 24-2-2015.
 */
public abstract class CommonProxy implements IGuiHandler{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 1:
                if (world.getTileEntity(x, y, z) instanceof BreakableMachineTile)
                    return ((BreakableMachineTile) world.getTileEntity(x, y, z)).getBreakableMachineInventory().brokenGui(Side.SERVER, player);
            default:
                if (world.getTileEntity(x, y, z) instanceof IInventoryTile)
                    return ((IInventoryTile) world.getTileEntity(x, y, z)).getGuiServer(player);
                else return null;
        }
    }
}
