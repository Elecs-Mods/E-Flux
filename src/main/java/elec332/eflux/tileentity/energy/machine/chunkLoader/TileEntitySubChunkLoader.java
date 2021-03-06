package elec332.eflux.tileentity.energy.machine.chunkLoader;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.main.ElecCore;
import elec332.core.server.ServerHelper;
import elec332.core.util.PlayerHelper;
import elec332.eflux.handler.ChunkLoaderPlayerProperties;
import elec332.eflux.tileentity.TileEntityEFlux;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

/**
 * Created by Elec332 on 25-5-2015.
 */
@RegisteredTileEntity("TileEntityEFluxSubChunkLoader")
public class TileEntitySubChunkLoader extends TileEntityEFlux implements IChunkLoader{

    private UUID owner;

    @Override
    public void onBlockPlacedBy(final EntityLivingBase entityLiving, ItemStack stack) {
        super.onBlockPlacedBy(entityLiving, stack);
        ElecCore.tickHandler.registerCall(new Runnable() {
            @Override
            public void run() {
                if (!ServerHelper.isServer(getWorld()))
                    return;
                if (entityLiving instanceof EntityPlayer) {
                    if (TileEntitySubChunkLoader.this.owner == null)
                        TileEntitySubChunkLoader.this.owner = PlayerHelper.getPlayerUUID((EntityPlayer) entityLiving);
                    PlayerHelper.sendMessageToPlayer((EntityPlayer)entityLiving, "Placed chunkloader at "+getPos());
                    ChunkLoaderPlayerProperties.get(PlayerHelper.getPlayerUUID((EntityPlayer) entityLiving)).addLoader(TileEntitySubChunkLoader.this);
                }
            }
        }, entityLiving.getEntityWorld());
    }

    @Override
    public void onBlockRemoved() {
        super.onBlockRemoved();
        if (owner != null)
            ChunkLoaderPlayerProperties.get(owner).removeLoader(this);
    }

    public boolean canPlace(EntityPlayer player){
        return player != null && (owner == null || PlayerHelper.getPlayerUUID(player).equals(owner));
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
