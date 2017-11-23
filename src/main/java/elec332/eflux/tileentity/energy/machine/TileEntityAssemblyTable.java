package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.inventory.window.IWindowFactory;
import elec332.core.inventory.window.Window;
import elec332.core.util.BasicItemHandler;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.inventory.WindowAssemblyTable;
import elec332.eflux.tileentity.TileEntityBreakableMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    public int getWorkingVoltage() {
        return 24;
    }

    @Override
    public float getAcceptance() {
        return 0.10f;
    }

    @Override
    public int getMaxRP() {
        return 9;
    }

    @Override
    public double getResistance() {
        return 4;
    }

    @Nonnull
    @Override
    public ConnectionPoint getConnectionPoint(int post) {
        return post == 0 ? cp1 : cp2;
    }

    @Nullable
    @Override
    public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec) {
        return side != getTileFacing().getOpposite() ? null : (hitVec.y > 0.5 ? cp2 : cp1);
    }

    @Override
    protected void createConnectionPoints() {
        cp1 = new ConnectionPoint(pos, world, getTileFacing().getOpposite(), 1);
        cp2 = new ConnectionPoint(pos, world, getTileFacing().getOpposite(), 2);
    }

    private ConnectionPoint cp1, cp2;


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
    public Window createWindow(Object... args) {
        return new WindowAssemblyTable(inv, energyContainer);
    }

}
