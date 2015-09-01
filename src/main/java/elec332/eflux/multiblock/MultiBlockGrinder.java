package elec332.eflux.multiblock;

import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.recipes.EnumRecipeMachine;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Elec332 on 27-8-2015.
 */
public class MultiBlockGrinder extends EFluxMultiBlockProcessingMachine {

    public MultiBlockGrinder() {
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
    public float getAcceptance() {
        return 1.0f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 40;
    }

    @Override
    public int getOptimalRP() {
        return 3;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.iron_ingot);
    }

    @Override
    public EnumRecipeMachine getMachine() {
        return EnumRecipeMachine.GRINDER;
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new EFluxResourceLocation("yew");
    }
}
