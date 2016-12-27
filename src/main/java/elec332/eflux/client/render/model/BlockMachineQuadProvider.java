package elec332.eflux.client.render.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.model.IQuadProvider;
import elec332.core.client.model.template.MutableQuadTemplate;
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

    @SuppressWarnings("unchecked")
    public BlockMachineQuadProvider(TextureAtlasSprite[][] textures, IElecQuadBakery quadBakery){
        this.textures = textures;
        this.quadBakery = quadBakery;
        this.quadCache = new List[textures.length][EnumFacing.VALUES.length][EnumFacing.VALUES.length];
        for (int i = 0; i < textures.length; i++) {
            TextureAtlasSprite[] textures_ = textures[i];
            for (EnumFacing facing : EnumFacing.VALUES){
                ModelRotation rotation = defaultFor(facing);
                for (EnumFacing side : EnumFacing.VALUES){
                    EnumFacing usedRot = rotation.rotate(side);
                    quadCache[i][facing.ordinal()][side.ordinal()] = ImmutableList.of(quadBakery.bakeQuad(MutableQuadTemplate.templateForTexture(side, textures_[usedRot.ordinal()])));
                }
            }
        }
    }

    private final TextureAtlasSprite[][] textures;
    private final List<BakedQuad>[][][] quadCache; //on/off, facing, side
    private final IElecQuadBakery quadBakery;

    @Override
    public List<BakedQuad> getBakedQuads(@Nullable IBlockState state, EnumFacing side, long random) {
        if (side == null){
            return ImmutableList.of();
        }
        return quadCache[(state != null && ((IExtendedBlockState)state).getValue(BlockMachine.ACTIVATED_PROPERTY)) ? 1 : 0][(state == null ? EnumFacing.NORTH : BlockMachine.getFacing(state)).ordinal()][side.ordinal()];
        //TextureAtlasSprite[] textures = this.textures[(state != null && ((IExtendedBlockState)state).getValue(BlockMachine.ACTIVATED_PROPERTY)) ? 1 : 0];
        //ModelRotation rotation = state == null ? ModelRotation.X0_Y0 : defaultFor(BlockMachine.getFacing(state));
        //EnumFacing usedRot = rotation.rotate(side);
        //return Lists.newArrayList());
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
