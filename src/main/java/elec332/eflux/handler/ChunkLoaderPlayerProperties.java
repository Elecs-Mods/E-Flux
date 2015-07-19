package elec332.eflux.handler;

import elec332.core.server.ElecPlayer;
import elec332.core.server.ServerHelper;
import elec332.core.util.BlockLoc;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.energy.machine.chunkLoader.MainChunkLoaderTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * Created by Elec332 on 25-5-2015.
 */
public class ChunkLoaderPlayerProperties extends ElecPlayer.ExtendedProperties{

    public ChunkLoaderPlayerProperties(){
        this.hasHandler = false;
    }

    public static ChunkLoaderPlayerProperties get(UUID uuid){
        return (ChunkLoaderPlayerProperties) ServerHelper.instance.getPlayer(uuid).getExtendedProperty("EFluxChunks");//(ChunkLoaderPlayerProperties) player.getExtendedProperties();
    }

    private MainChunkLoaderTile main;
    private World world;
    private boolean hasHandler;

    public void setMainChunkLoader(MainChunkLoaderTile tile){
        main = tile;
        world = tile == null ? null : tile.getWorldObj();
        hasHandler = (tile != null);
    }

    public MainChunkLoaderTile getMain() {
        return main;
    }

    public boolean hasHandler() {
        return hasHandler;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        if (nbtTagCompound.hasKey("mainLoc")) {
            this.hasHandler = nbtTagCompound.getBoolean("handler?");
            this.world = ServerHelper.instance.getMinecraftServer().worldServerForDimension(nbtTagCompound.getInteger("dim"));
            this.main = (MainChunkLoaderTile) WorldHelper.getTileAt(world, new BlockLoc(nbtTagCompound.getCompoundTag("mainLoc")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        System.out.println("Saved data for "+getPlayerUUID());
        if (main != null) {
            nbtTagCompound.setBoolean("handler?", this.hasHandler);
            nbtTagCompound.setTag("mainLoc", main.myLocation().toNBT(new NBTTagCompound()));
            nbtTagCompound.setInteger("dim", WorldHelper.getDimID(world));
        }
    }

}
