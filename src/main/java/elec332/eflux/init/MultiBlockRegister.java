package elec332.eflux.init;

import elec332.core.multiblock.BlockData;
import elec332.core.multiblock.BlockStructure;
import elec332.core.multiblock.IMultiBlockStructure;
import elec332.eflux.EFlux;
import elec332.eflux.multiblock.MultiBlockCompressor;
import elec332.eflux.multiblock.MultiBlockGrinder;
import elec332.eflux.util.EnumMachines;
import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Elec332 on 27-8-2015.
 */
public class MultiBlockRegister {

    public static void init(){
        EFlux.multiBlockRegistry.registerMultiBlock(new IMultiBlockStructure() {
            @Override
            public BlockStructure getStructure() {
                return new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                    @Override
                    public BlockData getBlockAtPos(int length, int width, int height) {
                        if (length == 0 && width == 0 && height == 0)
                            return new BlockData(EnumMachines.POWERINLET.getBlock(), OreDictionary.WILDCARD_VALUE);
                        return new BlockData(Blocks.brick_block);
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
        }, "grider", MultiBlockCompressor.class);
    }
}
