package elec332.eflux.tileentity;

import com.google.common.collect.Lists;
import elec332.eflux.EFlux;
import elec332.eflux.client.inventory.GuiMachine;
import elec332.eflux.inventory.BaseContainer;
import elec332.eflux.inventory.ContainerMachine;
import elec332.eflux.inventory.ITileWithSlots;
import elec332.eflux.inventory.slot.SlotOutput;
import elec332.eflux.recipes.RecipeRegistry;
import elec332.eflux.util.BasicInventory;
import elec332.eflux.util.IEFluxMachine;
import elec332.eflux.util.IInventoryTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

/**
 * Created by Elec332 on 5-5-2015.
 */
public abstract class TileEntityProcessingMachine extends BreakableReceiverTile implements ITileWithSlots, IInventoryTile, IEFluxMachine{

    public TileEntityProcessingMachine(int i){
        this.inventory = new BasicInventory("Inventory", i, this);
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

    public BasicInventory getInventory() {
        return inventory;
    }

    protected abstract Slot[] getInputSlots();

    protected abstract SlotOutput[] getOutputSlots();

    public abstract int getRequiredPowerPerTick();

    protected abstract int getProcessTime();

    protected boolean canProcess() {
        ArrayList<Slot> slots = Lists.newArrayList(getInputSlots());
        slots.addAll(Lists.newArrayList(getOutputSlots()));
        return RecipeRegistry.instance.hasOutput(this, slots);
    }

    protected void onProcessDone() {
        ArrayList<Slot> slots = Lists.newArrayList(getInputSlots());
        slots.addAll(Lists.newArrayList(getOutputSlots()));
        RecipeRegistry.instance.handleOutput(this, slots);
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
        for (Slot slotInput : getInputSlots())
            container.addSlotToContainer(slotInput);
        for (SlotOutput slotOutput : getOutputSlots())
            container.addSlotToContainer(slotOutput);
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

    protected SlotOutput[] oneOutPutSlot(int index){
        return new SlotOutput[]{new SlotOutput(inventory, index, 116, 35)};
    }

    protected Slot[] oneInputSlot(int index){
        return new Slot[]{new Slot(inventory, index, 56, 35)};
    }
}
