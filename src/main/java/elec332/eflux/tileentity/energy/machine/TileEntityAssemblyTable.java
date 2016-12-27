package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.inventory.window.IWindowFactory;
import elec332.core.inventory.window.Window;
import elec332.core.util.BasicItemHandler;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.inventory.WindowAssemblyTable;
import elec332.eflux.tileentity.TileEntityBreakableMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 4-5-2015.
 */
@RegisteredTileEntity("TileEntityEFluxAssemblyTable")
public class TileEntityAssemblyTable extends TileEntityBreakableMachine implements IWindowFactory {

    private BasicItemHandler inv = new BasicItemHandler(1){

        @Override
        public boolean isStackValidForSlot(int slot, @Nonnull ItemStack stack) {
            return CircuitHelper.getCircuit(stack) != null;
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

    };

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.IRON_HOE);
    }

    @Override
    public float getAcceptance() {
        return 0.10f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 50;
    }

    @Override
    public int getRequestedRP() {
        return 20;
    }


    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        inv.writeToNBT(tagCompound);
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        inv.deserializeNBT(tagCompound);
    }

    @Override
    public boolean onBlockActivatedSafe(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        openLocalWindow(player);
        return true;
    }

    @Override
    protected int getMaxStoredPower() {
        return 500;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
        return direction == EnumFacing.DOWN;
    }

    @Override
    public Window createWindow(Object... args) {
        return new WindowAssemblyTable(inv, energyContainer);
    }

}
