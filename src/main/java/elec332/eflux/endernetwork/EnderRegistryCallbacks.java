package elec332.eflux.endernetwork;

import com.google.common.collect.Lists;
import elec332.core.client.ITextureLoader;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.model.IModelLoader;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.items.ender.capability.ItemEFluxEnderCapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 7-5-2016.
 */
@SuppressWarnings("unchecked")
public enum EnderRegistryCallbacks implements IForgeRegistry.AddCallback<IEnderCapabilityFactory>, IForgeRegistry.ClearCallback<IEnderCapabilityFactory>, IForgeRegistry.CreateCallback<IEnderCapabilityFactory> {

    INSTANCE;

    private final List<Integer> registeredTypes = Lists.newArrayList();

    @Override
    public void onCreate(Map<ResourceLocation, ?> slaveset) {
    }

    @Override
    public void onClear(Map<ResourceLocation, ?> slaveset) {
    }

    @Override
    public void onAdd(IEnderCapabilityFactory obj, int id, Map<ResourceLocation, ?> slaveset) {
        if (!registeredTypes.contains(id)) {
            if (obj.createItem()) {
                GameRegistry.register(new ItemEFluxEnderCapability(obj));
            }
            if (FMLCommonHandler.instance().getSide().isClient()) {
                if (obj instanceof IModelLoader) {
                    RenderingRegistry.instance().registerLoader((IModelLoader) obj);
                }
                if (obj instanceof ITextureLoader) {
                    RenderingRegistry.instance().registerLoader((ITextureLoader) obj);
                }
            }
            registeredTypes.add(id);
        }
    }

}
