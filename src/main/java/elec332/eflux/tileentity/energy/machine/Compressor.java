package elec332.eflux.tileentity.energy.machine;

import elec332.eflux.inventory.slot.SlotOutput;
import elec332.eflux.tileentity.TileEntityProcessingMachine;
import elec332.eflux.util.EnumMachines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 17-5-2015.
 */
public class Compressor extends TileEntityProcessingMachine {
    public Compressor() {
        super(3);
    }

    @Override
    protected Slot[] getInputSlots() {
        return new Slot[]{
                new Slot(inventory, 0, 56, 17),
                new Slot(inventory, 1, 56, 53)
        };
    }

    @Override
    protected SlotOutput[] getOutputSlots() {
        return oneOutPutSlot(2);
    }

    @Override
    public int getRequiredPowerPerTick() {
        return 50;
    }


    @Override
    protected int getProcessTime() {
        return 70;
    }


    @Override
    protected int getMaxStoredPower() {
        return 2000;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Blocks.iron_block);
    }

    @Override
    public float getAcceptance() {
        return 0.34f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 10;
    }

    @Override
    public EnumMachines getMachine() {
        return EnumMachines.COMPRESSOR;
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(ForgeDirection direction) {
        return 5;
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return basicGui(player);
    }
}
