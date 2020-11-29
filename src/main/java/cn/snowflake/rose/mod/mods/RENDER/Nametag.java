package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.manager.FriendManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.GlStateManager;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.RenderUtil;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Nametag extends Module {
    public Nametag() {
        super("Nametag", "Name Tag", Category.RENDER);
    }
    private Value<Double> size = new Value<Double>("NameTag_Size", Double.valueOf(1D), Double.valueOf(1D), Double.valueOf(10D), 0.1D);
    public Value<Boolean> invisible = new Value<Boolean>("NameTag_Invisible", false);


    @EventTarget
    public void on3D(EventRender3D eventRender3D){
        for (Object o : this.mc.theWorld.playerEntities) {
            EntityPlayer p = (EntityPlayer) o;
            if(p != mc.thePlayer) {
                double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * JReflectUtility.getRenderPartialTicks()
                        - RenderManager.renderPosX;
                double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * JReflectUtility.getRenderPartialTicks()
                        - RenderManager.renderPosY;
                double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * JReflectUtility.getRenderPartialTicks()
                        - RenderManager.renderPosZ;
                renderNameTag(p, String.valueOf(p.getCommandSenderName()) , pX, pY, pZ);
            }
        }
    }
    public void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {

    }

    public static void revertAllCaps()
    {
        for (Iterator localIterator = glCapMap.keySet().iterator(); localIterator.hasNext();)
        {
            int cap = ((Integer)localIterator.next()).intValue();
            revertGLCap(cap);
        }
    }

    public static void revertGLCap(int cap)
    {
        Boolean origCap = (Boolean)glCapMap.get(Integer.valueOf(cap));
        if (origCap != null) {
            if (origCap.booleanValue()) {
                GL11.glEnable(cap);
            } else {
                GL11.glDisable(cap);
            }
        }
    }


    public void whatTheFuckOpenGLThisFixesItemGlint() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    private static Map<Integer, Boolean> glCapMap = new HashMap();

    public static void setGLCap(int cap, boolean flag)
    {
        glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
        if (flag) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }
    }

    public void drawBorderedRectNameTag(float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
        RenderUtil.drawRect(var1, var2, var3, var4, var7);
        float var8 = (float)(var6 >> 24 & 255) / 255.0F;
        float var9 = (float)(var6 >> 16 & 255) / 255.0F;
        float var10 = (float)(var6 >> 8 & 255) / 255.0F;
        float var11 = (float)(var6 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(var9, var10, var11, var8);
        GL11.glLineWidth(var5);
        GL11.glBegin(1);
        GL11.glVertex2d((double)var1, (double)var2);
        GL11.glVertex2d((double)var1, (double)var4);
        GL11.glVertex2d((double)var3, (double)var4);
        GL11.glVertex2d((double)var3, (double)var2);
        GL11.glVertex2d((double)var1, (double)var2);
        GL11.glVertex2d((double)var3, (double)var2);
        GL11.glVertex2d((double)var1, (double)var4);
        GL11.glVertex2d((double)var3, (double)var4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
}
