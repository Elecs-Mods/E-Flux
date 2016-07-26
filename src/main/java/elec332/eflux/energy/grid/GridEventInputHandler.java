package elec332.eflux.energy.grid;

import com.google.common.collect.Sets;
import elec332.core.world.DimensionCoordinate;
import elec332.eflux.api.EFluxAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Set;

/**
 * Created by Elec332 on 23-7-2016.
 */
public enum GridEventInputHandler {

    INSTANCE;

    GridEventInputHandler(){
        this.bud = Sets.newHashSet();
        this.chunkAdd = Sets.newHashSet();
        this.chunkRemove = Sets.newHashSet();
        this.notify = Sets.newHashSet();
    }

    private final Set<DimensionCoordinate> bud, chunkAdd, chunkRemove, notify;


    public void worldBlockUpdate(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
        if (!world.isRemote && (newState.getBlock().hasTileEntity(newState)) || oldState.getBlock().hasTileEntity(oldState)) {
            bud.add(new DimensionCoordinate(world, pos));
        }
    }

    /**
     * Gets called when a block tells its neighbors that its state has changed.
     *
     * @param world The world
     * @param pos The pos of the block that changed state
     * @param state The new state
     */
    public void onBlockNotify(World world, BlockPos pos, IBlockState state){
        if (!world.isRemote) {
            if (!state.getBlock().hasTileEntity(state)) {
                return;
            }
            DimensionCoordinate dimCoord = new DimensionCoordinate(world, pos);
            if (bud.contains(dimCoord)) {
                return;
            }
            notify.add(dimCoord);
        }
    }

    public void chunkLoad(Chunk chunk){
        if (!chunk.getWorld().isRemote) {
            for (TileEntity tile : chunk.getTileEntityMap().values()) {
                if (isEnergyObject(tile)) {
                    chunkAdd.add(DimensionCoordinate.fromTileEntity(tile));
                }
            }
        }
    }

    public void chunkUnLoad(Chunk chunk){
        if (!chunk.getWorld().isRemote) {
            for (TileEntity tile : chunk.getTileEntityMap().values()) {
                if (isEnergyObject(tile)) {
                    chunkRemove.add(DimensionCoordinate.fromTileEntity(tile));
                }
            }
        }
    }

    public void tickEnd(){
        GridObjectHandler.INSTANCE.checkNotifyStuff(notify);
        notify.clear();
        GridObjectHandler.INSTANCE.checkBlockUpdates(bud);
        bud.clear();
        GridObjectHandler.INSTANCE.checkChunkUnload(chunkRemove);
        chunkRemove.clear();
        GridObjectHandler.INSTANCE.checkChunkLoad(chunkAdd);
        chunkAdd.clear();
        GridObjectHandler.INSTANCE.tickEnd();
    }

    public void worldUnload(World world){
        if (!world.isRemote){
            GridObjectHandler.INSTANCE.worldUnload(world);
        }
    }

    public static boolean isEnergyObject(ICapabilityProvider capabilityProvider){
        if (capabilityProvider == null){
            return false;
        }
        for (EnumFacing facing : EnumFacing.VALUES){
            if (capabilityProvider.hasCapability(EFluxAPI.RECEIVER_CAPABILITY, facing) || capabilityProvider.hasCapability(EFluxAPI.PROVIDER_CAPABILITY, facing) || capabilityProvider.hasCapability(EFluxAPI.TRANSMITTER_CAPABILITY, facing) || capabilityProvider.hasCapability(EFluxAPI.MONITOR_CAPABILITY, facing)){
                return true;
            }
        }
        return false;
    }

}
