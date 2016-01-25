package elec332.eflux.api.event;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class TransmitterUnloadedEvent extends PowerTransmitterEvent {

    public TransmitterUnloadedEvent(World world, BlockPos pos, Object tile) {
        super(world, pos, tile);
    }

}
