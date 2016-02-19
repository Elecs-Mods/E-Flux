package elec332.eflux.blocks.data;

import elec332.eflux.client.blocktextures.IBlockTextureProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 14-1-2016.
 */
public interface IEFluxBlockMachineData {

    public boolean hasTwoStates();

    public Class<? extends ItemBlock> getItemBlockClass();

    public Class<? extends TileEntity> getTileClass();

    public Material getBlockMaterial();

    public Block getBlock();

    public IBlockTextureProvider getTextureProvider();

    public void setCreativeTab(CreativeTabs creativeTabs);

    public int getRenderID();

    public String getName();

}
