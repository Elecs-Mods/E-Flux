package elec332.eflux.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;
import elec332.eflux.EFlux;
import elec332.eflux.tileentity.multiblock.TileEntityInsideItemRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 5-9-2015.
 */
public class BlockRenderItemInWorld extends Block implements ITileEntityProvider {

    public BlockRenderItemInWorld(String blockName) {
        super(Material.wood);
        //this.setBlockName(EFlux.ModID + "." + blockName);
        this.blockName = blockName;
    }

    protected final String blockName;

    public BlockRenderItemInWorld register(){
        GameRegistry.registerBlock(this, blockName);
        GameRegistry.registerTileEntity(TileEntityInsideItemRenderer.class, blockName);
        return this;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityInsideItemRenderer();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 3;
    }
}
