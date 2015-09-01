package elec332.eflux.init;

import elec332.core.multiblock.BlockData;
import elec332.core.multiblock.BlockStructure;
import elec332.core.multiblock.IMultiBlockStructure;
import elec332.eflux.EFlux;
import elec332.eflux.multiblock.MultiBlockCompressor;
import elec332.eflux.multiblock.MultiBlockLaser;
import elec332.eflux.util.EnumMachines;
import net.minecraftforge.oredict.OreDictionary;

import static elec332.eflux.init.BlockRegister.*;

/**
 * Created by Elec332 on 27-8-2015.
 */
public class MultiBlockRegister {

    private static final BlockData powerInlet = new BlockData(EnumMachines.POWERINLET.getBlock(), OreDictionary.WILDCARD_VALUE);

    public static void init(){
        EFlux.multiBlockRegistry.registerMultiBlock(new IMultiBlockStructure() {
            @Override
            public BlockStructure getStructure() {
                return new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                    @Override
                    public BlockData getBlockAtPos(int length, int width, int height) {
                        if (length == 0 && width == 0 && height == 0)
                            return powerInlet;
                        return frameNormal;
                    }
                });
            }

            @Override
            public BlockStructure.IStructureFiller replaceUponCreated() {
                return null;
            }

            @Override
            public BlockData getTriggerBlock() {
                return powerInlet;
            }
        }, "grider", MultiBlockCompressor.class);

        EFlux.multiBlockRegistry.registerMultiBlock(new IMultiBlockStructure() {
            @Override
            public BlockStructure getStructure() {
                return new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                    @Override
                    public BlockData getBlockAtPos(int length, int width, int height) {
                        if (length == 0) {
                            if (width == 1 && height == 1) {
                                return laserLens;
                            } else if (width == 0 && height == 0){
                                return powerInlet;
                            }
                        } else if (length == 2 && width == 0 && height == 0){
                            return itemOutlet;
                        }
                        return frameAdvanced;
                    }
                });
            }

            @Override
            public BlockStructure.IStructureFiller replaceUponCreated() {
                return null;
            }

            @Override
            public BlockData getTriggerBlock() {
                return new BlockData(EnumMachines.POWERINLET.getBlock(), OreDictionary.WILDCARD_VALUE);
            }
        }, "laser", MultiBlockLaser.class);
    }
}
