package elec332.eflux.tileentity.energy.machine.chunkLoader;

import com.google.common.collect.Lists;
import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInformation;
import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.server.ServerHelper;
import elec332.core.util.PlayerHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.handler.ChunkLoaderPlayerProperties;
import elec332.eflux.tileentity.TileEntityBreakableMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.ForgeChunkManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 25-5-2015.
 */
@RegisteredTileEntity("TileEntityEFluxMainChunkLoader")
public class TileEntityMainChunkLoader extends TileEntityBreakableMachine implements IChunkLoader, ITickable {

    public TileEntityMainChunkLoader(){
        repairItems = Lists.newArrayList(new ItemStack(Items.ENDER_EYE), new ItemStack(Items.ENDER_PEARL));
        active = true;
        this.changed = true;
        this.tickets = Lists.newArrayList();
    }

    private List<ItemStack> repairItems;
    private UUID owner;
    private boolean active;
    private int neededPower;
    public boolean changed;
    private int loadedChunks;
    private boolean recentlyWithoutPower;

    private List<ForgeChunkManager.Ticket> tickets;

    private List<BlockPos> getLocations(){
        return ChunkLoaderPlayerProperties.get(this.owner).getLocations();
    }

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;
        calculatePower();
        if (energyContainer.drainPower(neededPower) && active){
            if (recentlyWithoutPower)
                this.changed = true;
            handle(true);
            this.recentlyWithoutPower = false;
        } else {
            handle(false);
            this.recentlyWithoutPower = true;
        }
    }

    private void calculatePower(){
        this.neededPower = 12*loadedChunks*33;
    }

    public void addLoader(TileEntitySubChunkLoader tile){
        getLocations().add(tile.getPos());
        changed = true;
        loadedChunks++;
    }

    public void removeLoader(TileEntitySubChunkLoader tile){
        getLocations().remove(tile.getPos());
        changed = true;
        loadedChunks--;
    }

    private void handle(boolean b) {
        if ((changed || !b) && !getWorld().isRemote) {
            if (!b && tickets.isEmpty()) {
                return;
            }
            for (ForgeChunkManager.Ticket ticket : tickets) {
                ForgeChunkManager.releaseTicket(ticket);
            }
            tickets.clear();
            //loadedChunks = 0;
            if (b) {
                for (BlockPos loc : getLocations()) {
                    ForgeChunkManager.Ticket ticket = WorldHelper.requestTicket(getWorld(), loc, EFlux.instance);
                    //loadedChunks++;
                    tickets.add(ticket);
                    WorldHelper.forceChunk(ticket);
                    PlayerHelper.sendMessageToPlayer(getWorld().getPlayerEntityByUUID(owner), "Put ticket on blockLoc: " + loc.toString());
                }
            }
            loadedChunks = tickets.size();
            changed = false;
        }
    }

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        if (owner != null)
            tagCompound.setString("Owner_MCT", owner.toString());
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        if (tagCompound.hasKey("Owner_MCT"))
            this.owner = UUID.fromString(tagCompound.getString("Owner_MCT"));
    }

    @Override
    public ItemStack getRandomRepairItem() {
        Collections.shuffle(repairItems);
        return repairItems.get(0);
    }

    public boolean canPlace(EntityPlayer player){
        return (owner == null || isOwner(player)) && !ChunkLoaderPlayerProperties.get(PlayerHelper.getPlayerUUID(player)).hasHandler();
    }

    @Override
    public void onBlockPlacedBy(final EntityLivingBase entityLiving, ItemStack stack) {
        super.onBlockPlacedBy(entityLiving, stack);
       // ElecCore.tickHandler.registerCall(
        //    new Runnable() {
        //        @Override
        //        public void run() {
                    if (!ServerHelper.isServer(getWorld()))
                        return;
                    if (entityLiving instanceof EntityPlayer) {
                        UUID uuid = PlayerHelper.getPlayerUUID((EntityPlayer) entityLiving);
                        if (!ChunkLoaderPlayerProperties.get(uuid).hasHandler()) {
                            if (TileEntityMainChunkLoader.this.owner == null)
                                TileEntityMainChunkLoader.this.owner = uuid;
                            PlayerHelper.sendMessageToPlayer((EntityPlayer) entityLiving, "Placed chunkloader at " + getPos());
                            ChunkLoaderPlayerProperties.get(TileEntityMainChunkLoader.this.owner).setMainChunkLoader(TileEntityMainChunkLoader.this);
                            TileEntityMainChunkLoader.this.getLocations().add(getPos());
                            TileEntityMainChunkLoader.this.loadedChunks = TileEntityMainChunkLoader.this.getLocations().size();
                            TileEntityMainChunkLoader.this.changed = true;
                            markDirty();
                        }
                    }
            //    }
           // }, worldObj);
    }

    public boolean isOwner(EntityPlayer player){
        return player != null && PlayerHelper.getPlayerUUID(player).equals(owner);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("chunksLoaded", loadedChunks);
        tagCompound.setBoolean("powerBool", recentlyWithoutPower);
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("chunksLoaded"))
            this.loadedChunks = tagCompound.getInteger("chunksLoaded");
        if (tagCompound.hasKey("powerBool"))
            this.recentlyWithoutPower = tagCompound.getBoolean("powerBool");

    }

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return isOwner(player) && super.onBlockActivated(state, player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public int getWorkingVoltage() {
        return 120;
    }

    @Override
    public float getAcceptance() {
        return 0.5f;
    }

    @Override
    public int getMaxRP() {
        return 15;
    }

    @Override
    public double getResistance() {
        return 10;
    }

    @Nonnull
    @Override
    public ConnectionPoint getConnectionPoint(int post) {
        return post == 0 ? cp1 : cp2;
    }

    @Nullable
    @Override
    public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec) {
        return side != getTileFacing().getOpposite() ? null : (hitVec.y > 0.5 ? cp2 : cp1);
    }

    @Override
    protected void createConnectionPoints() {
        cp1 = new ConnectionPoint(pos, world, getTileFacing().getOpposite(), 1);
        cp2 = new ConnectionPoint(pos, world, getTileFacing().getOpposite(), 2);
    }

    private ConnectionPoint cp1, cp2;

    @Override
    public void onBlockRemoved() {
        super.onBlockRemoved();
        handle(false);
        if (owner != null) {
            getLocations().remove(getPos());
            ChunkLoaderPlayerProperties.get(owner).setMainChunkLoader(null);
        }
    }

    @Override
    public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData) {
        super.addInformation(information, hitData);
        information.addInformation("Loaded chunks: "+hitData.getData().getInteger("chunksL"));
    }

    @Nonnull
    @Override
    public NBTTagCompound getInfoNBTData(@Nonnull NBTTagCompound tag, TileEntity tile, @Nonnull EntityPlayerMP player, @Nonnull IInfoDataAccessorBlock hitData) {
        tag.setInteger("chunksL", loadedChunks);
        return super.getInfoNBTData(tag, tile, player, hitData);
    }

}
