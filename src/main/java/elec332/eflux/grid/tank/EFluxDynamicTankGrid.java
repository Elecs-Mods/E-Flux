package elec332.eflux.grid.tank;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import elec332.core.util.FluidTankWrapper;
import elec332.core.world.DimensionCoordinate;
import elec332.core.world.WorldHelper;
import elec332.eflux.util.IEFluxTank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Created by Elec332 on 9-10-2016.
 */
@SuppressWarnings("all")
public class EFluxDynamicTankGrid implements IFluidHandler {

    public EFluxDynamicTankGrid(EFluxTankHandler.TileLink o) {
        IEFluxTank tile = o.getTank();
        tank = new FluidTank(tile.getTankSize());
        fluidHandler = new FluidTankWrapper() {

            @Override
            protected IFluidTank getTank() {
                return tank;
            }

        };
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
        capacityMap.put(o.getPosition(), tile.getTankSize());
        tanks = Sets.newHashSet(o);
        tanks_ = Collections.unmodifiableSet(tanks);
        needsSorting = true;
    }

    private FluidTank tank;
    private Fluid lastFluid;
    private IFluidHandler fluidHandler;
    private final Set<EFluxTankHandler.TileLink> tanks, tanks_;
    private final Map<Integer, List<DimensionCoordinate>> renderData;
    private final Map<Integer, Integer> rowAmountMap;
    private final Map<DimensionCoordinate, Integer> capacityMap;
    private final Map<DimensionCoordinate, Integer> saveAmountMap;
    private boolean needsSorting;
    private int timer;

    public void tick() {
        if (timer >= 20){
            Fluid f = getStoredFluid();
            forEachTank(TankActions.syncFluidType, f);
            setTankFluidHeights();
            forEachTank(TankActions.setLastSeenFluid, f);
            lastFluid = f;
            timer = 0;
        }
        timer++;
    }

    protected void mergeWith(EFluxDynamicTankGrid multiBlock) {
        for (EFluxTankHandler.TileLink tile : multiBlock.tanks_){
            tile.setGrid(this);
            tanks.add(tile);
        }
        mergeTank(multiBlock.tank);
        capacityMap.putAll(multiBlock.capacityMap);
        needsSorting = true;
        forEachTank(TankActions.syncFluidType, getStoredFluid());
        setTankFluidHeights();
        forEachTank(TankActions.markDirty);
    }

    protected void invalidate() {
        forEachTank(TankActions.setSaveData, this);
    }

    public Set<EFluxTankHandler.TileLink> getConnections(){
        return tanks_;
    }

    public void onRemoved(EFluxTankHandler.TileLink o){
        o.getTank().setSaveData(getSaveData(o.getTank()));
        o.setGrid(null);
    }

    protected boolean canMerge(EFluxDynamicTankGrid grid){
        if (grid == this){
            return false;
        }
        FluidStack fluid = getStoredFluidStack(), otherFluid = grid.getStoredFluidStack();
        return fluid == null || otherFluid == null || fluid.isFluidEqual(otherFluid);
    }

    public NBTTagCompound getSaveData(IEFluxTank tank){
        NBTTagCompound ret = new NBTTagCompound();
        FluidStack stack = this.tank.getFluid();
        if (stack != null) {
            int amount = getTankContentAmount(DimensionCoordinate.fromTileEntity((TileEntity) tank));
            stack = stack.copy();
            stack.amount = amount;
            NBTTagCompound fluidTag = new NBTTagCompound();
            stack.writeToNBT(fluidTag);
            ret.setTag("fluid", fluidTag);
        }
        return ret;
    }

    @SuppressWarnings("unused")
    public void onTankSizeChanged(IEFluxTank tank){
        checkSize();
        forEachTank(TankActions.syncFluidType, getStoredFluid());
        capacityMap.put(DimensionCoordinate.fromTileEntity((TileEntity) tank), tank.getTankSize());
        needsSorting = true;
        setTankFluidHeights();
        forEachTank(TankActions.markDirty);
    }

