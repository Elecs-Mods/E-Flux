package elec332.eflux.items;

import elec332.core.api.wrench.IRightClickCancel;
import elec332.core.helper.RegisterHelper;
import elec332.eflux.EFlux;
import elec332.eflux.tileentity.BreakableMachineTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 15-5-2015.
 */
public class Breaker extends Item implements IRightClickCancel{
    public Breaker(){
        String name = "Breaker";
        setCreativeTab(EFlux.CreativeTab);
        setUnlocalizedName(EFlux.ModID + "." + name);
        setTextureName(EFlux.ModID + ":" + name);
        setContainerItem(this);
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
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!world.isRemote) {
            if (tileEntity instanceof BreakableMachineTile)
                ((BreakableMachineTile)tileEntity).setBroken(true);
            return true;
        }
        return false;
    }
}
