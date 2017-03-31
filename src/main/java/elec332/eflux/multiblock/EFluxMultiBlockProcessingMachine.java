package elec332.eflux.multiblock;

import com.google.common.collect.Maps;
import elec332.core.api.inventory.IHasProgressBar;
import elec332.core.client.inventory.IResourceLocationProvider;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.util.IEFluxMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

/**
 * Created by Elec332 on 27-8-2015.
 */
public abstract class EFluxMultiBlockProcessingMachine extends EFluxMultiBlockMachine implements IHasProgressBar, IResourceLocationProvider, IEFluxMachine {

    public EFluxMultiBlockProcessingMachine() {
        super();
        progressMap = Maps.newHashMap();
        for (int i = 0; i < getSlots(); i++) {
            progressMap.put(i, 0);
        }
    }

    @Override
    public void init() {
        super.init();
        inventory = new BasicItemHandler(getSlots());
    }

    public int getSlots(){
        return 6;
    }

    protected BasicItemHandler inventory;
    protected int startup;
    protected int startupTime;
    protected HashMap<Integer, Integer> progressMap;

    @Override
    public ItemStack inject(ItemStack stack) {
        if (!ItemStackHelper.isStackValid(stack))
            return stack;
        final int s = stack.stackSize;
        for (int i = 0; i < s; i++) {
            ItemStack stack1 = stack.copy();
            stack1.stackSize = 1;
            //if (!InventoryHelper.addItemToInventory(inventory, stack1))
              //todo  return stack;
            stack.stackSize--;
        }
        return ItemStackHelper.NULL_STACK;
    }

    @Override
    public final void onTick() {
        super.onTick();
        if (!getWorldObj().isRemote){
            if (startup >= 0 && startup < startupTime){
                if (getEnergyContainer().drainPower(getRequiredPower(startup)) && !isBroken()){
                    startup++;
                } else if (startup > 0){
                    startup--;
                }
            } else {
                if (getEnergyContainer().drainPower(getRequiredPowerAfterStartup()) && !isBroken()) {
                    if (startup > startupTime) {
                        startup = startupTime;
                    }
                } else {
                    if (startup > 0) {
                        startup--;
                    }
                }
            }
            if (!isBroken()) {
                for (int i = 0; i < inventory.getSlots(); i++) {
                    int q = progressMap.get(i);
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (!ItemStackHelper.isStackValid(stack)){
                        resetProgressData(i);
                        continue;
                    }
                    progressMap.put(i, updateProgressOnItem(q, stack, i, (float)startup / (float)startupTime));
                    if (progressMap.get(i) > getMaxProgress()) {
                        onProcessComplete(inventory.getStackInSlot(i), i);
                        resetProgressData(i);
                    }
                }
            }
            tick(startup);
        }
    }

    public int getMaxProgress(){
        return 100;
    }

    protected void resetProgressData(int slot){
        progressMap.put(slot, 0);
    }

    public void onProcessComplete(ItemStack stack, int slot){
    }

    public boolean hasStartedUp(){
        return startup >= startupTime;
    }

    /*public boolean switchToItemMode(){
        if (!inventory.isEmpty())
            return false;
        inventory = new RIWInventory(0, getSaveDelegate());
        setHook();
        return true;
    }

    public boolean switchToBlockMode(){
        if (!inventory.isEmpty())
            return false;
        inventory = new RIWInventory(1, getSaveDelegate());
        setHook();
        return true;
    }

    private void setHook(){
        inventory.addHook(this);
    }

    @Override
    public void onRIWInventoryChanged() {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i) == null)
                progressMap.put(i, 0);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        inventory.removeHook(this);
    }*/

    public void setStartupTime(int newTime){
        this.startupTime = newTime;
    }

    public abstract int getRequiredPower(int startup);

    public abstract int getRequiredPowerAfterStartup();

    public void tick(int startup){
    }

    public abstract int updateProgressOnItem(int oldProgress, ItemStack stack, int slot, float startup);

    @Override
    public int getProgress(){
        return startup;
    }

    @Override
    public float getProgressScaled(int progress) {
        return progress/(float)startup;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        this.inventory.writeToNBT(tagCompound);
        tagCompound.setInteger("startup", this.startup);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.inventory.deserializeNBT(tagCompound);
        this.startup = tagCompound.getInteger("startup");
    }

    public IItemHandler getInventory(){
        return inventory;
    }

}
