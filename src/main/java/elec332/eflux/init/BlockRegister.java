package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
import elec332.core.world.location.BlockData;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.*;
import elec332.eflux.util.EnumMachines;
import net.minecraft.block.Block;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class BlockRegister {
    public static final BlockRegister instance = new BlockRegister();
    private BlockRegister(){
    }

    public static Block ores, machinePart, machineGlass, renderBlock;
    public static BlockData frameBasic, frameNormal, frameAdvanced, itemGate, laserLens, laserCore, heatResistantGlass, heater, monitor, radiator, motor, precisionMotor, dustStorage;

    public void init(FMLInitializationEvent event){
        if (ElecCore.developmentEnvironment){
            new DirectionBlock().setCreativeTab(EFlux.creativeTab);
        }

        for (EnumMachines machine : EnumMachines.values()){
            machine.init();
        }

        ores = new BlockOres().register().setCreativeTab(EFlux.creativeTab);
        machinePart = new BlockMachinePart(11).register().setCreativeTab(EFlux.creativeTab);
        machineGlass = new BlockHeatGlass(2).register().setCreativeTab(EFlux.creativeTab);
        renderBlock = new BlockRenderItemInWorld("renderBlock").register().setCreativeTab(EFlux.creativeTab);

        frameBasic = newMachineBlock(0);
        frameNormal = newMachineBlock(1);
        frameAdvanced = newMachineBlock(2);
        itemGate = newMachineBlock(3);
        laserCore = newMachineBlock(4);
        heater = newMachineBlock(5);
        heatResistantGlass = newGlassBlock(0);
        laserLens = newGlassBlock(1);
        monitor = newMachineBlock(6);
        radiator = newMachineBlock(7);
        motor = newMachineBlock(8);
        precisionMotor = newMachineBlock(9);
        dustStorage = newMachineBlock(10);

        new BlockCable("testCable").registerTile().register();
    }

    private static BlockData newMachineBlock(int i){
        return new BlockData(machinePart, i);
    }

    private static BlockData newGlassBlock(int i){
        return new BlockData(machineGlass, i);
    }
}
