package elec332.eflux.endernetwork;

import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.ISpecialEnergySource;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.api.energy.container.IEFluxPowerHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

/**
 * Created by Elec332 on 18-2-2016.
 */
public class EnderNetwork implements INBTSerializable<NBTTagCompound>, IEFluxPowerHandler, IEnergyReceiver, ISpecialEnergySource {

    EnderNetwork(UUID id){
        this.energyContainer = new EnergyContainer(10000, this);
        this.id = id;
    }

    private EnergyContainer energyContainer;
    private final UUID id;

    public void attemptLinkItem(ItemStack stack){
        if (stack != null && stack.getItem() != null && stack.getItem() instanceof ILinkableItem){
            ((ILinkableItem)stack.getItem()).setLinkID(stack, id);
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound saveTag = new NBTTagCompound();
        energyContainer.writeToNBT(saveTag);
        return saveTag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        energyContainer.readFromNBT(nbt);
    }

    /**
     * The amount returned here is NOT supposed to change, anf if it does,
     * do not forget that it will receive a penalty if the machine is not running at optimum RP
     *
     * @return the amount of requested EF
     */
    @Override
    public int getEFForOptimalRP() {
        return 10;
    }

    @Override
    public float getAcceptance() {
        return 0.2f;
    }

    @Override
    public int getOptimalRP() {
        return 12;
    }

    @Override
    public void markObjectDirty() {
        //Nope
    }

    /**
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP() {
        return energyContainer.requestedRP();
    }

    /**
     * @param rp The Redstone Potential in the network
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    @Override
    public int getRequestedEF(int rp) {
        return energyContainer.getRequestedEF(rp);
    }

    /**
     * @param rp the RedstonePotential in the network
     * @param ef the amount of EnergeticFlux that is being provided
     * @return The amount of EnergeticFlux that wasn't used
     */
    @Override
    public int receivePower(int rp, int ef) {
        return energyContainer.receivePower(rp, ef);
    }

    /**
     * @param rp    the RedstonePotential in the network
     * @param reqEF the requested amount of EnergeticFlux
     * @return The amount of EnergeticFlux the tile will provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergeticFlux(int rp, int reqEF) {
        return rp * reqEF > mpe() ? provideEnergy(rp, true) : (energyContainer.drainPower(rp*reqEF) ? reqEF : 0);
    }

    /**
     * @param rp      the RedstonePotential in the network
     * @param execute weather the power is actually drawn from the tile,
     *                this flag is always true for IEnergySource.
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, boolean execute) {
        int ret = mpe()/rp;
        if (execute){
            energyContainer.drainPower(ret);
        }
        return ret;
    }

    private int mpe(){
        return Math.min(1000, energyContainer.getStoredPower());
    }

}
