package elec332.eflux.tileentity.energy.machine.chunkLoader;

import com.google.common.collect.Lists;
import elec332.core.main.ElecCore;
import elec332.core.util.BlockLoc;
import elec332.core.util.IRunOnce;
import elec332.eflux.handler.ChunkLoaderPlayerProperties;
import elec332.eflux.tileentity.BreakableReceiverTile;
import elec332.eflux.util.PlayerUtil;
import elec332.eflux.world.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 25-5-2015.
 */
public class MainChunkLoaderTile extends BreakableReceiverTile {

    public MainChunkLoaderTile(){
        repairItems = Lists.newArrayList(new ItemStack(Items.ender_eye), new ItemStack(Items.ender_pearl));
        active = true;
        this.changed = true;
        this.tickets = Lists.newArrayList();
        this.blockLocations = Lists.newArrayList();
    }

    private List<ItemStack> repairItems;
    private UUID owner;
    private boolean active;
    private int neededPower;
    public boolean changed;
    private List<BlockLoc> blockLocations;
    private List<ForgeChunkManager.Ticket> tickets;

    @Override
    public void updateEntity() {
        super.updateEntity();
        calculatePower();
        //if (storedPower >= neededPower && active){
        //    storedPower -= neededPower;
            handle(true);
        //} else handle(false);
    }

    private void calculatePower(){
        this.neededPower = 3; //TODO: Dynamic, per-chunk cost
    }

    protected void addLoader(ChunkLoaderSubTile tile){
        blockLocations.add(tile.myLocation());
        changed = true;
    }

    protected void removeLoader(ChunkLoaderSubTile tile){
        blockLocations.remove(tile.myLocation());
        changed = true;
    }

    private void handle(boolean b){
        if ((changed || !b) && !worldObj.isRemote){
            for (ForgeChunkManager.Ticket ticket : tickets){
                ForgeChunkManager.releaseTicket(ticket);
            }
            tickets.clear();
            if (b) {
                for (BlockLoc loc : blockLocations) {
                    ForgeChunkManager.Ticket ticket = WorldUtils.requestTicket(worldObj, loc);
                    tickets.add(ticket);
                    WorldUtils.forceChunk(ticket);
                    PlayerUtil.sendMessageToPlayer(worldObj.func_152378_a(owner), "Put ticket on blockLoc: "+loc.toString());
                }
            }
            changed = false;
        }
    }

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        tagCompound.setString("Owner", owner.toString());
        NBTTagList tagList = new NBTTagList();
        for (BlockLoc blockLoc : blockLocations){
            tagList.appendTag(blockLoc.toNBT(new NBTTagCompound()));
        }
        tagCompound.setTag("locations", tagList);
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        this.owner = UUID.fromString(tagCompound.getString("Owner"));
        for (int i = 0; i < tagCompound.getTagList("locations", 10).tagCount(); i++) {
            blockLocations.add(new BlockLoc(tagCompound.getTagList("locations", 10).getCompoundTagAt(i)));
        }
    }

    @Override
    public ItemStack getRandomRepairItem() {
        Collections.shuffle(repairItems);
        return repairItems.get(0);
    }

    @Override
    public void onBlockPlacedBy(final EntityLivingBase entityLiving, ItemStack stack) {
        super.onBlockPlacedBy(entityLiving, stack);
        ElecCore.tickHandler.registerCall(new IRunOnce() {
            @Override
            public void run() {
                if (entityLiving instanceof EntityPlayer && !ChunkLoaderPlayerProperties.get((EntityPlayer) entityLiving).hasHandler()) {
                    if (MainChunkLoaderTile.this.owner == null)
                        MainChunkLoaderTile.this.owner = PlayerUtil.getPlayerUUID((EntityPlayer) entityLiving);
                    PlayerUtil.sendMessageToPlayer((EntityPlayer)entityLiving, "Placed chunkloader at "+myLocation().toString());
                    ChunkLoaderPlayerProperties.get((EntityPlayer) entityLiving).setMainChunkLoader(MainChunkLoaderTile.this);
                    MainChunkLoaderTile.this.blockLocations.add(myLocation());
                }
            }
        });
    }

    public boolean isOwner(EntityPlayer player){
        return PlayerUtil.getPlayerUUID(player).equals(owner);
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return isOwner(player) && super.onBlockActivated(player, side, hitX, hitY, hitZ);
    }

    @Override
    public float getAcceptance() {
        return 0.5f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 0;
    }

    @Override
    protected int getMaxStoredPower() {
        return 0;
    }

    @Override
    public void onBlockRemoved() {
        super.onBlockRemoved();
        handle(false);
        if (owner != null)
            ChunkLoaderPlayerProperties.get(worldObj.func_152378_a(owner)).setMainChunkLoader(null);
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return true;
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(ForgeDirection direction) {
        return 33;
    }

    @Override
    public String[] getProvidedData() {
        return new String[0];
    }
}
