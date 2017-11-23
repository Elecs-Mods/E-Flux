package elec332.eflux.tileentity.energy.generator;

import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInfoProvider;
import elec332.core.api.info.IInformation;
import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.window.ISimpleWindowFactory;
import elec332.core.inventory.window.Window;
import elec332.core.tile.IActivatableMachine;
import elec332.core.tile.IRandomDisplayTickProviderTile;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.ItemStackHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.BreakReasons;
import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.tileentity.TileEntityEFlux;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Elec332 on 29-4-2015.
 */
@RegisteredTileEntity("TileEntityEFluxCoalGenerator")
public class TileEntityCoalGenerator extends TileEntityEFlux implements IEnergySource, ISimpleWindowFactory, IActivatableMachine, IRandomDisplayTickProviderTile, ITickable, IInfoProvider {

    public TileEntityCoalGenerator(){
        inventory = new BasicItemHandler(1){

            @Override
            public boolean isStackValidForSlot(int slot, @Nonnull ItemStack stack) {
                return TileEntityFurnace.isItemFuel(stack);
            }

        };
    }

    private int tick;

    @Override
    public void update() {
        if (tick == 20){
            tick = 0;
        } else {
            tick++;
        }
        if (burnTime > 0) {
            burnTime--;
        } else {
            voltage = 0;
            ItemStack stack = inventory.extractItem(0, 1, true);
            if (ItemStackHelper.isStackValid(stack)) {
                int burnTime = TileEntityFurnace.getItemBurnTime(stack.copy());
                if (burnTime > 0) {
                    inventory.extractItem(0, 1, false);
                    this.burnTime = burnTime;
                    voltage = 25;
                    if (!active) {
                        active = true;
                        reRenderBlock();
                    }
                } else {
                    inventory.setStackInSlot(0, ItemStackHelper.NULL_STACK);
                    WorldHelper.dropStack(getWorld(), pos.offset(getTileFacing()), stack.copy());
                }
            }
            if (active && !(burnTime > 0)) {
                active = false;
                reRenderBlock();
            }
        }
    }

    //@GridInformation(IEnergyGridInformation.class)
    //private IEnergyGridInformation info;
    private int voltage, burnTime;
    private BasicItemHandler inventory;
    private boolean active;



    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.deserializeNBT(tagCompound);
        voltage = tagCompound.getInteger("ibt");
        burnTime = tagCompound.getInteger("bt");
        active = tagCompound.getBoolean("aC");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        tagCompound.setInteger("ibt", voltage);
        tagCompound.setInteger("bt", burnTime);
        tagCompound.setBoolean("aC", active);
        return tagCompound;
    }

    @Override
    public void onBlockRemoved() {
        WorldHelper.dropInventoryItems(getWorld(), pos, inventory);
        super.onBlockRemoved();
    }

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return openLocalWindow(player);
    }

    @Override
    public void modifyWindow(Window window, Object... args) {
        window.addWidget(new WidgetSlot(inventory, 0, 66, 53){

            @Override
            public boolean isItemValid(ItemStack stack) {
                return TileEntityFurnace.isItemFuel(stack);
            }

        });
        window.addPlayerInventoryToContainer();
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
                    getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    getWorld().spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    getWorld().spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
                    getWorld().spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                    getWorld().spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
       return capability == EFluxAPI.ENERGY_CAP || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("all")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == EFluxAPI.ENERGY_CAP ? (T) this : super.getCapability(capability, facing);
    }

    @Override
    public float getVariance() {
        return 0; //TODO
    }

    @Override
    public int getCurrentAverageEF() {
        return voltage;
    }

    @Override
    public float getMaxRP() {
        return 4;
    }

    @Override
    public void addInformation(@Nonnull IInformation iInformation, @Nonnull IInfoDataAccessorBlock iInfoDataAccessorBlock) {
        iInformation.add("Provided voltage: "+iInfoDataAccessorBlock.getData().getInteger("volts"));
        iInformation.add("Max power: 1KW");
    }

    @Nonnull
    @Override
    public NBTTagCompound getInfoNBTData(@Nonnull NBTTagCompound nbtTagCompound, TileEntity tileEntity, @Nonnull EntityPlayerMP entityPlayerMP, @Nonnull IInfoDataAccessorBlock iInfoDataAccessorBlock) {
        nbtTagCompound.setInteger("volts", voltage);
        return nbtTagCompound;
    }

    @Nonnull
    @Override
    public EnergyType getEnergyType(int post) {
        return EnergyType.AC;
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
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        createConnectionPoints();
    }

    @Override
    public void onLoad() {
        createConnectionPoints();
    }

    protected void createConnectionPoints() {
        cp1 = new ConnectionPoint(pos, world, getTileFacing().getOpposite(), 1);
        cp2 = new ConnectionPoint(pos, world, getTileFacing().getOpposite(), 2);
    }

    private ConnectionPoint cp1, cp2;

    @Override
    public void breakMachine(BreakReasons reason) {
        System.out.println("Biem: "+reason);
    }

}
