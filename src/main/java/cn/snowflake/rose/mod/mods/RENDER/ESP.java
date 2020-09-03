package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.asm.ClassTransformer;
import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ESP extends Module {
    public Value<Boolean> players = new Value("ESP_Player", true);

    public Value<Boolean> otherentity = new Value("ESP_OtherEntity", true);
    public Value<Boolean> animal = new Value("ESP_Animal", false);
    public Value<Boolean> moster = new Value("ESP_Mob", false);
    public Value<Boolean> village = new Value("ESP_village", false);
    public Value<Boolean> invisible = new Value("ESP_Invisible", false);
    private static Map<EntityPlayer, float[][]> entities = new HashMap<EntityPlayer, float[][]>();
    public Value<String> mode = new Value<>("ESP","Mode",0);
    public ESP() {
        super("ESP", Category.RENDER);
        this.mode.addValue("2DBox");
        this.mode.addValue("Box");

    }

    public void renderBox(Entity entity,double r,double g, double b) {
        if(entity.isInvisible() && !invisible.getValueState().booleanValue()) {
            return;
        }
        Field fTimer = null;
        try {
            fTimer = mc.getClass().getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_71428_T" : "timer");
            fTimer.setAccessible(true);
        } catch (NoSuchFieldException ev) {
        }
        Field frenderPartialTicks = null;
        try {
            frenderPartialTicks = Timer.class.getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_74281_c" : "renderPartialTicks");
        } catch (NoSuchFieldException v) {
        }

        float pTicks = 0;
        try {
            frenderPartialTicks.setAccessible(true);
            pTicks = (float) frenderPartialTicks.get(fTimer.get(mc));
        } catch (IllegalAccessException ev) {
        }
        double x = RenderUtil.interpolate((double)entity.posX, (double)entity.lastTickPosX,pTicks);
        double y = RenderUtil.interpolate((double)entity.posY, (double)entity.lastTickPosY,pTicks);
        double z = RenderUtil.interpolate((double)entity.posZ, (double)entity.lastTickPosZ,pTicks);
        GL11.glPushMatrix();
        RenderUtil.pre();
        GL11.glLineWidth((float)1.0f);
        GL11.glEnable((int)2848);
        GL11.glColor3d(r,g,b);
        RenderUtil.drawOutlinedBoundingBox(new AltAxisAlignedBB(
                entity.boundingBox.minX
                        - 0.05
                        - entity.posX
                        + (entity.posX - RenderManager.renderPosX),
                entity.boundingBox.minY
                        - entity.posY
                        + (entity.posY - RenderManager.renderPosY),
                entity.boundingBox.minZ
                        - 0.05
                        - entity.posZ
                        + (entity.posZ - RenderManager.renderPosZ),
                entity.boundingBox.maxX
                        + 0.05
                        - entity.posX
                        + (entity.posX - RenderManager.renderPosX),
                entity.boundingBox.maxY
                        + 0.1
                        - entity.posY
                        + (entity.posY - RenderManager.renderPosY),
                entity.boundingBox.maxZ
                        + 0.05
                        - entity.posZ
                        + (entity.posZ - RenderManager.renderPosZ)));
        GL11.glDisable((int)2848);
        RenderUtil.post();
        GL11.glPopMatrix();
    }

    @EventTarget
    public void onRender(EventRender3D e){
        Field fTimer = null;
        try {
            fTimer = mc.getClass().getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_71428_T" : "timer");
            fTimer.setAccessible(true);
        } catch (NoSuchFieldException ev) {
        }
        Field frenderPartialTicks = null;
        try {
            frenderPartialTicks = Timer.class.getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_74281_c" : "renderPartialTicks");
        } catch (NoSuchFieldException v) {
        }

        float pTicks = 0;
        try {
            frenderPartialTicks.setAccessible(true);
            pTicks = (float) frenderPartialTicks.get(fTimer.get(mc));
        } catch (IllegalAccessException ev) {
        }
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                Entity entity1 = entity;
                if (canTarget(entity1)) {
                    double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * pTicks - RenderManager.renderPosX;
                    double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * pTicks - RenderManager.renderPosY;
                    double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * pTicks - RenderManager.renderPosZ;
                    if (this.mode.isCurrentMode("Box")){
                        renderBox(entity, ColorUtil.getClickGUIColora().getRed(),ColorUtil.getClickGUIColora().getGreen(),ColorUtil.getClickGUIColora().getBlue());
                    }
                }
            }
        }
    }

    private boolean canTarget(Entity entity) {

        if (entity instanceof EntityPlayer && !players.getValueState()) {
            return false;
        }
        if (entity instanceof EntityAnimal && !animal.getValueState()) {
            return false;
        }
        if ((entity instanceof EntityMob || entity instanceof EntitySlime|| entity instanceof EntityBat || entity instanceof EntityVillager)&& !moster.getValueState()) {
            return false;
        }
        if (entity instanceof EntityVillager && !village.getValueState()) {
            return false;
        }
        if (entity.isInvisible() && !invisible.getValueState()) {
            return false;
        }
        if (entity instanceof EntityCreature && !otherentity.getValueState()) {
            return false;
        }
        if (entity == mc.thePlayer){
            return false;
        }
        return true;
    }
}
