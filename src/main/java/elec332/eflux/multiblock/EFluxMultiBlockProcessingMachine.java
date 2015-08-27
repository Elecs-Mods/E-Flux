package elec332.eflux.multiblock;

import com.google.common.collect.Lists;
import elec332.core.client.inventory.BaseGuiContainer;
import elec332.core.client.inventory.IResourceLocationProvider;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.IHasProgressBar;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.inventory.slot.SlotOutput;
import elec332.core.util.BasicInventory;
import elec332.eflux.api.energy.container.IProgressMachine;
import elec332.eflux.inventory.slot.SlotUpgrade;
import elec332.eflux.recipes.RecipeRegistry;
import elec332.eflux.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Elec332 on 27-8-2015.
 */
public abstract class EFluxMultiBlockProcessingMachine extends EFluxMultiBlockMachine implements ITileWithSlots, IHasProgressBar, IResourceLocationProvider, IProgressMachine{

    public EFluxMultiBlockProcessingMachine(int i, int upgradeSlots) {
        super();
        this.i = i;
        this.upgradeSlotsInt = upgradeSlots;
        getEnergyContainer().setProgressMachine(this);
    }

    private final int i, upgradeSlotsInt;

    @Override
    public void init() {
        this.inventory = new BasicInventory("Inventory", i+upgradeSlotsInt, getSaveDelegate());
        this.upgradeSlotsCounter = upgradeSlotsInt;
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

    protected BasicInventory inventory;
    private int upgradeSlotsCounter;
    private List<Slot> machineSlots;
    private List<SlotUpgrade> upgradeSlots;
    private List<Slot> allSLots;

    @Override
    public void onTick() {
        super.onTick();
        if (!getWorldObj().isRemote){
            getEnergyContainer().tick();
        }
    }

    @Override
    public int getProgress(){
        return getEnergyContainer().getProgress();
    }

    @Override
    public float getProgressScaled(int progress) {
        return getEnergyContainer().getProgressScaled(progress);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        this.inventory.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.inventory.readFromNBT(tagCompound);
    }

    @Override
    public boolean onAnyBlockActivatedSafe(EntityPlayer player) {
        System.out.println("activate|safe");
        return openGui(player);
    }

    @Override
    public void addSlots(BaseContainer container) {
        for (Slot slot : getAllSLots())
            container.addSlotToContainer(slot);
        container.addPlayerInventoryToContainer();
    }

    @Override
    public Object getMachineGui(EntityPlayer player, boolean client) {
        System.out.println("opened gui");
        BaseContainer container = new ContainerMachine(this, player, 0);
        if (client)
            return new BaseGuiContainer(container) {
                @Override
                public ResourceLocation getBackgroundImageLocation() {
                    return EFluxMultiBlockProcessingMachine.this.getBackgroundImageLocation();
                }
            };
        return container;
    }

    @Override
    public void invalidate() {
    }

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

    public abstract int getProcessTime();

    public boolean canProcess() {
        return RecipeRegistry.instance.hasOutput(this, getMachineSlots());
    }

    public void onProcessDone() {
        RecipeRegistry.instance.handleOutput(this, getMachineSlots());
    }

    protected void oneOutPutSlot(List<Slot> list){
        list.add(new SlotOutput(inventory, list.size(), 116, 35));
    }

    protected void oneInputSlot(List<Slot> list){
        list.add(new Slot(inventory, list.size(), 56, 35));
    }
}
