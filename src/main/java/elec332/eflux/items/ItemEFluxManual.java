package elec332.eflux.items;

import elec332.core.inventory.window.WindowManager;
import elec332.eflux.client.manual.gui.WindowManual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 30-1-2016.
 */
public class ItemEFluxManual extends AbstractTexturedEFluxItem {

    public ItemEFluxManual() {
        super("manual");
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClickC(EntityPlayer playerIn, @Nonnull EnumHand hand, World worldIn) {
        if (worldIn.isRemote){
            WindowManager.openClientWindow(new WindowManual());
        }
        //WindowManager.openWindow(playerIn, EFlux.proxy, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ, (byte) 3);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    }

}
