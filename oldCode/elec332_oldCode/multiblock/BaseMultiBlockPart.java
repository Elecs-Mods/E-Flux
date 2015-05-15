package elec332_oldCode.multiblock;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.core.main.ElecCore;
import elec332.core.util.IRunOnce;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Elec332 on 15-5-2015.
 */
public class BaseMultiBlockPart extends TileBase implements IMultiBlockPart{

    private LocList locList;

    @Override
    public LocList getLocList() {
        return locList;
    }

    public void setLocList(LocList locList) {
        this.locList = locList;
    }

    public void validate() {
        this.tileEntityInvalid = false;
        ElecCore.tickHandler.registerCall(new IRunOnce() {
            @Override
            public void run() {
                MinecraftForge.EVENT_BUS.post(new MultiBlockEvent.Loaded(BaseMultiBlockPart.this));
            }
        });
    }

    public void invalidate() {
        this.tileEntityInvalid = true;
        MinecraftForge.EVENT_BUS.post(new MultiBlockEvent.Unloaded(this, false));
    }

    public void onChunkUnload() {
        MinecraftForge.EVENT_BUS.post(new MultiBlockEvent.Unloaded(this, true));
    }
}
