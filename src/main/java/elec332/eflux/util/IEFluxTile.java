package elec332.eflux.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 6-4-2015.
 */
public interface IEFluxTile {

    public int getLightOpacity();

    public int getLightValue();

    public void onBlockRemoved();

    public void onBlockAdded();

    public void onBlockPlacedBy(EntityLivingBase entityLiving, ItemStack stack);

    public void onNeighborBlockChange(Block block);

    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ);

    public void onWrenched(ForgeDirection forgeDirection);

}
