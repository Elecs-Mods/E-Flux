package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.inventory.BaseContainer;
import elec332.core.tile.IInventoryTile;
import elec332.core.util.BasicInventory;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.client.inventory.GuiStandardFormat;
import elec332.eflux.inventory.ContainerAssemblyTable;
import elec332.eflux.tileentity.BreakableMachineTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 4-5-2015.
 */
@RegisterTile(name = "TileEntityEFluxAssemblyTable")
public class AssemblyTable extends BreakableMachineTile implements IInventoryTile{

    private BasicInventory inv = new BasicInventory("SolderStuff", 1){

        @Override
        public boolean isItemValidForSlot(int id, ItemStack stack) {
            return CircuitHelper.getCircuit(stack) != null;
        }

        @Override
        public int getInventoryStackLimit() {
            return 1;
        }

    };

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.IRON_HOE);
    }

    @Override
    public float getAcceptance() {
        return 0.10f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 50;
    }

    @Override
    public int getRequestedRP() {
        return 20;
    }


    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        inv.writeToNBT(tagCompound);
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        inv.readFromNBT(tagCompound);
    }

    @Override
    public boolean onBlockActivatedSafe(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        openGui(player, EFlux.instance, 0);
        return true;
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new GuiStandardFormat((BaseContainer)getGuiServer(player), new ResourceLocation("textures/gui/container/crafting_table.png")){
            @Override //handleMouseClick
            protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, ClickType clickType) {
                if ((!((ContainerAssemblyTable)inventorySlots).canClick) && slotId > 0 && slotId < 10) {
                    //System.out.println("nope   "+((ContainerAssemblyTable)inventorySlots).canClick);
                    return;
                }
                super.handleMouseClick(slotIn, slotId, clickedButton, clickType);
            }

        };
    }

    @Override
    public Container getGuiServer(EntityPlayer player) {
        return new ContainerAssemblyTable(player, inv, energyContainer);
    }

    @Override
    protected int getMaxStoredPower() {
        return 500;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
        return direction == EnumFacing.DOWN;
    }

}
