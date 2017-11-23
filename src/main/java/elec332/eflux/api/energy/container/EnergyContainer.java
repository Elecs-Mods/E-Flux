package elec332.eflux.api.energy.container;

import elec332.core.api.inventory.IHasProgressBar;
import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.api.util.ConnectionPoint;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 24-8-2015.
 */
public class EnergyContainer implements IHasProgressBar, IEnergyReceiver {

    public EnergyContainer(IEFluxPowerHandler powerHandler, IBreakableMachine breakableMachine){
        this(powerHandler);
        this.breakableMachine = breakableMachine;
    }

    public EnergyContainer(IEFluxPowerHandler powerHandler){
        this.maxEnergy = powerHandler.getWorkingVoltage() * powerHandler.getMaxRP();
        this.powerHandler = powerHandler;
        this.breakableMachine = null;
    }

    private IEFluxPowerHandler powerHandler;
    private int maxEnergy;
    private int storedPower;
    public double lastEf, lastRP;
    private IBreakableMachine breakableMachine;
    private int processTime;
    private int progress;
    private IProgressMachine progressMachine;
    //private int lastRP;

    public final void tick(){
        if (progressMachine != null){
            if (progressMachine.canProcess() && drainPower(progressMachine.getRequiredPowerPerTick())) {
                if (progress == 0) {
                    processTime = calculateProcessTime(progressMachine.getProcessTime());
                }
                progress++;
                if (progress >= processTime) {
                    this.progress = 0;
                    progressMachine.onProcessDone();
                    markDirty();
                }
            } else if (progress > 0) {
                progress = 0;
            }
            storedPower = 0;
        }
    }

    protected int calculateProcessTime(int initialTime){
        //int dev = Math.max(lastRP, powerHandler.getOptimalRP());
        //int devTo = Math.min(lastRP, powerHandler.getOptimalRP());
        //float mul = (float)devTo/dev;
        //float fin = 1/(mul/2);
        return (int) initialTime;//(initialTime*fin);
    }

    public boolean drainPower(int toDrain){
        if (storedPower >= toDrain){
            storedPower -= toDrain;
            markDirty();
            return true;
        }
        if (storedPower > 0) {
            storedPower = 0;
            markDirty();
        }
        return false;
    }

    public void setProgressMachine(IProgressMachine progressMachine) {
        this.progressMachine = progressMachine;
    }

    public void setBreakableMachine(IBreakableMachine breakableMachine) {
        this.breakableMachine = breakableMachine;
    }

    public void setStoredPower(int storedPower) {
        this.storedPower = storedPower;
        markDirty();
    }

    public int getMaxStoredEnergy() {
        return maxEnergy;
    }

    public int getStoredPower() {
        return storedPower;
    }

    public float getAcceptance() {
        return powerHandler.getAcceptance();
    }

    public IBreakableMachine getBreakableMachine() {
        return breakableMachine;
    }

    public void writeToNBT(NBTTagCompound tag){
        tag.setInteger("EnergyContainer_Energy", storedPower);
        tag.setInteger("EnergyContainer_Progress", progress);
        tag.setInteger("EnergyContainer_CalculatedProcessTime", processTime);
    }

    public void readFromNBT(NBTTagCompound tag){
        this.storedPower = tag.getInteger("EnergyContainer_Energy");
        this.progress = tag.getInteger("EnergyContainer_Progress");
        this.processTime = tag.getInteger("EnergyContainer_CalculatedProcessTime");
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public float getProgressScaled(int progress) {
        return progress/(float) processTime;
    }

    public final void breakMachine(){
        if (breakableMachine != null){
            breakableMachine.setBroken(true);
            breakableMachine.onBroken();
        }
    }

    private void markDirty(){
        powerHandler.markObjectDirty();
    }

    @Override
    public double getResistance() {
        return powerHandler.getResistance();
    }

    @Override
    public void receivePower(double ef, double rp) {
        ef = Math.abs(ef);
        rp = Math.abs(rp);
        lastEf = ef;
        lastRP = rp;
        storedPower += ef * rp;
        if (storedPower > maxEnergy){
            storedPower = maxEnergy;
        }
    }

    @Nonnull
    @Override
    public EnergyType getEnergyType(int post) {
        return powerHandler.getEnergyType();
    }

    @Nonnull
    @Override
    public ConnectionPoint getConnectionPoint(int post) {
        return powerHandler.getConnectionPoint(post);
    }

    @Nullable
    @Override
    public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec) {
        return powerHandler.getConnectionPoint(side, hitVec);
    }

}
