package elec332.eflux.tileentity;

import com.google.common.collect.Lists;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.IHasProgressBar;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.inventory.slot.SlotOutput;
import elec332.core.tile.IInventoryTile;
import elec332.core.util.BasicInventory;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.container.IProgressMachine;
import elec332.eflux.client.inventory.GuiMachine;
import elec332.eflux.inventory.slot.SlotUpgrade;
import elec332.eflux.recipes.old.RecipeRegistry;
import elec332.eflux.util.IEFluxMachine;
import elec332.eflux.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Elec332 on 5-5-2015.
 */
public abstract class TileEntityProcessingMachine extends BreakableMachineTileWithSlots implements ITileWithSlots, IInventoryTile, IEFluxMachine, IHasProgressBar, IProgressMachine{

    public TileEntityProcessingMachine(int i, int upgradeSlots){
        super(0);
        energyContainer.setProgressMachine(this);
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
    public void update() {
        super.update();
        if (!worldObj.isRemote) {
            energyContainer.tick();
        }
    }

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

    public abstract int getProcessTime();

    public boolean canProcess() {
        return RecipeRegistry.instance.hasOutput(this, getMachineSlots());
    }

    public void onProcessDone() {
        RecipeRegistry.instance.handleOutput(this, getMachineSlots());
    }

    @Override
    public int getProgress(){
        return energyContainer.getProgress();
    }

    @Override
    public float getProgressScaled(int progress) {
        return energyContainer.getProgressScaled(progress);
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
    public boolean onBlockActivatedSafe(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        return openGui(player);
    }

    public boolean openGui(EntityPlayer player){
        openGui(player, EFlux.instance, 0);
        return true;
    }

    @Override
    public void addSlots(BaseContainer container) {
        for (Slot slot : getAllSLots())
            container.addSlotToContainer(slot);
        container.addPlayerInventoryToContainer();
    }

    @Override
    public BaseContainer getGuiServer(EntityPlayer player) {
        return new ContainerMachine(this, player, 0);
    }

    @SideOnly(Side.CLIENT)
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
