package elec332.eflux.items;

import elec332.core.helper.RegisterHelper;
import elec332.eflux.EFlux;
import elec332.eflux.tileentity.BaseMachineTEWithInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 5-4-2015.
 */
public class MultiMeter extends Item {
    public MultiMeter(String name) {
        setCreativeTab(EFlux.CreativeTab);
        setUnlocalizedName(EFlux.ModID + "." + name);
        setTextureName(EFlux.ModID + ":" + name);
        setContainerItem(this);
        setMaxStackSize(1);
        RegisterHelper.registerItem(this, name);
    }

    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
        return false;
    }

    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float HitX, float HitY, float HitZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!world.isRemote) {
            /*if (tileEntity instanceof BaseMachineTEWithInventory) {
                player.addChatComponentMessage(new ChatComponentText("Power : " + ((BaseMachineTEWithInventory) tileEntity).getEnergyStored(ForgeDirection.UNKNOWN)));
                player.addChatComponentMessage(new ChatComponentText("Can receive power from clicked side : " + ((BaseMachineTEWithInventory) tileEntity).canConnectEnergy(ForgeDirection.getOrientation(side))));
            }*/
            return true;
        }
        return false;
    }
}
