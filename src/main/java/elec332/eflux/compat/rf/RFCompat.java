package elec332.eflux.compat.rf;

import elec332.core.baseclasses.tileentity.BlockTileBase;
import elec332.eflux.EFlux;
import elec332.eflux.compat.Compat;
import net.minecraft.block.material.Material;

/**
 * Created by Elec332 on 20-7-2015.
 */
public class RFCompat extends Compat.ICompatHandler {

    @Override
    public Compat.ModType getType() {
        return Compat.ModType.API;
    }

    @Override
    public String getName() {
        return "CoFHAPI|energy";
    }

    @Override
    public void init() {
        new BlockTileBase(Material.rock, TileRFConverter.class, "RFConverter", EFlux.ModID).register().setCreativeTab(EFlux.CreativeTab);
    }

}
