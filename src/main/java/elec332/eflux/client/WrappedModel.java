package elec332.eflux.client;

import elec332.core.client.model.ElecModelBakery;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 14-5-2016.
 */
@SideOnly(Side.CLIENT)
public class WrappedModel implements IBakedModel {

    public static IBakedModel wrapWithDefaultItemTransforms(IBakedModel model){
        return new WrappedModel(model, ElecModelBakery.DEFAULT_ITEM);
    }

    public static IBakedModel wrapWithDefaultBlockTransforms(IBakedModel model){
        return new WrappedModel(model, ElecModelBakery.DEFAULT_BLOCK);
    }

    private WrappedModel(IBakedModel modelB, ItemCameraTransforms transforms){
        this.modelB = modelB;
        this.transforms = transforms;
    }

    private final IBakedModel modelB;
    private final ItemCameraTransforms transforms;

    @Override
    @Nonnull
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        return modelB.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return modelB.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return modelB.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return modelB.isBuiltInRenderer();
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleTexture() {
        return modelB.getParticleTexture();
    }

    @Override
    @Nonnull
    public ItemCameraTransforms getItemCameraTransforms() {
        return transforms;
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides() {
        return modelB.getOverrides();
    }

}
