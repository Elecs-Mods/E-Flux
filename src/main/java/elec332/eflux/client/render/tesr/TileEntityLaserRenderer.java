package elec332.eflux.client.render.tesr;

import elec332.core.api.client.ITessellator;
import elec332.core.client.RenderHelper;
import elec332.core.main.ElecCore;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.tileentity.basic.TileEntityLaser;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;


/**
 * Created by Elec332 on 16-1-2016.
 */
public class TileEntityLaserRenderer extends TileEntitySpecialRenderer<TileEntityLaser> {

    private static final ResourceLocation laser = new EFluxResourceLocation("textures/laser.png");

    @Override
    public void func_192841_a(TileEntityLaser te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_TEXTURE_BIT);
            if (te.active() /*&& ((MultiBlockLaser)te.getMultiBlock()).isActive()*/) {
                GL11.glDepthMask(false);

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

                EntityPlayerSP p = (EntityPlayerSP) ElecCore.proxy.getClientPlayer();
                double doubleX = p.lastTickPosX + (p.posX - p.lastTickPosX) * partialTicks;
                double doubleY = p.lastTickPosY + (p.posY - p.lastTickPosY) * partialTicks;
                double doubleZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * partialTicks;

                Vector start = new Vector(te.getPos().getX() + .5f, te.getPos().getY() + .5f + .3f, te.getPos().getZ() + .5f);
                Vector player = new Vector((float) doubleX, (float) doubleY, (float) doubleZ);

                GL11.glPushMatrix();
                GL11.glTranslated(-doubleX, -doubleY, -doubleZ);

                ITessellator tessellator = RenderHelper.getTessellator();

                // ----------------------------------------

                this.bindTexture(laser);

                tessellator.getMCTessellator().getBuffer().begin(7, DefaultVertexFormats.BLOCK);
                tessellator.setBrightness(240);

                drawBeam(start, new Vector(te.getRenderPos()), player, .1f);

                tessellator.getMCTessellator().draw();

                GL11.glPopMatrix();
            }

        GL11.glPopAttrib();
    }

    public static void drawBeam(Vector S, Vector E, Vector P, float width) {
        Vector PS = Sub(S, P);
        Vector SE = Sub(E, S);

        Vector normal = Cross(PS, SE);
        normal = normal.normalize();

        Vector half = Mul(normal, width);
        Vector p1 = Add(S, half);
        Vector p2 = Sub(S, half);
        Vector p3 = Add(E, half);
        Vector p4 = Sub(E, half);

        drawQuad(RenderHelper.getTessellator(), p1, p3, p4, p2);
    }

    public static void drawQuad(ITessellator tessellator, Vector p1, Vector p2, Vector p3, Vector p4) {
        tessellator.addVertexWithUV(p1.getX(), p1.getY(), p1.getZ(), 0, 0);
        tessellator.addVertexWithUV(p2.getX(), p2.getY(), p2.getZ(), 1, 0);
        tessellator.addVertexWithUV(p3.getX(), p3.getY(), p3.getZ(), 1, 1);
        tessellator.addVertexWithUV(p4.getX(), p4.getY(), p4.getZ(), 0, 1);
    }

    public static class Vector {
        public final float x;
        public final float y;
        public final float z;

        public Vector(BlockPos pos){
            this(pos.getX(), pos.getY(), pos.getZ());
        }

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public float norm() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        public Vector normalize() {
            float n = norm();
            return new Vector(x / n, y / n, z / n);
        }
    }

    private static Vector Cross(Vector a, Vector b) {
        float x = a.y*b.z - a.z*b.y;
        float y = a.z*b.x - a.x*b.z;
        float z = a.x*b.y - a.y*b.x;
        return new Vector(x, y, z);
    }

    private static Vector Sub(Vector a, Vector b) {
        return new Vector(a.x-b.x, a.y-b.y, a.z-b.z);
    }
    private static Vector Add(Vector a, Vector b) {
        return new Vector(a.x+b.x, a.y+b.y, a.z+b.z);
    }
    private static Vector Mul(Vector a, float f) {
        return new Vector(a.x * f, a.y * f, a.z * f);
    }

}
