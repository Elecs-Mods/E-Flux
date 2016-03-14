package elec332.eflux.blocks;

import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.map.BakedModelMetaRotationMap;
import elec332.core.client.model.map.IBakedModelMetaRotationMap;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.util.DirectionHelper;
import elec332.eflux.blocks.data.AbstractEFluxBlockMachineData;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.client.blocktextures.BlockTextures;
import elec332.eflux.client.blocktextures.IBlockTextureProvider;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockItemGate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class BlockItemInlet extends BlockMachine {

    public BlockItemInlet() {
        super(DATA);
    }

    private static IEFluxBlockMachineData DATA;

    @SideOnly(Side.CLIENT)
    protected IBakedModelMetaRotationMap<IBakedModel> rotationMap;

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(IBlockState state) {
        EnumFacing facing = getFacing(state);
        if (facing != EnumFacing.UP && facing != EnumFacing.DOWN){
            return rotationMap.forMetaAndRotation(0, DirectionHelper.getRotationFromFacing(facing));
        } else if (facing == EnumFacing.UP){
            return rotationMap.forMetaAndRotation(0, ModelRotation.X270_Y0);
        }
        return rotationMap.forMetaAndRotation(0, ModelRotation.X90_Y0);
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        rotationMap = new BakedModelMetaRotationMap<IBakedModel>(true, true);
        for (int i = 0; i < 2; i++) {
            rotationMap.setModelsForRotation(i, modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(textures[i]), true, true));
        }
    }

    static {
        DATA = new AbstractEFluxBlockMachineData() {

            private final IBlockTextureProvider textureProvider = new IBlockTextureProvider() {
                @Override
                public String getIconName(EnumFacing side, boolean active) {
                    if (side == EnumFacing.NORTH){
                        return active ? "itemInletFront" : "itemOutletFront";
                    }
                    return BlockTextures.defaultSideTexture;
                }
            };

            @Override
            public Class<? extends TileEntity> getTileClass() {
                return TileEntityMultiBlockItemGate.class;
            }

            @Override
            public boolean hasTwoStates() {
                return true;
            }

            @Override
            public IBlockTextureProvider getTextureProvider() {
                return textureProvider;
            }

            @Override
            public String getName() {
                return "itemInlet";
            }

        };
    }

}
