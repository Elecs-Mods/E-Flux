package elec332.eflux.tileentity.energy.machine.furnace;

import elec332.eflux.inventory.slot.SlotFurnaceInput;
import elec332.eflux.inventory.slot.SlotInput;
import elec332.eflux.inventory.slot.SlotOutput;
import elec332.eflux.tileentity.TileEntityProcessingMachineSingleSlot;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Elec332 on 5-5-2015.
 */
public abstract class TileFurnaceBase extends TileEntityProcessingMachineSingleSlot {


    @Override
    protected SlotInput getInputSlot() {
        return new SlotFurnaceInput(this, 0, 56, 35);
    }

    @Override
    protected SlotOutput getOutputSlot() {
        return new SlotOutput(inventory, 1, 116, 35);
    }

    @Override
    protected int getProcessTime() {
        return 20;
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return basicGui(player);
    }
}
