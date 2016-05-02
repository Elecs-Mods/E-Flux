package elec332.eflux.client.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import elec332.core.client.RenderHelper;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.model.IQuadProvider;
import elec332.core.client.model.template.MutableQuadTemplate;
import elec332.core.util.DirectionHelper;
import elec332.eflux.blocks.BlockMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Elec332 on 13-3-2016.
 */
public class BlockMachineQuadProvider implements IQuadProvider {

    public BlockMachineQuadProvider(TextureAtlasSprite[][] textures, ElecQuadBakery quadBakery){
        this.textures = textures;
        this.quadBakery = quadBakery;
    }

    private final TextureAtlasSprite[][] textures;
    private final ElecQuadBakery quadBakery;

    @Override
    public List<BakedQuad> getBakedQuads(@Nullable IBlockState state, EnumFacing side, long random) {
        if (side == null){
            return ImmutableList.of();
        }
        TextureAtlasSprite[] textures = this.textures[(state != null && ((IExtendedBlockState)state).getValue(BlockMachine.ACTIVATED_PROPERTY)) ? 1 : 0];
        ModelRotation rotation = state == null ? ModelRotation.X0_Y0 : defaultFor(BlockMachine.getFacing(state));
        EnumFacing usedRot = rotation.rotate(side);
        return Lists.newArrayList(quadBakery.bakeQuad(MutableQuadTemplate.templateForTexture(side, textures[usedRot.ordinal()])));
    }

    public static ModelRotation defaultFor(EnumFacing facing) {
        switch(facing) {
            case UP:
                return ModelRotation.X90_Y0;
            case DOWN:
                return ModelRotation.X270_Y0;
            case NORTH:
                return ModelRotation.X0_Y0;
            case EAST:
                return ModelRotation.X0_Y270;
            case SOUTH:
                return ModelRotation.X0_Y180;
            case WEST:
                return ModelRotation.X0_Y90;
            default:
                return ModelRotation.X0_Y0;
        }
    }

}
