package elec332.eflux.tileentity.energy.machine;

import elec332.eflux.inventory.slot.SlotFurnaceInput;
import elec332.eflux.inventory.slot.SlotInput;
import elec332.eflux.inventory.slot.SlotOutput;
import elec332.eflux.tileentity.TileEntityProcessingMachineSingleSlot;
import elec332.eflux.util.EnumMachines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 5-5-2015.
 */
public class TileFurnace extends TileEntityProcessingMachineSingleSlot {

    @Override
    protected SlotInput getInputSlot() {
        return new SlotFurnaceInput(this, 0, 56, 35);
    }

    @Override
    protected SlotOutput getOutputSlot() {
        return new SlotOutput(inventory, 1, 116, 35);
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
    protected int getProcessTime() {
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

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(ForgeDirection direction) {
        return 5;
    }
}
