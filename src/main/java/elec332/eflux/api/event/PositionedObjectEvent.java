package elec332.eflux.api.event;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Elec332 on 24-1-2016.
 */
public class PositionedObjectEvent extends WorldEvent {

    public PositionedObjectEvent(World world, BlockPos pos) {
        super(world);
        this.pos = pos;
    }

    public final BlockPos pos;

}
