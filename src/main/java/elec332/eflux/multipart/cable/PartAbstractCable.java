package elec332.eflux.multipart.cable;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import elec332.core.client.RenderHelper;
import elec332.core.main.ElecCore;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.multipart.AbstractEnergyMultiPart;
import mcmultipart.MCMultiPartMod;
import mcmultipart.client.multipart.ICustomHighlightPart;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Elec332 on 25-1-2016.
 */
@SuppressWarnings("all")
public abstract class PartAbstractCable extends AbstractEnergyMultiPart implements IEnergyTransmitter, ISlottedPart, ICustomHighlightPart {

    public PartAbstractCable(){
        connectData = EnumSet.noneOf(EnumFacing.class);
    }

    public void setGridIdentifier(UUID uuid){
        this.gridIdentifier = uuid;
        //sendPacket(9, new NBTHelper().addToTag(uuid.toString(), "uuid").toNBT());
        sendUpdatePacket(true);
    }

    private UUID gridIdentifier;
    private Set<EnumFacing> connectData;
    private static final AxisAlignedBB[] HITBOXES;
    public static final PropertyBool UP = PropertyBool.create("up"), DOWN = PropertyBool.create("down"), NORTH = PropertyBool.create("north"), EAST = PropertyBool.create("east"), SOUTH = PropertyBool.create("south"), WEST = PropertyBool.create("west");

    public UUID getGridIdentifier(){
        return this.gridIdentifier;
    }

    public abstract String getUniqueIdentifier();

    public abstract int getMeta();

    protected ItemStack getPartStack(){
        ItemStack stack = ItemRegister.cableBasic.copy();
        stack.setItemDamage(getMeta());
        return stack;
    }

