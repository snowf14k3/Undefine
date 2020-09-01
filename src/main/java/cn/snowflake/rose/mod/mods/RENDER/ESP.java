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
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ESP extends Module {
    public Value<Boolean> players = new Value("ESP_Player", true);
    private static int switchIndex;
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
    private boolean doesntContain(EntityPlayer var0) {
        return !this.mc.theWorld.playerEntities.contains(var0);
    }

    public void drawESP(Entity entity, int color) {
        AxisAlignedBB boundingBox = entity.getBoundingBox();
        Timer timer = null;
        if (ClassTransformer.runtimeDeobfuscationEnabled){
            timer  = (Timer) JReflectUtility.getFieldAsObject(mc.getClass(),mc,"field_71428_T");
        }else{
            timer  = (Timer) JReflectUtility.getFieldAsObject(mc.getClass(),mc,"timer");
        }
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks;
        double width = Math.abs(boundingBox.maxX - boundingBox.minX);
        double height = Math.abs(boundingBox.maxY - boundingBox.minY);
        Vec3Util vec = new Vec3Util(x - width / 2.0, y, z - width / 2.0);
        Vec3Util vec2 = new Vec3Util(x + width / 2.0, y + height, z + width / 2.0);
        RenderUtil.pre3D();
        RenderUtil.glColor(color);
        RenderUtil.drawBoundingBox(new AltAxisAlignedBB(vec.getX() - RenderManager.renderPosX, vec.getY() - RenderManager.renderPosY, vec.getZ() - RenderManager.renderPosZ, vec2.getX() - RenderManager.renderPosX, vec2.getY() - RenderManager.renderPosY, vec2.getZ() - RenderManager.renderPosZ));
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        RenderUtil.post3D();
    }
    @EventTarget
    public void onRender(EventRender3D e){
        Timer timer = null;
        if (ClassTransformer.runtimeDeobfuscationEnabled){
            timer  = (Timer) JReflectUtility.getFieldAsObject(mc.getClass(),mc,"field_71428_T");
        }else{
            timer  = (Timer) JReflectUtility.getFieldAsObject(mc.getClass(),mc,"timer");
        }
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                Entity entity1 = entity;
                if (canTarget(entity1)) {
                    double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - RenderManager.renderPosX;
                    double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - RenderManager.renderPosY;
                    double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - RenderManager.renderPosZ;
                    if (this.mode.isCurrentMode("Box")){
                        drawESP(entity, ColorUtil.getClickGUIColora().getRGB());
                    }
                    if (this.mode.isCurrentMode("2DBox")) {
                        GL11.glPushMatrix();
                        GL11.glColor4d((double) 1.0, (double) 1.0, (double) 1.0, (double) 0.0);
                        double size = 0.25;
                        double boundindY = entity.getBoundingBox().maxY - entity.getBoundingBox().minY;
                        RenderUtil.drawBoundingBox(new AltAxisAlignedBB(posX - size, (double) posY, posZ - size, posX + size, (double) (posY + boundindY), posZ + size));
                        RenderUtil.renderOne();//renderOne
                        RenderUtil.drawBoundingBox(new AltAxisAlignedBB(posX - size, (double) posY, posZ - size, posX + size, (double) (posY + boundindY), posZ + size));
                        GL11.glStencilFunc((int) 512, (int) 0, (int) 15);
                        GL11.glStencilOp((int) 7681, (int) 7681, (int) 7681);
                        GL11.glPolygonMode((int) 1032, (int) 6914);
                        RenderUtil.drawBoundingBox(new AltAxisAlignedBB(posX - size, (double) posY, posZ - size, posX + size, (double) (posY + boundindY), posZ + size));
                        GL11.glStencilFunc((int) 514, (int) 1, (int) 15);
                        GL11.glStencilOp((int) 7680, (int) 7680, (int) 7680);
                        GL11.glPolygonMode((int) 1032, (int) 6913);
                        RenderUtil.setColor(entity1);//draw
                        RenderUtil.drawBoundingBox(new AltAxisAlignedBB(posX - size, (double) posY, posZ - size, posX + size, (double) (posY + boundindY), posZ + size));
                        GL11.glPolygonOffset((float) 1.0f, (float) 2000000.0f);
                        GL11.glDisable((int) 10754);
                        GL11.glEnable((int) 2929);
                        GL11.glDepthMask((boolean) true);
                        GL11.glDisable((int) 2960);
                        GL11.glDisable((int) 2848);
                        GL11.glHint((int) 3154, (int) 4352);
                        GL11.glEnable((int) 3042);
                        GL11.glEnable((int) 2896);
                        GL11.glEnable((int) 3553);
                        GL11.glEnable((int) 3008);
                        GL11.glPopAttrib();
                        GL11.glColor4d((double) 1.0, (double) 1.0, (double) 1.0, (double) 1.0);
                        GL11.glPopMatrix();
                    }
                }
            }
        }
    }
    private Vec3 getVec3(RenderHandEvent event, EntityPlayer var0) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        float timer = event.partialTicks;
        double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * (double)timer;
        double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * (double)timer;
        double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * (double)timer;
        Class clazz = null;
        try {
            clazz = Class.forName("net.minecraft.util.Vec3");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Constructor c = null;
        try {
            c = clazz.getDeclaredConstructor(double.class,double.class,double.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        c.setAccessible(true);
        return (Vec3)c.newInstance(x,y,z);
    }
    private boolean canTarget(Entity entity) {
        if (!(entity instanceof EntityMob || entity instanceof EntityAnimal) && entity instanceof EntityCreature && !otherentity.getValueState()) {
            return false;
        }
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
        if (entity == mc.thePlayer){
            return false;
        }
        return true;
    }
}