    public int getTankContentAmount(DimensionCoordinate pos){
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

            List<EFluxTankHandler.TileLink> list = Lists.newArrayList(tanks);

            Collections.sort(list, new Comparator<EFluxTankHandler.TileLink>() {
                @Override
                public int compare(EFluxTankHandler.TileLink o1, EFluxTankHandler.TileLink o2) {
                    return o1.getPosition().getPos().getY() - o2.getPosition().getPos().getY();
                }
            });
            for (EFluxTankHandler.TileLink tile : list){
                int y = tile.getPosition().getPos().getY();
                List<DimensionCoordinate> list1 = renderData.get(y);
                if (list1 == null){
                    renderData.put(y, list1 = Lists.newArrayList());
                }
                list1.add(tile.getPosition());
            }
            for (Integer i : renderData.keySet()){
                List<DimensionCoordinate> list2 = renderData.get(i);
                rowAmountMap.put(i, getSizeFor(list2));
            }
            needsSorting = false;
        }
        saveAmountMap.clear();
        int total = tank.getFluidAmount();
        List<Integer> y = Lists.newArrayList(renderData.keySet());
        Collections.sort(y);
        for (Integer j : y){
            List<DimensionCoordinate> list = renderData.get(j);
            float filled = 0.0f;
            if (total > 0) {
                int i = rowAmountMap.get(j);
                int toAdd = Math.min(total, i);
                total -= toAdd;
                filled = (float) toAdd / i;
                DimensionCoordinate posQ = list.get(list.size() - 1);
                for (DimensionCoordinate pos : list){
                    int i1 = capacityMap.get(pos);
                    int toSave = pos == posQ ? toAdd : Math.min((int)(filled * i1), toAdd);
                    toAdd -= toSave;
                    saveAmountMap.put(pos, toSave);
                }
            }
            forEachTank(list, TankActions.syncFluidHeight, filled);
        }
    }

    private void forEachTank(TankIterable tankIterable, Object... data){
        for (EFluxTankHandler.TileLink tileLink : tanks){
            DimensionCoordinate pos = tileLink.getPosition();
            @Nonnull
            @SuppressWarnings("all")
            World world = pos.getWorld();
            if (WorldHelper.chunkLoaded(world, pos.getPos())) {
                TileEntity tile = WorldHelper.getTileAt(world, pos.getPos());
                if (tile instanceof IEFluxTank) {
                    tankIterable.forTank((IEFluxTank) tile, data);
                }
            }
        }
    }

    private void forEachTank(List<DimensionCoordinate> positions, TankIterable tankIterable, Object... data){
        for (DimensionCoordinate pos : positions){
            @Nonnull
            @SuppressWarnings("all")
            World world = pos.getWorld();
            if (WorldHelper.chunkLoaded(world, pos.getPos())) {
                TileEntity tile = WorldHelper.getTileAt(world, pos.getPos());
                if (tile instanceof IEFluxTank) {
                    tankIterable.forTank((IEFluxTank) tile, data);
                }
            }
        }
    }

    private Fluid getStoredFluid(){
        FluidStack stack = getStoredFluidStack();
        return (stack == null || stack.amount <= 0) ? null : stack.getFluid();
    }

    private FluidStack getStoredFluidStack(){
        return tank.getFluid();
    }

    private void checkSize(){
        FluidStack stack = tank.getFluid();
        tank = new FluidTank(getSizeFor(null));
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
            this.tank.fill(inject, true);
        }
    }

    private int getSizeFor(List<DimensionCoordinate> positions){
        int[] param = new int[]{0};
        if (positions == null){
            forEachTank(TankActions.checkCapacity, new Object[]{param});
        } else {
            forEachTank(positions, TankActions.checkCapacity, new Object[]{param});
        }
        return param[0];
    }

    //Tank impl

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return fluidHandler.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        int ret = fluidHandler.fill(resource, doFill);
        if (doFill) {
            onDirectChange();
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
        FluidStack ret = fluidHandler.drain(maxDrain, doDrain);
        if (doDrain) {
            onDirectChange();
        }
        return ret;
    }

    private void onDirectChange(){
        setTankFluidHeights();
        Fluid f = getStoredFluid();
        if (lastFluid != f){
            forEachTank(TankActions.syncFluidType, f);
            this.lastFluid = f;
        }
        forEachTank(TankActions.setLastSeenFluid, getStoredFluid());
        forEachTank(TankActions.markDirty);
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

        }, setSaveData {

            @Override
            public void forTank(@Nonnull IEFluxTank tile, Object... data) {
                tile.setSaveData(((EFluxDynamicTankGrid)data[0]).getSaveData(tile));
            }

        }, markForUpdate {

            @Override
            public void forTank(@Nonnull IEFluxTank tile, Object... data) {
                WorldHelper.markBlockForUpdate(((TileEntity)tile).getWorld(), ((TileEntity)tile).getPos());
            }

        }

    }


}
