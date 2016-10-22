package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.inventory.slot.SlotOutput;
import elec332.core.inventory.widget.WidgetProgressArrow;
import elec332.core.tile.IInventoryTile;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.energy.container.IProgressMachine;
import elec332.eflux.client.ClientHelper;
import elec332.eflux.client.inventory.GuiMachine;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.items.ItemEFluxBluePrint;
import elec332.eflux.items.circuits.ICircuitDataProvider;
import elec332.eflux.items.circuits.IEFluxCircuit;
import elec332.eflux.tileentity.BreakableMachineTileWithSlots;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 4-6-2016.
 */
@RegisterTile(name = "TileEntityEFluxEtchingMachine")
public class TileEntityEtchingMachine extends BreakableMachineTileWithSlots implements IProgressMachine, IInventoryTile, ITileWithSlots, ITickable, ISidedInventory {

    public TileEntityEtchingMachine() {
        super(3);
        energyContainer.setProgressMachine(this);
    }

    private ItemStack result;

    @Override
    public boolean onBlockActivatedSafe(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        return openGui(player, EFlux.instance, 0);
    }

    @Override
    public void update() {
        energyContainer.tick();
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.APPLE); //TODO
    }

    @Override
    protected int getMaxStoredPower() {
        return 320;
    }

    @Override
    public int getRequestedRP() {
        return 8;
    }

    @Override
    public int getEFForOptimalRP() {
        return 9;
    }

    @Override
    public float getAcceptance() {
        return 0.3f;
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index < 2;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 2;
    }

    @Override
    public int getRequiredPowerPerTick() {
        return 48;
    }

    @Override
    public int getProcessTime() {
        return 90;
    }

    @Override
    public boolean canProcess() {
        if (result == null){
            ItemStack circuit = getStackInSlot(1);
            ItemStack blueprint = getStackInSlot(0);
            ICircuit iCircuit = CircuitHelper.getCircuit(circuit);
            if (!(iCircuit instanceof IEFluxCircuit) || blueprint == null || blueprint.getItem() != ItemRegister.nullBlueprint){
                return false;
            }
            ICircuitDataProvider data = ((ItemEFluxBluePrint)blueprint.getItem()).getBlueprintData(blueprint);
            if (data == null || data.getCircuitType() != iCircuit.getDifficulty()){
                return false;
            }
            ItemStack out = circuit.copy();
            decrStackSize(0, 1);
            decrStackSize(1, 1);
            ((IEFluxCircuit)CircuitHelper.getCircuit(out)).etch(data);
            this.result = out;
            return true;
        }
        return true;
    }

    @Override
    public void onProcessDone() {
        setInventorySlotContents(2, result.copy());
        result = null;
    }

    @Override
    public BaseContainer getGuiServer(EntityPlayer player) {
        return new ContainerMachine(this, player, 0);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new GuiMachine(getGuiServer(player)) {

            @Override
            public ResourceLocation getBackgroundImageLocation() {
                return ClientHelper.DEFAULT_GUI_LOCATION;
            }

        };
    }

    @Override
    public void addSlots(BaseContainer container) {
        container.addPlayerInventoryToContainer();
        container.addSlotToContainer(new Slot(this, 0, 20, 20));
        container.addSlotToContainer(new Slot(this, 1, 20, 40));
        container.addSlotToContainer(new SlotOutput(this, 2, 60, 30));
        container.addWidget(new WidgetProgressArrow(30, 30, energyContainer, true));
    }

}
