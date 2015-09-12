package elec332.eflux.multiblock;

import elec332.eflux.recipes.old.EnumRecipeMachine;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Elec332 on 27-8-2015.
 */
public class MultiBlockCompressor extends EFluxMultiBlockProcessingMachine {

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
    protected int getOptimalRP() {
        return 5;
    }

    @Override
    public int getEFForOptimalRP() {
        return 10;
    }

    @Override
    public EnumRecipeMachine getMachine() {
        return EnumRecipeMachine.COMPRESSOR;
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new ResourceLocation("textures/gui/container/furnace.png");
    }

    @Override
    public int getRequiredPower(int startup) {
        return 0;
    }

    @Override
    public int getRequiredPowerAfterStartup() {
        return 0;
    }

    @Override
    public int updateProgressOnItem(int oldProgress, ItemStack stack, int slot) {
        return 0;
    }
}
