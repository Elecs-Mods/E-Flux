package elec332.eflux.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import elec332.core.player.PlayerHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.tileentity.energy.machine.chunkLoader.ChunkLoaderSubTile;
import elec332.eflux.tileentity.energy.machine.chunkLoader.MainChunkLoaderTile;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Elec332 on 25-5-2015.
 */
public class PlayerEventHandler {

    /*@SubscribeEvent
    public void onPlayerConstructing(EntityEvent.EntityConstructing event){
        if (event.entity instanceof EntityPlayerMP || event.entity instanceof EntityClientPlayerMP)
            event.entity.registerExtendedProperties("EFluxChunks", new ChunkLoaderPlayerProperties());
    }*/

    @SubscribeEvent
    public void we(WorldEvent.Unload event){
        System.out.println("Unload: "+ WorldHelper.getDimID(event.world));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockPlaced(BlockEvent.PlaceEvent event){
        if (event.placedBlock instanceof BlockMachine && event.player != null){
            Class<?> clazz = ((BlockMachine)event.placedBlock).getMachine().getTileClass();
            boolean hasMain = ChunkLoaderPlayerProperties.get(PlayerHelper.getPlayerUUID(event.player)).hasHandler();

            //((().isAssignableFrom(ChunkLoaderSubTile.class) && ChunkLoaderPlayerProperties.get(event.player).getMain()==null) ||
            //    (((BlockMachine)event.placedBlock).getMachine().getTileClass().isAssignableFrom(MainChunkLoaderTile.class)&& ChunkLoaderPlayerProperties.get(event.player).hasHandler())) || event.player == null)
            if (ChunkLoaderSubTile.class.isAssignableFrom(clazz) && !hasMain || MainChunkLoaderTile.class.isAssignableFrom(clazz) && hasMain){
                event.setCanceled(true);
            } else if (clazz.isAssignableFrom(ChunkLoaderSubTile.class) && !hasMain || clazz.isAssignableFrom(MainChunkLoaderTile.class) && hasMain){
                event.setCanceled(true);
            }

            //event.world.setBlockToAir(event.x, event.y, event.z);
        }
    }

    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent event){
        if (event.world.getTileEntity(event.x, event.y, event.z) instanceof MainChunkLoaderTile && event.getPlayer() != null && !((MainChunkLoaderTile)event.world.getTileEntity(event.x, event.y, event.z)).isOwner(event.getPlayer())){
            event.setCanceled(true);
        }
    }
}
