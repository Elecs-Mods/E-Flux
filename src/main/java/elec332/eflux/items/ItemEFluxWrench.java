package elec332.eflux.items;

import elec332.core.api.util.IRightClickCancel;
import elec332.core.api.wrench.IWrenchable;
import elec332.core.client.model.loading.INoJsonItem;
import elec332.core.world.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class ItemEFluxWrench extends AbstractTexturedEFluxItem implements IRightClickCancel, INoJsonItem {

    public ItemEFluxWrench() {
        super("wrench");
        setContainerItem(this);
        setNoRepair();
        setMaxDamage(72);
        setMaxStackSize(1);
    }

    @Override
    @Nonnull
    public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    @Nonnull
    @Override
    protected EnumActionResult onItemUse(EntityPlayer player, EnumHand hand, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            Block block = WorldHelper.getBlockAt(world, pos);
            if (block instanceof IWrenchable) {
                if (player.isSneaking()) {
                    world.setBlockToAir(pos);
                    world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), ((IWrenchable) block).itemDropped(world, pos)));
                } else if (!((IWrenchable) block).onWrenched(world, pos, side) && !block.rotateBlock(world, pos, side)){
                    return EnumActionResult.SUCCESS;
                }
                stack.damageItem(1, player);
                player.swingArm(hand);
            } else if (block.rotateBlock(world, pos, side)){
                stack.damageItem(1, player);
                player.swingArm(hand);
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
