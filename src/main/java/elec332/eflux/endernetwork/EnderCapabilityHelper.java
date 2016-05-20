package elec332.eflux.endernetwork;

import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.endernetwork.capabilities.AbstractEnderCapability;
import elec332.eflux.endernetwork.capabilities.factory.DefaultFactory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

/**
 * Created by Elec332 on 7-5-2016.
 */
public class EnderCapabilityHelper {

    public static IEnderCapabilityFactory createFactoryFor(ResourceLocation name, Function<Pair<Side, IEnderNetwork>, IEnderCapability> factory, ResourceLocation... textures){
        return new DefaultFactory(name, factory, textures);
    }

    public static Function<Pair<Side, IEnderNetwork>, IEnderCapability> getConstructor(Class<? extends AbstractEnderCapability> clazz){
        return new Function<Pair<Side, IEnderNetwork>, IEnderCapability>() {
            @Override
            public IEnderCapability apply(Pair<Side, IEnderNetwork> params) {
                try {
                    return clazz.getConstructor(Side.class, IEnderNetwork.class).newInstance(params.getLeft(), params.getRight());
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        };
    }

}