    @Override
    public void onPartValidated() {
        super.onPartValidated();
        checkConnections();
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[0], new IUnlistedProperty[]{
                DOWN, UP, NORTH, SOUTH, WEST, EAST
        });
    }

    @Override
    public IBlockState getExtendedState(IBlockState ibs) {
        IExtendedBlockState state = (IExtendedBlockState) ibs;
        return state.withProperty(DOWN, connected(EnumFacing.DOWN)).withProperty(UP, connected(EnumFacing.UP)).withProperty(NORTH, connected(EnumFacing.NORTH)).withProperty(SOUTH, connected(EnumFacing.SOUTH)).withProperty(WEST, connected(EnumFacing.WEST)).withProperty(EAST, connected(EnumFacing.EAST));
    }

    @Override
    public ResourceLocation getModelPath() {
        return new ResourceLocation("eflux:i-aint-making-jsons_"+getMeta());
    }

    @Override
    public ItemStack getPickBlock(EntityPlayer player, PartMOP hit) {
        return getPartStack();
    }

    @Override
    public List<ItemStack> getDrops() {
        return Lists.newArrayList(getPartStack());
    }

    @Override
    public float getHardness(PartMOP hit) {
        return 1.3f;
    }

    @Override
    public Material getMaterial() {
        return Material.glass;
    }

    @Override
    public void onNeighborBlockChange(Block block) {
        checkConnections();
    }

    @Override
    public void onPartChanged(IMultipart part) {
        checkConnections();
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);
        byte[] bytes = new byte[6];
        for (EnumFacing facing : EnumFacing.VALUES){
            if (connected(facing)){
                bytes[facing.ordinal()] = 1;
            }
        }
        buf.writeByteArray(bytes);
        if (gridIdentifier != null) {
            buf.writeUuid(gridIdentifier);
        }
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        connectData.clear();
        byte[] bytes = buf.readByteArray();
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 1){
                connectData.add(EnumFacing.getFront(i));
            }
        }
        if (buf.readableBytes() > 15) {
            this.gridIdentifier = buf.readUuid();
        }
    }

    @Override
    public boolean canConnectTo(IEnergyTransmitter otherTransmitter) {
        return (!(otherTransmitter instanceof PartAbstractCable) || getUniqueIdentifier().equals(((PartAbstractCable) otherTransmitter).getUniqueIdentifier()));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == EFluxAPI.TRANSMITTER_CAPABILITY && canConnectToSide(facing) || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == EFluxAPI.TRANSMITTER_CAPABILITY && canConnectToSide(facing) ? (T)this : super.getCapability(capability, facing);
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(PartSlot.CENTER, PartSlot.NORTH, PartSlot.EAST, PartSlot.SOUTH, PartSlot.WEST, PartSlot.UP, PartSlot.DOWN);
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(HITBOXES[6]);
        for (EnumFacing facing : EnumFacing.VALUES){
            if (connected(facing)){
                list.add(HITBOXES[facing.ordinal()]);
            }
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox();
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        if (mask.intersectsWith(HITBOXES[6])){
            list.add(HITBOXES[6]);
        }
        for (EnumFacing facing : EnumFacing.VALUES){
            int i = facing.ordinal();
            if (connected(facing) && mask.intersectsWith(HITBOXES[i])){
                list.add(HITBOXES[i]);
            }
        }
    }

    /*@Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        //addSelectionBoxes(list);
        list.add(HITBOXES[6]);
    }*/

    @Override
    @SideOnly(Side.CLIENT)
    public boolean drawHighlight(PartMOP hit, EntityPlayer player, float partialTicks) {

        /*float thickness = 6 * RenderHelper.renderUnit;
        float heightStuff = (1 - thickness)/2;
        float f1 = thickness + heightStuff;

        float yMin = connectData.contains(EnumFacing.DOWN) ? 0 : heightStuff;
        float yMax = connectData.contains(EnumFacing.UP) ? 1 : f1;
        float xMin = connectData.contains(EnumFacing.WEST) ? 0 : heightStuff;
        float xMax = connectData.contains(EnumFacing.EAST) ? 1 : f1;
        float zMin = connectData.contains(EnumFacing.NORTH) ? 0 : heightStuff;
        float zMax = connectData.contains(EnumFacing.SOUTH) ? 1 : f1;

        AxisAlignedBB aabb = new AxisAlignedBB(xMin, yMin, zMin, xMax, yMax, zMax);*/
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        AxisAlignedBB aabb;
        for (EnumFacing facing : connectData) {
            aabb = HITBOXES[facing.ordinal()];
            RenderGlobal.drawSelectionBoundingBox(aabb.expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)/*.offset(-d0, -d1, -d2)*/);
        }
        aabb = HITBOXES[6];
        RenderGlobal.drawSelectionBoundingBox(aabb.expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)/*.offset(-d0, -d1, -d2)*/);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        return true;//return false;
    }

    private boolean canConnectToSide(EnumFacing side) {
        ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(side));
        boolean ret = !(part instanceof IMicroblock.IFaceMicroblock && ((IMicroblock.IFaceMicroblock) part).isFaceHollow()) && OcclusionHelper.occlusionTest(getContainer().getParts(), new Predicate<IMultipart>() {
            @Override
            public boolean apply(IMultipart input) {
                return input == PartAbstractCable.this;
            }
        }, HITBOXES[side.ordinal()]);
        //System.out.println("CCC: "+ret+"   "+side+"   "+getPos());
        return ret;
    }

    private boolean connected(EnumFacing side){
        return connectData.contains(side);
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
    }

    private void checkConnections(){
        if (!getWorld().isRemote) {
            ElecCore.tickHandler.registerCall(new Runnable() {
                @Override
                public void run() {
                    connectData.clear();
                    for (EnumFacing side : EnumFacing.VALUES) {
                        BlockPos pos = getPos().offset(side);
                        TileEntity tile = WorldHelper.getTileAt(getWorld(), pos);
                        if (EnergyAPIHelper.isEnergyTile(tile) && canConnectToSide(side)) {
                            if (EnergyAPIHelper.isProvider(tile, side) || EnergyAPIHelper.isReceiver(tile, side)) {
                                connectData.add(side);
                            } else {
                                IEnergyTransmitter transmitter2 = tile.getCapability(EFluxAPI.TRANSMITTER_CAPABILITY, side.getOpposite());
                                if (transmitter2 != null && transmitter2.canConnectTo(PartAbstractCable.this) && PartAbstractCable.this.canConnectTo(transmitter2)){
                                    connectData.add(side);
                                }
                            }
                        }
                    }
                    sendUpdatePacket(true);
               }
            }, Side.SERVER);
        }
    }

    private static class PropertyBool implements IUnlistedProperty<Boolean> {

        public static PropertyBool create(String s){
            return new PropertyBool(s);
        }

        private PropertyBool(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isValid(Boolean value) {
            return true;
        }

        @Override
        public Class<Boolean> getType() {
            return Boolean.class;
        }

        @Override
        public String valueToString(Boolean value) {
            return value.toString();
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

}
