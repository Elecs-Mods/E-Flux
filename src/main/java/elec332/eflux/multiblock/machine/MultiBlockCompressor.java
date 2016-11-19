package elec332.eflux.multiblock.machine;

import elec332.core.util.ItemStackHelper;
import elec332.eflux.EFlux;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.multiblock.EFluxMultiBlockProcessingMachine;
import elec332.eflux.recipes.CompressorRecipes;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 27-8-2015.
 */
public class MultiBlockCompressor extends EFluxMultiBlockProcessingMachine {

    @Override
    public void init() {
        super.init();
        setStartupTime(1500);
        setItemOutput(1, 1, 0);
    }

    @Override
    protected int getMaxStoredPower() {
        return 2000;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Blocks.IRON_BLOCK);
    }

    @Override
    public float getAcceptance() {
        return 0.34f;
    }

    @Override
    public int getOptimalRP() {
        return 15;
    }

    @Override
    public int getEFForOptimalRP() {
        return hasStartedUp() ? 28 : 83;
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
        return 1200 - startup/2;
    }

    @Override
    public int getRequiredPowerAfterStartup() {
        return 400;
    }

    @Override
    public int updateProgressOnItem(int oldProgress, ItemStack stack, int slot, float startup) {
        return oldProgress + (((EFlux.random.nextFloat()*2 * (startup+0.1f)) > .5f  && ItemStackHelper.isStackValid(stack)) ? 1 : 0);
    }

    @Override
    public void onProcessComplete(ItemStack stack, int slot) {
        inventory.decrStackSize(slot, 1);
        ItemStack result = CompressorRecipes.getInstance().getOutput(stack);
        if (result == null){
            result = ItemRegister.scrap.copy();
        }
        if (!canAddToOutput(result)){
            setBroken(true);
            onBroken();
        }
    }

}
