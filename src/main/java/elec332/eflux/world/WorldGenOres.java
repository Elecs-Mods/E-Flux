package elec332.eflux.world;

import elec332.core.util.WorldGenInfo;
import elec332.core.world.WorldGen;
import elec332.core.world.WorldHelper;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.init.FluidRegister;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.feature.WorldGenLakes;

import java.io.File;
import java.util.Random;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class WorldGenOres extends WorldGen {

    public WorldGenOres(File file){
        super(file);
        copper = configurableWorldGen("Copper", 12, 60, 10, BlockRegister.ores);
        tin = configurableWorldGen("Tin", 9, 55, 8, BlockRegister.ores, 1);
        zinc = configurableWorldGen("Zinc", 8, 40, 4, BlockRegister.ores, 2);
        silver = configurableWorldGen("Silver", 6, 40, 3, BlockRegister.ores, 3);
    }

    private static WorldGenInfo copper, tin, zinc, silver;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (!(chunkGenerator instanceof ChunkProviderFlat)) {
            int dim = WorldHelper.getDimID(world);
            switch (dim) {
                case 0:
                    generateOverworld(world, random, chunkX, chunkZ, copper);
                    generateOverworld(world, random, chunkX, chunkZ, tin);
                    generateOverworld(world, random, chunkX, chunkZ, zinc);
                    generateOverworld(world, random, chunkX, chunkZ, silver);
                    new WorldGenLakes(FluidRegister.oil.getBlock()).generate(world, random, new BlockPos(chunkX * 16 + random.nextInt(16), 20 + random.nextInt(60), chunkZ * 16 + random.nextInt(16)));
                default:
                    break;
            }
        }
    }

}
