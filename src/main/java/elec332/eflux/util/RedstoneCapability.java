package elec332.eflux.util;

import com.google.common.base.Predicate;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.inventory.widget.WidgetButton;
import elec332.core.inventory.widget.WidgetEnumChange;
import elec332.core.util.InventoryHelper;
import elec332.core.util.PlayerHelper;
import elec332.eflux.EFlux;
import elec332.eflux.init.ItemRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by Elec332 on 27-4-2016.
 */
public class RedstoneCapability implements ITileWithSlots, INBTSerializable<NBTTagCompound>, WidgetButton.IButtonEvent {

    @CapabilityInject(RedstoneCapability.class)
    public static Capability<RedstoneCapability> CAPABILITY;

    public RedstoneCapability(Predicate<IRedstoneUpgradable.Mode> predicate){
        this.predicate = predicate;
    }

    public boolean onActivated(EntityPlayer player, ItemStack stack){
        if (InventoryHelper.areEqualNoSizeNoNBT(stack, ItemRegister.redstoneUpgrade)){
            player.inventory.decrStackSize(player.inventory.getSlotFor(stack), 1);
            upgraded = true;
            return true;
        }
        if (stack == null && upgraded) {
            if (!player.worldObj.isRemote) {
                BlockPos pos = PlayerHelper.getPosPlayerIsLookingAt(player, 6D).getBlockPos();
                player.openGui(EFlux.instance, 2, player.worldObj, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
        return false;
    }

    private boolean upgraded = true, check = true, redstone;
    private IRedstoneUpgradable.Mode mode = IRedstoneUpgradable.Mode.HIGH;
    private final Predicate<IRedstoneUpgradable.Mode> predicate;

    public void onNeighborChanged(World world, BlockPos pos){
        check = true;
    }

    public boolean isPowered(World world, BlockPos pos){
        if (check){
            redstone = world.isBlockIndirectlyGettingPowered(pos) > 0;
            check = false;
        }
        return mode.isPowered(redstone);
    }

    @Override
    public void addSlots(BaseContainer baseContainer) {
        WidgetEnumChange<IRedstoneUpgradable.Mode> widget = new WidgetEnumChange<IRedstoneUpgradable.Mode>(20, 20, 70, 20, IRedstoneUpgradable.Mode.class);
        widget.setEnum(mode);
        baseContainer.addWidget(widget.addButtonEvent(this));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setBoolean("u", upgraded);
        ret.setString("m", mode.toString());
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        upgraded = nbt.getBoolean("u");
        mode = IRedstoneUpgradable.Mode.valueOf(nbt.getString("m"));
    }

    @Override
    public void onButtonClicked(WidgetButton widgetButton) {
        mode = (IRedstoneUpgradable.Mode)((WidgetEnumChange)widgetButton).getEnum();
        System.out.println("click");
        System.out.println(mode);
    }

}
