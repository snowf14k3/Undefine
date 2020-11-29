package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.RenderUtil;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Tracer extends Module {

    public Tracer() {
        super("Tracer","Tracer", Category.RENDER);
    }

    @EventTarget
    public void On3D(EventRender3D e) {
        for(Object ent : mc.theWorld.loadedEntityList) {
            if(ent instanceof EntityPlayer && ent != mc.thePlayer) {
                this.drawLine((EntityPlayer) ent);
            }
        }
    }
    private void drawLine(EntityPlayer player) {
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.renderPosX;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.renderPosY;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.renderPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.5F);
        float DISTANCE = this.mc.thePlayer.getDistanceToEntity(player);
        if (DISTANCE <= 200.0F) {
            GL11.glColor3f(1.0F, DISTANCE / 40.0F, 0.0F);
        }

        GL11.glLoadIdentity();
        boolean bobbing = this.mc.gameSettings.viewBobbing;
        this.mc.gameSettings.viewBobbing = false;
        JReflectUtility.orientCamera(JReflectUtility.getRenderPartialTicks());
        GL11.glBegin(3);
        GL11.glVertex3d(0.0D, (double)this.mc.thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + (double)player.getEyeHeight(), z);
        GL11.glEnd();
        this.mc.gameSettings.viewBobbing = bobbing;
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}
