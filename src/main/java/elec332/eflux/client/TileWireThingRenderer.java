package elec332.eflux.client;

import elec332.eflux.tileentity.TileWireConnector;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import java.awt.*;

/**
 * Created by Elec332 on 7-11-2017.
 */
public class TileWireThingRenderer extends TileEntitySpecialRenderer<TileWireConnector> {

	@Override
	public void render(TileWireConnector te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}

}
