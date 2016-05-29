package elec332.eflux.items.ender;

import elec332.core.util.PlayerHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IWeakEnderConnection;
import elec332.eflux.endernetwork.util.DefaultEnderConnectableItem;
import elec332.eflux.items.AbstractTexturedEFluxItem;
import elec332.eflux.util.CapabilityWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 14-5-2016.
 */
public abstract class AbstractEnderCapabilityItem<T> extends AbstractTexturedEFluxItem {

    public AbstractEnderCapabilityItem(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        stack = player.getHeldItem(hand); //Just to make sure.
        if (stack != null && stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)){
            @SuppressWarnings("unchecked")
            IEnderNetworkComponent<T> component = stack.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null);
            if (component != null){
                IWeakEnderConnection<T> connection = (IWeakEnderConnection<T>) component.getCurrentConnection();
                return onRightClick(component, connection, stack, world, player, hand);
            }
        }
        return super.onItemRightClick(stack, world, player, hand);
    }

    protected ActionResult<ItemStack> onRightClick(@Nonnull IEnderNetworkComponent<T> component, @Nullable IWeakEnderConnection<T> connection, @Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand){
        if (connection != null && connection.isValid()){
            return execute(component, connection, stack, world, player, hand);
        } else {
            if (!world.isRemote){
                PlayerHelper.sendMessageToPlayer(player, "Connecting...");
            }
            boolean b = ((DefaultEnderConnectableItem)component).connect(world);
            if (!world.isRemote){
                PlayerHelper.sendMessageToPlayer(player, b ? "Succes!" : "Failed");
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
    }

    protected IWeakEnderConnection<T> getCurrentConnection(ItemStack stack){
        if (stack != null && stack.getItem() == this && stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)) {
            @SuppressWarnings("unchecked")
            IEnderNetworkComponent<T> component = stack.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null);
            if (component != null) {
                return (IWeakEnderConnection<T>) component.getCurrentConnection();
            }
        }
        return null;
    }

    protected abstract ActionResult<ItemStack> execute(@Nonnull IEnderNetworkComponent<T> component, @Nonnull IWeakEnderConnection<T> connection, @Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand);

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return CapabilityWrapper.getProviderFor(EFluxAPI.ENDER_COMPONENT_CAPABILITY, createNewComponent(stack));
    }

    protected abstract IEnderNetworkComponent<T> createNewComponent(ItemStack stack);

}
