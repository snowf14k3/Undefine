package cn.snowflake.rose.mod.mods.RENDER;


import java.awt.Color;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.GlStateManager;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.RenderUtil;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.Vec3;

public class RearView extends Module {

    private Value<Double> X;

    private Value<Double> Y;



    public RearView(){
        super("RearView","Rear View", Category.RENDER);
        this.pos = JReflectUtility.newInstanceVec3(0, 0, 0);
        this.yaw = 0;
        this.pitch = 0;
        this.frameBuffer = new Framebuffer(WIDTH_RESOLUTION, HEIGHT_RESOLUTION, true);
        this.frameBuffer.createFramebuffer(WIDTH_RESOLUTION, HEIGHT_RESOLUTION);
        X = new Value<Double>("RearView_X", 2.0, 2.0, 1920.0, 1.0);
        Y = new Value<Double>("RearView_Y", 100.0, 25.0, 1080.0, 1.0);
    }

    @EventTarget
    public void on2D(EventRender2D e) {
        float xOffset = X.getValueState().floatValue();
        float yOffset = Y.getValueState().floatValue();
        ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
        this.setRendering(true);
        RenderUtil.drawRect(sr.getScaledWidth() - xOffset -201,sr.getScaledHeight() - yOffset - 121, sr.getScaledWidth() - xOffset + 1, sr.getScaledHeight() - yOffset + 1, -1);//background
        if (this.isValid()) {
            this.setPos(getPositionEyes(JReflectUtility.getRenderPartialTicks()).subtract(JReflectUtility.newInstanceVec3(0,1,0)));
            this.setYaw(Minecraft.getMinecraft().thePlayer.rotationYaw - 180.0f);
            this.setPitch(0.0f);
            this.render(
                    sr.getScaledWidth() - xOffset  -200, //x1

                    sr.getScaledHeight() - yOffset - 120,//y1

                    sr.getScaledWidth() - xOffset , //x2

                    sr.getScaledHeight() - yOffset //y2
            );
        }
    }
    public Vec3 getPositionEyes(float partialTicks) {
        if (partialTicks == 1.0F) {
            return JReflectUtility.newInstanceVec3(mc.thePlayer.posX, mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        } else {
            double d0 = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * (double) partialTicks;
            double d1 = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * (double) partialTicks + (double) mc.thePlayer.getEyeHeight();
            double d2 = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (double) partialTicks;
            return JReflectUtility.newInstanceVec3(d0, d1, d2);
        }
    }
    public void update() {
        if (!isRecording() && isRendering()) {
            updateFbo();
        }
    }

    @EventTarget
    public void onPre(EventMotion e) {
        if (e.isPre())
        update();
    }

    private Vec3 pos;

    private float yaw;

    private float pitch;

    private boolean recording;

    private boolean valid;

    private boolean rendering;

    private boolean firstUpdate;

    private Framebuffer frameBuffer;

    private final int WIDTH_RESOLUTION = 800;
    private final int HEIGHT_RESOLUTION = 600;

    private final Minecraft mc = Minecraft.getMinecraft();



    public void render(float x, float y, float w, float h) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            frameBuffer.bindFramebufferTexture();
            final Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(6);
            tessellator.addVertexWithUV(x, h, 0,0,0);
            tessellator.addVertexWithUV(w, h, 0,1,0);
            tessellator.addVertexWithUV(w, y, 0,1,1);
            tessellator.addVertexWithUV(x, y, 0,0,1);
            tessellator.draw();
            frameBuffer.unbindFramebufferTexture();
            GlStateManager.popMatrix();
        }
    }

    public void updateFbo() {
        if (!this.firstUpdate) {
            mc.renderGlobal.loadRenderers(); // 加载
            this.firstUpdate = true;
        }
        if (mc.thePlayer != null) {
            double posX = mc.thePlayer.posX;
            double posY = mc.thePlayer.posY;
            double posZ = mc.thePlayer.posZ;
            double prevPosX = mc.thePlayer.prevPosX;
            double prevPosY = mc.thePlayer.prevPosY;
            double prevPosZ = mc.thePlayer.prevPosZ;
            double lastTickPosX = mc.thePlayer.lastTickPosX;
            double lastTickPosY = mc.thePlayer.lastTickPosY;
            double lastTickPosZ = mc.thePlayer.lastTickPosZ;

            float rotationYaw = mc.thePlayer.rotationYaw;
            float prevRotationYaw = mc.thePlayer.prevRotationYaw;
            float rotationPitch = mc.thePlayer.rotationPitch;
            float prevRotationPitch = mc.thePlayer.prevRotationPitch;
            boolean sprinting = mc.thePlayer.isSprinting();

            boolean hideGUI = mc.gameSettings.hideGUI;
            boolean clouds = mc.gameSettings.clouds;
            int thirdPersonView = mc.gameSettings.thirdPersonView;
            float gamma = mc.gameSettings.gammaSetting;
            int ambientOcclusion = mc.gameSettings.ambientOcclusion;
            boolean viewBobbing = mc.gameSettings.viewBobbing;
            int particles = mc.gameSettings.particleSetting;
            int displayWidth = mc.displayWidth;
            int displayHeight = mc.displayHeight;

            int frameLimit = mc.gameSettings.limitFramerate;
            float fovSetting = mc.gameSettings.fovSetting;

            mc.thePlayer.posX = this.getPos().xCoord;
            mc.thePlayer.posY = this.getPos().yCoord;
            mc.thePlayer.posZ = this.getPos().zCoord;

            mc.thePlayer.prevPosX = this.getPos().xCoord;
            mc.thePlayer.prevPosY = this.getPos().yCoord;
            mc.thePlayer.prevPosZ = this.getPos().zCoord;

            mc.thePlayer.lastTickPosX = this.getPos().xCoord;
            mc.thePlayer.lastTickPosY = this.getPos().yCoord;
            mc.thePlayer.lastTickPosZ = this.getPos().zCoord;

            mc.thePlayer.rotationYaw = this.yaw;
            mc.thePlayer.prevRotationYaw = this.yaw;
            mc.thePlayer.rotationPitch = this.pitch;
            mc.thePlayer.prevRotationPitch = this.pitch;
            mc.thePlayer.setSprinting(false);

            mc.gameSettings.hideGUI = true;
            mc.gameSettings.clouds = false;
            mc.gameSettings.thirdPersonView = 0;
            mc.gameSettings.ambientOcclusion = 0;
            mc.gameSettings.viewBobbing = false;
            mc.gameSettings.particleSetting = 0;
            mc.displayWidth = WIDTH_RESOLUTION;
            mc.displayHeight = HEIGHT_RESOLUTION;

            mc.gameSettings.limitFramerate = 10;
            mc.gameSettings.fovSetting = 110;//FOV

            this.setRecording(true);
            frameBuffer.bindFramebuffer(true);

            mc.entityRenderer.renderWorld(JReflectUtility.getRenderPartialTicks(), System.nanoTime());
            mc.entityRenderer.setupOverlayRendering();

            frameBuffer.unbindFramebuffer();
            this.setRecording(false);

            mc.thePlayer.posX = posX;
            mc.thePlayer.posY = posY;
            mc.thePlayer.posZ = posZ;

            mc.thePlayer.prevPosX = prevPosX;
            mc.thePlayer.prevPosY = prevPosY;
            mc.thePlayer.prevPosZ = prevPosZ;

            mc.thePlayer.lastTickPosX = lastTickPosX;
            mc.thePlayer.lastTickPosY = lastTickPosY;
            mc.thePlayer.lastTickPosZ = lastTickPosZ;

            mc.thePlayer.rotationYaw = rotationYaw;
            mc.thePlayer.prevRotationYaw = prevRotationYaw;
            mc.thePlayer.rotationPitch = rotationPitch;
            mc.thePlayer.prevRotationPitch = prevRotationPitch;
            mc.thePlayer.setSprinting(sprinting);

            mc.gameSettings.hideGUI = hideGUI;
            mc.gameSettings.clouds = clouds;
            mc.gameSettings.thirdPersonView = thirdPersonView;
            mc.gameSettings.gammaSetting = gamma;
            mc.gameSettings.ambientOcclusion = ambientOcclusion;
            mc.gameSettings.viewBobbing = viewBobbing;
            mc.gameSettings.particleSetting = particles;
            mc.displayWidth = displayWidth;
            mc.displayHeight = displayHeight;
            mc.gameSettings.limitFramerate = frameLimit;
            mc.gameSettings.fovSetting = fovSetting;

            this.setValid(true);
            this.setRendering(false);
        }
    }

    public void resize() {
        this.frameBuffer.createFramebuffer(WIDTH_RESOLUTION, HEIGHT_RESOLUTION);
        if (!isRecording() && isRendering()) {
            this.updateFbo();
        }
    }

    public Vec3 getPos() {
        return pos;
    }

    public void setPos(Vec3 pos) {
        this.pos = pos;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isRendering() {
        return rendering;
    }

    public void setRendering(boolean rendering) {
        this.rendering = rendering;
    }

}
