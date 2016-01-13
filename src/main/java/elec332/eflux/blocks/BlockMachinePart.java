package elec332.eflux.blocks;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.*;
import elec332.core.client.model.map.BakedModelMetaRotationMap;
import elec332.core.client.model.map.IBakedModelMetaRotationMap;
import elec332.core.client.model.model.IBlockModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.tile.TileBase;
import elec332.core.world.location.BlockStateWrapper;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import elec332.core.api.wrench.IWrenchable;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.tileentity.multiblock.TileEntityDustStorage;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockItemGate;
import elec332.eflux.tileentity.multiblock.TileMultiBlockTile;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

import java.util.List;

/**
 * Created by Elec332 on 28-8-2015.
 */
public class BlockMachinePart extends BlockWithMeta implements ITileEntityProvider, IWrenchable, INoJsonBlock {

    public BlockMachinePart(int types){
        this(types, "BlockMachinePart");
    }

    public BlockMachinePart(int types, String name) {
        super(Material.rock, name, EFlux.ModID);
        this.types = types;
        setResistance(5.0f);
        setHardness(2.5f);
    }

    private final int types;
    @SideOnly(Side.CLIENT)
    private IBakedModelMetaRotationMap<IBlockModel> rotationMap;
    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite normal, itemOutlet, laserCore, heater, monitorF, monitorR, monitorL, radiator;
    private boolean register;

    @Override
    public BlockMachinePart register() {
        super.register();
        if (!register) {
            registerTiles();
            register = true;
        }
        return this;
    }

    protected void registerTiles(){
        GameRegistry.registerTileEntity(TileEntityBlockMachine.class, blockName);
        GameRegistry.registerTileEntity(TileEntityMultiBlockItemGate.class, "itemGate");
        GameRegistry.registerTileEntity(TileEntityDustStorage.class, "dustStorage");
    }

