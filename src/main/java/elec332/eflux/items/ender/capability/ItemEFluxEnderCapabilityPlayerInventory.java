package elec332.eflux.items.ender.capability;

import elec332.core.util.PlayerHelper;
import elec332.eflux.init.CapabilityRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 7-5-2016.
 */
public class ItemEFluxEnderCapabilityPlayerInventory extends ItemEFluxEnderCapability {

    public ItemEFluxEnderCapabilityPlayerInventory() {
        super(CapabilityRegister.playerInventory);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(EntityPlayer player, @Nonnull EnumHand hand, World world) {
        if (!(player instanceof FakePlayer)) {
            ItemStack itemStack = player.getHeldItem(hand);
            if (!itemStack.hasTagCompound()) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            itemStack.getTagCompound().setString("enderPlayer", PlayerHelper.getPlayerUUID(player).toString());
        }
        return super.onItemRightClick(player, hand, world);
    }

}
