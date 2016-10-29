package elec332.eflux.blocks;

import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.client.model.loading.INoJsonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelFluid;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 2-5-2016.
 */
public class BlockFluid extends BlockFluidClassic implements INoJsonBlock {

    public BlockFluid(Fluid fluid){
        this(fluid, Material.WATER);
    }

    public BlockFluid(Fluid fluid, Material material) {
        super(fluid, material);
        setRegistryName(fluid.getName());
        setUnlocalizedName(getRegistryName().toString());
        this.fluid = fluid;
    }

    public final Fluid fluid;
    @SideOnly(Side.CLIENT)
    private IBakedModel model;

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(IBlockState iBlockState) {
        return model;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {
        return model;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iIconRegistrar) {
        iIconRegistrar.registerSprite(fluid.getStill());
        iIconRegistrar.registerSprite(fluid.getFlowing());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(IElecQuadBakery elecQuadBakery, IElecModelBakery elecModelBakery, IElecTemplateBakery elecTemplateBakery) {
        model = new ModelFluid(fluid).bake(ModelRotation.X0_Y0, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
    }

}
