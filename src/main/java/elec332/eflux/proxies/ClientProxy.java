package elec332.eflux.proxies;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.api.client.model.map.IBakedModelMetaMap;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.loading.IModelAndTextureLoader;
import elec332.core.client.model.map.BakedModelMetaMap;
import elec332.core.main.ElecCore;
import elec332.eflux.client.ClientHandler;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.FurnaceRenderTile;
import elec332.eflux.client.render.FurnaceContentsRenderer;
import elec332.eflux.client.render.tesr.TESRAreaMover;
import elec332.eflux.client.render.tesr.TileEntityLaserRenderer;
import elec332.eflux.client.render.tesr.TileEntityTankRenderer;
import elec332.eflux.multipart.EnumCableType;
import elec332.eflux.multipart.TileEntityCable;
import elec332.eflux.tileentity.basic.TileEntityLaser;
import elec332.eflux.tileentity.misc.TileEntityAreaMover;
import elec332.eflux.tileentity.misc.TileEntityTank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 24-2-2015.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IModelAndTextureLoader {

    public ClientProxy(){
        MinecraftForge.EVENT_BUS.register(this);
        RenderingRegistry.instance().registerLoader(this);
    }

    @Override
    public World getClientWorld() {
        return ElecCore.proxy.getClientWorld();
    }

    @Override
    public void initRenderStuff(){
        MinecraftForge.EVENT_BUS.register(new ClientHandler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaser.class, new TileEntityLaserRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FurnaceRenderTile.class, new FurnaceContentsRenderer());
        TileEntityTankRenderer<TileEntityTank> tankRenderer = new TileEntityTankRenderer<TileEntityTank>();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, tankRenderer);
        RenderingRegistry.instance().registerLoader(tankRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAreaMover.class, new TESRAreaMover());

        /*EFlux.multiBlockRegistry.registerMultiBlockRenderer(MultiBlockEnderContainer.class, new IMultiBlockRenderer<MultiBlockEnderContainer>() {

            EntityItem eI = new EntityItem(null, 0, 0, 0, new ItemStack(Items.ENDER_PEARL));

            @Override
            public void renderMultiBlock(MultiBlockEnderContainer multiblock, float partialTicks) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(System.currentTimeMillis() % 360, 1, 0, 0);
                BlockPos pos = multiblock.getBlockLocAtTranslatedPos(1, 1, 1);
                Minecraft.getMinecraft().getRenderManager().doRenderEntity(eI, pos.getX() + .5, pos.getY(), pos.getZ() + .5, 0, partialTicks, true);
                GlStateManager.popMatrix();
            }

            @Override
            public AxisAlignedBB getRenderingBoundingBox(MultiBlockEnderContainer multiblock) {
                return new AxisAlignedBB(multiblock.getLocation(), multiblock.getBlockLocAtTranslatedPos(2, 2, 2));
            }
        });
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getBlockStateMapper().registerBlockStateMapper(BlockRegister.cable, new StateMapperBase() {

            @Override
            @Nonnull
            protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                return new ModelResourceLocation("eflux:i-aint-making-jsons_"+state.getBlock().getMetaFromState(state));
            }

        });*/
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SuppressWarnings("all")
    public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
        models = new BakedModelMetaMap<IBakedModel>();
        for (int i = 0; i < textures.length; i++) {
            models.setModelForMeta(i, modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(textures[i]).setTexture(textures[i])));
        }
        this.quadBakery = quadBakery;
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    public void registerTextures(IIconRegistrar iconRegistrar) {
        textures = new TextureAtlasSprite[EnumCableType.values().length];
        for (EnumCableType cableType : EnumCableType.values()){
            textures[cableType.ordinal()] = iconRegistrar.registerSprite(new EFluxResourceLocation(cableType.getTextureLocation()));
        }
    }

    private TextureAtlasSprite[] textures;
    public IBakedModelMetaMap<IBakedModel> models;
    private IElecQuadBakery quadBakery;

    public IBakedModel getCableModel(int i){
        return new CR(i);
    }

    private class CR implements IBakedModel {

        private CR(int meta){
            this.meta = meta;
        }

        private final int meta;

        @Override
        @Nonnull
        public List<BakedQuad> getQuads(IBlockState state_, EnumFacing side, long rand) {
            boolean up, down, north, east, south, west;
            up = down = north = east = south = west = false;
            if (state_ != null) {
                IExtendedBlockState state = (IExtendedBlockState) state_;
                up = state.getValue(TileEntityCable.UP);
                down = state.getValue(TileEntityCable.DOWN);
                north = state.getValue(TileEntityCable.NORTH);
                east = state.getValue(TileEntityCable.EAST);
                south = state.getValue(TileEntityCable.SOUTH);
                west = state.getValue(TileEntityCable.WEST);
            }
            //if (up && down && north && east && south && west){
            //    return models.forMeta(meta).getQuads(state_, side, rand);
            //}
            return side == null ? getGeneralQuads(up, down, north, east, south, west) : ImmutableList.<BakedQuad>of();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return textures[meta];
        }

        @Override
        @SuppressWarnings("deprecation")
        public ItemCameraTransforms getItemCameraTransforms() {
            return ElecModelBakery.DEFAULT_BLOCK;
        }

        @Override
        public ItemOverrideList getOverrides() {
            return ItemOverrideList.NONE;
        }

        @SuppressWarnings("all")
        public List<BakedQuad> getGeneralQuads(boolean up, boolean down, boolean north, boolean east, boolean south, boolean west) {
            List<BakedQuad> ret = Lists.newArrayList();

            float thickness = 6;
            float heightStuff = (16 - thickness)/2;
            float f1 = thickness + heightStuff;

            float yMin = (down ? 0 : heightStuff);
            float yMax = (up ? 16 : f1);
            float xMin = (west ? 0 : heightStuff);
            float xMax = (east ? 16 : f1);
            float zMin = (north ? 0 : heightStuff);
            float zMax = (south ? 16 : f1);

            ret.add(bakeQuad(xMin, f1, zMin, xMax, f1, zMax, EnumFacing.UP, xMin, zMin, xMax, zMax));
            ret.add(bakeQuad(xMax, heightStuff, zMax, xMin, heightStuff, zMin, EnumFacing.DOWN, xMax, zMin, xMin, zMax));
            ret.add(bakeQuad(xMax, yMax, f1, xMin, yMin, f1, EnumFacing.SOUTH, xMax, yMin, xMin, yMax));
            ret.add(bakeQuad(xMax, yMax, heightStuff, xMin, yMin, heightStuff, EnumFacing.NORTH, xMin, yMin, xMax, yMax));
            ret.add(bakeQuad(f1, yMax, zMax, f1, yMin, zMin, EnumFacing.EAST, zMin, yMin, zMax, yMax));
            ret.add(bakeQuad(heightStuff, yMax, zMax, heightStuff, yMin, zMin, EnumFacing.WEST, zMax, yMin, zMin, yMax));

            return ret;
        }

        private BakedQuad bakeQuad(float xMa, float yMa, float zMa, float xMi, float yMi, float zMi, EnumFacing facing, float uMin, float vMin, float uMax, float vMax){
            return quadBakery.bakeQuad(new Vector3f(xMa, yMa, zMa), new Vector3f(xMi, yMi, zMi), textures[meta], facing, ModelRotation.X0_Y0, uMin, vMin, uMax, vMax, -1);
        }

    }

}
