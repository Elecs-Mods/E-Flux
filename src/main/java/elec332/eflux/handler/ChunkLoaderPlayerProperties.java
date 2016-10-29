package elec332.eflux.handler;

import com.google.common.collect.Lists;
import elec332.core.server.ElecPlayer;
import elec332.core.server.ServerHelper;
import elec332.core.util.NBTHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.energy.machine.chunkLoader.TileEntityMainChunkLoader;
import elec332.eflux.tileentity.energy.machine.chunkLoader.TileEntitySubChunkLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
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

    public static ChunkLoaderPlayerProperties get(@Nonnull UUID uuid){
        return (ChunkLoaderPlayerProperties) ServerHelper.instance.getPlayer(uuid).getExtendedProperty("EFluxChunks");
    }

    private TileEntityMainChunkLoader main;
    private World world;
    private boolean hasHandler;
    private List<BlockPos> blockLocations;

    public void setMainChunkLoader(TileEntityMainChunkLoader tile){
        main = tile;
        world = tile == null ? null : tile.getWorld();
        hasHandler = (tile != null);
    }

    public void addLoader(TileEntitySubChunkLoader tile){
        if (main != null)
            main.addLoader(tile);
        else
            blockLocations.add(tile.getPos());
    }

    public void removeLoader(TileEntitySubChunkLoader tile){
        if (main != null) {
            main.removeLoader(tile);
        } else {
            blockLocations.remove(tile.getPos());
        }
    }

    public TileEntityMainChunkLoader getMain() {
        return main;
    }

    public boolean hasHandler() {
        return hasHandler;
    }

    public List<BlockPos> getLocations(){
        return this.blockLocations;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        if (nbtTagCompound.hasKey("mainLoc")) {
            this.hasHandler = nbtTagCompound.getBoolean("handler?");
            this.world = ServerHelper.instance.getMinecraftServer().worldServerForDimension(nbtTagCompound.getInteger("dim"));
            this.main = (TileEntityMainChunkLoader) WorldHelper.getTileAt(world, new NBTHelper(nbtTagCompound.getCompoundTag("mainLoc")).getPos());
            NBTTagList list = nbtTagCompound.getTagList("locations", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                blockLocations.add(new NBTHelper(list.getCompoundTagAt(i)).getPos());
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        if (main != null) {
            nbtTagCompound.setBoolean("handler?", this.hasHandler);
            nbtTagCompound.setTag("mainLoc", new NBTHelper().addToTag(main.getPos()).serializeNBT());
            nbtTagCompound.setInteger("dim", WorldHelper.getDimID(world));
            NBTTagList tagList = new NBTTagList();
            for (BlockPos blockLoc : blockLocations){
                tagList.appendTag(new NBTHelper().addToTag(blockLoc).serializeNBT());
            }
            nbtTagCompound.setTag("locations", tagList);
        }
    }

}
