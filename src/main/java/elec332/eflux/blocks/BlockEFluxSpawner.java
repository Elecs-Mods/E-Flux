package elec332.eflux.blocks;

import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import net.minecraft.util.EnumWorldBlockLayer;

/**
 * Created by Elec332 on 20-2-2016.
 */
public class BlockEFluxSpawner extends BlockMachine {

    public BlockEFluxSpawner(IEFluxBlockMachineData machine) {
        super(machine);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }



}
