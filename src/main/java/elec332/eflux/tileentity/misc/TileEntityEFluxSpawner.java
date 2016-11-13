package elec332.eflux.tileentity.misc;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.util.InventoryHelper;
import elec332.core.util.NBTHelper;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.tileentity.TileEntityEFlux;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

/**
 * Created by Elec332 on 20-2-2016.
 */
@RegisteredTileEntity("TileEntityEFluxSpawner")
public class TileEntityEFluxSpawner extends TileEntityEFlux implements ITickable {

    public TileEntityEFluxSpawner(){
        mobSpawner = new EFluxMobSpawner();
    }

    private boolean hasRedstone, ignorePlayer, brainDead;
    private final MobSpawnerBaseLogic mobSpawner;
    private boolean redstoneActivated;

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean b = false;
        if (CircuitHelper.isCircuitType(stack, new EFluxResourceLocation("shock")) && CircuitHelper.isValidCircuit(stack)){
            brainDead = b = true;
        }
        if (InventoryHelper.areEqualNoSizeNoNBT(stack, new ItemStack(Items.ENDER_EYE))){
            hasRedstone = b = true;
        }
        if (InventoryHelper.areEqualNoSizeNoNBT(stack, new ItemStack(Items.EMERALD))){
            ignorePlayer = b = true;
        }
        if (b){
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
            markDirty();
            return true;
        }
        return super.onBlockActivated(state, player, hand, stack, side, hitX, hitY, hitZ);
    }

    @Override
    public void update() {
        mobSpawner.updateSpawner();
    }

    public void readFromOldSpawner(NBTTagCompound tag){
        mobSpawner.readFromNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        hasRedstone = tagCompound.getBoolean("Hr");
        ignorePlayer = tagCompound.getBoolean("ignoreP");
        brainDead = tagCompound.getBoolean("brainDead");
        redstoneActivated = tagCompound.getBoolean("rA");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("Hr", hasRedstone);
        tagCompound.setBoolean("ignoreP", ignorePlayer);
        tagCompound.setBoolean("brainDead", brainDead);
        tagCompound.setBoolean("rA", redstoneActivated);
        return tagCompound;
    }

    @Override
    public void onNeighborTileChange(BlockPos neighbor) {
        super.onNeighborTileChange(neighbor);
        checkRedstone();
    }

    @Override
    public void onNeighborBlockChange(Block block) {
        super.onNeighborBlockChange(block);
        checkRedstone();
    }

    public void checkRedstone(){
        redstoneActivated = worldObj.isBlockPowered(pos);
        sendPacket(1, new NBTHelper().addToTag(redstoneActivated, "r").serializeNBT());
    }

    @Override
    public void onDataPacket(int id, NBTTagCompound tag) {
        if (id == 1){
            redstoneActivated = tag.getBoolean("r");
        } else {
            super.onDataPacket(id, tag);
        }
    }

    private class EFluxMobSpawner extends MobSpawnerBaseLogic {

        @Override
        public void broadcastEvent(int id) {
            //???
        }

        @Override
        public World getSpawnerWorld() {
            return getWorld();
        }

        @Override
        public BlockPos getSpawnerPosition() {
            return getPos();
        }

        @Override
        public void updateSpawner() {
            if (!this.isActivated())
            {
                this.prevMobRotation = this.mobRotation;
            }
            else
            {
                BlockPos blockpos = this.getSpawnerPosition();

                if (this.getSpawnerWorld().isRemote)
                {
                    double d3 = (double)((float)blockpos.getX() + this.getSpawnerWorld().rand.nextFloat());
                    double d4 = (double)((float)blockpos.getY() + this.getSpawnerWorld().rand.nextFloat());
                    double d5 = (double)((float)blockpos.getZ() + this.getSpawnerWorld().rand.nextFloat());
                    this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D);
                    this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D);

                    if (this.spawnDelay > 0)
                    {
                        --this.spawnDelay;
                    }

                    this.prevMobRotation = this.mobRotation;
                    this.mobRotation = (this.mobRotation + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0D;
                }
                else
                {
                    if (this.spawnDelay == -1)
                    {
                        this.resetTimer();
                    }

                    if (this.spawnDelay > 0)
                    {
                        --this.spawnDelay;
                        return;
                    }

                    boolean flag = false;

                    for (int i = 0; i < this.spawnCount; ++i)
                    {
                        NBTTagCompound nbttagcompound = this.randomEntity.getNbt();
                        NBTTagList nbttaglist = nbttagcompound.getTagList("Pos", 6);
                        World world = this.getSpawnerWorld();
                        int j = nbttaglist.tagCount();
                        double d0 = j >= 1 ? nbttaglist.getDoubleAt(0) : (double)blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                        double d1 = j >= 2 ? nbttaglist.getDoubleAt(1) : (double)(blockpos.getY() + world.rand.nextInt(3) - 1);
                        double d2 = j >= 3 ? nbttaglist.getDoubleAt(2) : (double)blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                        Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, false);

                        if (entity == null)
                        {
                            return;
                        }

                        int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), (double)(blockpos.getX() + 1), (double)(blockpos.getY() + 1), (double)(blockpos.getZ() + 1))).expandXyz((double)this.spawnRange)).size();

                        if (k >= this.maxNearbyEntities)
                        {
                            this.resetTimer();
                            return;
                        }

                        EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                        entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0F, 0.0F);

                        if (entityliving == null || entityliving.getCanSpawnHere() && entityliving.isNotColliding())
                        {
                            modifyEntity(entity);
                            if (this.randomEntity.getNbt().getSize() == 1 && this.randomEntity.getNbt().hasKey("id", 8) && entity instanceof EntityLiving) {
                                ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                            }

                            AnvilChunkLoader.spawnEntity(entity, world);
                            world.playEvent(2004, blockpos, 0);

                            if (entityliving != null)
                            {
                                entityliving.spawnExplosionParticle();
                            }

                            flag = true;
                        }
                    }

                    if (flag)
                    {
                        this.resetTimer();
                    }
                }
            }
        }

        @Override
        public boolean isActivated() {
            return redstoneAllowed() && playerAllowed();
        }

        private void modifyEntity(Entity entity){
            if (brainDead && (entity instanceof EntityLiving)){
                EntityLiving entityLiving = (EntityLiving) entity;
                makeBrainDead(entityLiving);
                for (Entity entity1 : entityLiving.getPassengers()){
                    if (entity1 instanceof EntityLiving){
                        makeBrainDead((EntityLiving) entity1);
                    }
                }
            }
        }

        /*@Override //Gone in 1.9 :(
        public Entity spawnNewEntity(Entity entityIn, boolean spawn) {
            Entity ret = super.spawnNewEntity(entityIn, spawn);
            if (brainDead && (ret instanceof EntityLiving)){
                EntityLiving entity = (EntityLiving) ret;
                while (entity != null){
                    makeBrainDead(entity);
                    entity = entity.riddenByEntity instanceof EntityLiving ? (EntityLiving) entity.riddenByEntity : null;
                }
            }
            return ret;
        }*/

        private boolean redstoneAllowed(){
            return !hasRedstone || redstoneActivated;
        }

        private boolean playerAllowed(){
            return ignorePlayer || super.isActivated();
        }

    }

    private void makeBrainDead(EntityLiving entity){
        entity.tasks.taskEntries.clear();
        entity.targetTasks.taskEntries.clear();
        entity.getNavigator().clearPathEntity();
        entity.setCanPickUpLoot(false);
    }

}
