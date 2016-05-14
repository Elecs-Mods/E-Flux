package elec332.eflux.items.ender.capability;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.EFlux;
import elec332.eflux.api.ender.IEnderCapabilityContainingItem;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 7-5-2016.
 */
public class EFluxEnderCapabilityItem extends Item implements INoJsonItem, IEnderCapabilityContainingItem {

    public EFluxEnderCapabilityItem(IEnderCapabilityFactory enderCapabilityFactory) {
        super();
        this.factory = enderCapabilityFactory;
        setCreativeTab(EFlux.creativeTab);
        setRegistryName(enderCapabilityFactory.getRegistryName());
        setUnlocalizedName(enderCapabilityFactory.getRegistryName().toString().replace(":", ".").toLowerCase());
    }

    private final IEnderCapabilityFactory factory;

    @Override
    public IEnderCapabilityFactory getCapabilityFactory(ItemStack stack) {
        return factory;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return factory.getItemModel(stack, world, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
    }

}
