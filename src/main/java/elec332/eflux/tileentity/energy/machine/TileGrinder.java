package elec332.eflux.tileentity.energy.machine;

import elec332.eflux.tileentity.TileEntityProcessingMachine;
import elec332.eflux.util.EnumMachines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 10-5-2015.
 */
public class TileGrinder extends TileEntityProcessingMachine {

    public TileGrinder() {
        super(2, 4);
    }

    @Override
    protected void registerMachineSlots(List<Slot> registerList) {
        oneInputSlot(registerList);
        oneOutPutSlot(registerList);
    }

    @Override
    public int getRequiredPowerPerTick() {
        return 400;
    }

    @Override
    protected int getMaxStoredPower() {
        return 9000;
    }

    @Override
    public int getProcessTime() {
        return 20;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return null;
    }

    @Override
    public float getAcceptance() {
        return 1.0f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 40;
    }

    @Override
    public int getRequestedRP() {
        return 3;
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return basicGui(player);
    }

    @Override
    public EnumMachines getMachine() {
        return EnumMachines.GRINDER;
    }
}
