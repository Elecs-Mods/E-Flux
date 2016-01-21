package elec332.eflux.tileentity.energy.generator;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.tile.IInventoryTile;
import elec332.core.tile.TileBase;
import elec332.core.util.BasicInventory;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.inventory.GuiStandardFormat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Elec332 on 29-4-2015.
 */
@RegisterTile(name = "TileEntityEFluxCoalGenerator")
public class CoalGenerator extends TileBase implements IEnergySource, IInventory, IInventoryTile, ITileWithSlots{

    public CoalGenerator(){
        inventory = new BasicInventory("", 1){
            @Override
            public boolean isItemValidForSlot(int id, ItemStack stack) {
                return TileEntityFurnace.isItemFuel(stack);
            }
        };
        dirData = new byte[6];
    }

    @Override
    public void updateEntity() {
        if (burnTime > 0){
            ltp = sppt;
            burnTime--;
        } else {
            ltp = 0;
            sppt = 0;
            ItemStack stack = inventory.getStackInSlot(0);
            if (stack != null){
                int burnTime = TileEntityFurnace.getItemBurnTime(stack.copy());
                if (burnTime > 0){
                    inventory.decrStackSize(0, 1);
                    this.burnTime = burnTime;
                    sppt = 150;
                } else {
                    inventory.setInventorySlotContents(0, null);
                    WorldHelper.dropStack(worldObj, pos.offset(getTileFacing()), stack.copy());
                }
            }
        }
        if (worldObj.getTotalWorldTime() % 5 == 0){
            outA = 0;
            for (int i = 0; i < dirData.length; i++) {
                if (dirData[i] == 1){
                    outA++;
                    dirData[i] = 0;
                }
            }
        }
    }

    private int ltp, sppt, burnTime, outA;
    private byte[] dirData;
    private BasicInventory inventory;

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and provide power to the given side
     */
    @Override
    public boolean canProvidePowerTo(EnumFacing direction) {
        return direction != getTileFacing();
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param direction the direction where the power will be provided to
     * @param execute   weather the power is actually drawn from the tile,
     *                  this flag is always true for IEnergySource.
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, EnumFacing direction, boolean execute) {
        dirData[direction.ordinal()] = 1;
        if (ltp <= 0){
            ltp = 0;
            return 0;
        }
        int ret = (int) ((ltp/(float)outA)/rp);
        ltp -= ret;
        return ret;
    }

    @Override
    public void onTileLoaded() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
    }

    @Override
    public void onTileUnloaded() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(this));
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        sppt = tagCompound.getInteger("ibt");
        burnTime = tagCompound.getInteger("bt");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        tagCompound.setInteger("ibt", sppt);
        tagCompound.setInteger("bt", burnTime);
    }

    @Override
    public void onBlockRemoved() {
        InventoryHelper.dropInventoryItems(worldObj, pos, this);
        super.onBlockRemoved();
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return inventory.removeStackFromSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return inventory.hasCustomName();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        inventory.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        inventory.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

    @Override
    public int getField(int id) {
        return inventory.getField(id);
    }

    @Override
    public int getFieldCount() {
        return inventory.getFieldCount();
    }

    @Override
    public void setField(int id, int value) {
        inventory.setField(id, value);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public IChatComponent getDisplayName() {
        return inventory.getDisplayName();
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        return openGui(player, EFlux.instance, 0);
    }

    @Override
    public Container getGuiServer(EntityPlayer player) {
        return new ContainerMachine(this, player, 0);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new GuiStandardFormat((BaseContainer) getGuiServer(player), new EFluxResourceLocation("gui/GuiNull.png"));
    }

    @Override
    public void addSlots(final BaseContainer container) {
        container.addSlotToContainer(new Slot(inventory, 0, 66, 53){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return TileEntityFurnace.isItemFuel(stack);
            }
        });
        container.addPlayerInventoryToContainer();
    }

}
