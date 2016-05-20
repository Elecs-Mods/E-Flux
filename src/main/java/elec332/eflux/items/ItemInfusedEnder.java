package elec332.eflux.items;

import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEnderConnection;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import elec332.eflux.endernetwork.EnderConnectionHelper;
import elec332.eflux.endernetwork.ItemConnection;
import elec332.eflux.init.ItemRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Elec332 on 4-5-2016.
 */
public class ItemInfusedEnder extends EFluxItem {

    public ItemInfusedEnder() {
        super("infusedEnder");
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IEnderNetworkComponent component = EnderConnectionHelper.getClearedComponent(WorldHelper.getTileAt(worldIn, pos), facing);
        if (component != null){
            component.setUUID(getUUID(stack));
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Nullable
    public static UUID getUUID(ItemStack stack){
        if (stack != null && stack.getItem() == ItemRegister.entangledEnder.getItem() && stack.hasTagCompound() && stack.getTagCompound().hasKey("nUUID")){
            return UUID.fromString(stack.getTagCompound().getString("nUUID"));
        }
        return null;
    }

    @Nonnull
    public static ItemStack createStack(@Nonnull UUID uuid){
        ItemStack stack = ItemRegister.entangledEnder.copy();
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("nUUID", uuid.toString());
        stack.setTagCompound(tag);
        return stack;
    }

}
