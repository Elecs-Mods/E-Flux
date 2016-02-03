package elec332.eflux.proxies;

import elec332.core.tile.IInventoryTile;
import elec332.core.util.EventHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.client.FurnaceRenderTile;
import elec332.eflux.client.manual.gui.GuiManual;
import elec332.eflux.client.render.FurnaceContentsRenderer;
import elec332.eflux.client.render.RenderHandler;
import elec332.eflux.client.render.TileEntityLaserRenderer;
import elec332.eflux.tileentity.BreakableMachineTile;
import elec332.eflux.tileentity.basic.TileEntityLaser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;

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
            case 3:
                return new GuiManual();
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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaser.class, new TileEntityLaserRenderer());
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInsideItemRenderer.class, new InsideItemRenderer());
        RenderHandler.dummy();
        ClientRegistry.bindTileEntitySpecialRenderer(FurnaceRenderTile.class, new FurnaceContentsRenderer());
    }

}
