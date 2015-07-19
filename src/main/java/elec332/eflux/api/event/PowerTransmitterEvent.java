package elec332.eflux.api.event;

import elec332.eflux.api.energy.EnergyAPIHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Elec332 on 16-4-2015.
 */
class PowerTransmitterEvent extends WorldEvent {

    public TileEntity transmitterTile;

    public PowerTransmitterEvent(TileEntity transmitterTile) {
        super(transmitterTile.getWorldObj());
        EnergyAPIHelper.checkValidity(transmitterTile);
        this.transmitterTile = transmitterTile;
    }
}
