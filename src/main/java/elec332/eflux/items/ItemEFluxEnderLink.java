package elec332.eflux.items;

import elec332.core.world.WorldHelper;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEnderNetworkItem;
import elec332.eflux.endernetwork.EnderConnectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Elec332 on 16-5-2016.
 */
public class ItemEFluxEnderLink extends AbstractTexturedEFluxItem implements IEnderNetworkItem {

    public ItemEFluxEnderLink() {
        super("enderLink");
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUseC(EntityPlayer playerIn, EnumHand hand, World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        IEnderNetworkComponent component = EnderConnectionHelper.getClearedComponent(WorldHelper.getTileAt(worldIn, pos), null);
        if (component != null) {
            component.setUUID(getNetworkID(stack));
        }
        return super.onItemUseC(playerIn, hand, worldIn, pos, facing, hitX, hitY, hitZ);
    }

    @Override
    public void setNetworkID(UUID uuid, ItemStack stack) {
        if (!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (uuid == null){
            tag.removeTag("netID");
        } else {
            tag.setString("netID", uuid.toString());
        }
    }

    @Override
    @Nullable
    public UUID getNetworkID(ItemStack stack) {
        if (stack.hasTagCompound()){
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("netID")) {
                return UUID.fromString(tag.getString("netID"));
            }
        }
        return null;
    }

}
