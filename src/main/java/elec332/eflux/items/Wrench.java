package elec332.eflux.items;

import elec332.core.api.wrench.IWrenchable;
import elec332.core.helper.RegisterHelper;
import elec332.eflux.EFlux;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class Wrench extends Item {
    public Wrench(String name) {
        setCreativeTab(EFlux.CreativeTab);
        setUnlocalizedName(EFlux.ModID + "." + name);
        setTextureName(EFlux.ModID + ":" + name);
        setContainerItem(this);
        setNoRepair();
        setMaxDamage(72);
        setMaxStackSize(1);
        RegisterHelper.registerItem(this, name);
    }

    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
        return false;
    }

    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float HitX, float HitY, float HitZ) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof IWrenchable) {
            if (player.isSneaking()) {
                world.setBlockToAir(x, y, z);
                if (!world.isRemote) {
                    world.spawnEntityInWorld(new EntityItem(world, x, y, z, ((IWrenchable) block).ItemDropped()));
                }
            } else {
                ((IWrenchable) block).onWrenched(world, x, y, z, ForgeDirection.getOrientation(side));
            }
            player.swingItem();
            itemStack.damageItem(1, player);
            return true;
        } else if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
            player.swingItem();
            itemStack.damageItem(1, player);
            return true;
        }
        return false;
    }
}