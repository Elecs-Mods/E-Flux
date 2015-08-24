package elec332.eflux.tileentity.energy.machine;

import elec332.eflux.inventory.slot.SlotFurnaceInput;
import elec332.eflux.tileentity.TileEntityProcessingMachine;
import elec332.eflux.util.EnumMachines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 5-5-2015.
 */
public class TileFurnace extends TileEntityProcessingMachine {

    public TileFurnace() {
        super(2, 4);
    }

    private SlotFurnaceInput getInputSlot(){
        return new SlotFurnaceInput(this, 0, 56, 35);
    }

    @Override
    protected void registerMachineSlots(List<Slot> registerList) {
        registerList.add(getInputSlot());
        oneOutPutSlot(registerList);
    }

    @Override
    public int getRequiredPowerPerTick() {
        return 50;
    }

    @Override
    protected int getMaxStoredPower() {
        return 5000;
    }

    @Override
    public int getProcessTime() {
        return 20;
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return basicGui(player);
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.iron_ingot);
    }

    @Override
    public float getAcceptance() {
        return 0.5f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 7;
    }

    @Override
    public EnumMachines getMachine() {
        return EnumMachines.FURNACE;
    }

    @Override
    public int getRequestedRP() {
        return 5;
    }

    @Override
    public boolean canProcess() {
        if (inventory.getStackInSlot(0) == null)
            return false;
        ItemStack result = getInputSlot().getOutput();
        return result != null && inventory.canAddItemStackFully(result, 1, true);
    }

    @Override
    public void onProcessDone() {
        if (inventory.getStackInSlot(1) == null)
            inventory.setInventorySlotContents(1, getInputSlot().getOutput().copy());
        else inventory.getStackInSlot(1).stackSize += getInputSlot().getOutput().stackSize;
        getInputSlot().consumeOnProcess();
        notifyNeighboursOfDataChange();
        syncData();
    }
}
