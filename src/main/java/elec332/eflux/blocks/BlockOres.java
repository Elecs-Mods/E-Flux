package elec332.eflux.blocks;

import elec332.core.client.model.loading.INoBlockStateJsonBlock;
import elec332.core.util.IDefaultStringSerializable;
import elec332.eflux.EFlux;
import net.minecraft.block.material.Material;

/**
 * Created by Elec332 on 21-7-2015.
 */
public class BlockOres extends BlockWithMeta<BlockOres.EnumOreType> implements INoBlockStateJsonBlock.DefaultImpl {

    public BlockOres() {
        super(Material.ROCK, "ore", EFlux.ModID.toLowerCase());
        setResistance(5.0f);
        setHardness(2.5f);
    }

    @Override
    public Class<EnumOreType> getEnumClass() {
        return EnumOreType.class;
    }

    public enum EnumOreType implements IDefaultStringSerializable {

        COPPER,
        TIN,
        ZINC,
        SILVER

    }

}
