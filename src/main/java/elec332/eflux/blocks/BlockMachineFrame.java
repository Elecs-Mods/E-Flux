package elec332.eflux.blocks;

import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.api.client.model.model.IQuadProvider;
import elec332.core.client.model.loading.INoJsonBlock;
import elec332.core.util.UniversalUnlistedProperty;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.render.model.MachineFrameQuadProvider;
import elec332.eflux.tileentity.basic.TileEntityBlockMachine;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class BlockMachineFrame extends BlockWithMeta implements ITileEntityProvider {

    public BlockMachineFrame(String name) {
        super(Material.ROCK, name, EFlux.ModID.toLowerCase());
    }

    public static final IUnlistedProperty<BlockPos> FRAME_POS_PROPERTY = new UniversalUnlistedProperty<BlockPos>("position", BlockPos.class);

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;
    @SideOnly(Side.CLIENT)
    public static IBakedModel model, itemModel;
    @SideOnly(Side.CLIENT)
    private static IQuadProvider quadProvider;

    @Override
    public BlockMachineFrame register() {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()){
            //registerClient();
        }
        super.register();
        return this;
    }

    @Override
    public IUnlistedProperty[] getUnlistedProperties() {
        return new IUnlistedProperty[]{FRAME_POS_PROPERTY};
    }

    @Override
    public int getTypes() {
        return 3;
    }

    @Override
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return ((IExtendedBlockState)state).withProperty(FRAME_POS_PROPERTY, pos);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBlockMachine();
    }

}
