package elec332.eflux.tileentity.ender;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.tile.IInventoryTile;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.inventory.GuiMachine;
import elec332.eflux.inventory.ContainerEnderInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Created by Elec332 on 9-5-2016.
 */
@RegisterTile(name = "testert")
public class TileEntityEnderChest extends AbstractEnderTileEntity<IItemHandler> {

    public TileEntityEnderChest() {
        super(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldObj.isRemote && getEnderHandler().getCurrentConnection() != null && getEnderHandler().getCurrentConnection().get() != null){
            System.out.println("opening");
            openGui(player, EFlux.instance, 5);
        }
        return false;
    }

}
