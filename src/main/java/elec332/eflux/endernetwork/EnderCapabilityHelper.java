package elec332.eflux.endernetwork;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.model.IModelAndTextureLoader;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

/**
 * Created by Elec332 on 7-5-2016.
 */
public class EnderCapabilityHelper {

    public static IEnderCapabilityFactory createFactoryFor(ResourceLocation name, Function<Pair<Side, IEnderNetwork>, IEnderCapability> factory, ResourceLocation... textures){
        return new DefaultFactory(name, factory, textures);
    }

    public static class DefaultFactory extends IEnderCapabilityFactory implements IModelAndTextureLoader {

        public DefaultFactory(ResourceLocation name, Function<Pair<Side, IEnderNetwork>, IEnderCapability> factory, ResourceLocation... textures){
            setRegistryName(name);
            this.factory = factory;
            this.texture = textures;
        }

        private final Function<Pair<Side, IEnderNetwork>, IEnderCapability> factory;
        private final ResourceLocation[] texture;
        @SideOnly(Side.CLIENT)
        private TextureAtlasSprite[] textures;
        @SideOnly(Side.CLIENT)
        private IBakedModel model;

        /**
         * Creates a new IEnderCapability
         *
         * @param side The side for which this capability will be created
         * @param network The network this capability will be created for
         * @return The newly created EnderCapability
         */
        @Override
        public IEnderCapability createNewCapability(Side side, IEnderNetwork network) {
            return factory.apply(Pair.of(side, network));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
            return model;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void registerTextures(IIconRegistrar iconRegistrar) {
            textures = new TextureAtlasSprite[texture.length];
            for (int i = 0; i < textures.length; i++) {
                textures[i] = iconRegistrar.registerSprite(texture[i]);
            }
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
            for (int i = 0; i < 100; i++) {
                System.out.println("registerModels");
            }

            model = modelBakery.itemModelForTextures(textures);
        }

    }

}
