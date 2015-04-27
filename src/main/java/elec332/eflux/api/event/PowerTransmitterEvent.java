package elec332.eflux.api.event;

import elec332.eflux.api.energy.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Elec332 on 16-4-2015.
 */
class PowerTransmitterEvent extends WorldEvent {

    public IEnergyTile transmitterTile;

    public PowerTransmitterEvent(IEnergyTile transmitterTile) {
        super(((TileEntity)transmitterTile).getWorldObj());
        this.transmitterTile = transmitterTile;
    }
}
