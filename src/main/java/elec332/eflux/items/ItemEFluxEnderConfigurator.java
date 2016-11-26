package elec332.eflux.items;

import elec332.core.util.ItemStackHelper;
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

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 9-5-2016.
 */
public class ItemEFluxEnderConfigurator extends AbstractTexturedEFluxItem {

    public ItemEFluxEnderConfigurator() {
        super("enderConfigurator");
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer playerIn, EnumHand hand, World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote){
            return EnumActionResult.SUCCESS;
        }
        ItemStack stack = playerIn.getHeldItem(hand);
        if (!ItemStackHelper.isStackValid(stack) || stack.getItem() != this){
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
