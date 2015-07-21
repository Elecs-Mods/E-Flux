package elec332.eflux.world;

import elec332.core.util.WorldGenInfo;
import elec332.core.world.WorldGen;
import elec332.eflux.init.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

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
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.dimensionId){
            case 0:
                generateOverworld(world, random, chunkX, chunkZ, copper);
                generateOverworld(world, random, chunkX, chunkZ, tin);
                generateOverworld(world, random, chunkX, chunkZ, zinc);
                generateOverworld(world, random, chunkX, chunkZ, silver);
            default:
                break;
        }
    }

}
