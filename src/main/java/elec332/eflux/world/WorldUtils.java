package elec332.eflux.world;

import elec332.core.util.BlockLoc;
import elec332.eflux.EFlux;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

/**
 * Created by Elec332 on 24-5-2015.
 */
public class WorldUtils {

    public static ForgeChunkManager.Ticket requestTicket(World world, BlockLoc loc){
        ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestTicket(EFlux.instance, world, ForgeChunkManager.Type.NORMAL);
        if (ticket != null){
            loc.toNBT(ticket.getModData());
        }
        return ticket;
    }

    public static ChunkCoordIntPair fromBlockLoc(BlockLoc loc){
        return new ChunkCoordIntPair(loc.xCoord >> 4, loc.zCoord >> 4);
    }

    public static void forceChunk(ForgeChunkManager.Ticket ticket){
        ForgeChunkManager.forceChunk(ticket, fromBlockLoc(new BlockLoc(ticket.getModData())));
    }

    public static void dropStack(World world, BlockLoc blockLoc, ItemStack stack){
        dropStack(world, blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord, stack);
    }

    public static void dropStack(World world, int x, int y, int z, ItemStack itemStack){
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")){
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, itemStack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }

    public static void scheduleBlockUpdate(World world, BlockLoc blockLoc){
        scheduleBlockUpdate(world, blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord);
    }

    public static void scheduleBlockUpdate(World world, int x, int y, int z){
        world.scheduleBlockUpdate(x, y, z, world.getBlock(x, y, z), 1);
    }

    public static int getBlockMeta(World world, BlockLoc blockLoc){
        return world.getBlockMetadata(blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord);
    }
}
