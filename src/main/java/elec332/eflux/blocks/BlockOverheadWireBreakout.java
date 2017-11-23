package elec332.eflux.blocks;

import elec332.core.tile.BlockTileBase;
import elec332.eflux.tileentity.TileOverheadWireBreakout;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 7-11-2017.
 */
public class BlockOverheadWireBreakout extends BlockTileBase {

	public BlockOverheadWireBreakout(ResourceLocation name) {
		super(Material.BARRIER, TileOverheadWireBreakout.class, name);
	}

}
