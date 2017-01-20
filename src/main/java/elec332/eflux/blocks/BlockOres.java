package elec332.eflux.blocks;

import com.google.common.base.Strings;
import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.client.model.loading.INoJsonBlock;
import elec332.core.client.model.map.BakedModelMetaMap;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 21-7-2015.
 */
public class BlockOres extends BlockWithMeta {

    public BlockOres() {
        super(Material.ROCK, "ore", EFlux.ModID.toLowerCase());
        setResistance(5.0f);
        setHardness(2.5f);
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite[] textures;
    @SideOnly(Side.CLIENT)
    private BakedModelMetaMap<IBakedModel> models;

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String s = nameForType(stack.getItemDamage());
        if (Strings.isNullOrEmpty(s)) {
            return "ERROR_BLOCK_EFLUX";
        }
        return getUnlocalizedName()+"."+s;
    }

    public String nameForType(int meta){
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

}
