package elec332.eflux.init;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by Elec332 on 13-9-2015.
 */
public class FluidRegister {

    public static final FluidRegister instance = new FluidRegister();
    private FluidRegister(){
        slib = new Fluid("Slib");
    }

    public static Fluid slib;

    public void init(){
        FluidRegistry.registerFluid(slib);
    }

    public void registerTextures(IIconRegister iconRegister){
        IIcon icon = iconRegister.registerIcon("slibFluid");
        slib.setIcons(icon, icon);
    }

}
