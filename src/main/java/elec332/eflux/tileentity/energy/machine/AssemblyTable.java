package elec332.eflux.tileentity.energy.machine;

import elec332.core.baseclasses.tileentity.IInventoryTile;
import elec332.core.util.BasicInventory;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.client.inventory.GuiStandardFormat;
import elec332.eflux.inventory.ContainerAssemblyTable;
import elec332.eflux.tileentity.BreakableReceiverTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class AssemblyTable extends BreakableReceiverTile implements IInventoryTile{

    private BasicInventory inv = new BasicInventory("SolderStuff", 1){
        @Override
        public boolean isItemValidForSlot(int id, ItemStack stack) {
            return stack.getItem() instanceof ICircuit;
        }
    };

    @Override
    public ItemStack getRandomRepairItem() {
        return null;
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
    public boolean onBlockActivatedSafe(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        player.openGui(EFlux.instance, 0, worldObj, xCoord, yCoord, zCoord);
        return true;
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new GuiStandardFormat(new ContainerAssemblyTable(player, inv), new ResourceLocation("textures/gui/container/crafting_table.png"));
    }

    @Override
    public Container getGuiServer(EntityPlayer player) {
        return new ContainerAssemblyTable(player, inv);
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
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return direction == ForgeDirection.DOWN;
    }

    @Override
    public String[] getProvidedData() {
        return new String[]{"broken: "+isBroken()};
    }
}
