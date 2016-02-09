package elec332.eflux.api.event;

import elec332.eflux.api.energy.EnergyAPIHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class PowerTransmitterEvent extends WorldEvent {

    private PowerTransmitterEvent(TileEntity tile) {
        super(tile.getWorld());
        EnergyAPIHelper.checkValidity(tile);
        this.transmitterTile = tile;
    }

    public final TileEntity transmitterTile;

    public static class Load extends PowerTransmitterEvent {

        public Load(TileEntity tile) {
            super(tile);
        }

    }

    public static class UnLoad extends PowerTransmitterEvent {

        public UnLoad(TileEntity tile) {
            super(tile);
        }

    }

}
