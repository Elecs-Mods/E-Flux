package elec332.eflux.grid.power.itemsTEST;

import elec332.core.helper.RegisterHelper;
import elec332.eflux.EFlux;
import elec332.eflux.grid.power.TestTile;
import elec332.eflux.grid.power.TestTileII;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 27-4-2015.
 */
public class Charger extends Item {
    public Charger(){
        super();
        setCreativeTab(EFlux.CreativeTab);
        setUnlocalizedName(EFlux.ModID + ".charger");
        setTextureName(EFlux.ModID + ":charger");
        RegisterHelper.registerItem(this, "charger");
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        TileEntity tileEntity = p_77648_3_.getTileEntity(p_77648_4_, p_77648_5_, p_77648_6_);
        if (tileEntity instanceof TestTile && !p_77648_3_.isRemote){
            ((TestTile) tileEntity).storedPower = 300;
        } else if (tileEntity instanceof TestTileII && !p_77648_3_.isRemote)
            ((TestTileII) tileEntity).storedPower = 300;
        return true;
    }
}
