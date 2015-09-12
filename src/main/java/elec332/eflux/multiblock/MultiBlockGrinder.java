package elec332.eflux.multiblock;

import elec332.core.player.PlayerHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.util.DustPile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.List;

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
        tagCompound.setTag("dustPile", dustPile.toNBT());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        dustPile.readFromNBT(tagCompound.getCompoundTag("dustPile"));
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

    @Override
    public int getRequiredPower(int startup) {
        return 0;
    }

    @Override
    public int getRequiredPowerAfterStartup() {
        return 0;
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
    public int updateProgressOnItem(int oldProgress, ItemStack stack, int slot) {
        if (startup > 30 && stack != null){
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
