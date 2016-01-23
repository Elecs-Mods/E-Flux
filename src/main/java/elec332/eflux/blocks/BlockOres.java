package elec332.eflux.blocks;

import com.google.common.base.Strings;
import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonBlock;
import elec332.core.client.model.map.BakedModelMetaMap;
import elec332.core.client.model.model.IBlockModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 21-7-2015.
 */
public class BlockOres extends BlockWithMeta implements INoJsonBlock {

    public BlockOres() {
        super(Material.rock, "ore", EFlux.ModID);
        setResistance(5.0f);
        setHardness(2.5f);
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite[] textures;
    @SideOnly(Side.CLIENT)
    private BakedModelMetaMap<IBlockModel> models;

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String s = nameForType(stack.getItemDamage());
        if (Strings.isNullOrEmpty(s)) {
            return "ERROR_BLOCK_EFLUX";
        }
        return getUnlocalizedName()+"."+s;
    }

    private String nameForType(int meta){
        switch (meta){
            case 0:
                return "copper";
            case 1:
                return "tin";
            case 2:
                return "zinc";
            case 3:
                return "silver";
            default:
                return null;
        }
    }

    @Override
    public int getTypes() {
        return 4;
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
        return models.forMeta(state.getValue(getProperty()));
    }

    /**
     * This method is used when a model is requested when its not placed, so for an item.
     *
     * @return The model to render when the block is not placed.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(Item item, int meta) {
        return models.forMeta(meta);
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        models = new BakedModelMetaMap<IBlockModel>();
        for (int i = 0; i < getTypes(); i++) {
            models.setModelForMeta(i, modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(textures[i]).setTexture(textures[i])));
        }
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        textures = new TextureAtlasSprite[getTypes()];
        for (int i = 0; i < getTypes(); i++) {
            textures[i] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/ore/"+nameForType(i)+"_ore"));
        }
    }

}
