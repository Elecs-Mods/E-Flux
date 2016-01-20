package elec332.eflux.multiblock.machine;

import elec332.core.util.BlockLoc;
import elec332.core.util.PlayerHelper;
import elec332.eflux.multiblock.EFluxMultiBlockProcessingMachine;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 28-8-2015.
 */
public class MultiBlockFurnace extends EFluxMultiBlockProcessingMachine {

    public MultiBlockFurnace() {
        super();
        setStartupTime(1200);
    }

    @Override
    public void init() {
        super.init();
        //middle = getBlockLocAtTranslatedPos(1, 1, 1);
        //getWorldObj().setBlockState(middle, BlockRegister.renderBlock.getStateFromMeta(0), 3);
        //((TileEntityInsideItemRenderer)WorldHelper.getTileAt(getWorldObj(), middle)).setMultiBlock(this, getMultiBlockFacing(), getStructureID());
    }

    private BlockLoc middle;

    @Override
    public int getRequiredPower(int startup) {
        return 300;
    }

    @Override
    public int getRequiredPowerAfterStartup() {
        return 200;
    }

    @Override
    public int updateProgressOnItem(int oldProgress, ItemStack stack, int slot) {
        return oldProgress+1;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        getWorldObj().setBlockToAir(middle);
    }

    @Override
    public boolean onAnyBlockActivatedSafe(EntityPlayer player) {
        if (player instanceof EntityPlayerMP)
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                PlayerHelper.sendMessageToPlayer(player, ""+inventory.getStackInSlot(i));
            }

        return super.onAnyBlockActivatedSafe(player);
    }

    @Override
    protected int getMaxStoredPower() {
        return 5000;
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
    public EnumRecipeMachine getMachine() {
        return EnumRecipeMachine.FURNACE;
    }

    @Override
    protected int getOptimalRP() {
        return 5;
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new ResourceLocation("textures/gui/container/furnace.png");
    }

}
