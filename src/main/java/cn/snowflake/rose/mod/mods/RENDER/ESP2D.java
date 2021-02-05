package cn.snowflake.rose.mod.mods.RENDER;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.injection.ClientLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.darkmagician6.eventapi.EventTarget;




public class ESP2D extends Module {
    public Value<String> MODE = new Value<String>("ESP2D","Mode", 0);
    public Value<Boolean> TEAM = new Value<Boolean>("ESP2D_Team", false);
    public Value<Boolean> BOX = new Value<Boolean>("ESP2D_Box", false);
    public Value<Boolean> INVISIBLES = new Value<Boolean>("ESP2D_Invisibles", false);
    public Value<Boolean> CUSTOMTAG = new Value<Boolean>("ESP2D_ItemTag", false);
    public Value<Boolean> HEALTH = new Value<Boolean>("ESP2D_Health", true);
    public Value<Boolean> armor = new Value<Boolean>("ESP2D_Armor", false);
    public static Value<Double> r = new Value<Double>("ESP2D_Red", 255.0D, 0.0D,255.0D, 5.0D);
    public static Value<Double> g = new Value<Double>("ESP2D_Green", 255.0D, 0.0D, 255.0D, 5.0D);
    public static Value<Double> b = new Value<Double>("ESP2D_Blue", 255.0D, 0.0D, 255.0D, 5.0D);
    private double gradualFOVModifier;

    public static Map entityPositionstop = new HashMap();
    public static Map entityPositionsbottom = new HashMap();
    public ESP2D() {
        super("ESP2D","ESP2D", Category.RENDER);
        this.MODE.mode.add("Box");
        this.MODE.mode.add("Split");
        this.MODE.mode.add("Corner B");

    }

    @EventTarget
    public void onEvent(EventRender3D event) {
        try {
            this.updatePositions();
        }catch (Exception ex) {
        }
    }

