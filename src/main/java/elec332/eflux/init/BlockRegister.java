package elec332.eflux.init;

import elec332.core.world.location.BlockStateWrapper;
import elec332.eflux.items.ChunkLoaderItemBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
import elec332.core.world.location.BlockStateWrapper;
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

    public static Block ores, machinePart, machineGlass, renderBlock, cable;
    public static BlockStateWrapper frameBasic, frameNormal, frameAdvanced, itemGate, laserLens, laserCore, heatResistantGlass, heater, monitor, radiator, motor, precisionMotor, dustStorage;

    public void init(FMLInitializationEvent event){
        if (ElecCore.developmentEnvironment){
            new DirectionBlock(Material.rock).setCreativeTab(EFlux.creativeTab);
        }

        for (EnumMachines machine : EnumMachines.values()){
            switch (machine){
                case CHUNKMAIN:
                    machine.itemBlockClass = ChunkLoaderItemBlock.class;
                    break;
                case CHUNKSUB:
                    machine.itemBlockClass = ChunkLoaderItemBlock.class;
                    break;
            }
            machine.init();
        }

        ores = new BlockOres().register().setCreativeTab(EFlux.creativeTab);
        machinePart = new BlockMachinePart(11).register().setCreativeTab(EFlux.creativeTab);
        machineGlass = new BlockHeatGlass(2).register().setCreativeTab(EFlux.creativeTab);
        renderBlock = new BlockRenderItemInWorld("renderBlock").register().setCreativeTab(EFlux.creativeTab);

        Block frame = new BlockMachineFrame("frameBlock").register().setCreativeTab(EFlux.creativeTab);
        Block itemInlet = new BlockItemInlet("itemInlet").register().setCreativeTab(EFlux.creativeTab);

        frameBasic = new BlockStateWrapper(frame, 0);
        frameNormal = new BlockStateWrapper(frame, 1);
        frameAdvanced = new BlockStateWrapper(frame, 2);

        itemGate = new BlockStateWrapper(itemInlet, 0);

        laserCore = newMachineBlock(0);
        heater = newMachineBlock(1);

        heatResistantGlass = newGlassBlock(0);
        laserLens = newGlassBlock(1);

        monitor = newMachineBlock(6);
        radiator = newMachineBlock(3);
        motor = newMachineBlock(4);
        precisionMotor = newMachineBlock(5);
        dustStorage = newMachineBlock(2);

        cable = new BlockCable("efluxCable").register();
    }

    private static BlockStateWrapper newMachineBlock(int i){
        return new BlockStateWrapper(machinePart, i);
    }

    private static BlockStateWrapper newGlassBlock(int i){
        return new BlockStateWrapper(machineGlass, i);
    }
}
