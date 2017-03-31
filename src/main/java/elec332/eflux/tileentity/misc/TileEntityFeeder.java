package elec332.eflux.tileentity.misc;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.window.ISimpleWindowFactory;
import elec332.core.inventory.window.Window;
import elec332.core.util.BasicItemHandler;
import elec332.eflux.EFlux;
import elec332.eflux.tileentity.TileEntityEFlux;
import elec332.eflux.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 19-2-2016.
 */
@RegisteredTileEntity("TileEntityEFluxFeeder")
public class TileEntityFeeder extends TileEntityEFlux implements ISimpleWindowFactory, ITickable {

    public TileEntityFeeder(){
        invWrapper = new BasicItemHandler(1);
    }

    private int feedCounter = 0;
    private IItemHandlerModifiable invWrapper;

    @Override
    public void update() {
        feedCounter--;
        if (feedCounter <= 0){
            feedCounter = 500 + EFlux.random.nextInt(1000);
            List<EntityAnimal> animals = getWorld().getEntitiesWithinAABB(EntityAnimal.class, Utils.getAABBAroundBlock(pos, 2, 2, 2, 2, 2, 2));
            for (EntityAnimal animal : animals){
                if (animal.isBreedingItem(invWrapper.extractItem(0, 1, true)) && animal.getGrowingAge() == 0 && !animal.isInLove()){
                    animal.setInLove(null);
                    invWrapper.extractItem(0, 1, false);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.feedCounter = tagCompound.getInteger("fc");
        NBTBase inv = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(null, invWrapper, null);
        tagCompound.setTag("inv", inv);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("fc", feedCounter);
        NBTBase inv = tagCompound.getTag("inv");
        if (inv != null) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(null, invWrapper, null, inv);
        }
        return tagCompound;
    }

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
         return openLocalWindow(player);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)invWrapper : super.getCapability(capability, facing);
    }

    @Override
    public void modifyWindow(Window window, Object... args) {
        window.addWidget(new WidgetSlot(invWrapper, 0, 66, 53));
        window.addPlayerInventoryToContainer();
    }

}
