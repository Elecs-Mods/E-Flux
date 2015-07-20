package elec332.eflux.handler;

import com.google.common.collect.Lists;
import elec332.core.server.ElecPlayer;
import elec332.core.server.ServerHelper;
import elec332.core.util.BlockLoc;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.energy.machine.chunkLoader.ChunkLoaderSubTile;
import elec332.eflux.tileentity.energy.machine.chunkLoader.MainChunkLoaderTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 25-5-2015.
 */
public class ChunkLoaderPlayerProperties extends ElecPlayer.ExtendedProperties{

    public ChunkLoaderPlayerProperties(){
        this.hasHandler = false;
        this.blockLocations = Lists.newArrayList();
    }

    public static ChunkLoaderPlayerProperties get(UUID uuid){
        return (ChunkLoaderPlayerProperties) ServerHelper.instance.getPlayer(uuid).getExtendedProperty("EFluxChunks");//(ChunkLoaderPlayerProperties) player.getExtendedProperties();
    }

    private MainChunkLoaderTile main;
    private World world;
    private boolean hasHandler;
    private List<BlockLoc> blockLocations;

    public void setMainChunkLoader(MainChunkLoaderTile tile){
        main = tile;
        world = tile == null ? null : tile.getWorldObj();
        hasHandler = (tile != null);
    }

    public void addLoader(ChunkLoaderSubTile tile){
        if (main != null)
            main.addLoader(tile);
        else
            blockLocations.add(tile.myLocation());
    }

    public void removeLoader(ChunkLoaderSubTile tile){
        if (main != null)
            main.removeLoader(tile);
        else
            blockLocations.remove(tile.myLocation());
    }

    public MainChunkLoaderTile getMain() {
        return main;
    }

    public boolean hasHandler() {
        return hasHandler;
    }

    public List<BlockLoc> getLocations(){
        return this.blockLocations;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        if (nbtTagCompound.hasKey("mainLoc")) {
            this.hasHandler = nbtTagCompound.getBoolean("handler?");
            this.world = ServerHelper.instance.getMinecraftServer().worldServerForDimension(nbtTagCompound.getInteger("dim"));
            this.main = (MainChunkLoaderTile) WorldHelper.getTileAt(world, new BlockLoc(nbtTagCompound.getCompoundTag("mainLoc")));
            for (int i = 0; i < nbtTagCompound.getTagList("locations", 10).tagCount(); i++) {
                blockLocations.add(new BlockLoc(nbtTagCompound.getTagList("locations", 10).getCompoundTagAt(i)));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        if (main != null) {
            nbtTagCompound.setBoolean("handler?", this.hasHandler);
            nbtTagCompound.setTag("mainLoc", main.myLocation().toNBT(new NBTTagCompound()));
            nbtTagCompound.setInteger("dim", WorldHelper.getDimID(world));
            NBTTagList tagList = new NBTTagList();
            for (BlockLoc blockLoc : blockLocations){
                tagList.appendTag(blockLoc.toNBT(new NBTTagCompound()));
            }
            nbtTagCompound.setTag("locations", tagList);
        }
    }

}
