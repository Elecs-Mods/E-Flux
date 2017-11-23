package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.widget.slot.WidgetSlotOutput;
import elec332.core.inventory.window.ISimpleWindowFactory;
import elec332.core.inventory.window.Window;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.tileentity.BreakableMachineTileWithSlots;
import elec332.eflux.util.DustPile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 12-9-2015.
 */
@RegisteredTileEntity("TileEntityEFluxScanner")
public class TileEntityScanner extends BreakableMachineTileWithSlots implements ITickable, ISimpleWindowFactory {

    public TileEntityScanner(){
        super(new BasicItemHandler(2){

            @Override
            public boolean canInsert(int slot, @Nonnull ItemStack stack) {
                return slot == 0 && stack.getItem() == ItemRegister.groundMesh;
            }

            @Override
            public boolean canExtract(int slot) {
                return slot == 1;
            }

        });
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.BLAZE_POWDER);
    }

    @Override
    public void update() {
        if (timeCheck() && ItemStackHelper.isStackValid(inventory.getStackInSlot(0)) && inventory.getStackInSlot(0).getItem() == ItemRegister.groundMesh && !ItemStackHelper.isStackValid(inventory.getStackInSlot(1)) && energyContainer.drainPower(150)){
            ItemStack stack = inventory.getStackInSlot(0).copy();
            DustPile dustPile = DustPile.fromNBT(stack.getTagCompound());
            inventory.setStackInSlot(0, ItemStackHelper.NULL_STACK);
            inventory.setStackInSlot(1, dustPile.scan(stack.stackSize));
        }
    }

    @Override
    public boolean onBlockActivatedSafe(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return openLocalWindow(player);
    }

    @Override
    public int getWorkingVoltage() {
        return 5;
    }

    @Override
    public float getAcceptance() {
        return 0.03f;
    }

    @Override
    public int getMaxRP() {
        return 1;
    }

    @Override
    public double getResistance() {
        return 25;
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
    public void modifyWindow(Window window, Object... args) {
        window.addWidget(new WidgetSlot(inventory, 0, 56, 35));
        window.addWidget(new WidgetSlotOutput(inventory, 1, 116, 35));
        window.addPlayerInventoryToContainer();
    }

}
