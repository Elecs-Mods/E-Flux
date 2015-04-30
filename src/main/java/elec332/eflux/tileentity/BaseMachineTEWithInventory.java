package elec332.eflux.tileentity;

import elec332.core.baseclasses.tileentity.BaseTileWithInventory;
import elec332.eflux.EFlux;
import elec332.eflux.util.IEFluxMachine;
import elec332.eflux.util.IEFluxTile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 4-4-2015.
 */
public abstract class BaseMachineTEWithInventory extends BaseTileWithInventory implements IEFluxTile, IEFluxMachine {

    public BaseMachineTEWithInventory(int invSize) {
        super(invSize);

    }

    public boolean openGui(EntityPlayer player){
        player.openGui(EFlux.instance, 0, worldObj, xCoord, yCoord, zCoord);
        return true;
    }

    public abstract Object getGuiServer(EntityPlayer player);

    public abstract Object getGuiClient(EntityPlayer player);

    @Override
    public int getLightOpacity() {
        return 0;
    }

    @Override
    public int getLightValue() {
        return 0;
    }

    @Override
    public void onBlockRemoved() {
    }

    @Override
    public void onBlockAdded() {
    }

    @Override
    public void onBlockPlacedBy(EntityLivingBase entityLiving, ItemStack stack) {
    }

    @Override
    public void onNeighborBlockChange(Block block) {
    }

    @Override
    public void onWrenched(ForgeDirection forgeDirection) {
        switch (forgeDirection){
            case NORTH:
               this.setBlockMetadataWithNotify(0);
                break;
            case EAST:
                this.setBlockMetadataWithNotify(1);
                break;
            case SOUTH:
                this.setBlockMetadataWithNotify(2);
                break;
            case WEST:
                this.setBlockMetadataWithNotify(3);
                break;
            default:
                break;
        }
        markDirty();
    }

    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ){
        return openGui(player);
    }
}
