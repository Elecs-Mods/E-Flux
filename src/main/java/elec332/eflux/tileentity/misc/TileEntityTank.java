package elec332.eflux.tileentity.misc;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.client.util.KeyHelper;
import elec332.core.compat.waila.WailaCompatHandler;
import elec332.core.util.FluidHelper;
import elec332.core.util.NBTHelper;
import elec332.core.world.DimensionCoordinate;
import elec332.eflux.grid.tank.EFluxDynamicTankGrid;
import elec332.eflux.tileentity.TileEntityEFlux;
import elec332.eflux.util.IEFluxTank;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Elec332 on 16-4-2016.
 */
@RegisterTile(name = "TileEntityEFluxTank")
public class TileEntityTank extends TileEntityEFlux implements IEFluxTank, WailaCompatHandler.IWailaInfoTile {

    public TileEntityTank(){
        this.multiBlockTag = new NBTTagCompound();
    }

    private NBTTagCompound multiBlockTag;
    private EFluxDynamicTankGrid tankMultiBlock;
    private Fluid lastSeenFluid;
    //Not client-only, because we need it for the luminosity
    private Fluid clientFluid;
    @SideOnly(Side.CLIENT)
    private float clientFluidHeight;

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        return worldObj.isRemote || FluidHelper.onTankActivated(player, hand, tankMultiBlock, tankMultiBlock.getCapacity()) || super.onBlockActivated(state, player, hand, stack, side, hitX, hitY, hitZ);
    }

    @Override
    public int getLightValue() {
        return clientFluid == null ? super.getLightValue() : clientFluid.getLuminosity();
    }

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        if (tankMultiBlock != null){
            multiBlockTag = tankMultiBlock.getSaveData(this);
        }
        if (multiBlockTag != null) {
            tagCompound.setTag("multiBlockData", multiBlockTag);
        }
        if (lastSeenFluid != null){
            tagCompound.setString("lsf", lastSeenFluid.getName());
        }
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        multiBlockTag = tagCompound.getCompoundTag("multiBlockData");
        lastSeenFluid = FluidRegistry.getFluid(tagCompound.getString("lsf"));
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return tankMultiBlock != null && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    @SuppressWarnings("all")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return tankMultiBlock != null && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T) tankMultiBlock : super.getCapability(capability, facing);
    }

    //Multiblock impl

    @Override
    public void setTankGrid(EFluxDynamicTankGrid grid) {
        this.tankMultiBlock = grid;
    }

    @Override
    public EFluxDynamicTankGrid getTankGrid() {
        return this.tankMultiBlock;
    }

    @Override
    public void setSaveData(NBTTagCompound nbtTagCompound) {
        multiBlockTag = nbtTagCompound;
    }

    @Override
    public NBTTagCompound getSaveData() {
        return multiBlockTag;
    }

    @Override
    public int getTankSize() {
        return 9000;
    }

    @Override
    public void setLastSeenFluid(Fluid fluid) {
        this.lastSeenFluid = fluid;
    }

    @Override
    public Fluid getLastSeenFluid() {
        return lastSeenFluid;
    }

    @Override
    public void setClientRenderFluid(Fluid fluid) {
        sendPacket(1, new NBTHelper().addToTag(fluid == null ? "" : fluid.getName(), "f").serializeNBT());
        setClientFluid_(fluid);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Fluid getClientRenderFluid() {
        return clientFluid;
    }

    @Override
    public void setClientRenderHeight(float height) {
        sendPacket(2, new NBTHelper().addToTag(height, "h").serializeNBT());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getClientRenderHeight() {
        return clientFluidHeight;
    }

    @Override
    public void onDataPacket(int id, NBTTagCompound tag) {
        if (id == 1){
            setClientFluid_(FluidRegistry.getFluid(tag.getString("f")));
        } else if (id == 2){
            this.clientFluidHeight = tag.getFloat("h");
        } else {
            super.onDataPacket(id, tag);
        }
    }

    private void setClientFluid_(Fluid fluid){
        if (this.clientFluid != fluid){
            this.clientFluid = fluid;
            worldObj.checkLight(pos);
        }
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        NBTTagCompound tag = iWailaDataAccessor.getNBTData();
        if (tag != null && tag.hasKey("c")){
            list.add("Fluid: "+(getClientRenderFluid() == null ? "null" : getClientRenderFluid().getLocalizedName(null)));
            list.add("Amount: "+tag.getInteger("c")+" / "+tag.getInteger("t"));
            if (KeyHelper.isShiftDown()){
                list.add("Internal stored: "+tag.getInteger("i")+" / "+getTankSize());
            }
        }
        return list;
    }

    @Override
    public NBTTagCompound getWailaTag(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, BlockPos blockPos) {
        if (tankMultiBlock != null) {
            nbtTagCompound.setInteger("c", tankMultiBlock.getTotalStoredAmount());
            nbtTagCompound.setInteger("t", tankMultiBlock.getCapacity());
            nbtTagCompound.setInteger("i", tankMultiBlock.getTankContentAmount(DimensionCoordinate.fromTileEntity(this)));
        }
        return nbtTagCompound;
    }

}
