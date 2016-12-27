package elec332.eflux.client;

import elec332.eflux.EFlux;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 5-5-2015.
 */
public class EFluxResourceLocation extends ResourceLocation{

    public EFluxResourceLocation(String path) {
        super(EFlux.ModID.toLowerCase(), path);
    }

}
