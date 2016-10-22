package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.client.inventory.BaseGuiContainer;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.inventory.slot.SlotOutput;
import elec332.core.tile.IInventoryTile;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.tileentity.BreakableMachineTileWithSlots;
import elec332.eflux.util.DustPile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 12-9-2015.
 */
@RegisterTile(name = "TileEntityEFluxScanner")
public class TileEntityScanner extends BreakableMachineTileWithSlots implements IInventoryTile, ITileWithSlots, ITickable, ISidedInventory {

    public TileEntityScanner(){
        super(2);
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.BLAZE_POWDER);
    }

    @Override
    public void update() {
        if (timeCheck() && inventory.getStackInSlot(0) != null && inventory.getStackInSlot(0).getItem() == ItemRegister.groundMesh && inventory.getStackInSlot(1) == null && energyContainer.drainPower(150)){
            ItemStack stack = inventory.getStackInSlot(0).copy();
            DustPile dustPile = DustPile.fromNBT(stack.getTagCompound());
            inventory.setInventorySlotContents(0, null);
            inventory.setInventorySlotContents(1, dustPile.scan(stack.stackSize));
        }
    }

    @Override
    public boolean onBlockActivatedSafe(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        return openGui(player, EFlux.instance, 0);
    }

    @Override
    public float getAcceptance() {
        return 0.03f;
    }

    @Override
    protected int getMaxStoredPower() {
        return 400;
    }

    @Override
    public int getEFForOptimalRP() {
        return 40;
    }

    @Override
    public int getRequestedRP() {
        return 4;
    }

    @Override
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
        return direction != getTileFacing();
    }

    @Override
    public BaseContainer getGuiServer(EntityPlayer player) {
        return new ContainerMachine(this, player, 0);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new BaseGuiContainer(getGuiServer(player)) {
            @Override
            public ResourceLocation getBackgroundImageLocation() {
                return new EFluxResourceLocation("gui/GuiNull.png");
            }
        };
    }

    @Override
    public void addSlots(BaseContainer container) {
        container.addSlotToContainer(new Slot(inventory, 0, 56, 35));
        container.addSlotToContainer(new SlotOutput(inventory, 1, 116, 35));
        container.addPlayerInventoryToContainer();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
        return slot == 0 && stack.getItem() == ItemRegister.groundMesh;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
        return slot == 1;
    }

}
