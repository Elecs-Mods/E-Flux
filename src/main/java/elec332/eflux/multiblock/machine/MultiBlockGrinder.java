package elec332.eflux.multiblock.machine;

import elec332.core.util.PlayerHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.multiblock.EFluxMultiBlockProcessingMachine;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.util.DustPile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 27-8-2015.
 */
public class MultiBlockGrinder extends EFluxMultiBlockProcessingMachine {

    public MultiBlockGrinder(){
        setStartupTime(300);
        dustPile = DustPile.newDustPile();

    }

    @Override
    public void init() {
        super.init();//switchToBlockMode();
    }

    private DustPile dustPile;
    private static final int maxPileSize = 180;

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagCompound tag = dustPile.toNBT();
        if (tag != null) {
            tagCompound.setTag("dustPile", tag);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("dustPile")) {
            dustPile.readFromNBT(tagCompound.getCompoundTag("dustPile"));
        }
    }

    @Override
    protected int getMaxStoredPower() {
        return 9000;
    }

    @Override
    public float getAcceptance() {
        return 1.0f;
    }

    @Override
    public int getEFForOptimalRP() {
        return !hasStartedUp() ? 170 : 53;
    }

    @Override
    public int getOptimalRP() {
        return 10;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.IRON_INGOT);
    }

    @Override
    public EnumRecipeMachine getMachine() {
        return EnumRecipeMachine.GRINDER;
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new EFluxResourceLocation("yew");
    }

    @Override
    public int getRequiredPower(int startup) {
        return 1500 - startup * 3;
    }

    @Override
    public int getRequiredPowerAfterStartup() {
        return 500;
    }

    @Override
    public boolean onAnyBlockActivatedSafe(EntityPlayer player) {
        if (!getWorldObj().isRemote){
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                PlayerHelper.sendMessageToPlayer(player, ""+inventory.getStackInSlot(i));
            }
            PlayerHelper.sendMessageToPlayer(player, ""+dustPile.getSize());
        }
        return super.onAnyBlockActivatedSafe(player);
    }

    @Override
    public int updateProgressOnItem(int oldProgress, ItemStack stack, int slot, float startup) {
        if (startup > .8f && stack != null){
            oldProgress++;
            if (oldProgress > 100){
                inventory.setInventorySlotContents(slot, null);
                resetProgressData(slot);
                dustPile.addGrindResult(stack);
                oldProgress = 0;
            }
            if (dustPile.getSize() > maxPileSize){
                getEnergyContainer().breakMachine();
            }
        }
        return oldProgress;
    }

    public ItemStack extractItem(){
        if (dustPile.getSize() <= 0)
            return null;
        ItemStack ret = new ItemStack(ItemRegister.groundMesh);
        NBTTagCompound tag = dustPile.getStack();
        if (tag == null)
            return null;
        ret.setTagCompound(tag);
        return ret;
    }

    @Override
    public void setBroken(boolean broken) {
        super.setBroken(broken);
        if (broken)
            startup = 0;
    }
}
