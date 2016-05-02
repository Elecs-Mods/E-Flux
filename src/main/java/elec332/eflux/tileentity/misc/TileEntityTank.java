package elec332.eflux.tileentity.misc;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.client.util.KeyHelper;
import elec332.core.compat.handlers.WailaCompatHandler;
import elec332.core.tile.TileBase;
import elec332.core.util.NBTHelper;
import elec332.core.util.PlayerHelper;
import elec332.eflux.grid.WorldRegistry;
import elec332.eflux.grid.tank.EFluxDynamicTank;
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
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Elec332 on 16-4-2016.
 */
@RegisterTile(name = "TileEntityEFluxTank")
public class TileEntityTank extends TileBase implements IEFluxTank, WailaCompatHandler.IWailaInfoTile {

    public TileEntityTank(){
        this.multiBlockTag = new NBTTagCompound();
    }

    private NBTTagCompound multiBlockTag;
    private EFluxDynamicTank tankMultiBlock;
    private Fluid lastSeenFluid;
    @SideOnly(Side.CLIENT)
    private Fluid clientFluid;
    @SideOnly(Side.CLIENT)
    private float clientFluidHeight;


    @Override
    public void onTileLoaded() {
        super.onTileLoaded();
        if (!worldObj.isRemote){
            WorldRegistry.get(worldObj).getTankRegistry().addTile(this);
        }
    }

    @Override
    public void onTileUnloaded() {
        super.onTileLoaded();
        if (!worldObj.isRemote){
            WorldRegistry.get(worldObj).getTankRegistry().removeTile(this);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldObj.isRemote){
            return true;
        }
        if (stack != null && stack.getItem() instanceof IFluidContainerItem){
            IFluidContainerItem item = (IFluidContainerItem) stack.getItem();
            int capacity = item.getCapacity(stack);
            FluidStack fluidStack = item.getFluid(stack);
            if (fluidStack == null){
                FluidStack fs = drain(null, capacity, false);
                if (item.fill(stack, fs, false) == capacity){
                    item.fill(stack, drain(null, capacity, true), true);
                    return true;
                }
            } else {
                int i = fill(null, fluidStack, false);
                if (i == fluidStack.amount){
                    fill(null, item.drain(stack, i, true), true);
                    return true;
                }
            }
        }
        if (stack != null){
            if (FluidContainerRegistry.isFilledContainer(stack)){
                FluidStack stack1 = FluidContainerRegistry.getFluidForFilledItem(stack);
                if (stack1 != null){
                    int i = fill(null, stack1.copy(), false);
                    if (i == stack1.amount){
                        fill(null, stack1.copy(), true);
                        ItemStack s = FluidContainerRegistry.drainFluidContainer(stack);
                        if (s != null && !PlayerHelper.isPlayerInCreative(player)) {
                            stack.setItem(s.getItem());
                            stack.setItemDamage(s.getItemDamage());
                            stack.setTagCompound(s.getTagCompound());
                        }
                        return true;
                    }
                }
            }
            if (FluidContainerRegistry.isEmptyContainer(stack)){
                ItemStack s = FluidContainerRegistry.fillFluidContainer(drain(null, 1000, false), stack.copy());
                if (s != null){
                    FluidStack f = FluidContainerRegistry.getFluidForFilledItem(s.copy());
                    if (f != null && f.amount == 1000){
                        drain(null, 1000, true);
                        if (!PlayerHelper.isPlayerInCreative(player)) {
                            stack.setItem(s.getItem());
                            stack.setItemDamage(s.getItemDamage());
                            stack.setTagCompound(s.getTagCompound());
                        }
                        return true;
                    }
                }
            }
        }
        return super.onBlockActivated(state, player, hand, stack, side, hitX, hitY, hitZ);
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

    //Multiblock impl

    @Override
    public void setMultiBlock(EFluxDynamicTank eFluxDynamicTank) {
        this.tankMultiBlock = eFluxDynamicTank;
    }

    @Override
    public EFluxDynamicTank getMultiBlock() {
        return tankMultiBlock;
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
            this.clientFluid = FluidRegistry.getFluid(tag.getString("f"));
        } else if (id == 2){
            this.clientFluidHeight = tag.getFloat("h");
        } else {
            super.onDataPacket(id, tag);
        }
    }

    //Tank impl

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return tankMultiBlock == null ? 0 : tankMultiBlock.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return tankMultiBlock == null ? null : tankMultiBlock.drain(resource, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return tankMultiBlock == null ? null : tankMultiBlock.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return tankMultiBlock != null && tankMultiBlock.canFill(fluid);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return tankMultiBlock != null && tankMultiBlock.canDrain(fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return tankMultiBlock == null ? new FluidTankInfo[0] : tankMultiBlock.getTankInfo();
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
            nbtTagCompound.setInteger("i", tankMultiBlock.getTankContentAmount(getPos()));
        }
        return nbtTagCompound;
    }

}
