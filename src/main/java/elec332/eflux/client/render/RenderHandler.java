package elec332.eflux.client.render;

import elec332.core.client.render.AbstractBlockRenderer;
import elec332.core.client.render.RenderHelper;

/**
 * Created by Elec332 on 20-9-2015.
 */
public class RenderHandler {

    public static void dummy(){
    }

    public static AbstractBlockRenderer cableRenderer;

    static {
        cableRenderer = RenderHelper.registerBlockRenderer(new CableRenderer());
    }

}
