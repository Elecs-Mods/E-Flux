package elec332.eflux.blocks;

import com.google.common.base.Strings;
import elec332.core.client.model.loading.INoBlockStateJsonBlock;
import elec332.eflux.EFlux;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 21-7-2015.
 */
public class BlockOres extends BlockWithMeta implements INoBlockStateJsonBlock.DefaultImpl {

    public BlockOres() {
        super(Material.ROCK, "ore", EFlux.ModID.toLowerCase());
        setResistance(5.0f);
        setHardness(2.5f);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String s = nameForType(stack.getItemDamage());
        if (Strings.isNullOrEmpty(s)) {
            return "ERROR_BLOCK_EFLUX";
        }
        return getUnlocalizedName()+"."+s;
    }

    public String nameForType(int meta){
        switch (meta){
            case 0:
                return "copper";
            case 1:
                return "tin";
            case 2:
                return "zinc";
            case 3:
                return "silver";
            default:
                return null;
        }
    }

    @Override
    public int getTypes() {
        return 4;
    }

}