    @Override
    public int getTypes(){
        return 7;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(List<ItemStack> list, Item item, CreativeTabs creativeTab) {
        for (int i = 0; i < getTypes(); i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return StatCollector.translateToLocal("eflux.machine.part."+stack.getItemDamage());
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileBase)
            return ((TileBase) tile).onBlockActivated(player, side, hitX, hitY, hitZ);
        return super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLiving, ItemStack stack) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityBlockMachine) {
            ((TileEntityBlockMachine) tile).onBlockPlacedBy(entityLiving, stack);
        } else {
            super.onBlockPlacedBy(world, pos, state, entityLiving, stack);
        }
    }

    @Override
    public ItemStack ItemDropped(World world, BlockPos pos) {
        return new ItemStack(this, 1, WorldHelper.getBlockMeta(world, pos));
    }

    @Override
    public void onWrenched(World world, BlockPos pos, EnumFacing forgeDirection) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileBase)
            ((TileBase) tile).onWrenched(forgeDirection);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        pokeCheck(worldIn, pos);
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        pokeCheck(worldIn, pos);
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        pokeCheck(worldIn, pos);
        super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityBlockMachine) {
            ((TileEntityBlockMachine) tile).pokeCheckStuff();
        } else if (tile instanceof TileBase) {
            ((TileBase) tile).onNeighborBlockChange(block);
        } else {
            super.onNeighborBlockChange(world, pos, state, block);
        }
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (meta == BlockRegister.itemGate.meta)
            return new TileEntityMultiBlockItemGate();
        if (meta == BlockRegister.dustStorage.meta)
            return new TileEntityDustStorage();
        return new TileEntityBlockMachine();
    }

    /**
     * This method is used when a model is requested to render the block in a world.
     *
     * @param state The current BlockState.
     * @param iba   The IBlockAccess the block is in.
     * @param pos   The position of the block.
     * @return The model to render for this block for the given arguments.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockModel getBlockModel(IBlockState state, IBlockAccess iba, BlockPos pos) {
        TileEntityBlockMachine tile = (TileEntityBlockMachine)WorldHelper.getTileAt(iba, pos);
        int meta = (int) Math.pow(WorldHelper.getBlockMeta(state), tile.monitorSide + 1);
        //if (meta > getTypes() && meta != 36 && meta != 6*6*6)
         //   meta = 0;
        return rotationMap.forMetaAndRotation(meta, DirectionHelper.getRotationFromFacing(tile.getTileFacing()));
    }

    /**
     * This method is used when a model is requested when its not placed, so for an item.
     *
     * @return The model to render when the block is not placed.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(Item item, int meta) {
        return rotationMap.forMeta(meta);
    }


    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     *
     * @param quadBakery     The QuadBakery.
     * @param modelBakery
     * @param templateBakery
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(final ElecQuadBakery quadBakery, ElecModelBakery modelBakery, final ElecTemplateBakery templateBakery) {
        rotationMap = new BakedModelMetaRotationMap<IBlockModel>();
        //rotationMap.setModelsForRotation(0, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(normal)));
        //rotationMap.setModelsForRotation(1, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(normal)));
        //rotationMap.setModelsForRotation(2, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(normal)));
        //rotationMap.setModelsForRotation(0, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(itemOutlet))));
        rotationMap.setModelsForRotation(0, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(laserCore))));
        rotationMap.setModelsForRotation(1, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(heater))));
        rotationMap.setModelsForRotation(2, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(normal)));
        rotationMap.setModelsForRotation(3, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(radiator))));
        rotationMap.setModelsForRotation(4, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(normal)));
        rotationMap.setModelsForRotation(5, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(normal)));

        rotationMap.setModelsForRotation(6, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(monitorF))));
        rotationMap.setModelsForRotation(6*6, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(monitorR))));
        rotationMap.setModelsForRotation(6*6*6, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(monitorL))));
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        normal = iconRegistrar.registerSprite(getTextureLocation("normal"));
        itemOutlet = iconRegistrar.registerSprite(getTextureLocation("itemOutletFront"));
        laserCore = iconRegistrar.registerSprite(getTextureLocation("laserCoreFront"));
        heater = iconRegistrar.registerSprite(getTextureLocation("heaterFront"));
        monitorF = iconRegistrar.registerSprite(getTextureLocation("monitorFull"));
        monitorR = iconRegistrar.registerSprite(getTextureLocation("monitorRightSide"));
        monitorL = iconRegistrar.registerSprite(getTextureLocation("monitorLeftSide"));
        radiator = iconRegistrar.registerSprite(getTextureLocation("radiator"));
    }

    @SideOnly(Side.CLIENT)
    protected ResourceLocation getTextureLocation(String s){
        return new EFluxResourceLocation("blocks/machinepart/"+s);
    }

    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite[] forSpecialFront(TextureAtlasSprite front){
        return new TextureAtlasSprite[]{
                normal, normal, front, normal, normal, normal
        };
    }

    public static class TileEntityBlockMachine extends TileMultiBlockTile {

        public TileEntityBlockMachine(){
            super();
        }

        private EnumFacing facing;
        private int monitorSide;

        @Override
        public void onBlockPlacedBy(EntityLivingBase entityLiving, ItemStack stack) {
            this.facing = DirectionHelper.getFacingOnPlacement(entityLiving);
            onNeighborBlockChange(null);
        }

        @Override
        public void onWrenched(EnumFacing forgeDirection) {
            if (getMultiBlock() == null) {
                if ((forgeDirection != EnumFacing.UP && forgeDirection != EnumFacing.DOWN) || (getBlockType() == BlockRegister.itemGate.block && getBlockMetadata() == BlockRegister.itemGate.meta))
                this.facing = forgeDirection;
                markDirty();
                syncData();
                reRenderBlock();
            }
        }

        public void pokeCheckStuff() {
            if (getBlockType() != BlockRegister.monitor.block || getBlockMetadata() != BlockRegister.monitor.meta)
                return;
            boolean neighbour = false;
            for (EnumFacing direction : EnumFacing.VALUES) {
                if (direction == EnumFacing.UP || direction == EnumFacing.DOWN)
                    continue;
                BlockPos toCheck = pos.offset(direction);
                Block block = WorldHelper.getBlockAt(worldObj, toCheck);
                int meta = WorldHelper.getBlockMeta(worldObj, toCheck);
                if (new BlockStateWrapper(block, meta).equals(BlockRegister.monitor)) {
                    neighbour = true;
                    TileEntityBlockMachine tile = (TileEntityBlockMachine) WorldHelper.getTileAt(worldObj, toCheck);
                    if (monitorSide == 0 && tile.getTileFacing() == facing){
                        if (DirectionHelper.rotateLeft(facing) == direction){
                            monitorSide = 1;
                            tile.onNeighborBlockChange(getBlockType());
                            if (tile.monitorSide == monitorSide)
                                monitorSide = 0;
                            reRenderBlock();
                        } else if (DirectionHelper.rotateRight(facing) == direction){
                            monitorSide = 2;
                            tile.onNeighborBlockChange(getBlockType());
                            if (tile.monitorSide == monitorSide)
                                monitorSide = 0;
                            reRenderBlock();
                        }
                    }
                }
            }
            if (!neighbour){
                monitorSide = 0;
                reRenderBlock();
            }
        }

        @Override
        public EnumFacing getTileFacing() {
            return facing;
        }

        @Override
        public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            tagCompound.setString("facing", facing.toString());
            tagCompound.setInteger("monS", monitorSide);
        }

        @Override
        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.facing = EnumFacing.valueOf(tagCompound.getString("facing").toUpperCase());
            this.monitorSide = tagCompound.getInteger("monS");
        }

    }

    private void pokeCheck(World world, BlockPos pos){
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityBlockMachine)
            ((TileEntityBlockMachine) tile).pokeCheckStuff();
    }

}
