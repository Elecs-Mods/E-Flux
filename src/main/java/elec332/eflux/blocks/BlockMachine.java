package elec332.eflux.blocks;

import elec332.core.baseclasses.tileentity.BlockTileBase;
import elec332.eflux.EFlux;
import elec332.eflux.util.EnumMachines;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 30-4-2015.
 */
public class BlockMachine extends BlockTileBase {

    public BlockMachine(EnumMachines machine){
        super(machine.getBlockMaterial(), machine.getTileClass(), machine.toString(), EFlux.ModID);
        setCreativeTab(EFlux.CreativeTab);
        this.machine = machine;
    }

    private EnumMachines machine;

    public EnumMachines getMachine(){
        return machine;
    }

    @Override
    public ItemStack ItemDropped() {
        return new ItemStack(this);
    }

    @Override
    public int getRenderType() {
        return this.machine.getRenderID();
    }
}
