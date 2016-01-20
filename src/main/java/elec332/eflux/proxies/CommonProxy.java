package elec332.eflux.proxies;

import elec332.core.tile.IInventoryTile;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.BreakableMachineTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class CommonProxy implements IGuiHandler{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = WorldHelper.getTileAt(world, new BlockPos(x, y, z));
        switch (ID){
            case 1:
                if (tile instanceof BreakableMachineTile)
                    return ((BreakableMachineTile) tile).getBreakableMachineInventory().brokenGui(Side.SERVER, player);
            default:
                if (tile instanceof IInventoryTile)
                    return ((IInventoryTile) tile).getGuiServer(player);
                else return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public void initRenderStuff(){
    }

}
