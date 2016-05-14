package elec332.eflux.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by Elec332 on 14-5-2016.
 */
public class CapabilityWrapper {

    public static <T> ICapabilityProvider getProviderFor(Capability<T> capability, T impl) {
        if (impl instanceof INBTSerializable) {
            return new SerializableCapabilityWrapper(capability, impl);
        } else {
            return new CapabilityWrapper_(capability, impl);
        }
    }

    private static class CapabilityWrapper_ implements ICapabilityProvider {

        private  <T> CapabilityWrapper_(Capability<T> capability, T impl) {
            this.capability = capability;
            this.impl = impl;
        }

        private final Capability capability;
                final Object impl;

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == this.capability;
        }


        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == this.capability ? (T) impl : null;
        }

    }

    private static class SerializableCapabilityWrapper extends CapabilityWrapper_ implements ICapabilitySerializable<NBTBase> {

        private  <T> SerializableCapabilityWrapper(Capability<T> capability, T impl) {
            super(capability, impl);
        }

        @Override
        public NBTBase serializeNBT() {
            return ((INBTSerializable) impl).serializeNBT();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void deserializeNBT(NBTBase nbt) {
            ((INBTSerializable) impl).deserializeNBT(nbt);
        }
    }

}
