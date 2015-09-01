package elec332.eflux.api.energy.container;

import elec332.core.inventory.IHasProgressBar;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.util.CalculationHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 24-8-2015.
 */
public class EnergyContainer implements IHasProgressBar, IEnergyReceiver{

    public EnergyContainer(int energy, int efForOptimalRP, float acceptance, int rp, IBreakableMachine breakableMachine){
        this(energy, efForOptimalRP, acceptance, rp);
        this.breakableMachine = breakableMachine;
    }

    public EnergyContainer(int energy, int efForOptimalRP, float acceptance, int rp){
        this(energy, efForOptimalRP);
        this.acceptance = acceptance;
        this.optimalRP = rp;
        this.breakableMachine = null;
    }

    public EnergyContainer(int maxEnergy, int efForOptimalRP){
        this.maxEnergy = maxEnergy;
        this.efForOptimalRP = efForOptimalRP;
        this.acceptance = 1.0f;
        this.optimalRP = 10;
        this.breakableMachine = null;
    }

    private int maxEnergy;
    private int storedPower;
    private int efForOptimalRP;
    private int optimalRP;
    private float acceptance;
    private IBreakableMachine breakableMachine;
    private int processTime;
    private int progress;
    private IProgressMachine progressMachine;
    private int lastRP;

    public final void tick(){
        if (progressMachine != null){
            if (progressMachine.canProcess() && storedPower >= progressMachine.getRequiredPowerPerTick()) {
                if (progress == 0)
                    processTime = calculateProcessTime(progressMachine.getProcessTime());
                this.storedPower -= progressMachine.getRequiredPowerPerTick();
                progress++;
                if (progress >= processTime) {
                    this.progress = 0;
                    progressMachine.onProcessDone();
                }
            } else if (progress > 0) {
                progress = 0;
            }
        }
    }

    protected int calculateProcessTime(int initialTime){
        int dev = Math.max(lastRP, optimalRP);
        int devTo = Math.min(lastRP, optimalRP);
        float mul = (float)devTo/dev;
        float fin = 1/(mul/2);
        return (int) (initialTime*fin);
    }

    public boolean drainPower(int toDrain){
        if (storedPower >= toDrain){
            storedPower -= toDrain;
            return true;
        }
        return false;
    }

    public void setProgressMachine(IProgressMachine progressMachine) {
        this.progressMachine = progressMachine;
    }

    public void setStoredMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public void setAcceptance(float acceptance) {
        this.acceptance = acceptance;
    }

    public void setBreakableMachine(IBreakableMachine breakableMachine) {
        this.breakableMachine = breakableMachine;
    }

    public void setStoredPower(int storedPower) {
        this.storedPower = storedPower;
    }

    public int getMaxStoredEnergy() {
        return maxEnergy;
    }

    public int getStoredPower() {
        return storedPower;
    }

    public float getAcceptance() {
        return acceptance;
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
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return true;
    }

    @Override
    public int requestedRP(ForgeDirection direction) {
        return optimalRP;
    }

    @Override
    public final int getRequestedEF(int rp, ForgeDirection direction) {
        if (breakableMachine != null) {
            if (rp < requestedRP(direction) * (1 - getAcceptance()) || breakableMachine.isBroken()) {
                return 0;
            }
            if (rp > requestedRP(direction) * (1 + getAcceptance())) {
                breakableMachine.setBroken(true);
                breakableMachine.onBroken();
            }
        }
        return efForOptimalRP; //Math.min(efForOptimalRP, (maxEnergy-storedPower)/rp);
    }

    @Override
    public final int receivePower(ForgeDirection direction, int rp, int ef) {
        if (breakableMachine != null && breakableMachine.isBroken())
            return 0;
        int calcEF = CalculationHelper.calcRequestedEF(rp, requestedRP(direction), efForOptimalRP, (maxEnergy-storedPower)/rp, getAcceptance());
        this.storedPower += rp*calcEF;
        if (storedPower > maxEnergy)
            this.storedPower = maxEnergy;
        this.lastRP = rp;
        return 0;
    }

}
