package elec332.eflux.world;

import elec332.core.main.ElecCoreRegistrar;
import elec332.core.world.features.FeatureWorldGenMinable;
import elec332.core.world.features.FeatureWorldGenerator;
import elec332.eflux.EFlux;
import elec332.eflux.init.BlockRegister;

/**
 * Created by Elec332 on 18-10-2016.
 */
public final class WorldGenRegister {

    public static void init(){
        register(FeatureWorldGenMinable.newOreGenerator("Copper", 10, 128, 12, BlockRegister.oreCopper.getBlockState(), FeatureWorldGenMinable.STONE));
        register(FeatureWorldGenMinable.newOreGenerator("Tin", 8, 52, 9, BlockRegister.oreTin.getBlockState(), FeatureWorldGenMinable.STONE));
        register(FeatureWorldGenMinable.newOreGenerator("Zinc", 4, 40, 8, BlockRegister.oreZinc.getBlockState(), FeatureWorldGenMinable.STONE));
        register(FeatureWorldGenMinable.newOreGenerator("Silver", 3, 40, 6, BlockRegister.oreSilver.getBlockState(), FeatureWorldGenMinable.STONE));
        register(FeatureWorldGenerator.wrap("OilLakes", new WorldGenOilLakes()).setGenerationMultiplier(0.05f).setMaxY(128));
    }

    private static void register(FeatureWorldGenerator fwgm){
        ElecCoreRegistrar.WORLD_FEATURE_GENERATORS.register(fwgm);
        EFlux.configOres.registerConfigurableElement(fwgm);
    }

}
