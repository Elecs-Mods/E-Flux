package elec332.eflux.multipart;

import com.google.common.collect.Lists;
import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInfoProvider;
import elec332.core.api.info.IInformation;
import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.client.RenderHelper;
import elec332.core.main.ElecCore;
import elec332.core.tile.TileBase;
import elec332.core.util.UniversalUnlistedProperty;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.ConnectionType;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergyTile;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.init.ItemRegister;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.BitSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 17-12-2016.
 */
@RegisteredTileEntity("TileEntityEFluxCable")
public final class TileEntityCable extends TileBase implements IEnergyTransmitter, IInfoProvider {

    public TileEntityCable(){
        this(null);
    }

    public TileEntityCable(EnumCableType type){
        connectData = new BitSet(EnumFacing.VALUES.length);
        this.cableType = type;
    }

    public void setGridIdentifier(UUID uuid){
        this.gridIdentifier = uuid;
        sendUpdatePacket(true);
    }

    private EnumCableType cableType;
    private UUID gridIdentifier;
    private BitSet connectData;
    private static final AxisAlignedBB[] HITBOXES;
    public static final UniversalUnlistedProperty<Boolean> UP = new UniversalUnlistedProperty<>("up", Boolean.class), DOWN = new UniversalUnlistedProperty<>("down", Boolean.class), NORTH = new UniversalUnlistedProperty<>("north", Boolean.class), EAST = new UniversalUnlistedProperty<>("east", Boolean.class), SOUTH = new UniversalUnlistedProperty<>("south", Boolean.class), WEST = new UniversalUnlistedProperty<>("west", Boolean.class);

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("TypeC", cableType.getMeta());
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.cableType = EnumCableType.values()[tagCompound.getInteger("TypeC")];
    }

    public UUID getGridIdentifier(){
        return this.gridIdentifier;
    }

    public String getUniqueIdentifier(){
        return cableType.getName();
    }

    protected ItemStack getPartStack(){
        ItemStack stack = ItemRegister.cableBasic.copy();
        stack.setItemDamage(cableType.getMeta());
        return stack;
    }

    @Override
    public void validate() {
        super.validate();
        checkConnections(false);
    }

    @Override
    public void onTileLoaded() {
        super.onTileLoaded();
        new ClientChecker().run();
    }

    protected IBlockState getExtendedState(IExtendedBlockState ibs) {
        return ibs.withProperty(DOWN, connected(EnumFacing.DOWN)).withProperty(UP, connected(EnumFacing.UP)).withProperty(NORTH, connected(EnumFacing.NORTH)).withProperty(SOUTH, connected(EnumFacing.SOUTH)).withProperty(WEST, connected(EnumFacing.WEST)).withProperty(EAST, connected(EnumFacing.EAST));
    }

    @Override
    public List<ItemStack> getDrops(int fortune) {
        return Lists.newArrayList(getPartStack());
    }

    //@Override
   // public ResourceLocation getModelPath() {
   //     return new ResourceLocation("eflux:i-aint-making-jsons_"+getMeta());
   // }

    @Override
    public void onNeighborBlockChange(Block block) {
        checkConnections(false);
    }

   // @Override
   // public void onPartChanged(IMultipart part) {
   //     checkConnections(false);
    //}


    public void sendUpdatePacket(boolean hahahahahahahahahahahaha) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByteArray("bitz", connectData.toByteArray());
        if (gridIdentifier != null){
            tag.setString("uidC", gridIdentifier.toString());
        }
        sendPacket(1, tag);
    }

    @Override
    public void onDataPacket(int id, NBTTagCompound tag) {
        connectData = BitSet.valueOf(tag.getByteArray("bitz"));
        if (tag.hasKey("uidC")) {
            this.gridIdentifier = UUID.fromString(tag.getString("uidC"));
        }
        reRenderBlock();
    }

    @Override
    public boolean canConnectTo(ConnectionType myType, @Nonnull TileEntity otherTile, ConnectionType otherType, @Nonnull IEnergyTile otherConnector) {
        return otherType != ConnectionType.TRANSMITTER || (!(otherConnector instanceof TileEntityCable) || cableType == ((TileEntityCable) otherConnector).cableType);
    }



    @Override
    public int getMaxEFTransfer() {
        return cableType.getMaxEF();
    }

    @Override
    public int getMaxRPTransfer() {
        return cableType.getMaxRP();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == EFluxAPI.TRANSMITTER_CAPABILITY && canConnectToSide(facing) || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        return capability == EFluxAPI.TRANSMITTER_CAPABILITY && canConnectToSide(facing) ? (T)this : super.getCapability(capability, facing);
    }

   // @Override
   // public EnumSet<PartSlot> getSlotMask() {
   //     return EnumSet.of(PartSlot.CENTER, PartSlot.NORTH, PartSlot.EAST, PartSlot.SOUTH, PartSlot.WEST, PartSlot.UP, PartSlot.DOWN);
   // }

   /* @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(HITBOXES[6]);
        for (EnumFacing facing : EnumFacing.VALUES){
            if (connected(facing)){
                list.add(HITBOXES[facing.ordinal()]);
            }
        }
    }*/

    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        if (mask.intersects(HITBOXES[6])){
            list.add(HITBOXES[6]);
        }
        for (EnumFacing facing : EnumFacing.VALUES){
            int i = facing.ordinal();
            if (connected(facing) && mask.intersects(HITBOXES[i])){
                list.add(HITBOXES[i]);
            }
        }
    }

    /*@Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        //addSelectionBoxes(list);
        list.add(HITBOXES[6]);
    }*/
