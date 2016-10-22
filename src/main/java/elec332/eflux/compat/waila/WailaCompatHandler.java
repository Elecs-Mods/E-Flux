package elec332.eflux.compat.waila;

import elec332.core.compat.waila.IWailaCapabilityDataProvider;
import elec332.core.main.ElecCoreRegistrar;
import elec332.core.module.ElecModule;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.List;

/**
 * Created by Elec332 on 6-9-2015.
 */
@ElecModule(owner = EFlux.ModID, name = "WailaCompat", modDependencies = "Waila")
public class WailaCompatHandler {

    @ElecModule.EventHandler
    public void init(FMLInitializationEvent event) {
        ElecCoreRegistrar.WAILA_CAPABILITY_PROVIDER.register(EFluxAPI.ENDER_COMPONENT_CAPABILITY, new IWailaCapabilityDataProvider<IEnderNetworkComponent>() {

            @Override
            public List<String> getWailaBody(List<String> currentTip, IEnderNetworkComponent capability, NBTTagCompound tag, EntityPlayer player, RayTraceResult rts, World world, BlockPos pos, TileEntity tile) {
                if (tag != null){
                    currentTip.add("Network ID: "+tag.getString("u"));
                    currentTip.add("Frequency: "+tag.getInteger("i"));
                    currentTip.add("Has connection: "+tag.getBoolean("b"));
                }
                return currentTip;
            }

            @Override
            public NBTTagCompound getWailaTag(IEnderNetworkComponent capability, EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, BlockPos pos) {
                if (tag != null && capability != null){
                    tag.setString("u", String.valueOf(capability.getUuid()));
                    tag.setInteger("i", capability.getFrequency());
                    tag.setBoolean("b", capability.getCurrentConnection() != null);
                }
                return tag;
            }

            @Override
            public boolean isCompatibleCapability(Capability<?> capability) {
                return capability == EFluxAPI.ENDER_COMPONENT_CAPABILITY;
            }

        });
    }

}
