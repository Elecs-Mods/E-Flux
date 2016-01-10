package elec332.eflux.blocks;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.map.BakedModelMetaRotationMap;
import elec332.core.client.model.map.IBakedModelMetaRotationMap;
import elec332.core.client.model.model.IBlockModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import elec332.eflux.init.BlockRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 2-9-2015.
 */
public class BlockHeatGlass extends BlockMachinePart {

    public BlockHeatGlass(int types) {
        super(types, "BlockMachineGlass");
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite heatGlass, lensFront;
    @SideOnly(Side.CLIENT)
    private IBakedModelMetaRotationMap<IBlockModel> rotationMap;

    @Override
    protected void registerTiles() {
        GameRegistry.registerTileEntity(LaserIndicator.class, "LaserIndicatorClass");
    }

    @Override
    public int getTypes() {
        return 2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (meta == BlockRegister.laserLens.meta)
            return new LaserIndicator();
        return new TileEntityBlockMachine();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return StatCollector.translateToLocal("eflux.machine.glass." + stack.getItemDamage());
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public IBlockModel getBlockModel(IBlockState state, IBlockAccess iba, BlockPos pos) {
        return rotationMap.forMetaAndRotation(WorldHelper.getBlockMeta(state), DirectionHelper.getRotationFromFacing(((TileEntityBlockMachine)WorldHelper.getTileAt(iba, pos)).getTileFacing()));
    }

    @Override
    public IBakedModel getBlockModel(Item item, int meta) {
        return rotationMap.forMeta(meta);
    }

    @Override
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        rotationMap = new BakedModelMetaRotationMap<IBlockModel>();
        rotationMap.setModelsForRotation(0, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(heatGlass)));
        rotationMap.setModelsForRotation(1, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(heatGlass, heatGlass, lensFront, heatGlass, lensFront, heatGlass)));
    }

    @Override
    public void registerTextures(IIconRegistrar iconRegistrar) {
        heatGlass = iconRegistrar.registerSprite(getTextureLocation("heatGlass"));
        lensFront = iconRegistrar.registerSprite(getTextureLocation("laserLensFront"));
    }

    public static class LaserIndicator extends TileEntityBlockMachine {
    }

}
