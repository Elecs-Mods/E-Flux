package elec332.eflux.api.event;

import elec332.eflux.api.energy.EnergyAPIHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Elec332 on 16-4-2015.
 */
class PowerTransmitterEvent extends PositionedObjectEvent {

    public PowerTransmitterEvent(World world, BlockPos pos, Object tile) {
        super(world, pos);
        EnergyAPIHelper.checkValidity(tile);
        this.transmitterTile = tile;
    }

    public final Object transmitterTile;

}
