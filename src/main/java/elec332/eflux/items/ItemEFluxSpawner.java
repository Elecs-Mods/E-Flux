package elec332.eflux.items;

import elec332.core.world.WorldHelper;
import elec332.eflux.endernetwork.ILinkableItem;
import elec332.eflux.tileentity.misc.TileEntityEFluxSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
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

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Elec332 on 21-2-2016.
 */
public class ItemEFluxSpawner extends ItemBlock implements ILinkableItem {

    public ItemEFluxSpawner(Block block) {
        super(block);
    }

    @Override
    @SuppressWarnings("all")
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand side, EnumFacing hitX, float hitY, float hitZ, float p_180614_9_) {
        TileEntity tile = WorldHelper.getTileAt(worldIn, pos);

        if (!(tile instanceof TileEntityMobSpawner)/* || !hasLink(stack)*/) {
            return EnumActionResult.FAIL;
        }

        NBTTagCompound spawnerTag = new NBTTagCompound();
        ((TileEntityMobSpawner) tile).getSpawnerBaseLogic().writeToNBT(spawnerTag);

        if (stack.stackSize != 0 && playerIn.canPlayerEdit(pos, hitX, stack) && worldIn.canBlockBePlaced(this.block, pos, false, hitX, null, stack)) {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, hitX, hitY, hitZ, p_180614_9_, i, playerIn);

            if (placeBlockAt(stack, playerIn, worldIn, pos, hitX, hitY, hitZ, p_180614_9_, iblockstate1)) {
                SoundType soundtype = this.block.getSoundType();
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

    @Override
    @Nullable
    public UUID getLinkID(ItemStack stack) {
        return stack.hasTagCompound() ? UUID.fromString(stack.getTagCompound().getString("lID")) : null;
    }

    @Override
    public boolean hasLink(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("lID");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack);
    }

    @Override
    public void setLinkID(ItemStack stack, @Nullable UUID newID) {
        if (!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }
        if (newID == null){
            stack.getTagCompound().removeTag("lID");
            return;
        }
        stack.getTagCompound().setString("lID", newID.toString());
    }

}
