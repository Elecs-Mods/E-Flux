package elec332.eflux.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 21-7-2015.
 */
public abstract class BlockWithMeta extends Block {

    public BlockWithMeta(Material mat, String blockName, String modID) {
        super(mat);
        this.setBlockName(modID + "." + blockName);
        this.blockName = blockName;
    }

    protected final String blockName;

    public BlockWithMeta register(){
        GameRegistry.registerBlock(this, MetaItemBlock.class, blockName);
        return this;
    }

    public abstract String getUnlocalizedName(ItemStack stack);

    @Override
    @SuppressWarnings("unchecked")
    public final void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        getSubBlocks(list, item, creativeTab);
    }

    public abstract void getSubBlocks(List<ItemStack> list, Item item, CreativeTabs creativeTab);

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    public static class MetaItemBlock extends ItemBlockWithMetadata{

        public MetaItemBlock(Block block) {
            super(block, block);
            if (!(block instanceof BlockWithMeta))
                throw new IllegalArgumentException();
        }

        @Override
        public String getUnlocalizedName(ItemStack stack) {
            return ((BlockWithMeta) field_150939_a).getUnlocalizedName(stack);
        }

    }

}
