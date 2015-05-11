package elec332.eflux.tileentity;

import elec332.eflux.client.inventory.GuiMachine;
import elec332.eflux.inventory.slot.SlotInput;
import elec332.eflux.inventory.slot.SlotMachine;
import elec332.eflux.inventory.slot.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 8-5-2015.
 */
public abstract class TileEntityProcessingMachineSingleSlot extends TileEntityProcessingMachine {

    public TileEntityProcessingMachineSingleSlot() {
        super(2);
    }

    @Override
    protected SlotInput[] getInputSlots() {
        return new SlotInput[]{
                getInputSlot()
        };
    }

    @Override
    protected SlotOutput[] getOutputSlots() {
        return new SlotOutput[]{
                getOutputSlot()
        };
    }

    protected SlotInput getInputSlot() {
        return new SlotMachine(this, 0, 56, 35);
    }

    protected SlotOutput getOutputSlot() {
        return new SlotOutput(inventory, 1, 116, 35);
    }

    @Override
    protected boolean canProcess() {
        if (inventory.getStackInSlot(0) == null)
            return false;
        ItemStack result = getInputSlot().getOutput();
        return result != null && inventory.canAddItemStackFully(result, 1, true);
    }

    @Override
    protected void onProcessDone() {
        if (inventory.getStackInSlot(1) == null)
            inventory.setInventorySlotContents(1, getInputSlots()[0].getOutput().copy());
        else inventory.getStackInSlot(1).stackSize += getInputSlots()[0].getOutput().stackSize;
        getInputSlots()[0].consumeOnProcess();
        notifyNeighboursOfDataChange();
        syncData();
    }

    protected GuiMachine basicGui(EntityPlayer player){
        return new GuiMachine(getGuiServer(player)) {
            @Override
            public ResourceLocation getBackgroundImageLocation() {
                return new ResourceLocation("textures/gui/container/furnace.png");
            }
        };
    }
}
