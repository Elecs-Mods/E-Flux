package elec332.eflux.items;

import elec332.core.world.WorldHelper;
import elec332.eflux.endernetwork.ILinkableItem;
import elec332.eflux.tileentity.misc.TileEntityEFluxSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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

    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = WorldHelper.getTileAt(worldIn, pos);

        if (!(tile instanceof TileEntityMobSpawner)/* || !hasLink(stack)*/) {
            return false;
        }

        NBTTagCompound spawnerTag = new NBTTagCompound();
        ((TileEntityMobSpawner) tile).getSpawnerBaseLogic().writeToNBT(spawnerTag);

        if (stack.stackSize == 0) {
            return false;
        } else if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        } else {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn);

            if (placeBlockAt(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ, iblockstate)) {
                worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
                --stack.stackSize;
            }
            tile = WorldHelper.getTileAt(worldIn, pos);
            if (tile instanceof TileEntityEFluxSpawner){
                ((TileEntityEFluxSpawner) tile).readFromOldSpawner(spawnerTag);
            }

            return true;
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
