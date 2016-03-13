package elec332.eflux.tileentity.misc;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.tile.IInventoryTile;
import elec332.core.tile.TileBase;
import elec332.core.util.BasicInventory;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.inventory.GuiStandardFormat;
import elec332.eflux.util.Utils;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;

/**
 * Created by Elec332 on 19-2-2016.
 */
@RegisterTile(name = "TileEntityEFluxFeeder")
public class TileEntityFeeder extends TileBase implements IInventoryTile, ITileWithSlots {

    public TileEntityFeeder(){
        invWrapper = new InvWrapper(new BasicInventory("", 1, this));
    }

    private int feedCounter = 0;
    private InvWrapper invWrapper;

    @Override
    public void update() {
        feedCounter--;
        if (feedCounter <= 0){
            feedCounter = 500 + EFlux.random.nextInt(1000);
            List<EntityAnimal> animals = worldObj.getEntitiesWithinAABB(EntityAnimal.class, Utils.getAABBAroundBlock(pos, 2, 2, 2, 2, 2, 2));
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
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("fc", feedCounter);
        NBTBase inv = tagCompound.getTag("inv");
        if (inv != null) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(null, invWrapper, null, inv);
        }
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        return openGui(player, EFlux.instance, 0);
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
    public BaseContainer getGuiServer(EntityPlayer player) {
        return new ContainerMachine(this, player, 0);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new GuiStandardFormat(getGuiServer(player), new EFluxResourceLocation("gui/GuiNull.png"));
    }

    @Override
    public void addSlots(BaseContainer container) {
        container.addSlotToContainer(new SlotItemHandler(invWrapper, 0, 66, 53));
        container.addPlayerInventoryToContainer();
    }

}
