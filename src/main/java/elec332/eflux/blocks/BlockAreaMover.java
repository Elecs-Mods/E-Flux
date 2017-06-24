package elec332.eflux.blocks;

import elec332.core.client.model.loading.INoBlockStateJsonBlock;
import elec332.core.tile.AbstractBlock;
import elec332.core.util.ItemStackHelper;
import elec332.core.util.RegistryHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.items.AbstractTexturedItemBlock;
import elec332.eflux.tileentity.misc.TileEntityAreaMover;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class BlockAreaMover extends AbstractBlock implements ITileEntityProvider, INoBlockStateJsonBlock.DefaultImpl {

    public BlockAreaMover() {
        super(Material.ROCK);
        setUnlocalizedName(EFlux.ModID.toLowerCase()+".areaMover");
        setRegistryName(new EFluxResourceLocation("areaMover"));
    }

    public BlockAreaMover register(){
        RegistryHelper.register(this);
        RegistryHelper.register(new BAMItemBlock(this).setRegistryName(getRegistryName()));
        return this;
    }

    @Override
    public void getSubBlocksC(@Nonnull Item item, List<ItemStack> subBlocks, CreativeTabs creativeTab) {
        for (int i = 0; i < 3; i++) {
            subBlocks.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileEntityAreaMover();
    }

    @Override
    public void addInformationC(ItemStack stack, World player, List<String> tooltip, boolean advanced) {
        super.addInformationC(stack, player, tooltip, advanced);
        int range = TileEntityAreaMover.getRange(stack.getItemDamage());
        tooltip.add("Range: " + range + 2);
    }

    public class BAMItemBlock extends AbstractTexturedItemBlock {

        private BAMItemBlock(Block block) {
            super(block);
        }

        @Override
        @Nonnull
        public String getUnlocalizedName(ItemStack stack) {
            boolean creative = stack.getItemDamage() > 3;
            return super.getUnlocalizedName(stack) + "." + (creative ? "creative" : stack.getItemDamage());
        }

        @Override
        @Nonnull
        public EnumActionResult onItemUseC(EntityPlayer playerIn, EnumHand hand, World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
            ItemStack stack = ItemStackHelper.copyItemStack(playerIn.getHeldItem(hand));
            EnumActionResult ret = super.onItemUseC(playerIn, hand, worldIn, pos, facing, hitX, hitY, hitZ);
            if (ret == EnumActionResult.SUCCESS){
                IBlockState iblockstate = worldIn.getBlockState(pos);
                Block block = iblockstate.getBlock();
                if (!block.isReplaceable(worldIn, pos)) {
                    pos = pos.offset(facing);
                }
                TileEntity tile = WorldHelper.getTileAt(worldIn, pos);
                if (tile instanceof TileEntityAreaMover){
                    ((TileEntityAreaMover) tile).setRange(stack.getItemDamage());
                }
            }
            return ret;
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }

    }

}
