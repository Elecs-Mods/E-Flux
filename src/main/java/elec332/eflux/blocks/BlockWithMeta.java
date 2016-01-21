package elec332.eflux.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

/**
 * Created by Elec332 on 21-7-2015.
 */
public abstract class BlockWithMeta extends Block {

    public BlockWithMeta(Material mat, String blockName, String modID) {
        super(mat);
        this.setDefaultState(getStateFromMeta(0));
        this.setUnlocalizedName(modID + "." + blockName);
        this.blockName = blockName;
    }

    private PropertyInteger META;
    protected final String blockName;

    public final PropertyInteger getProperty(){
        if (META == null)
            META = PropertyInteger.create("meta", 0, getTypes());
        return META;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getBlockState().getBaseState().withProperty(getProperty(), meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(getProperty());
    }

    public BlockWithMeta register(){
        GameRegistry.registerBlock(this, MetaItemBlock.class, blockName);
        return this;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, getProperty());
    }

    public String getUnlocalizedName(ItemStack stack){
        return getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        getSubBlocks(list, item, creativeTab);
    }

    public abstract int getTypes();

    public void getSubBlocks(List<ItemStack> list, Item item, CreativeTabs creativeTab){
        for (int i = 0; i < getTypes(); i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    public static class MetaItemBlock extends ItemBlock {

        public MetaItemBlock(Block block) {
            super(block);
            if (!(block instanceof BlockWithMeta))
                throw new IllegalArgumentException();
            this.setHasSubtypes(true);
            this.setMaxDamage(0);
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }

        @Override
        public String getUnlocalizedName(ItemStack stack) {
            return ((BlockWithMeta) block).getUnlocalizedName(stack);
        }

    }

}
