package elec332.eflux.tileentity.energy.machine.chunkLoader;

import com.google.common.collect.Lists;
import elec332.core.api.annotations.RegisterTile;
import elec332.core.server.ServerHelper;
import elec332.core.util.BlockLoc;
import elec332.core.util.PlayerHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.handler.ChunkLoaderPlayerProperties;
import elec332.eflux.tileentity.BreakableMachineTile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 25-5-2015.
 */
@RegisterTile(name = "TileEntityEFluxChunkLoaderMainTile")
public class MainChunkLoaderTile extends BreakableMachineTile implements IChunkLoader{

    public MainChunkLoaderTile(){
        repairItems = Lists.newArrayList(new ItemStack(Items.ender_eye), new ItemStack(Items.ender_pearl));
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
/*
    @Override
    public void validate() {
        System.out.println("validating: "+toString());
        super.validate();
    }

    @Override
    public void invalidate() {
        System.out.println("Invalidating: "+toString());
        super.invalidate();
    }

    @Override
    public boolean isInvalid() {
        System.out.println("Invalid: "+super.isInvalid()+" for "+toString());
        return super.isInvalid();
    }*/

    private List<ForgeChunkManager.Ticket> tickets;

    private List<BlockPos> getLocations(){
        return ChunkLoaderPlayerProperties.get(this.owner).getLocations();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote)
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

    public void addLoader(ChunkLoaderSubTile tile){
        getLocations().add(tile.getPos());
        changed = true;
        loadedChunks++;
    }

    public void removeLoader(ChunkLoaderSubTile tile){
        getLocations().remove(tile.getPos());
        changed = true;
        loadedChunks--;
    }

    private void handle(boolean b) {
        if ((changed || !b) && !worldObj.isRemote) {
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
                    ForgeChunkManager.Ticket ticket = WorldHelper.requestTicket(worldObj, loc, EFlux.instance);
                    //loadedChunks++;
                    tickets.add(ticket);
                    WorldHelper.forceChunk(ticket);
                    PlayerHelper.sendMessageToPlayer(worldObj.getPlayerEntityByUUID(owner), "Put ticket on blockLoc: " + loc.toString());
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
                    if (!ServerHelper.isServer(worldObj))
                        return;
                    if (entityLiving instanceof EntityPlayer) {
                        UUID uuid = PlayerHelper.getPlayerUUID((EntityPlayer) entityLiving);
                        if (!ChunkLoaderPlayerProperties.get(uuid).hasHandler()) {
                            if (MainChunkLoaderTile.this.owner == null)
                                MainChunkLoaderTile.this.owner = uuid;
                            PlayerHelper.sendMessageToPlayer((EntityPlayer) entityLiving, "Placed chunkloader at " + getPos());
                            ChunkLoaderPlayerProperties.get(MainChunkLoaderTile.this.owner).setMainChunkLoader(MainChunkLoaderTile.this);
                            MainChunkLoaderTile.this.getLocations().add(getPos());
                            MainChunkLoaderTile.this.loadedChunks = MainChunkLoaderTile.this.getLocations().size();
                            MainChunkLoaderTile.this.changed = true;
                            markDirty();
                        }
                    }
            //    }
           // }, worldObj);
    }

    @Override
    public boolean canUpdate() {
        return !worldObj.isRemote;
    }

    public boolean isOwner(EntityPlayer player){
        return player != null && PlayerHelper.getPlayerUUID(player).equals(owner);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("chunksLoaded", loadedChunks);
        tagCompound.setBoolean("powerBool", recentlyWithoutPower);
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
    public boolean onBlockActivated(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        return isOwner(player) && super.onBlockActivated(player, side, hitX, hitY, hitZ);
    }

    @Override
    public float getAcceptance() {
        return 0.5f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 144;
    }

    @Override
    protected int getMaxStoredPower() {
        return 20000;
    }

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
    public int getRequestedRP() {
        return 33;
    }

    @Override
    public String[] getProvidedData() {
        return new String[]{
                "Stored power: "+energyContainer.getStoredPower(),
                "Max stored power: "+energyContainer.getMaxStoredEnergy(),
                "In working order: "+!isBroken(),
                "Loaded chunks: "+loadedChunks
        };
    }
}
