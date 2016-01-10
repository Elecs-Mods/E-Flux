package elec332.eflux.items;

import elec332.core.api.wrench.IRightClickCancel;
import elec332.core.api.wrench.IWrenchable;
import elec332.core.util.RegisterHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class Wrench extends Item implements IRightClickCancel {
    public Wrench(String name) {
        setCreativeTab(EFlux.creativeTab);
        setUnlocalizedName(EFlux.ModID + "." + name);
        //setTextureName(EFlux.ModID + ":" + name);
        setContainerItem(this);
        setNoRepair();
        setMaxDamage(72);
        setMaxStackSize(1);
        RegisterHelper.registerItem(this, name);
    }

    //@Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float HitX, float HitY, float HitZ) {
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
            player.swingItem();
            itemStack.damageItem(1, player);
            return false;
        } else if (block.rotateBlock(world, pos, side)) {
            player.swingItem();
            itemStack.damageItem(1, player);
            return false;
        }
        return false;
    }
}
