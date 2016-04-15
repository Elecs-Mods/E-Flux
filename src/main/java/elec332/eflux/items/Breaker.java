package elec332.eflux.items;

import elec332.core.api.wrench.IRightClickCancel;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.tileentity.BreakableMachineTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
        GameRegistry.registerItem(this, name);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumHand side, EnumFacing hitX, float hitY, float hitZ, float p_180614_9_) {
        TileEntity tileEntity = WorldHelper.getTileAt(world, pos);
        if (!world.isRemote) {
            if (tileEntity instanceof BreakableMachineTile)
                ((BreakableMachineTile)tileEntity).setBroken(true);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
