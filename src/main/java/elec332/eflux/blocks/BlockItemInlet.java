package elec332.eflux.blocks;

import elec332.eflux.blocks.data.AbstractEFluxBlockMachineData;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockItemGate;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class BlockItemInlet extends BlockMachine {

    public BlockItemInlet() {
        super(DATA);
    }

    private static IEFluxBlockMachineData DATA;

    static {
        DATA = new AbstractEFluxBlockMachineData() {

            @Override
            public Class<? extends TileEntity> getTileClass() {
                return TileEntityMultiBlockItemGate.class;
            }

            @Override
            public boolean hasTwoStates() {
                return true;
            }

            @Override
            public String getName() {
                return "itemInlet";
            }

        };
    }

}
