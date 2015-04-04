package elec332.eflux.blocks;

import elec332.core.baseclasses.block.BaseBlock;
import elec332.eflux.EFlux;
import net.minecraft.block.material.Material;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class BlockBase extends BaseBlock {
    public BlockBase(Material baseMaterial, String blockName) {
        super(baseMaterial, blockName, EFlux.ModID);
        setCreativeTab(EFlux.CreativeTab);
    }
}