    @EventTarget
    public void onEvent(EventRender2D event) {
        GlStateManager.pushMatrix();
        ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        double twoDscale = (double)scaledRes.getScaleFactor() / Math.pow((double)scaledRes.getScaleFactor(), 2.0D);
        GlStateManager.scale(twoDscale, twoDscale, twoDscale);
        Iterator var6 = entityPositionstop.keySet().iterator();

        while(true) {
            EntityLivingBase ent;
            double[] renderPositions;
            double[] renderPositionsBottom;
            do {
                if (!var6.hasNext()) {
                    GL11.glScalef(1.0F, 1.0F, 1.0F);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                    return;
                }

                ent = (EntityLivingBase)var6.next();
                renderPositions = (double[])entityPositionstop.get(ent);
                renderPositionsBottom = (double[])entityPositionsbottom.get(ent);
            } while(renderPositions[3] <= 0.0D && renderPositions[3] > 1.0D);

            GlStateManager.pushMatrix();
            if (this.INVISIBLES.getValueState().booleanValue() || !ent.isInvisible() && ent instanceof EntityPlayer && !(ent instanceof EntityPlayerSP)) {
                this.scale();

                try {
                    float y = (float)renderPositions[1];
                    float endy = (float)renderPositionsBottom[1];
                    float meme = endy - y;
                    float x = (float)renderPositions[0] - meme / 4.0F;
                    float endx = (float)renderPositionsBottom[0] + meme / 4.0F;
                    if (x > endx) {
                        endx = x;
                        x = (float)renderPositionsBottom[0] + meme / 4.0F;
                    }
                    double xDiff = (endx - x) / 4.0;
                    double x2Diff = (endx - x) / (double)(MODE.isCurrentMode("Corner B") ? 4 : 3);
                    double yDiff = this.MODE.isCurrentMode("Corner B") ? xDiff : (endy - y) / 4.0;

                    GlStateManager.pushMatrix();
                    GlStateManager.scale(2.0F, 2.0F, 2.0F);
                    GlStateManager.popMatrix();
                    GL11.glEnable(3042);
                    GL11.glDisable(3553);

                    int color = Colors.getColor(255, 255);
                    if(ent.hurtTime > 0) {
                        color = Colors.getColor(255,0,0, 255);
                    } else if(ent.isInvisible()) {
                        color = Colors.getColor(255,255,0, 255);
                    }else {
//                    color = -7924716;
                        color = new Color(r.getValueState().intValue(),g.getValueState().intValue(),b.getValueState().intValue()).getRGB();
                    }
                    if(BOX.getValueState().booleanValue()) {
                        if(MODE.isCurrentMode("Box")) {
                            RenderUtil.rectangleBordered((double)x, (double)y, (double)endx, (double)endy, 2.25D, Colors.getColor(2, 0, 0, 0), color);
                            RenderUtil.rectangleBordered((double)x - 0.5D, (double)y - 0.5D, (double)endx + 0.5D, (double)endy + 0.5D, 0.9D, Colors.getColor(0, 0), Colors.getColor(0));
                            RenderUtil.rectangleBordered((double)x + 2.5D, (double)y + 2.5D, (double)endx - 2.5D, (double)endy - 2.5D, 0.9D, Colors.getColor(0, 0), Colors.getColor(0));
                        }
                        if(MODE.isCurrentMode("Split")) {
                            RenderUtil.rectangle(x + 0.5, y + 0.5, x + 1.5, endy - 0.5, color);

                            RenderUtil.rectangle(x - 0.5, y + 0.5, x + 0.5, endy - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.5, y + 2.5, x + 2.5, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.0, y + 0.5, x + xDiff, y + 1.5, color);
                            RenderUtil.rectangle(x - 0.5, y - 0.5, x + xDiff, y + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.5, y + 1.5, x + xDiff, y + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + xDiff, y - 0.5, x + xDiff + 1.0, y + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.0, endy - 0.5, x + xDiff, endy - 1.5, color);
                            RenderUtil.rectangle(x - 0.5, endy + 0.5, x + xDiff, endy - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.5, endy - 1.5, x + xDiff, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + xDiff, endy + 0.5, x + xDiff + 1.0, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 0.5, y + 0.5, endx - 1.5, endy - 0.5, color);
                            RenderUtil.rectangle(endx + 0.5, y + 0.5, endx - 0.5, endy - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, y + 2.5, endx - 2.5, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.0, y + 0.5, endx - xDiff, y + 1.5, color);
                            RenderUtil.rectangle(endx + 0.5, y - 0.5, endx - xDiff, y + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, y + 1.5, endx - xDiff, y + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - xDiff, y - 0.5, endx - xDiff - 1.0, y + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.0, endy - 0.5, endx - xDiff, endy - 1.5, color);
                            RenderUtil.rectangle(endx + 0.5, endy + 0.5, endx - xDiff, endy - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, endy - 1.5, endx - xDiff, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - xDiff, endy + 0.5, endx - xDiff - 1.0, endy - 2.5, Colors.getColor(0, 150));
                        }
                        if(MODE.isCurrentMode("Corner B")) {
                            RenderUtil.rectangle(x + 0.5, y + 0.5, x + 1.5, y + yDiff + 0.5, color);
                            RenderUtil.rectangle(x + 0.5, endy - 0.5, x + 1.5, endy - yDiff - 0.5, color);
                            RenderUtil.rectangle(x - 0.5, y + 0.5, x + 0.5, y + yDiff + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.5, y + 2.5, x + 2.5, y + yDiff + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x - 0.5, y + yDiff + 0.5, x + 2.5, y + yDiff + 1.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x - 0.5, endy - 0.5, x + 0.5, endy - yDiff - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.5, endy - 2.5, x + 2.5, endy - yDiff - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x - 0.5, endy - yDiff - 0.5, x + 2.5, endy - yDiff - 1.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.0, y + 0.5, x + x2Diff, y + 1.5, color);
                            RenderUtil.rectangle(x - 0.5, y - 0.5, x + x2Diff, y + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.5, y + 1.5, x + x2Diff, y + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + x2Diff, y - 0.5, x + x2Diff + 1.0, y + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.0, endy - 0.5, x + x2Diff, endy - 1.5, color);
                            RenderUtil.rectangle(x - 0.5, endy + 0.5, x + x2Diff, endy - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + 1.5, endy - 1.5, x + x2Diff, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x + x2Diff, endy + 0.5, x + x2Diff + 1.0, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 0.5, y + 0.5, endx - 1.5, y + yDiff + 0.5, color);
                            RenderUtil.rectangle(endx - 0.5, endy - 0.5, endx - 1.5, endy - yDiff - 0.5, color);
                            RenderUtil.rectangle(endx + 0.5, y + 0.5, endx - 0.5, y + yDiff + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, y + 2.5, endx - 2.5, y + yDiff + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx + 0.5, y + yDiff + 0.5, endx - 2.5, y + yDiff + 1.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx + 0.5, endy - 0.5, endx - 0.5, endy - yDiff - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, endy - 2.5, endx - 2.5, endy - yDiff - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx + 0.5, endy - yDiff - 0.5, endx - 2.5, endy - yDiff - 1.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.0, y + 0.5, endx - x2Diff, y + 1.5, color);
                            RenderUtil.rectangle(endx + 0.5, y - 0.5, endx - x2Diff, y + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, y + 1.5, endx - x2Diff, y + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - x2Diff, y - 0.5, endx - x2Diff - 1.0, y + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.0, endy - 0.5, endx - x2Diff, endy - 1.5, color);
                            RenderUtil.rectangle(endx + 0.5, endy + 0.5, endx - x2Diff, endy - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, endy - 1.5, endx - x2Diff, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - x2Diff, endy + 0.5, endx - x2Diff - 1.0, endy - 2.5, Colors.getColor(0, 150));
                        }
                    }

                    float diff1;
                    UnicodeFontRenderer uft = Client.instance.fontManager.simpleton13;


                    Client.instance.fontManager.simpleton30.drawStringWithShadow(ent.getCommandSenderName(), (float)(x), (float)y - 15.0F, -1);


                    float health;
                    if(HEALTH.getValueState().booleanValue()) {
                        health = ((EntityPlayer)ent).getHealth();
                        float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
                        Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                        float progress = health * 5.0F * 0.01F;
                        Color customColor = blendColors(fractions, colors, progress).brighter();
                        double difference = y - endy + 0.5;
                        double healthLocation = endy + difference * (double)progress;
                        RenderUtil.rectangleBordered(x - 6.5, y - 0.5, x - 2.5, endy, 1.0, Colors.getColor(0, 100), Colors.getColor(0, 150));
                        RenderUtil.rectangle(x - 5.5, endy - 1.0, x - 3.5, healthLocation, customColor.getRGB());
                        if (-difference > 50.0) {
                            for (int i = 1; i < 10; ++i) {
                                double dThing = difference / 10.0 * (double)i;
                                RenderUtil.rectangle(x - 6.5, endy - 0.5 + dThing, x - 2.5, endy - 0.5 + dThing - 1.0, Colors.getColor(0));
                            }
                        }
                        if ((int)this.getIncremental((double)(health * 5.0F), 1.0D) != 100 && this.HEALTH.getValueState().booleanValue()) {
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(2.0F, 2.0F, 2.0F);
                            String nigger = (int)this.getIncremental((double)(health * 5.0F), 1.0D) + "HP";
                            uft.drawStringWithShadow(nigger, (x - 22.0F - uft.getStringHeight(nigger) * 2.0F) / 2.0F, ((float)((int)healthLocation) + uft.getStringHeight(nigger) / 2.0F) / 2.0F, -1);
                            GlStateManager.popMatrix();
                        }
                    }
                } catch (Exception var29) {
                    ;
                }
            }

            GlStateManager.popMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0D / inc;
        return (double) Math.round(val * one) / one;
    }
    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        Color color = null;
        if (fractions != null) {
            if (colors != null) {
                if (fractions.length == colors.length) {
                    int[] indicies = getFractionIndicies(fractions, progress);
                    float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
                    Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
                    float max = range[1] - range[0];
                    float value = progress - range[0];
                    float weight = value / max;
                    color = blend(colorRange[0], colorRange[1], (double)(1.0F - weight));
                    return color;
                } else {
                    throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
                }
            } else {
                throw new IllegalArgumentException("Colours can't be null");
            }
        } else {
            throw new IllegalArgumentException("Fractions can't be null");
        }
    }

    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int[] range = new int[2];

