package elec332.eflux.items;

import elec332.eflux.EFlux;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Elec332 on 10-9-2015.
 */
public abstract class EFluxItem extends Item {

    public EFluxItem(String name){
        this.name = name;
        setUnlocalizedName(EFlux.ModID+"."+name);
        setCreativeTab(EFlux.creativeTab);
    }

    private final String name;

    public EFluxItem register(){
        GameRegistry.registerItem(this, name);
        return this;
    }

}
