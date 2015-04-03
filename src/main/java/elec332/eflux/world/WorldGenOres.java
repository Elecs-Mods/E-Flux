package elec332.eflux.world;

import elec332.core.util.WorldGenInfo;
import elec332.core.world.WorldGen;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.io.File;
import java.util.Random;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class WorldGenOres extends WorldGen {

    public WorldGenOres(File file){
        super(file);
        testOreOverworld = configurableWorldGen("TestoreOverworld", 5, 60, 3, Blocks.brick_block);
    }

    private static WorldGenInfo testOreOverworld;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.dimensionId){
            case 0:
                break;
            default:
                break;
        }
    }
}
