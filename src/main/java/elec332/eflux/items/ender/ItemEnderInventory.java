package elec332.eflux.items.ender;

import elec332.core.inventory.window.WindowManager;
import elec332.eflux.EFlux;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IWeakEnderConnection;
import elec332.eflux.endernetwork.util.DefaultEnderConnectableItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 14-5-2016.
 */
public class ItemEnderInventory extends AbstractEnderCapabilityItem<IItemHandler> {

    public ItemEnderInventory() {
        super("inventoryviewer");
    }

    @Override
    protected ActionResult<ItemStack> execute(@Nonnull IEnderNetworkComponent<IItemHandler> component, @Nonnull IWeakEnderConnection<IItemHandler> connection, @Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote){
            WindowManager.openWindow(player, EFlux.proxy, world, -3, hand == EnumHand.MAIN_HAND ? -3 : -9, -3, (byte) 5);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    protected IEnderNetworkComponent<IItemHandler> createNewComponent(ItemStack stack) {
        return new DefaultEnderConnectableItem<>(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

}
