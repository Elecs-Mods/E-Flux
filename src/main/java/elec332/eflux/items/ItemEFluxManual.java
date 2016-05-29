package elec332.eflux.items;

import elec332.eflux.EFlux;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 30-1-2016.
 */
public class ItemEFluxManual extends AbstractTexturedEFluxItem {

    public ItemEFluxManual() {
        super("manual");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.openGui(EFlux.instance, 3, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

}
