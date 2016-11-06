package elec332.eflux.items;

import com.google.common.base.Objects;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.endernetwork.EnderConnectionHelper;
import elec332.eflux.init.ItemRegister;
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
import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 4-5-2016.
 */
public class ItemEFluxInfusedEnder extends AbstractTexturedEFluxItem {

    public ItemEFluxInfusedEnder() {
        super("infusedEnder");
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IEnderNetworkComponent component = EnderConnectionHelper.getClearedComponent(WorldHelper.getTileAt(worldIn, pos), facing);
        if (component != null){
            component.setUUID(getUUID(stack));
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add("Network ID: "+getUUID(stack));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return isActive(stack);
    }

    @Nullable
    public static UUID getUUID(ItemStack stack){
        if (stack != null && stack.getItem() == ItemRegister.entangledEnder && stack.hasTagCompound() && stack.getTagCompound().hasKey("nUUID")){
            return UUID.fromString(stack.getTagCompound().getString("nUUID"));
        }
        return null;
    }

    @Nullable
    public static NBTTagCompound getNetworkData(ItemStack stack){
        if (stack != null && stack.getItem() == ItemRegister.entangledEnder && stack.hasTagCompound() && stack.getTagCompound().hasKey("nData")){
            return stack.getTagCompound().getCompoundTag("nData");
        }
        return null;
    }

    @Nonnull
    public static ItemStack createStack(@Nonnull UUID uuid, @Nullable NBTTagCompound data){
        ItemStack stack = new ItemStack(ItemRegister.entangledEnder);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("nUUID", uuid.toString());
        if (data != null) {
            tag.setTag("nData", data);
        }
        stack.setTagCompound(tag);
        return stack;
    }

    public static boolean isActive(ItemStack stack) {
        return stack != null && stack.getItem() == ItemRegister.entangledEnder && stack.getItemDamage() == 0;
    }

}