        int startPoint;
        for(startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
            ;
        }

        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }

        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0F - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0F) {
            red = 0.0F;
        } else if (red > 255.0F) {
            red = 255.0F;
        }

        if (green < 0.0F) {
            green = 0.0F;
        } else if (green > 255.0F) {
            green = 255.0F;
        }

        if (blue < 0.0F) {
            blue = 0.0F;
        } else if (blue > 255.0F) {
            blue = 255.0F;
        }

        Color color = null;

        try {
            color = new Color(red, green, blue);
        } catch (IllegalArgumentException var14) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format((double)red) + "; " + nf.format((double)green) + "; " + nf.format((double)blue));
            var14.printStackTrace();
        }

        return color;
    }

    private void updatePositions() {
        entityPositionstop.clear();
        entityPositionsbottom.clear();
        float pTicks = JReflectUtility.getRenderPartialTicks();
        Iterator var2 = mc.theWorld.getLoadedEntityList().iterator();

        while(var2.hasNext()) {
            Object o = var2.next();
            if (o instanceof EntityPlayer) {
                EntityPlayer ent = (EntityPlayer)o;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)pTicks - RenderManager.instance.viewerPosY;
                double x = ent.lastTickPosX + (ent.posX + 10.0D - (ent.lastTickPosX + 10.0D)) * (double)pTicks - RenderManager.instance.viewerPosX;
                double z = ent.lastTickPosZ + (ent.posZ + 10.0D - (ent.lastTickPosZ + 10.0D)) * (double)pTicks - RenderManager.instance.viewerPosZ;
                y += (double)ent.height + 0.2D;
                double[] convertedPoints = this.convertTo2D(x, y, z);
                double xd = Math.abs(this.convertTo2D(x, y + 1.0D, z, ent)[1] - this.convertTo2D(x, y, z, ent)[1]);
                if (convertedPoints[2] >= 0.0D && convertedPoints[2] < 1.0D) {
                    entityPositionstop.put(ent, new double[]{convertedPoints[0], convertedPoints[1], xd, convertedPoints[2]});
                    y = ent.lastTickPosY + (ent.posY - 2.2D - (ent.lastTickPosY - 2.2D)) * (double)pTicks - RenderManager.instance.viewerPosY;
                    entityPositionsbottom.put(ent, new double[]{this.convertTo2D(x, y, z)[0], this.convertTo2D(x, y, z)[1], xd, this.convertTo2D(x, y, z)[2]});
                }
            }
        }

    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight();
        double dist = (double)MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
        return new float[]{yaw, pitch};
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        double[] convertedPoints = this.convertTo2D(x, y, z);
        return convertedPoints;
    }

    private void scale() {
        float scale = 1.0F;
        float target = scale * (mc.gameSettings.fovSetting / mc.gameSettings.fovSetting);
        if (this.gradualFOVModifier == 0.0D || Double.isNaN(this.gradualFOVModifier)) {
            this.gradualFOVModifier = (double)target;
        }

        try {
            this.gradualFOVModifier += ((double)target - this.gradualFOVModifier) / ((double)JReflectUtility.getField(mc.getClass(), ClientLoader.runtimeDeobfuscationEnabled ? "field_71470_ab": "debugFPS",true).getInt(mc) * 0.7D);
        } catch (IllegalAccessException e) {
        }
        scale = (float)((double)scale * this.gradualFOVModifier);
        GlStateManager.scale(scale, scale, scale);
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
        return result ? new double[]{(double)screenCoords.get(0), (double)((float)Display.getHeight() - screenCoords.get(1)), (double)screenCoords.get(2)} : null;
    }

}

