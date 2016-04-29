package elec332.eflux.init;

import elec332.eflux.multiblock.MultiBlockInterfaces;
import elec332.eflux.util.IEFluxFluidHandler;
import elec332.eflux.util.RedstoneCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

/**
 * Created by Elec332 on 2-4-2016.
 */
public enum CapabilityRegister {

    instance;

    public void init(){
        registerWithoutAnything(MultiBlockInterfaces.IEFluxMultiBlockFluidHandler.class);
        registerWithoutAnything(MultiBlockInterfaces.IEFluxMultiBlockPowerAcceptor.class);
        registerWithoutAnything(MultiBlockInterfaces.IEFluxMultiBlockPowerProvider.class);

        registerWithoutAnything(IEFluxFluidHandler.class);
        registerWithoutAnything(RedstoneCapability.class);
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
