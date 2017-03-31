package elec332.eflux.items;

import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.client.model.loading.INoJsonItem;
import elec332.core.item.AbstractItemBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 18-3-2017.
 */
public class AbstractTexturedItemBlock extends AbstractItemBlock implements INoJsonItem {

    public AbstractTexturedItemBlock(Block block) {
        super(block);
    }

    @SideOnly(Side.CLIENT)
    private IBakedModel model;

    @Override
    public IBakedModel getItemModel(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {
        if (model == null){
            model = Minecraft.getMinecraft().modelManager.modelProvider.getModelForState(block.getDefaultState());
        }
        return model;
    }

    @Override
    public void registerTextures(IIconRegistrar iIconRegistrar) {
    }

    @Override
    public void registerModels(IElecQuadBakery iElecQuadBakery, IElecModelBakery iElecModelBakery, IElecTemplateBakery iElecTemplateBakery) {
    }

}
