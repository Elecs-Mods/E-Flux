package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.inventory.widget.WidgetProgressArrow;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.widget.slot.WidgetSlotOutput;
import elec332.core.inventory.window.ISimpleWindowFactory;
import elec332.core.inventory.window.Window;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.energy.container.IProgressMachine;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.items.ItemEFluxBluePrint;
import elec332.eflux.circuit.ICircuitDataProvider;
import elec332.eflux.circuit.IEFluxCircuit;
import elec332.eflux.tileentity.BreakableMachineTileWithSlots;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 4-6-2016.
 */
@RegisteredTileEntity("TileEntityEFluxEtchingMachine")
public class TileEntityEtchingMachine extends BreakableMachineTileWithSlots implements ISimpleWindowFactory, IProgressMachine, ITickable {

    public TileEntityEtchingMachine() {
        super(new BasicItemHandler(3){

            @Override
            public boolean canInsert(int slot, @Nonnull ItemStack stack) {
                return slot < 2;
            }

            @Override
            public boolean canExtract(int slot) {
                return slot == 2;
            }

        });
        energyContainer.setProgressMachine(this);
    }

    private ItemStack result;

    @Override
    public boolean onBlockActivatedSafe(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return openLocalWindow(player);
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
            ItemStack circuit = inventory.getStackInSlot(1);
            ItemStack blueprint = inventory.getStackInSlot(0);
            ICircuit iCircuit = CircuitHelper.getCircuit(circuit);
            if (!(iCircuit instanceof IEFluxCircuit) || !ItemStackHelper.isStackValid(blueprint) || blueprint.getItem() != ItemRegister.nullBlueprint){
                return false;
            }
            ICircuitDataProvider data = ((ItemEFluxBluePrint)blueprint.getItem()).getBlueprintData(blueprint);
            if (data == null || data.getCircuitType() != iCircuit.getDifficulty()){
                return false;
            }
            ItemStack out = circuit.copy();
            inventory.extractItem(0, 1, false);
            inventory.extractItem(1, 1, false);
            ((IEFluxCircuit)CircuitHelper.getCircuit(out)).etch(data);
            this.result = out;
            return true;
        }
        return true;
    }

    @Override
    public void onProcessDone() {
        inventory.setStackInSlot(2, result.copy());
        result = null;
    }

    @Override
    public void modifyWindow(Window window, Object... args) {
        window.setBackground(new EFluxResourceLocation("gui/guinull_.png"));
        window.addPlayerInventoryToContainer();
        window.addWidget(new WidgetSlot(inventory, 0, 20, 20));
        window.addWidget(new WidgetSlot(inventory, 1, 20, 40));
        window.addWidget(new WidgetSlotOutput(inventory, 2, 60, 30));
        window.addWidget(new WidgetProgressArrow(30, 30, energyContainer, true));
    }

}
