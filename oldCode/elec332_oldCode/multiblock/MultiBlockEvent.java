package elec332_oldCode.multiblock;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Elec332 on 14-5-2015.
 */
public class MultiBlockEvent extends WorldEvent {
    private MultiBlockEvent(TileEntity tile) {
        super(tile.getWorldObj());
        this.tile = tile;
    }

    private TileEntity tile;

    public TileEntity getTile() {
        return tile;
    }

    public static class Loaded extends MultiBlockEvent{
        public Loaded(TileEntity tile) {
            super(tile);
        }
    }

    public static class Unloaded extends MultiBlockEvent {
        public Unloaded(TileEntity tile, boolean b) {
            super(tile);
            this.chunkUnloading = b;
        }

        private boolean chunkUnloading;

        public boolean isChunkUnloading() {
            return chunkUnloading;
        }
    }
}
