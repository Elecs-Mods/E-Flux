package elec332.eflux.blocks;

import elec332.eflux.blocks.data.AbstractEFluxBlockMachineData;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.client.blocktextures.BlockTextures;
import elec332.eflux.client.blocktextures.IBlockTextureProvider;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockItemGate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class BlockItemInlet extends BlockMachine {

    public BlockItemInlet() {
        super(DATA);
    }

    private static IEFluxBlockMachineData DATA;
/*
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

    @Override
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return rotationMap.get();
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     *
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
        rotationMap = new BakedModelMetaRotationMap<IBakedModel>(true, true);
        for (int i = 0; i < 2; i++) {
            rotationMap.setModelsForRotation(i, modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(textures[i]), true, true));
        }
    }*/

    static {
        DATA = new AbstractEFluxBlockMachineData() {

            private final IBlockTextureProvider textureProvider = (side, active) -> {

                if (side == EnumFacing.NORTH){
                    return active ? "itemInletFront" : "itemOutletFront";
                }
                return BlockTextures.defaultSideTexture;

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
