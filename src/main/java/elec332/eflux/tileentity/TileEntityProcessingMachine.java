package elec332.eflux.tileentity;

import com.google.common.collect.Lists;
import elec332.eflux.EFlux;
import elec332.eflux.client.inventory.GuiMachine;
import elec332.eflux.inventory.BaseContainer;
import elec332.eflux.inventory.ContainerMachine;
import elec332.eflux.inventory.IHasProgressBar;
import elec332.eflux.inventory.ITileWithSlots;
import elec332.eflux.inventory.slot.SlotOutput;
import elec332.eflux.inventory.slot.SlotUpgrade;
import elec332.eflux.recipes.RecipeRegistry;
import elec332.core.util.BasicInventory;
import elec332.eflux.util.IEFluxMachine;
import elec332.eflux.util.IInventoryTile;
import elec332.eflux.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 5-5-2015.
 */
public abstract class TileEntityProcessingMachine extends BreakableReceiverTile implements ITileWithSlots, IInventoryTile, IEFluxMachine, IHasProgressBar{

    public TileEntityProcessingMachine(int i, int upgradeSlots){
        this.inventory = new BasicInventory("Inventory", i+upgradeSlots, this);
        this.upgradeSlotsCounter = upgradeSlots;
        List<Slot> list = Lists.newArrayList();
        registerMachineSlots(list);
        this.machineSlots = Utils.copyOf(list);
        registerStorageSlots(list);
        int startIndex = list.size();
        registerUpgradeSlots(list);
        List<SlotUpgrade> upgradeSlotsList = Lists.newArrayList();
        for (Slot slot : list.subList(startIndex, list.size())){
            upgradeSlotsList.add((SlotUpgrade) slot);
        }
        this.upgradeSlots = Utils.copyOf(upgradeSlotsList);
        this.allSLots = list;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote) {
            if ((canProcess() && storedPower >= getRequiredPowerPerTick())) {
                this.storedPower = storedPower - getRequiredPowerPerTick();
                progress++;
                if (progress >= getProcessTime()) {
                    this.progress = 0;
                    onProcessDone();
                }
            } else if (progress > 0) {
                progress = 0;
            }
        }
    }

    private int progress = 0;

    protected BasicInventory inventory;
    private int upgradeSlotsCounter;
    private List<Slot> machineSlots;
    private List<SlotUpgrade> upgradeSlots;
    private List<Slot> allSLots;

    public BasicInventory getInventory() {
        return inventory;
    }

    protected abstract void registerMachineSlots(List<Slot> registerList);

    protected void registerStorageSlots(List<Slot> registerList){
    }

    protected void registerUpgradeSlots(List<Slot> registerList){
        for (int i = 0; i < upgradeSlotsCounter; i++) {
            registerList.add(new SlotUpgrade(inventory, registerList.size(), 3, 4 + i * 18));
        }
    }

    protected List<Slot> getMachineSlots(){
        return machineSlots;
    }

    public List<Slot> getAllSLots() {
        return allSLots;
    }

    public List<SlotUpgrade> getUpgradeSlots() {
        return upgradeSlots;
    }

    public abstract int getRequiredPowerPerTick();

    protected abstract int getProcessTime();

    protected boolean canProcess() {
        return RecipeRegistry.instance.hasOutput(this, getMachineSlots());
    }

    protected void onProcessDone() {
        RecipeRegistry.instance.handleOutput(this, getMachineSlots());
    }

    @Override
    public int getProgress(){
        return progress;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public float getProgressScaled() {
        return (float)this.progress/getProcessTime();
    }

    @Override
    public boolean isWorking() {
        return progress > 0;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("progress", progress);
        this.inventory.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.progress = tagCompound.getInteger("progress");
        this.inventory.readFromNBT(tagCompound);
    }

    @Override
    public String[] getProvidedData() {
        return new String[]{
                "Stored power: "+storedPower,
                "In working order: "+!isBroken()
        };
    }

    @Override
    public boolean onBlockActivatedSafe(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return openGui(player);
    }

    public boolean openGui(EntityPlayer player){
        player.openGui(EFlux.instance, 0, worldObj, xCoord, yCoord, zCoord);
        return true;
    }

    @Override
    public void addSlots(BaseContainer container) {
        for (Slot slot : getAllSLots())
            container.addSlotToContainer(slot);
        container.addPlayerInventoryToContainer();
    }

    @Override
    public Container getGuiServer(EntityPlayer player) {
        return new ContainerMachine(this, player, 0);
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return true;
    }

    protected GuiMachine basicGui(EntityPlayer player){
        return new GuiMachine(getGuiServer(player)) {
            @Override
            public ResourceLocation getBackgroundImageLocation() {
                return new ResourceLocation("textures/gui/container/furnace.png");
            }
        };
    }

    protected void oneOutPutSlot(List<Slot> list){
        list.add(new SlotOutput(inventory, list.size(), 116, 35));
    }

    protected void oneInputSlot(List<Slot> list){
        list.add(new Slot(inventory, list.size(), 56, 35));
    }
}
