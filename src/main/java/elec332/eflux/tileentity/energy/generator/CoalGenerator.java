package elec332.eflux.tileentity.energy.generator;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.tile.IActivatableMachine;
import elec332.core.tile.IInventoryTile;
import elec332.core.tile.IRandomDisplayTickProviderTile;
import elec332.core.util.BasicInventory;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.inventory.GuiStandardFormat;
import elec332.eflux.tileentity.EnergyTileBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Created by Elec332 on 29-4-2015.
 */
@RegisterTile(name = "TileEntityEFluxCoalGenerator")
public class CoalGenerator extends EnergyTileBase implements IEnergySource, IInventory, IInventoryTile, ITileWithSlots, IActivatableMachine, IRandomDisplayTickProviderTile {

    public CoalGenerator(){
        inventory = new BasicInventory("", 1){
            @Override
            public boolean isItemValidForSlot(int id, ItemStack stack) {
                return TileEntityFurnace.isItemFuel(stack);
            }
        };
        dirData = new byte[6];
        transmitter = new IEnergyTransmitter() {
            @Override
            public boolean canConnectTo(IEnergyTransmitter otherTransmitter) {
                return true;
            }

            @Override
            public int getMaxEFTransfer() {
                return 1000;
            }

            @Override
            public int getMaxRPTransfer() {
                return 50;
            }
        };
    }

    @Override
    @SuppressWarnings("deprecation")
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
                    if (!active) {
                        active = true;
                        reRenderBlock();
                    }
                } else {
                    inventory.setInventorySlotContents(0, null);
                    WorldHelper.dropStack(worldObj, pos.offset(getTileFacing()), stack.copy());
                }
            }
            if (active && !(burnTime > 0)){
                active = false;
                reRenderBlock();
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
            outA = 1;
        }
    }

    private int ltp, sppt, burnTime, outA;
    private byte[] dirData;
    private BasicInventory inventory;
    private boolean active;
    private IEnergyTransmitter transmitter;

    /**
     * @param rp        the RedstonePotential in the network
     * @param execute   weather the power is actually drawn from the tile,
     *                  this flag is always true for IEnergySource.
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, boolean execute) {
        if (ltp <= 0){
            ltp = 0;
            return 0;
        }
        int ret = (int) ((ltp/(float)outA)/rp);
        ltp -= ret;
        return ret;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        sppt = tagCompound.getInteger("ibt");
        burnTime = tagCompound.getInteger("bt");
        active = tagCompound.getBoolean("aC");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        tagCompound.setInteger("ibt", sppt);
        tagCompound.setInteger("bt", burnTime);
        tagCompound.setBoolean("aC", active);
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

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, Random random) {
        if (this.active) {
            EnumFacing enumfacing = getTileFacing();
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + random.nextDouble() * 6.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = random.nextDouble() * 0.6D - 0.3D;

            switch (enumfacing) {
                case WEST:
                    worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldObj.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldObj.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
                    worldObj.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                    worldObj.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        boolean ret = ((capability == EFluxAPI.PROVIDER_CAPABILITY || capability == EFluxAPI.TRANSMITTER_CAPABILITY) && facing != getTileFacing()) || super.hasCapability(capability, facing);
        EFlux.systemPrintDebug("req: " + facing + " " + capability.getName() + " ret: " + ret);
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == EFluxAPI.PROVIDER_CAPABILITY ? (facing != getTileFacing() ? (T)this : null) : (capability == EFluxAPI.TRANSMITTER_CAPABILITY ? (facing != getTileFacing() ? (T)transmitter : null) : super.getCapability(capability, facing));
    }

}
