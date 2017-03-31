package elec332.eflux.blocks;

import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 2-9-2015.
 */
public class BlockMachineGlass extends BlockMachine {

    public BlockMachineGlass(IEFluxBlockMachineData machineData) {
        super(machineData);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

}
