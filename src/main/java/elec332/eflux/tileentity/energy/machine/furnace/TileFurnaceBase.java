package elec332.eflux.tileentity.energy.machine.furnace;

import com.google.common.collect.Lists;
import elec332.eflux.client.inventory.BaseGuiContainer;
import elec332.eflux.inventory.slot.SlotFurnaceInput;
import elec332.eflux.inventory.slot.SlotInput;
import elec332.eflux.inventory.slot.SlotOutput;
import elec332.eflux.tileentity.BreakableMachineTile;
import elec332.eflux.tileentity.TileEntityProcessingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elec332 on 5-5-2015.
 */
public abstract class TileFurnaceBase extends TileEntityProcessingMachine {

    public TileFurnaceBase() {
        super(2);
    }

    @Override
    protected SlotInput[] getInputSlots() {
        return new SlotInput[]{
                new SlotFurnaceInput(inventory, 0, 56, 35)
        };
    }

    @Override
    protected SlotOutput[] getOutputSlots() {
        return new SlotOutput[]{
                new SlotOutput(inventory, 1, 116, 35)
        };
    }

    @Override
    protected boolean canProcess() {
        if (inventory.getStackInSlot(0) == null)
            return false;
        ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(inventory.getStackInSlot(0));
        return result != null && inventory.canAddItemStackFully(result, 1, true);
    }

    @Override
    protected int getProcessTime() {
        return 200;
    }

    @Override
    protected void onProcessDone() {
        if (inventory.getStackInSlot(1) == null)
            inventory.setInventorySlotContents(1, getInputSlots()[0].getOutput().copy());
        else inventory.getStackInSlot(1).stackSize += getInputSlots()[0].getOutput().stackSize;
        getInputSlots()[0].consumeOnProcess();
        notifyNeighboursOfDataChange();
        syncData();
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new BaseGuiContainer(getGuiServer(player)) {
            @Override
            public ResourceLocation getBackgroundImageLocation() {
                return new ResourceLocation("textures/gui/container/furnace.png");
            }
        };
    }
}
