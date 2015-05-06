package elec332.eflux.tileentity.energy.generator;

import elec332.core.util.DirectionHelper;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.client.inventory.GuiCoalGenerator;
import elec332.eflux.inventory.ContainerCoalGenerator;
import elec332.eflux.tileentity.BaseMachineTEWithInventory;
import elec332.eflux.util.EnumMachines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 29-4-2015.
 */
public class CoalGenerator extends BaseMachineTEWithInventory implements IEnergySource{

    public CoalGenerator() {
        super(1);
    }

    @Override
    public Container getGuiServer(EntityPlayer player) {
        return new ContainerCoalGenerator(this, player);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new GuiCoalGenerator(this, player);
    }

    @Override
    protected String standardInventoryName() {
        return "CoalGenerator";
    }

    @Override
    public EnumMachines getMachine() {
        return null;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and provide power to the given side
     */
    @Override
    public boolean canProvidePowerTo(ForgeDirection direction) {
        return direction == DirectionHelper.getDirectionFromNumber(getBlockMetadata());
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param direction the direction where the power will be provided to
     * @param execute   weather the power is actually drawn from the tile,
     *                  this flag is always true for IEnergySource.
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, ForgeDirection direction, boolean execute) {
        return 20; //getStackInSlot(0) != null?20:0;
    }

    @Override
    public void onTileLoaded() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
    }

    @Override
    public void onTileUnloaded() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(this));
    }
}
