package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;


public class ItemESP extends Module {
    public ItemESP() {
        super("ItemESP","Item ESP",Category.RENDER);
    }

    @EventTarget
    public void on2D(EventRender2D event) {
    	System.out.println("1");
		double finalnumber;
        double percentoffset;
        Vector4d position;
        List<Vector3d> vectors;
        AxisAlignedBB aabb;
        double height;
        double width;
        double z;
        double y;
        double x;
        Entity entity;
        for (Object ent : mc.theWorld.getLoadedEntityList()) {
            if (ent instanceof EntityItem && RenderUtil.isInViewFrustrum(entity = (EntityItem)ent)) {
                x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, JReflectUtility.getRenderPartialTicks());
                y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, JReflectUtility.getRenderPartialTicks());
                z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, JReflectUtility.getRenderPartialTicks());
                width = (double)entity.width / 1.25;
                height = (double)entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                aabb = JReflectUtility.newAxisAlignedBBInstance(x - width, y, z - width, x + width, y + height, z + width);
                vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                JReflectUtility.cameraTransform(JReflectUtility.getRenderPartialTicks(), 0);
                position = null;
                for (Vector3d vector : vectors) {
                    vector = RenderUtil.project(vector.x - RenderManager.instance.viewerPosX, vector.y - RenderManager.instance.viewerPosY, vector.z - RenderManager.instance.viewerPosZ);
                    if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
                this.mc.entityRenderer.setupOverlayRendering();
                if (position != null) {
                	System.out.println("drawing");
                    GL11.glPushMatrix();
                    RenderUtil.drawBorderedRect(position.x - 1.5, position.y - 0.5, position.z - position.x + 4.0, position.w - position.y + 1.0, 1.5, -16777216, 0,true);
                    RenderUtil.drawBorderedRect(position.x - 1.0, position.y, position.z - position.x + 3.0, position.w - position.y, 0.5, -1, 0,true);
                    if (((EntityItem)ent).getEntityItem().getMaxDamage() > 0) {
                        double offset = position.w - position.y;
                        percentoffset = offset / (double)((EntityItem)ent).getEntityItem().getMaxDamage();
                        finalnumber = percentoffset * (double)(((EntityItem)ent).getEntityItem().getMaxDamage() - ((EntityItem)ent).getEntityItem().getItemDamage());
                        RenderUtil.drawBorderedRect(position.x - 3.5, position.y - 0.5, 1.5, position.w - position.y + 1.0, 0.5, -16777216, 0x60000000,true);
                        RenderUtil.drawRect(position.x - 3.0, position.y + offset, 0.5, -finalnumber, -12680221);
                    }
                    GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
                    String nametext = StringUtils.stripControlCodes(((EntityItem)ent).getEntityItem().getItem().getItemStackDisplayName(((EntityItem)ent).getEntityItem())) + (((EntityItem)ent).getEntityItem().getMaxDamage() > 0 ? "\u00a79 : " + (((EntityItem)ent).getEntityItem().getMaxDamage() - ((EntityItem)ent).getEntityItem().getItemDamage()) : "");
                    Minecraft.getMinecraft().fontRenderer.drawString(nametext, (int)(position.x + (position.z - position.x) / 2.0 - (this.mc.fontRenderer.getStringWidth(nametext) / 4) * 2.0f), (int)(position.y - this.mc.fontRenderer.FONT_HEIGHT + 2.0 * 2.0f), -1);
                    GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                    GL11.glPopMatrix();
                }
            }
		}
    }
}
