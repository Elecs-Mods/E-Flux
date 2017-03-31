package elec332.eflux.blocks;

import elec332.core.tile.AbstractBlock;
import elec332.eflux.items.AbstractTexturedItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 21-7-2015.
 */
public abstract class BlockWithMeta extends AbstractBlock {

    public BlockWithMeta(Material mat, String blockName, String modID) {
        super(mat);
        this.setDefaultState(getStateFromMeta(0));
        this.setUnlocalizedName(modID + "." + blockName);
        this.setRegistryName(new ResourceLocation(modID, blockName));
        this.blockName = blockName;
    }

    private PropertyInteger META;
    protected final String blockName;

    public final PropertyInteger getProperty(){
        if (META == null)
            META = PropertyInteger.create("meta", 0, getTypes()-1);
        return META;
    }

    public IUnlistedProperty[] getUnlistedProperties(){
        return new IUnlistedProperty[0];
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getBlockState().getBaseState().withProperty(getProperty(), meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(getProperty());
    }

    public BlockWithMeta register(){
        GameRegistry.register(this);
        GameRegistry.register(new MetaItemBlock(this).setRegistryName(getRegistryName()));
        return this;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{getProperty()}, getUnlistedProperties());
    }

    public String getUnlocalizedName(ItemStack stack){
        return getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public void getSubBlocksC(@Nonnull Item item, List<ItemStack> subBlocks, CreativeTabs creativeTab) {
        for (int i = 0; i < getTypes(); i++) {
            subBlocks.add(new ItemStack(item, 1, i));
        }
    }

    public abstract int getTypes();

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    public static class MetaItemBlock extends AbstractTexturedItemBlock {

        public MetaItemBlock(Block block) {
            super(block);
            if (!(block instanceof BlockWithMeta)) {
                throw new IllegalArgumentException();
            }
            this.setHasSubtypes(true);
            this.setMaxDamage(0);
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }

        @Override
        @Nonnull
        public String getUnlocalizedName(ItemStack stack) {
            return ((BlockWithMeta) block).getUnlocalizedName(stack);
        }

    }

}
