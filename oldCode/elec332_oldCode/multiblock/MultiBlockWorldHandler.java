package elec332_oldCode.multiblock;

import elec332.core.main.ElecCore;
import elec332.core.util.BlockLoc;
import elec332.core.util.IRunOnce;
import elec332.core.world.WorldHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Elec332 on 14-5-2015.
 */
public class MultiBlockWorldHandler {

    protected MultiBlockWorldHandler(World world){
        this.registeredControllers = new HashSet<MultiBlockControllerBase>();
        this.locations = new HashSet<BlockLoc>();
        this.tilesInUnloadedChunks = new HashMap<Long, Set<IMultiBlockPart>>();
        this.world = world;
    }

    private Set<MultiBlockControllerBase> registeredControllers;
    private Set<BlockLoc> locations;
    private Map<Long, Set<IMultiBlockPart>> tilesInUnloadedChunks;
    private World world;

    protected void onMBPLoaded(MultiBlockEvent.Loaded event){
        if (!isChunkLoaded(event.getTile())) {
            addUnloadedTile(chunkHashFromCoord(event.getTile()), (IMultiBlockPart) event.getTile());
            return;
        }
        if (event.getTile() instanceof IMultiBlockMainTile){
            if (!locations.contains(new BlockLoc(event.getTile()))) {
                MultiBlockControllerBase controller = ((IMultiBlockMainTile) event.getTile()).newController();
                ((IMultiBlockMainTile)event.getTile()).setController(controller);
                registeredControllers.add(controller);
                print("Registered new tileHandler");
                locations.add(new BlockLoc(event.getTile()));
            }
        } //else
        if (event.getTile() instanceof IMultiBlockPart){
            BlockLoc loc = new BlockLoc(event.getTile());
            IMultiBlockPart multiBlockPart = (IMultiBlockPart) event.getTile();
            multiBlockPart.setLocList(new LocList(multiBlockPart));
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
                final TileEntity tile = WorldHelper.getTileAt(world, loc.atSide(direction));
                if (tile instanceof IMultiBlockPart)
                    multiBlockPart.setLocList(((IMultiBlockPart)tile).getLocList().merge(multiBlockPart.getLocList()));
                if (tile instanceof IMultiBlockTile&& ((IMultiBlockTile)tile).getController() != null && ((IMultiBlockTile)tile).getController().paused) {
                    ElecCore.tickHandler.registerCall(new IRunOnce() {
                        @Override
                        public void run() {
                            ((IMultiBlockTile) tile).getController().validate();
                        }
                    });
                }
            }
        }
        print("LoadEvent complete");
    }

    protected void tick(){
        //Set<MultiBlockControllerBase> toRemove = new HashSet<MultiBlockControllerBase>();
        for (MultiBlockControllerBase controller : registeredControllers){
            if (controller.paused || !controller.isValid())
                return;
            if (!world.isRemote)
                controller.tickServer();
            else controller.tickClient();
        }
        //registeredControllers.removeAll(toRemove);
    }

    public void onMBPUnloaded(MultiBlockEvent.Unloaded event){
        TileEntity tile = event.getTile();
        IMultiBlockPart multiBlockPart = (IMultiBlockPart) event.getTile();
        if (multiBlockPart instanceof IMultiBlockTile) {
            IMultiBlockTile multiBlockTile = (IMultiBlockTile) multiBlockPart;
            if (multiBlockTile.getController() != null) {
                if (event.isChunkUnloading()) {
                    multiBlockTile.getController().pauseTile();
                    long chunkHash = chunkHashFromCoord(tile);
                    addUnloadedTile(chunkHash, multiBlockPart);
                } else {
                    multiBlockTile.getController().invalidate();
                    multiBlockTile.onMultiBlockInvalidated();
                }
                //if (locations.contains(new BlockLoc(event.getTile()))) {
                //   locations.remove(new BlockLoc(event.getTile()));
                //   ((IMultiBlockMainTile) event.getTile()).getController().invalidate();
                //    registeredControllers.remove(((IMultiBlockMainTile) event.getTile()).getController());
                //}
            }
        }
        if (!event.isChunkUnloading() && event.getTile() instanceof IMultiBlockMainTile){
            IMultiBlockMainTile tile1 = (IMultiBlockMainTile) event.getTile();
            if (locations.contains(new BlockLoc(event.getTile()))) {
                locations.remove(new BlockLoc(event.getTile()));
                //MultiBlockControllerBase controller = tile1.getController();
                //controller.invalidate();
                registeredControllers.remove(((IMultiBlockMainTile) event.getTile()).getController());
            }
        }
    }

    public void onChunkLoaded(Chunk chunk){
        long chunkHash = chunkHashFromCoord(chunk);
        if (tilesInUnloadedChunks.containsKey(chunkHash)){
            validateTiles(tilesInUnloadedChunks.get(chunkHash));
            tilesInUnloadedChunks.remove(chunkHash);
        }
    }

    private void validateTiles(Set<IMultiBlockPart> tiles){
        for (IMultiBlockPart tile : tiles){
            MinecraftForge.EVENT_BUS.post(new MultiBlockEvent.Loaded((TileEntity)tile));
        }
    }

    private void addUnloadedTile(long chunkHash, IMultiBlockPart multiBlockTile){
        if (!tilesInUnloadedChunks.containsKey(chunkHash))
            tilesInUnloadedChunks.put(chunkHash, new HashSet<IMultiBlockPart>());
        tilesInUnloadedChunks.get(chunkHash).add(multiBlockTile);
    }

    public void invalidate(){

    }

    public boolean isChunkLoaded(TileEntity tile){
        Chunk saidChunk = getChunkFromTile(tile);
        return world.getChunkProvider().chunkExists(saidChunk.xPosition, saidChunk.zPosition);
    }

    public long chunkHashFromCoord(TileEntity tile){
        return chunkHashFromCoord(getChunkFromTile(tile));
    }

    public Chunk getChunkFromTile(TileEntity tile){
        return world.getChunkFromBlockCoords(tile.xCoord, tile.zCoord);
    }

    public long chunkHashFromCoord(Chunk saidChunk){
        return ChunkCoordIntPair.chunkXZ2Int(saidChunk.xPosition, saidChunk.zPosition);
    }

    private void print(String s){
        System.out.println(prefix()+s);
    }

    private String prefix(){
        return world.isRemote?"CLIENT ":"SERVER ";
    }

}
