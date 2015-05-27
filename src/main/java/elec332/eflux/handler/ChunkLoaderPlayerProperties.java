package elec332.eflux.handler;

import elec332.core.util.BlockLoc;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.energy.machine.chunkLoader.MainChunkLoaderTile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * Created by Elec332 on 25-5-2015.
 */
public class ChunkLoaderPlayerProperties implements IExtendedEntityProperties {

    public ChunkLoaderPlayerProperties(){
        this.hasHandler = false;
    }

    public static ChunkLoaderPlayerProperties get(EntityPlayer player){
        return (ChunkLoaderPlayerProperties) player.getExtendedProperties("EFluxChunks");
    }

    private EntityPlayer player;
    private boolean hasHandler;
    private MainChunkLoaderTile main;

    public void setMainChunkLoader(MainChunkLoaderTile tile){
        this.main = tile;
        this.hasHandler = (tile != null);
    }

    public MainChunkLoaderTile getMain() {
        return main;
    }

    public boolean hasHandler() {
        return hasHandler;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        if (main != null) {
            compound.setBoolean("handler?", this.hasHandler);
            compound.setTag("mainLoc", main.myLocation().toNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey("mainLoc")) {
            this.hasHandler = compound.getBoolean("handler?");
            this.main = (MainChunkLoaderTile) WorldHelper.getTileAt(player.worldObj, new BlockLoc(compound.getCompoundTag("mainLoc")));
        }
    }

    @Override
    public void init(Entity entity, World world) {
        if (entity instanceof EntityPlayer){
            this.player = (EntityPlayer) entity;
        }
    }
}
