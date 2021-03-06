package elec332.eflux.multiblock;

import com.google.common.collect.Maps;
import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInformation;
import elec332.core.api.inventory.IHasProgressBar;
import elec332.core.client.inventory.IResourceLocationProvider;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.util.IEFluxMachine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
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
        inventory = createItemHandler();
    }

    protected BasicItemHandler createItemHandler(){
        return new BasicItemHandler(getSlots()){

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

        };
    }

    public int getSlots(){
        return 6;
    }

    protected BasicItemHandler inventory;
    protected int startup;
    protected int startupTime;
    protected HashMap<Integer, Integer> progressMap;

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

    @Override
    public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData) {
        super.addInformation(information, hitData);
        if (hitData.getData().hasKey("perct")) {
            information.add("Startup: " + hitData.getData().getString("perct"));
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound getInfoNBTData(@Nonnull NBTTagCompound tag, TileEntity tile, @Nonnull EntityPlayerMP player, @Nonnull IInfoDataAccessorBlock hitData) {
        tag = super.getInfoNBTData(tag, tile, player, hitData);
        if (startup != startupTime) {
            tag.setString("perct", String.format("%.2f", ((float) startup / startupTime) * 100) + "%");
        }
        return tag;
    }

    public IItemHandler getInventory(){
        return inventory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSpecialCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory : super.getSpecialCapability(capability, facing, pos);
    }

}
