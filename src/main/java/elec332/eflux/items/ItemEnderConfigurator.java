package elec332.eflux.items;

import elec332.core.util.PlayerHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.endernetwork.EnderConnectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 9-5-2016.
 */
public class ItemEnderConfigurator extends EFluxItem {

    public ItemEnderConfigurator() {
        super("enderConfigurator");
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote){
            return EnumActionResult.SUCCESS;
        }
        if (stack == null || stack.getItem() != this){
            return EnumActionResult.SUCCESS;
        }
        TileEntity tile = WorldHelper.getTileAt(worldIn, pos);
        IEnderNetworkComponent component = EnderConnectionHelper.getComponent(tile, facing);
        if (component != null){
            if (component.getUuid() == null){
                PlayerHelper.sendMessageToPlayer(playerIn, "You cannot configure an unlinked tile!");
                return EnumActionResult.SUCCESS;
            }
            playerIn.openGui(EFlux.instance, 4, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return EnumActionResult.SUCCESS;
    }

}
