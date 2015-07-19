package elec332.eflux.tileentity.energy.machine.chunkLoader;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.core.main.ElecCore;
import elec332.core.player.PlayerHelper;
import elec332.core.util.IRunOnce;
import elec332.eflux.handler.ChunkLoaderPlayerProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

/**
 * Created by Elec332 on 25-5-2015.
 */
public class ChunkLoaderSubTile extends TileBase {

    private UUID owner;

    @Override
    public void onBlockPlacedBy(final EntityLivingBase entityLiving, ItemStack stack) {
        super.onBlockPlacedBy(entityLiving, stack);
        ElecCore.tickHandler.registerCall(new IRunOnce() {
            @Override
            public void run() {
                if (entityLiving instanceof EntityPlayer && ChunkLoaderPlayerProperties.get((EntityPlayer) entityLiving).hasHandler()) {
                    if (ChunkLoaderSubTile.this.owner == null)
                        ChunkLoaderSubTile.this.owner = PlayerHelper.getPlayerUUID((EntityPlayer) entityLiving);
                    PlayerHelper.sendMessageToPlayer((EntityPlayer)entityLiving, "Placed chunkloader at "+myLocation().toString());
                    ChunkLoaderPlayerProperties.get((EntityPlayer) entityLiving).getMain().addLoader(ChunkLoaderSubTile.this);
                }
            }
        });
    }

    @Override
    public void onBlockRemoved() {
        super.onBlockRemoved();
        if (owner != null && ChunkLoaderPlayerProperties.get(worldObj.func_152378_a(owner)).hasHandler())
            ChunkLoaderPlayerProperties.get(worldObj.func_152378_a(owner)).getMain().removeLoader(this);
    }

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        if (owner != null)
            tagCompound.setString("Owner", owner.toString());
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        if (tagCompound.hasKey("Owner"))
            this.owner = UUID.fromString(tagCompound.getString("Owner"));
    }
}
