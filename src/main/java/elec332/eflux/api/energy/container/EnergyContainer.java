package elec332.eflux.api.energy.container;

import elec332.core.inventory.IHasProgressBar;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.util.CalculationHelper;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Elec332 on 24-8-2015.
 */
public class EnergyContainer implements IHasProgressBar, IEnergyReceiver{

    public EnergyContainer(int energy, IEFluxPowerHandler powerHandler, IBreakableMachine breakableMachine){
        this(energy, powerHandler);
        this.breakableMachine = breakableMachine;
    }

    public EnergyContainer(int maxEnergy, IEFluxPowerHandler powerHandler){
        this.maxEnergy = maxEnergy;
        this.powerHandler = powerHandler;
        this.breakableMachine = null;
    }

    private IEFluxPowerHandler powerHandler;
    private int maxEnergy;
    private int storedPower;
    private IBreakableMachine breakableMachine;
    private int processTime;
    private int progress;
    private IProgressMachine progressMachine;
    private int lastRP;

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
        }
    }

    protected int calculateProcessTime(int initialTime){
        int dev = Math.max(lastRP, powerHandler.getOptimalRP());
        int devTo = Math.min(lastRP, powerHandler.getOptimalRP());
        float mul = (float)devTo/dev;
        float fin = 1/(mul/2);
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

    @Override
    public int requestedRP() {
        return powerHandler.getOptimalRP();
    }

    @Override
    public final int getRequestedEF(int rp) {
        if (breakableMachine != null) {
            if (rp < requestedRP() * (1 - getAcceptance()) || breakableMachine.isBroken()) {
                return 0;
            }
            if (rp > requestedRP() * (1 + getAcceptance())) {
                breakMachine();
                markDirty();
            }
        }
        return powerHandler.getEFForOptimalRP(); //Math.min(efForOptimalRP, (maxEnergy-storedPower)/rp);
    }

    @Override
    public final int receivePower(int rp, int ef) {
        if (breakableMachine != null && breakableMachine.isBroken())
            return 0;
        int calcEF = CalculationHelper.calcRequestedEF(rp, requestedRP(), powerHandler.getEFForOptimalRP(), (maxEnergy-storedPower)/rp, getAcceptance());
        this.storedPower += rp*calcEF;
        if (storedPower > maxEnergy)
            this.storedPower = maxEnergy;
        this.lastRP = rp;
        markDirty();
        return 0;
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

}
