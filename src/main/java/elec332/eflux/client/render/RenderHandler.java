package elec332.eflux.client.render;

import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * Created by Elec332 on 20-9-2015.
 */
public class RenderHandler {

    public static void init(){
        RenderingRegistry.registerBlockHandler(new CableRenderer());
    }

    public static final int cable;

    static {
        cable = RenderingRegistry.getNextAvailableRenderId();
    }
}
