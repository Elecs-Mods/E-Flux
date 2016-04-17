package elec332.eflux.grid.tank;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import elec332.core.multiblock.dynamic.AbstractDynamicMultiBlock;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.util.IEFluxFluidHandler;
import elec332.eflux.util.IEFluxTank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 16-4-2016.
 */
public class EFluxDynamicTank extends AbstractDynamicMultiBlock<EFluxDynamicTankWorldHolder, EFluxDynamicTank> implements IEFluxFluidHandler {

    public EFluxDynamicTank(IEFluxTank tile, EFluxDynamicTankWorldHolder worldHolder) {
        super((TileEntity) tile, worldHolder);
        tank = new FluidTank(tile.getTankSize());
        NBTTagCompound tag = tile.getSaveData();
        if (tag.hasKey("fluid")) {
            FluidStack stack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("fluid"));
            if (stack.getFluid() != null) {
                tank.fill(stack, true);
            }
        }
        tile.setLastSeenFluid(getStoredFluid());
        renderData = Maps.newHashMap();
        rowAmountMap = Maps.newHashMap();
        saveAmountMap = Maps.newHashMap();
        capacityMap = Maps.newHashMap();
        capacityMap.put(((TileEntity)tile).getPos(), tile.getTankSize());
        needsSorting = true;
    }

    private FluidTank tank;
    private final Map<Integer, List<BlockPos>> renderData;
    private final Map<Integer, Integer> rowAmountMap;
    private final Map<BlockPos, Integer> capacityMap;
    private final Map<BlockPos, Integer> saveAmountMap;
    private boolean needsSorting;

    @Override
    public void tick() {
        if (world.getWorldTime() % 20 == 0){
            forEachTank(TankActions.syncFluidType, getStoredFluid());
            setTankFluidHeights();
            forEachTank(TankActions.setLastSeenFluid, getStoredFluid());
        }
    }

    @Override
    protected void mergeWith(EFluxDynamicTank multiBlock) {
        super.mergeWith(multiBlock);
        mergeTank(multiBlock.tank);
        capacityMap.putAll(multiBlock.capacityMap);
        needsSorting = true;
        forEachTank(TankActions.syncFluidType, getStoredFluid());
        setTankFluidHeights();
        forEachTank(TankActions.markDirty);
    }

    public NBTTagCompound getSaveData(IEFluxTank tank){
        NBTTagCompound ret = new NBTTagCompound();
        FluidStack stack = this.tank.getFluid();
        if (stack != null) {
            int amount = getTankContentAmount(((TileEntity) tank).getPos());
            stack = stack.copy();
            stack.amount = amount;
            NBTTagCompound fluidTag = new NBTTagCompound();
            stack.writeToNBT(fluidTag);
            ret.setTag("fluid", ret);
        }
        return ret;
    }

    @SuppressWarnings("unused")
    public void onTankSizeChanged(IEFluxTank tank){
        checkSize();
        forEachTank(TankActions.syncFluidType, getStoredFluid());
        capacityMap.put(((TileEntity)tank).getPos(), tank.getTankSize());
        needsSorting = true;
        setTankFluidHeights();
        forEachTank(TankActions.markDirty);
    }

    public int getTankContentAmount(BlockPos pos){
        Integer integer = saveAmountMap.get(pos);
        return integer == null ? 0 : integer;
    }

    public int getCapacity(){
        return tank.getCapacity();
    }

    public int getTotalStoredAmount(){
        return tank.getFluidAmount();
    }

    private void setTankFluidHeights(){
        if (needsSorting){
            renderData.clear();
            rowAmountMap.clear();
            saveAmountMap.clear();
            Collections.sort(allLocations, new Comparator<BlockPos>() {
                @Override
                public int compare(BlockPos o1, BlockPos o2) {
                    return o1.getY() - o2.getY();
                }
            });
            for (BlockPos loc : allLocations){
                List<BlockPos> list = renderData.get(loc.getY());
                if (list == null){
                    renderData.put(loc.getY(), list = Lists.newArrayList());
                }
                list.add(loc);
            }
            for (Integer i : renderData.keySet()){
                List<BlockPos> list = renderData.get(i);
                rowAmountMap.put(i, getSizeFor(list));
            }
            needsSorting = false;
        }
        int total = tank.getFluidAmount();
        List<Integer> y = Lists.newArrayList(renderData.keySet());
        Collections.sort(y);
        for (Integer j : y){
            List<BlockPos> list = renderData.get(j);
            float filled = 0.0f;
            if (total > 0) {
                int i = rowAmountMap.get(j);
                int toAdd = Math.min(total, i);
                total -= toAdd;
                filled = (float) toAdd / i;
                for (BlockPos pos : list){
                    int i1 = capacityMap.get(pos);
                    int toSave = Math.min((int)(filled * i1), toAdd);
                    toAdd -= toSave;
                    saveAmountMap.put(pos, toSave);
                }
            }
            forEachTank(list, TankActions.syncFluidHeight, filled);
        }
    }

    private void forEachTank(TankIterable tankIterable, Object... data){
        forEachTank(allLocations, tankIterable, data);
    }

    private void forEachTank(List<BlockPos> positions, TankIterable tankIterable, Object... data){
        for (BlockPos pos :positions){
            TileEntity tile = WorldHelper.getTileAt(world, pos);
            if (tile instanceof IEFluxTank){
                tankIterable.forTank((IEFluxTank) tile, data);
            }
        }
    }

    private Fluid getStoredFluid(){
        FluidStack stack = tank.getFluid();
        return (stack == null || stack.amount <= 0) ? null : stack.getFluid();
    }

    private void checkSize(){
        FluidStack stack = tank.getFluid();
        tank = new FluidTank(getSizeFor(allLocations));
        if (stack != null){
            tank.fill(stack.copy(), true);
        }
    }

    private void mergeTank(FluidTank tank){
        FluidStack s1 = tank.getFluid();
        FluidStack s2 = this.tank.getFluid();
        FluidStack inject;
        if (s1 == null){
            inject = s2;
        } else if (s2 == null){
            inject = s1;
        } else {
            if (!s1.isFluidEqual(s2)){
                throw new IllegalArgumentException();
            }
            inject = s1.copy();
            inject.amount += s2.amount;
        }
        this.tank = new FluidTank(tank.getCapacity() + this.tank.getCapacity());
        if (inject != null){
            tank.fill(inject, true);
        }
    }

    private int getSizeFor(List<BlockPos> positions){
        int[] param = new int[]{0};
        forEachTank(positions, TankActions.checkCapacity, new Object[]{param});
        return param[0];
    }

    //Tank impl

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        int ret = tank.fill(resource, doFill);
        if (doFill) {
            setTankFluidHeights();
            forEachTank(TankActions.setLastSeenFluid, getStoredFluid());
            forEachTank(TankActions.markDirty);
        }
        return ret;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
            return null;
        }
        return drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        FluidStack ret = tank.drain(maxDrain, doDrain);
        if (doDrain) {
            setTankFluidHeights();
            forEachTank(TankActions.setLastSeenFluid, getStoredFluid());
            forEachTank(TankActions.markDirty);
        }
        return ret;
    }

    @Override
    public boolean canFill(Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo() {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    private interface TankIterable {

        public void forTank(@Nonnull IEFluxTank tile, Object... data);

    }

    private enum TankActions implements TankIterable {

        syncFluidType {

            @Override
            public void forTank(@Nonnull IEFluxTank tile, Object... data) {
                tile.setClientRenderFluid((Fluid) data[0]);
            }

        }, syncFluidHeight {

            @Override
            public void forTank(@Nonnull IEFluxTank tile, Object... data) {
                tile.setClientRenderHeight((Float)data[0]);
            }

        }, markDirty {

            @Override
            public void forTank(@Nonnull IEFluxTank tile, Object... data) {
                ((TileEntity)tile).markDirty();
            }

        }, checkCapacity {

            @Override
            public void forTank(@Nonnull IEFluxTank tile, Object... data) {
                ((int[])data[0])[0] += tile.getTankSize();
            }

        }, setLastSeenFluid {

            @Override
            public void forTank(@Nonnull IEFluxTank tile, Object... data) {
                tile.setLastSeenFluid((Fluid)data[0]);
            }

        }

    }

}
