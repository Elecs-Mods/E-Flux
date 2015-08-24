package elec332.eflux.proxies;

import cpw.mods.fml.relauncher.Side;
import elec332.eflux.tileentity.BreakableMachineTile;
import elec332.core.baseclasses.tileentity.IInventoryTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ClientProxy extends CommonProxy{

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 1:
                if (world.getTileEntity(x, y, z) instanceof BreakableMachineTile)
                    return ((BreakableMachineTile) world.getTileEntity(x, y, z)).getBreakableMachineInventory().brokenGui(Side.CLIENT, player);
            default:
                if (world.getTileEntity(x, y, z) instanceof IInventoryTile)
                    return ((IInventoryTile) world.getTileEntity(x, y, z)).getGuiClient(player);
                else return null;
        }
    }
}
