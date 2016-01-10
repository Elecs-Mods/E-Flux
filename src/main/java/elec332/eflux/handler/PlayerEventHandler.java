package elec332.eflux.handler;

import elec332.core.world.WorldHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.tileentity.energy.machine.chunkLoader.ChunkLoaderSubTile;
import elec332.eflux.tileentity.energy.machine.chunkLoader.MainChunkLoaderTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by Elec332 on 25-5-2015.
 */
public class PlayerEventHandler {
/*
    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent event){
        if (event.placedBlock.getBlock() instanceof BlockMachine && event.player != null){
            //Class<?> clazz = ((BlockMachine)event.placedBlock).getMachine().getTileClass();
            TileEntity te = WorldHelper.getTileAt(event.world, event.pos);
            //boolean hasMain = ChunkLoaderPlayerProperties.get(PlayerHelper.getPlayerUUID(event.player)).hasHandler();
            /*if (clazz.isAssignableFrom(MainChunkLoaderTile.class) && hasMain || MainChunkLoaderTile.class.isAssignableFrom(clazz) && hasMain){
                event.setCanceled(true);
            }
            System.out.println(te);
            if (te instanceof MainChunkLoaderTile && !((MainChunkLoaderTile) te).canPlace(event.player)){
                event.setCanceled(true);
            } else if (te instanceof ChunkLoaderSubTile && !((ChunkLoaderSubTile) te).canPlace(event.player)){
                event.setCanceled(true);
            }
        }
    }*/

    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent event){
        if (WorldHelper.getTileAt(event.world, event.pos) instanceof MainChunkLoaderTile && !((MainChunkLoaderTile)WorldHelper.getTileAt(event.world, event.pos)).isOwner(event.getPlayer())){
            event.setCanceled(true);
        }
    }

}