/*
    @Override
    @SideOnly(Side.CLIENT)
    public boolean drawHighlight(PartMOP hit, EntityPlayer player, float partialTicks) {

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (connectData.get(facing.getIndex())) {
                RenderHelper.drawExpandedSelectionBoundingBox(HITBOXES[facing.ordinal()]);
            }
        }
        RenderHelper.drawExpandedSelectionBoundingBox(HITBOXES[6]);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        return true;
    }*/

    private boolean canConnectToSide(EnumFacing side) {
        /*ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(side));
        boolean ret = !(part instanceof IMicroblock.IFaceMicroblock && ((IMicroblock.IFaceMicroblock) part).isFaceHollow()) && OcclusionHelper.occlusionTest(OcclusionHelper.boxes(HITBOXES[side.ordinal()]), new Predicate<IMultipart>() {

            @Override
            public boolean apply(IMultipart input) {
                return input == PartAbstractCable.this;
            }

        }, getContainer().getParts());
        return ret;*/
        return true;
    }

    private boolean connected(EnumFacing side){
        return connectData.get(side.getIndex());
    }

    private void checkConnections(boolean clientAdd){
        if (!getWorld().isRemote || clientAdd) {
            ElecCore.tickHandler.registerCall(new Runnable() {
                @Override
                public void run() {
                    checkConnectionsNow();
                }
            }, getWorld());
        }
    }

    private void checkConnectionsNow(){
        if (WorldHelper.chunkLoaded(getWorld(), getPos())) {
            connectData.clear();
            TileEntity my = WorldHelper.getTileAt(getWorld(), getPos());
            for (EnumFacing side : EnumFacing.VALUES) {
                BlockPos pos = getPos().offset(side);
                TileEntity tile = WorldHelper.getTileAt(getWorld(), pos);
                if (EnergyAPIHelper.isEnergyTile(tile) && canConnectToSide(side)) {
                    if (EnergyAPIHelper.isProvider(tile, side.getOpposite()) || EnergyAPIHelper.isReceiver(tile, side.getOpposite())) {
                        connectData.set(side.getIndex());
                    } else {
                        IEnergyTransmitter transmitter2 = tile.getCapability(EFluxAPI.TRANSMITTER_CAPABILITY, side.getOpposite());
                        if (transmitter2 != null && transmitter2.canConnectTo(ConnectionType.TRANSMITTER, my, ConnectionType.TRANSMITTER, this) && this.canConnectTo(ConnectionType.TRANSMITTER, tile, ConnectionType.TRANSMITTER, transmitter2)) {
                            connectData.set(side.getIndex());
                        }
                    }
                }
            }
            if (!getWorld().isRemote) {
                sendUpdatePacket(true);
            } else {
                WorldHelper.reRenderBlock(this);
            }
        }
    }

    static {
        HITBOXES = new AxisAlignedBB[7];
        float thickness = 6 * RenderHelper.renderUnit;
        float heightStuff = (1 - thickness)/2;
        float f1 = thickness + heightStuff;
        HITBOXES[EnumFacing.DOWN.ordinal()] = new AxisAlignedBB(heightStuff, heightStuff, heightStuff, f1, 0, f1);
        HITBOXES[EnumFacing.UP.ordinal()] = new AxisAlignedBB(heightStuff, 1, heightStuff, f1, f1, f1);
        HITBOXES[EnumFacing.NORTH.ordinal()] = new AxisAlignedBB(heightStuff, heightStuff, heightStuff, f1, f1, 0);
        HITBOXES[EnumFacing.SOUTH.ordinal()] = new AxisAlignedBB(heightStuff, heightStuff, 1, f1, f1, f1);
        HITBOXES[EnumFacing.WEST.ordinal()] = new AxisAlignedBB(heightStuff, heightStuff, heightStuff, 0, f1, f1);
        HITBOXES[EnumFacing.EAST.ordinal()] = new AxisAlignedBB(1, heightStuff, heightStuff, f1, f1, f1);
        HITBOXES[6] = new AxisAlignedBB(heightStuff, heightStuff, heightStuff, f1, f1, f1);
    }

    @Override
    public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData) {
        information.add("Max RP: "+cableType.getMaxRP());
        information.add("Max EF: "+cableType.getMaxEF());
    }

    @Nonnull
    @Override
    public NBTTagCompound getInfoNBTData(@Nonnull NBTTagCompound tag, TileEntity tile, @Nonnull EntityPlayerMP player, @Nonnull IInfoDataAccessorBlock hitData) {
        return new NBTTagCompound();
    }

    private class ClientChecker implements Runnable {

        public ClientChecker(){
            this(true);
        }

        private ClientChecker(boolean b){
            this.b = b;
        }

        private final boolean b;

        @Override
        public void run() {
            if (getWorld() != null) {
                if (getWorld().isRemote) {
                    checkConnections(true);
                }
            } else if (b) {
                ElecCore.tickHandler.registerCall(new ClientChecker(false), Side.CLIENT);
            }
        }

    }

}
