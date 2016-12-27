package elec332.eflux.items;

import elec332.core.item.AbstractItemBlock;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.misc.TileEntityEFluxSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 21-2-2016.
 */
public class ItemBlockEFluxSpawner extends AbstractItemBlock {

    public ItemBlockEFluxSpawner(Block block) {
        super(block);
    }

    @Nonnull
    @Override
    protected EnumActionResult onItemUse(EntityPlayer playerIn, EnumHand hand, World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = WorldHelper.getTileAt(worldIn, pos);
        ItemStack stack = playerIn.getHeldItem(hand);
        if (!(tile instanceof TileEntityMobSpawner)/* || !hasLink(stack)*/) {
            return EnumActionResult.FAIL;
        }

        NBTTagCompound spawnerTag = new NBTTagCompound();
        ((TileEntityMobSpawner) tile).getSpawnerBaseLogic().writeToNBT(spawnerTag);

        if (stack.stackSize != 0 && playerIn.canPlayerEdit(pos, facing, stack) && WorldHelper.canBlockBePlaced(worldIn, this.block, pos, false, facing, null)) {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, playerIn, hand);

            if (placeBlockAt(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
                SoundType soundtype = this.block.getSoundType(iblockstate1, worldIn, pos, playerIn);
                worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                --stack.stackSize;
            }
            tile = WorldHelper.getTileAt(worldIn, pos);
            if (tile instanceof TileEntityEFluxSpawner){
                ((TileEntityEFluxSpawner) tile).readFromOldSpawner(spawnerTag);
            }
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

}
