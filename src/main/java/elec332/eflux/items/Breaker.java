package elec332.eflux.items;

import elec332.core.api.wrench.IRightClickCancel;
import elec332.core.util.RegisterHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.tileentity.BreakableMachineTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 15-5-2015.
 */
public class Breaker extends Item implements IRightClickCancel{
    public Breaker(){
        String name = "Breaker";
        setCreativeTab(EFlux.creativeTab);
        setUnlocalizedName(EFlux.ModID + "." + name);
        //setTextureName(EFlux.ModID + ":" + name);
        setContainerItem(this);
        setMaxStackSize(1);
        RegisterHelper.registerItem(this, name);
    }

    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float HitX, float HitY, float HitZ) {
        TileEntity tileEntity = WorldHelper.getTileAt(world, pos);
        if (!world.isRemote) {
            if (tileEntity instanceof BreakableMachineTile)
                ((BreakableMachineTile)tileEntity).setBroken(true);
            return true;
        }
        return false;
    }
}
