package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
import elec332.core.multiblock.BlockData;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.BlockMachinePart;
import elec332.eflux.blocks.BlockOres;
import elec332.eflux.blocks.DirectionBlock;
import elec332.eflux.util.EnumMachines;
import net.minecraft.block.Block;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class BlockRegister {
    public static final BlockRegister instance = new BlockRegister();
    private BlockRegister(){
    }

    public static Block ores, machinePart;
    public static BlockData frameBasic, frameNormal, frameAdvanced, itemOutlet, laserLens, laserCore;

    public void init(FMLInitializationEvent event){
        if (ElecCore.developmentEnvironment){
            new DirectionBlock().setCreativeTab(EFlux.creativeTab);
        }

        for (EnumMachines machine : EnumMachines.values()){
            machine.init();
        }

        ores = new BlockOres().register().setCreativeTab(EFlux.creativeTab);
        machinePart = new BlockMachinePart(6).register().setCreativeTab(EFlux.creativeTab);

        frameBasic = newMachineData(0);
        frameNormal = newMachineData(1);
        frameAdvanced = newMachineData(2);
        itemOutlet = newMachineData(3);
        laserLens = newMachineData(4);
        laserCore = newMachineData(5);
    }

    private static BlockData newMachineData(int i){
        return new BlockData(machinePart, i);
    }
}
