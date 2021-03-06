package elec332.eflux.blocks.data;

import elec332.eflux.blocks.BlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;

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
        return Material.ROCK;
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
    public EnumBlockRenderType getRenderType() {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderingLayer() {
        return BlockRenderLayer.SOLID;
    }

}
