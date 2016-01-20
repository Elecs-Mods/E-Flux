package elec332.eflux.blocks;

import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import net.minecraft.util.EnumWorldBlockLayer;

/**
 * Created by Elec332 on 2-9-2015.
 */
public class BlockMachineGlass extends BlockMachine {

    public BlockMachineGlass(IEFluxBlockMachineData machineData) {
        super(machineData);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

}
