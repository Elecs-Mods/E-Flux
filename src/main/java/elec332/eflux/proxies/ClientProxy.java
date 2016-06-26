package elec332.eflux.proxies;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.map.BakedModelMetaMap;
import elec332.core.client.model.map.IBakedModelMetaMap;
import elec332.core.client.model.model.IModelAndTextureLoader;
import elec332.core.client.model.model.IQuadProvider;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.client.newstuff.INoJsonItemHandler;
import elec332.core.inventory.BaseContainer;
import elec332.core.tile.IInventoryTile;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.FurnaceRenderTile;
import elec332.eflux.client.inventory.GuiEnderContainer;
import elec332.eflux.client.inventory.GuiMachine;
import elec332.eflux.client.manual.gui.GuiManual;
import elec332.eflux.client.render.FurnaceContentsRenderer;
import elec332.eflux.client.render.tesr.TESRAreaMover;
import elec332.eflux.client.render.tesr.TileEntityLaserRenderer;
import elec332.eflux.client.render.tesr.TileEntityTankRenderer;
import elec332.eflux.inventory.ContainerEnderContainer;
import elec332.eflux.multipart.cable.PartBasicCable;
import elec332.eflux.tileentity.BreakableMachineTile;
import elec332.eflux.tileentity.basic.TileEntityLaser;
import elec332.eflux.tileentity.misc.TileEntityAreaMover;
import elec332.eflux.tileentity.misc.TileEntityTank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
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
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = WorldHelper.getTileAt(world, new BlockPos(x, y, z));
        switch (ID){
            case 3:
                return new GuiManual();
            case 1:
                if (tile instanceof BreakableMachineTile) {
                    return ((BreakableMachineTile) tile).getBreakableMachineInventory().brokenGui(Side.CLIENT, player);
                }
                return null;
            case 2:
                if (tile != null) {
                    return new GuiMachine((BaseContainer) getServerGuiElement(2, player, world, x, y, z)) {
                        @Override
                        public ResourceLocation getBackgroundImageLocation() {
                            return new EFluxResourceLocation("BGRED");
                        }
                    };
                }
                return null;
            case 4:
                Container container1 = getServerGuiElement(ID, player, world, x, y, z);
                if (container1 != null){
                    return new GuiEnderContainer((ContainerEnderContainer) container1);
                }
                return null;
            case 5:
                Container container2 = getServerGuiElement(ID, player, world, x, y, z);
                if (container2 != null){
                    return new GuiMachine((BaseContainer) container2) {
                        @Override
                        public ResourceLocation getBackgroundImageLocation() {
                            return new EFluxResourceLocation("gui/GuiNull.png");
                        }
                    };
                }
                return null;
            default:
                if (tile instanceof IInventoryTile) {
                    return ((IInventoryTile) tile).getGuiClient(player);
                }
                return null;
        }
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public void initRenderStuff(){
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaser.class, new TileEntityLaserRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FurnaceRenderTile.class, new FurnaceContentsRenderer());
        TileEntityTankRenderer<TileEntityTank> tankRenderer = new TileEntityTankRenderer<TileEntityTank>();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, tankRenderer);
        RenderingRegistry.instance().registerLoader(tankRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAreaMover.class, new TESRAreaMover());
        RenderingRegistry.instance().registerFakeItem(EFlux.creativeTab.getTabIconItem());

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
        });*/
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SuppressWarnings("all")
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
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
        textures = new TextureAtlasSprite[3];
        textures[0] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/basicCable"));
        textures[1] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/normalCable"));
        textures[2] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/advancedCable"));
    }

    private TextureAtlasSprite[] textures;
    private IBakedModelMetaMap<IBakedModel> models;
    private ElecQuadBakery quadBakery;

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        event.getModelRegistry().putObject(new ModelResourceLocation("eflux:i-aint-making-jsons_0#multipart"), new CR(0));
        event.getModelRegistry().putObject(new ModelResourceLocation("eflux:i-aint-making-jsons_1#multipart"), new CR(1));
        event.getModelRegistry().putObject(new ModelResourceLocation("eflux:i-aint-making-jsons_2#multipart"), new CR(2));
    }

    private class CR implements IBakedModel {

        private CR(int meta){
            this.meta = meta;
        }

        private final int meta;

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
            return side == null ? new CM((IExtendedBlockState) state, meta).getBakedQuads(state, null, rand) : ImmutableList.<BakedQuad>of();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return false;
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
            return null;
        }

        @Override
        public ItemOverrideList getOverrides() {
            return ItemOverrideList.NONE;
        }

    }

    private class CM implements IQuadProvider {

        private CM(IExtendedBlockState state, int meta){
            up = state.getValue(PartBasicCable.UP);
            down = state.getValue(PartBasicCable.DOWN);
            north = state.getValue(PartBasicCable.NORTH);
            east = state.getValue(PartBasicCable.EAST);
            south = state.getValue(PartBasicCable.SOUTH);
            west = state.getValue(PartBasicCable.WEST);
            this.meta = meta;
            this.state = state;
        }

        private final IBlockState state;
        private final boolean up, down, north, east, south, west;
        private final int meta;

        @Override
        public List<BakedQuad> getBakedQuads(@Nullable IBlockState state, EnumFacing side, long random) {
            return side != null ? ImmutableList.<BakedQuad>of() : getGeneralQuads();
        }

        @SuppressWarnings("all")
        public List<BakedQuad> getGeneralQuads() {
            if (up && down && north && east && south && west){
                return models.forMeta(meta).getQuads(state, null, 0L);
            }
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
