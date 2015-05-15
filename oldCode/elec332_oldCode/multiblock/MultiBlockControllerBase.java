package elec332_oldCode.multiblock;

import elec332.core.util.BlockLoc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Elec332 on 14-5-2015.
 */
public abstract class MultiBlockControllerBase {

    public MultiBlockControllerBase(IMultiBlockMainTile tile) {
        if (!(tile instanceof TileEntity))
            throw new IllegalArgumentException("Why does something else than a tile implement this!?!");
        this.mainTile = tile;
        this.valid = false;
        this.paused = false;
        this.connectedTiles = new HashSet<IMultiBlockTile>();
        this.chunkHashList = new HashMap<Long, BlockLoc>();
    }

    protected boolean valid;
    protected boolean paused;
    private IMultiBlockMainTile mainTile;
    protected Set<IMultiBlockTile> connectedTiles;
    private Map<Long, BlockLoc> chunkHashList;

    public TileEntity getTile() {
        return (TileEntity)mainTile;
    }

    public IMultiBlockMainTile getMainTile() {
        return mainTile;
    }

    public boolean isValid() {
        return valid;
    }

    public void invalidate(){
        for (IMultiBlockTile tile : connectedTiles)
            tile.onMultiBlockInvalidated();
    }

    public boolean mainTileActivated(EntityPlayer player){
        return false;
    }

    public boolean isActive(){
        return isValid() && !paused;
    }

    protected void pauseTile(){
        this.paused = true;
        markDirty();
    }

    protected void unPause(){
        this.paused = false;
        markDirty();
    }

    public void readFromNBT(NBTTagCompound tagCompound){
        this.paused = tagCompound.getBoolean("paused");
        this.valid = tagCompound.getBoolean("valid");
    }

    public void writeToNBT(NBTTagCompound tagCompound){
        tagCompound.setBoolean("paused", this.paused);
        tagCompound.setBoolean("valid", this.valid);
    }

    public void validate(){
        boolean valid = checkMultiBlockLocations();
        if (valid){
            chunkHashList.clear();
            for (IMultiBlockTile tile : connectedTiles){
                TileEntity tileEntity = (TileEntity)tile;
                if (!chunkHashList.containsKey(chunkHashFromCoord(tileEntity)))
                    chunkHashList.put(chunkHashFromCoord(tileEntity), new BlockLoc(tileEntity));
            }
            for (BlockLoc loc : chunkHashList.values()){
                Chunk chunk = getTile().getWorldObj().getChunkFromBlockCoords(loc.xCoord, loc.zCoord);
                if (!getTile().getWorldObj().getChunkProvider().chunkExists(chunk.xPosition, chunk.zPosition))
                    return;
            }
            this.valid = true;
            this.paused = false;
            markDirty();
        }
    }

    public void markDirty(){
        getTile().markDirty();
    }

    public long chunkHashFromCoord(TileEntity tile){
        return chunkHashFromCoord(getChunkFromTile(tile));
    }

    public Chunk getChunkFromTile(TileEntity tile){
        return tile.getWorldObj().getChunkFromBlockCoords(tile.xCoord, tile.zCoord);
    }

    public long chunkHashFromCoord(Chunk saidChunk){
        return ChunkCoordIntPair.chunkXZ2Int(saidChunk.xPosition, saidChunk.zPosition);
    }

    public abstract boolean checkMultiBlockLocations();

    public abstract void tickServer();

    public abstract void tickClient();
}
