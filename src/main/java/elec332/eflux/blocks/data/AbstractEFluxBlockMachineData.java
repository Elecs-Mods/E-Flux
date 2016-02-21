package elec332.eflux.blocks.data;

import elec332.eflux.blocks.BlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumWorldBlockLayer;

/**
 * Created by Elec332 on 14-1-2016.
 */
public abstract class AbstractEFluxBlockMachineData implements IEFluxBlockMachineData {

    @Override
    public boolean hasTwoStates() {
        return false;
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlock.class;
    }

    @Override
    public Material getBlockMaterial() {
        return Material.rock;
    }

    @Override
    public BlockMachine getBlock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCreativeTab(CreativeTabs creativeTabs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRenderID() {
        return 3;
    }

    @Override
    public EnumWorldBlockLayer getRenderingLayer() {
        return EnumWorldBlockLayer.SOLID;
    }

}
