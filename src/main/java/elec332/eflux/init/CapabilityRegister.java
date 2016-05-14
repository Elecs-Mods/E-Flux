package elec332.eflux.init;

import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.endernetwork.EnderCapabilityHelper;
import elec332.eflux.endernetwork.capabilities.AbstractEnderCapability;
import elec332.eflux.endernetwork.capabilities.EFluxEnderCapabilityPlayerInventory;
import elec332.eflux.multiblock.MultiBlockInterfaces;
import elec332.eflux.util.IEFluxFluidHandler;
import elec332.eflux.util.RedstoneCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Created by Elec332 on 2-4-2016.
 */
public enum CapabilityRegister {

    instance;

    public IEnderCapabilityFactory playerInventory;

    public void init(){
        registerWithoutAnything(MultiBlockInterfaces.IEFluxMultiBlockFluidHandler.class);

        registerWithoutAnything(IEFluxFluidHandler.class);
        registerWithoutAnything(RedstoneCapability.class);
        playerInventory = registerEnderCapabilityNoItem("playerInventory", EFluxEnderCapabilityPlayerInventory.class);
    }

    private static IEnderCapabilityFactory registerEnderCapabilityNoItem(String name, Class<? extends AbstractEnderCapability> clazz){
        return registerEnderCapabilityNoItem(name, getConstructor(clazz));
    }

    private static IEnderCapabilityFactory registerEnderCapability(String name, Class<? extends AbstractEnderCapability> clazz){
        return registerEnderCapability(name, getConstructor(clazz));
    }

    private static Function<Pair<Side, IEnderNetwork>, IEnderCapability> getConstructor(Class<? extends AbstractEnderCapability> clazz){
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

    private static IEnderCapabilityFactory registerEnderCapability(String name, Function<Pair<Side, IEnderNetwork>, IEnderCapability> factory){
        return GameRegistry.register(new EnderCapabilityHelper.DefaultFactory(new EFluxResourceLocation(name), factory, new EFluxResourceLocation("items/"+name)));
    }

    private static IEnderCapabilityFactory registerEnderCapabilityNoItem(String name, Function<Pair<Side, IEnderNetwork>, IEnderCapability> factory){
        return GameRegistry.register(new EnderCapabilityHelper.DefaultFactory(new EFluxResourceLocation(name), factory, new EFluxResourceLocation("items/"+name)){

            @Override
            public boolean createItem() {
                return false;
            }

        });
    }

    private static <T> void registerWithoutAnything(Class<T> clazz){
        CapabilityManager.INSTANCE.register(clazz, new Capability.IStorage<T>() {

            @Override
            public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
                throw new UnsupportedOperationException();
            }

        }, new Callable<T>() {

            @Override
            public T call() throws Exception {
                throw new UnsupportedOperationException();
            }

        });
    }

}
