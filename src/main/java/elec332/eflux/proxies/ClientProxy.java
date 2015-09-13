package elec332.eflux.proxies;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import elec332.core.baseclasses.tileentity.IInventoryTile;
import elec332.core.util.EventHelper;
import elec332.eflux.client.render.InsideItemRenderer;
import elec332.eflux.init.FluidRegister;
import elec332.eflux.tileentity.BreakableMachineTile;
import elec332.eflux.tileentity.multiblock.TileEntityInsideItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ClientProxy extends CommonProxy{

    public ClientProxy(){
        EventHelper.registerHandlerForge(this);
    }

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

    @Override
    public void initRenderStuff(){
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInsideItemRenderer.class, new InsideItemRenderer());
    }

    @SubscribeEvent
    public void registerBlockTextures(TextureStitchEvent.Post event){
        if (event.map.getTextureType() == 0){
            FluidRegister.instance.registerTextures(event.map);
        }
    }
}
