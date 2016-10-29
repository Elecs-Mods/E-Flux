package elec332.eflux.handler;

import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.network.PacketPlayerConnection;
import elec332.eflux.tileentity.energy.machine.chunkLoader.TileEntityMainChunkLoader;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by Elec332 on 25-5-2015.
 */
public class PlayerEventHandler {

    @SubscribeEvent
    public void onPlayerConnected(PlayerEvent.PlayerLoggedInEvent event){
        if (event.player instanceof EntityPlayerMP){
            EFlux.logger.info("Sending connection packet...");
            EFlux.networkHandler.sendTo(new PacketPlayerConnection((EntityPlayerMP) event.player), (EntityPlayerMP) event.player);
        }
    }

/*
    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent event){
        if (event.placedBlock.getBlock() instanceof BlockMachine && event.player != null){
            //Class<?> clazz = ((BlockMachine)event.placedBlock).getMachine().getTileClass();
            TileEntity te = WorldHelper.getTileAt(event.world, event.pos);
            //boolean hasMain = ChunkLoaderPlayerProperties.get(PlayerHelper.getPlayerUUID(event.player)).hasHandler();
            /*if (clazz.isAssignableFrom(TileEntityMainChunkLoader.class) && hasMain || TileEntityMainChunkLoader.class.isAssignableFrom(clazz) && hasMain){
                event.setCanceled(true);
            }
            System.out.println(te);
            if (te instanceof TileEntityMainChunkLoader && !((TileEntityMainChunkLoader) te).canPlace(event.player)){
                event.setCanceled(true);
            } else if (te instanceof TileEntitySubChunkLoader && !((TileEntitySubChunkLoader) te).canPlace(event.player)){
                event.setCanceled(true);
            }
        }
    }*/

    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent event){
        if (WorldHelper.getTileAt(event.getWorld(), event.getPos()) instanceof TileEntityMainChunkLoader && !((TileEntityMainChunkLoader)WorldHelper.getTileAt(event.getWorld(), event.getPos())).isOwner(event.getPlayer())){
            event.setCanceled(true);
        }
    }

}
