package elec332.eflux.tileentity.energy.machine;

import elec332.core.baseclasses.tileentity.IInventoryTile;
import elec332.core.client.inventory.BaseGuiContainer;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.inventory.slot.SlotOutput;
import elec332.core.util.BasicInventory;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.tileentity.BreakableMachineTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 12-9-2015.
 */
public class TileScanner extends BreakableMachineTile implements IInventoryTile, ITileWithSlots, ISidedInventory{

    public TileScanner(){
        inventory = new BasicInventory("y", 2, this);
    }

    private BasicInventory inventory;

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.blaze_powder);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (timeCheck() && inventory.getStackInSlot(0) != null && inventory.getStackInSlot(0).getItem() == ItemRegister.groundMesh && inventory.getStackInSlot(1) == null && energyContainer.drainPower(150)){
            ItemStack stack = inventory.getStackInSlot(0).copy();
            stack.stackTagCompound.setBoolean("dusts_scanned", true);
            inventory.setInventorySlotContents(0, null);
            inventory.setInventorySlotContents(1, stack);
        }
    }

    @Override
    public boolean onBlockActivatedSafe(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return openGui(player, EFlux.instance, 0);
    }

    @Override
    public float getAcceptance() {
        return 0.03f;
    }

    @Override
    protected int getMaxStoredPower() {
        return 400;
    }

    @Override
    public int getEFForOptimalRP() {
        return 40;
    }

    @Override
    public int getRequestedRP() {
        return 4;
    }

    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return direction != getTileFacing();
    }

    @Override
    public String[] getProvidedData() {
        return new String[0];
    }

    @Override
    public BaseContainer getGuiServer(EntityPlayer player) {
        return new ContainerMachine(this, player, 0);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new BaseGuiContainer(getGuiServer(player)) {
            @Override
            public ResourceLocation getBackgroundImageLocation() {
                return new EFluxResourceLocation("nope");
            }
        };
    }

    @Override
    public void addSlots(BaseContainer container) {
        container.addSlotToContainer(new Slot(inventory, 0, 56, 35));
        container.addSlotToContainer(new SlotOutput(inventory, 1, 116, 35));
        container.addPlayerInventoryToContainer();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slot == 0 && stack.getItem() == ItemRegister.groundMesh;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 1;
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public String getInventoryName() {
        return inventory.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return inventory.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public void openInventory() {
        inventory.openInventory();
    }

    @Override
    public void closeInventory() {
        inventory.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }
}
