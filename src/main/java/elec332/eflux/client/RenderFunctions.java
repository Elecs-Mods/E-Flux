package elec332.eflux.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

/**
 * Created by Elec332 on 7-11-2017.
 */
public class RenderFunctions {

	public static Vec3d getTranslationForRendering(Vec3d renderPos){
		return new Vec3d(renderPos.x - TileEntityRendererDispatcher.staticPlayerX, renderPos.y - TileEntityRendererDispatcher.staticPlayerY, renderPos.z - TileEntityRendererDispatcher.staticPlayerZ);
	}

	@SuppressWarnings("all")
	public static void renderWire(Vec3d from, Vec3d to, Color color, float width, boolean half){
		Vec3d delta = to.subtract(from);
		//System.out.println("f"+from);
		//System.out.println("t"+to);
		double len = delta.distanceTo(Vec3d.ZERO);
		double dr = len / 10;

		GlStateManager.pushMatrix();
		//GlStateManager.translate(0, -1, 0);
		GlStateManager.enableRescaleNormal();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		double d10 = delta.x;
		double d11 = delta.y;
		double d12 = delta.z;
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.glLineWidth(width);
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

		int stages = 16;

		if (len > 30){
			stages += (len - 30) / 5;
		}

		if (stages % 2 != 0){ //Make sure there is an even amount of stages
			stages++;
		}

		int times = half ? (stages / 2) + 1 : stages;

		for (int i1 = 0; i1 <= times; ++i1) {
			float f11 = (float) i1 / ((float) stages);
			bufferbuilder.pos(d10 * f11, d11 * f11 + ((((f11 - 0.5) * (f11 - 0.5)) * 4) * dr) - dr, d12 * f11).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();

	}

}
