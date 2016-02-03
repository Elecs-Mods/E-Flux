package elec332.eflux.tileentity;

import elec332.core.main.ElecCore;
import elec332.core.tile.TileBase;
import elec332.eflux.api.energy.EnergyAPIHelper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Elec332 on 16-5-2015.
 */
public abstract class EnergyTileBase extends TileBase {

    @Override
    public void onChunkUnload() {
        System.out.println("chunkUnload");
        if (!worldObj.isRemote) {
            ElecCore.tickHandler.registerCall(new Runnable() {
                @Override
                public void run() {
                    if (!worldObj.isRemote)
                        EnergyAPIHelper.postUnloadEvent(EnergyTileBase.this);
                }
            }, Side.SERVER);
        }
        super.onChunkUnload();
    }

    @Override
    public void onTileLoaded() {
        try {
            if (!worldObj.isRemote)
                EnergyAPIHelper.postLoadEvent(this);
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onTileLoaded();
    }

}
