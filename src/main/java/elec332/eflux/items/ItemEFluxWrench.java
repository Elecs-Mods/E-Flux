package elec332.eflux.items;

import elec332.core.api.wrench.IRightClickCancel;
import elec332.core.api.wrench.IWrenchable;
import elec332.core.client.model.INoJsonItem;
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
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Block block = WorldHelper.getBlockAt(world, pos);
        if (block instanceof IWrenchable) {
            if (player.isSneaking()) {
                world.setBlockToAir(pos);
                if (!world.isRemote) {
                    world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), ((IWrenchable) block).ItemDropped(world, pos)));
                }
            } else if (!world.isRemote){
                ((IWrenchable) block).onWrenched(world, pos, side);
            }
            //player.swingItem();
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        } else if (block.rotateBlock(world, pos, side)) {
            //player.swingItem();
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.SUCCESS;
    }

}