package elec332.eflux.proxies;

import elec332.core.world.WorldHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import elec332.core.tile.IInventoryTile;
import elec332.core.util.EventHelper;
import elec332.eflux.client.render.InsideItemRenderer;
import elec332.eflux.client.render.RenderHandler;
import elec332.eflux.tileentity.BreakableMachineTile;
import elec332.eflux.tileentity.multiblock.TileEntityInsideItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ClientProxy extends CommonProxy{

    public ClientProxy(){
        EventHelper.registerHandlerForge(this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = WorldHelper.getTileAt(world, new BlockPos(x, y, z));
        switch (ID){
            case 1:
                if (tile instanceof BreakableMachineTile)
                    return ((BreakableMachineTile) tile).getBreakableMachineInventory().brokenGui(Side.CLIENT, player);
            default:
                if (tile instanceof IInventoryTile)
                    return ((IInventoryTile) tile).getGuiClient(player);
                else return null;
        }
    }

    @Override
    public void initRenderStuff(){
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInsideItemRenderer.class, new InsideItemRenderer());
        RenderHandler.dummy();
    }

}
