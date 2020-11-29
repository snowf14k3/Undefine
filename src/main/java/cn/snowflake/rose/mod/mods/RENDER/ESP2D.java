package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ESP2D extends Module {
    public static Value<Double> r = new Value<Double>("ESP2D_Red", 255.0D, 0.0D,255.0D, 5.0D);
    public static Value<Double> g = new Value<Double>("ESP2D_Green", 0.0D, 0.0D, 255.0D, 5.0D);
    public static Value<Double> b = new Value<Double>("ESP2D_Blue", 0.0D, 0.0D, 255.0D, 5.0D);
    public static Value<String> mode = new Value("ESP2D", "Box", 0);
    public Value<Boolean> player = new Value("ESP2D_Player", true);
    public Value<Boolean> invis = new Value("ESP2D_Invisibles", true);
    public Value<Boolean> NAME = new Value("ESP2D_Name", true);
    public Value<Boolean> ITEM = new Value("ESP2D_Item", true);
    public Value<Boolean> armor = new Value<Boolean>("ESP2D_Armor", false);

    private Map<EntityLivingBase, double[]> entityConvertedPointsMap = new HashMap<EntityLivingBase, double[]>();
    public ESP2D() {
        super("ESP2D","ESP2D",  Category.RENDER);
        ArrayList<String> settings = new ArrayList();
        this.mode.mode.add("Box");
        this.mode.mode.add("Corner A");
        this.mode.mode.add("Corner B");
    }
    @EventTarget
    public void onRender(EventRender3D event) {
        try {
            this.updatePositions();
        }
        catch (Exception exception) {
        }
    }



    @EventTarget
    public void onRender2D(EventRender2D event) {
        boolean hovering;
        setDisplayName(this.mode.getModeName());
        GlStateManager.pushMatrix();
        for (Entity entity : this.entityConvertedPointsMap.keySet()) {
            boolean shouldRender;
            EntityLivingBase ent = (EntityLivingBase)entity;

            double[] renderPositions = this.entityConvertedPointsMap.get(ent);
            double[] renderPositionsBottom = new double[]{renderPositions[4], renderPositions[5], renderPositions[6]};
            double[] renderPositionsX = new double[]{renderPositions[7], renderPositions[8], renderPositions[9]};
            double[] renderPositionsX1 = new double[]{renderPositions[10], renderPositions[11], renderPositions[12]};
            double[] renderPositionsZ = new double[]{renderPositions[13], renderPositions[14], renderPositions[15]};
            double[] renderPositionsZ1 = new double[]{renderPositions[16], renderPositions[17], renderPositions[18]};
            double[] renderPositionsTop1 = new double[]{renderPositions[19], renderPositions[20], renderPositions[21]};
            double[] renderPositionsTop2 = new double[]{renderPositions[22], renderPositions[23], renderPositions[24]};
            boolean bl = shouldRender = renderPositions[3] > 0.0 && renderPositions[3] <= 1.0 && renderPositionsBottom[2] > 0.0 && renderPositionsBottom[2] <= 1.0 && renderPositionsX[2] > 0.0 && renderPositionsX[2] <= 1.0 && renderPositionsX1[2] > 0.0 && renderPositionsX1[2] <= 1.0 && renderPositionsZ[2] > 0.0 && renderPositionsZ[2] <= 1.0 && renderPositionsZ1[2] > 0.0 && renderPositionsZ1[2] <= 1.0 && renderPositionsTop1[2] > 0.0 && renderPositionsTop1[2] <= 1.0 && renderPositionsTop2[2] > 0.0 && renderPositionsTop2[2] <= 1.0;
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            try {
                double[] xValues = new double[]{renderPositions[0], renderPositionsBottom[0], renderPositionsX[0], renderPositionsX1[0], renderPositionsZ[0], renderPositionsZ1[0], renderPositionsTop1[0], renderPositionsTop2[0]};
                double[] yValues = new double[]{renderPositions[1], renderPositionsBottom[1], renderPositionsX[1], renderPositionsX1[1], renderPositionsZ[1], renderPositionsZ1[1], renderPositionsTop1[1], renderPositionsTop2[1]};
                double x = renderPositions[0];
                double y = renderPositions[1];
                double endx = renderPositionsBottom[0];
                double endy = renderPositionsBottom[1];

                for (double bdubs : xValues) {
                    if (bdubs >= x) continue;
                    x = bdubs;
                }
                for (double bdubs : xValues) {
                    if (bdubs <= endx) continue;
                    endx = bdubs;
                }
                for (double bdubs : yValues) {
                    if (bdubs >= y) continue;
                    y = bdubs;
                }
                for (double bdubs : yValues) {
                    if (bdubs <= endy) continue;
                    endy = bdubs;
                }
                double xDiff = (endx - x) / 6.0;
                double x2Diff = (endx - x) / (double)6;
                double yDiff =  xDiff ;
                int color = Colors.getColor(255, 255);
                if(ent.hurtTime > 0) {
                    color = Colors.getColor(255,0,0, 255);
                } else if(ent.isInvisible()) {
                    color = Colors.getColor(255,255,0, 255);
                }else {
//                    color = -7924716;
                    color = new Color(r.getValueState().intValue(),g.getValueState().intValue(),b.getValueState().intValue()).getRGB();
                }
                if(mode.isCurrentMode("Box")) {
                    RenderUtil.rectangleBordered(x + 0.5, y + 0.5, endx - 0.5, endy - 0.5, 1.0, Colors.getColor(0, 0, 0, 0), color);
                    RenderUtil.rectangleBordered(x - 0.5, y - 0.5, endx + 0.5, endy + 0.5, 1.0, Colors.getColor(0, 0), Colors.getColor(0, 150));
                    RenderUtil.rectangleBordered(x + 1.5, y + 1.5, endx - 1.5, endy - 1.5, 1.0, Colors.getColor(0, 0), Colors.getColor(0, 150));
                }
                if(this.mode.isCurrentMode("Corner B")) {
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
                if(mode.isCurrentMode("Corner A")) {
                }

                float diff1;

                float var1 = (float) ((endy - y) / 4.0F);
                ItemStack stack = ((EntityPlayer)ent).getEquipmentInSlot(4);
                if (stack != null) {
                    if(armor.getValueState().booleanValue()) {
                        RenderUtil.rectangleBordered((double)(endx + 1.0F), (double)(y + 1.0F), (double)(endx + 5.0F), (double)(y + var1), 1.0D, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                    }
                    diff1 = (float) (y + var1 - 1.0F - (y + 2.0F));
                    double percent = 1.0D - (double)stack.getItemDamage() / (double)stack.getMaxDamage();
                    if(armor.getValueState().booleanValue()) {
                        RenderUtil.rectangle((double)(endx + 2.0F), (double)(y + var1 - 1.0F), (double)(endx + 4.0F), (double)(y + var1 - 1.0F) - (double)diff1 * percent, Colors.getColor(219,79,237));
//                        mc.fontRenderer.drawStringWithShadow(stack.getDisplayName() + "", (int)(endx + 7.0F), (int)(y + var1 - 1.0F - diff1 / 2.0F)-mc.fontRenderer.FONT_HEIGHT, -1);
                        mc.fontRenderer.drawStringWithShadow(stack.getMaxDamage() - stack.getItemDamage() + "", (int)(endx + 7.0F), (int)(y + var1 - 1.0F - diff1 / 2.0F), -1);
                    }
                }

                ItemStack stack2 = ((EntityPlayer)ent).getEquipmentInSlot(3);
                if (stack2 != null) {
                    if(armor.getValueState().booleanValue()) {
                        RenderUtil.rectangleBordered((double)(endx + 1.0F), (double)(y + var1), (double)(endx + 5.0F), (double)(y + var1 * 2.0F), 1.0D, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                    }
                    diff1 = (float) (y + var1 * 2.0F - (y + var1 + 2.0F));
                    double percent = 1.0D - (double)stack2.getItemDamage() * 1.0D / (double)stack2.getMaxDamage();
                    if(armor.getValueState().booleanValue()) {
                        RenderUtil.rectangle((double)(endx + 2.0F), (double)(y + var1 * 2.0F), (double)(endx + 4.0F), (double)(y + var1 * 2.0F) - (double)diff1 * percent, Colors.getColor(219,79,237));
//                        mc.fontRenderer.drawStringWithShadow(stack2.getDisplayName() + "", (int)(endx + 7.0F), (int)(y + var1 * 2.0F - diff1 / 2.0F) -mc.fontRenderer.FONT_HEIGHT, -1);
                        mc.fontRenderer.drawStringWithShadow(stack2.getMaxDamage() - stack2.getItemDamage() + "", (int)(endx + 7.0F), (int)(y + var1 * 2.0F - diff1 / 2.0F), -1);
                    }
                }

                ItemStack stack3 = ((EntityPlayer)ent).getEquipmentInSlot(2);
                if (stack3 != null) {
                    if(armor.getValueState().booleanValue()) {
                        RenderUtil.rectangleBordered((double)(endx + 1.0F), (double)(y + var1 * 2.0F), (double)(endx + 5.0F), (double)(y + var1 * 3.0F), 1.0D, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                    }
                    diff1 = (float) (y + var1 * 3.0F - (y + var1 * 2.0F + 2.0F));
                    double percent = 1.0D - (double)stack3.getItemDamage() * 1.0D / (double)stack3.getMaxDamage();
                    if(armor.getValueState().booleanValue()) {
                        RenderUtil.rectangle((double)(endx + 2.0F), (double)(y + var1 * 3.0F), (double)(endx + 4.0F), (double)(y + var1 * 3.0F) - (double)diff1 * percent, Colors.getColor(219,79,237));
//                        mc.fontRenderer.drawStringWithShadow(stack2.getDisplayName() + "", (int)(endx + 7.0F), (int)(y + var1 * 3.0F - diff1 / 2.0F) - mc.fontRenderer.FONT_HEIGHT, -1);
                        mc.fontRenderer.drawStringWithShadow(stack3.getMaxDamage() - stack3.getItemDamage() + "", (int)(endx + 7.0F), (int)(y + var1 * 3.0F - diff1 / 2.0F), -1);
                    }
                }

                ItemStack stack4 = ((EntityPlayer)ent).getEquipmentInSlot(1);
                float health1;
                if (stack4 != null) {
                    if(armor.getValueState().booleanValue()) {
                        RenderUtil.rectangleBordered((double)(endx + 1.0F), (double)(y + var1 * 3.0F), (double)(endx + 5.0F), (double)(y + var1 * 4.0F), 1.0D, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                    }
                    health1 = (float) (y + var1 * 4.0F - (y + var1 * 3.0F + 2.0F));
                    double percent = 1.0D - (double)stack4.getItemDamage() * 1.0D / (double)stack4.getMaxDamage();
                    if(armor.getValueState().booleanValue()) {
                        RenderUtil.rectangle((double)(endx + 2.0F), (double)(y + var1 * 4.0F - 1.0F), (double)(endx + 4.0F), (double)(y + var1 * 4.0F) - (double)health1 * percent, Colors.getColor(219,79,237));
//                        mc.fontRenderer.drawStringWithShadow(stack2.getDisplayName() + "", (int)(endx + 7.0F), (int)(y + var1 * 4.0F - health1 / 2.0F)- mc.fontRenderer.FONT_HEIGHT, -1);
                        mc.fontRenderer.drawStringWithShadow(stack4.getMaxDamage() - stack4.getItemDamage() + "", (int)(endx + 7.0F), (int)(y + var1 * 4.0F - health1 / 2.0F), -1);
                    }
                }

                if (this.NAME.getValueState().booleanValue()){
                    drawCustomString(clean(ent.getCommandSenderName()), (float)(x), (float)y - 12.0F, -1);
                }
                if (this.ITEM.getValueState().booleanValue()){
                    ItemStack current = ((EntityPlayer)ent).getHeldItem();
                    String str = current != null ? clean(current.getDisplayName()) : "";
                    drawCustomString(str,(float)(x), (float)endy + 2, -1);
                }
//                if (this.HEALTH.getValueState()) {
                    float health = ent.getHealth();
                    float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
                    Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                    float progress = health / ent.getMaxHealth();
                    Color customColor = health >= 0.0f ? blendColors(fractions, colors, progress).brighter() : Color.RED;
                    double difference = y - endy + 0.5;
                    double healthLocation = endy + difference * (double)progress;
                    RenderUtil.rectangleBordered(x - 6.5, y - 0.5, x - 3.5, endy, 1.0, Colors.getColor(0, 100), Colors.getColor(0, 150));
                    RenderUtil.rectangle(x - 5.5, endy - 1.0, x - 4.5, healthLocation, customColor.getRGB());
//                        if (- difference > 50.0) {
//                            for (int i = 1; i < 10; ++i) {
//                                double dThing = difference / 10.0 * (double)i;
//                                RenderUtil.rectangle(x - 6.5, endy - 0.5 + dThing, x - 2.5, endy - 0.5 + dThing - 1.0, Colors.getColor(0));
//                            }
//                        }
//                    if ((int)getIncremental(progress * 100.0f, 1.0) <= 40) {

                        String nigger = "" + (int)getIncremental(health * 5.0f, 1.0) + "HP \2474\u2764";
                        //  Client.verdana10.drawStringWithShadow(nigger, (float)(x - 6.0 - (double)(Client.verdana16.getWidth(nigger) * 2.0f)) / 2.0f, ((float)((int)healthLocation) + Client.fss.getHeight(nigger) / 2.0f) / 2.0f, -1);

                        drawCustomString(""+nigger, (float)(x - 6.0 - (double)(mc.fontRenderer.getStringWidth(nigger))), ((float)((int)healthLocation) + mc.fontRenderer.getStringWidth(nigger) / 2.0f) - 10, -1);
//                    }

//                }
            }
            catch (Exception xValues) {
                // empty catch block

            }

            GlStateManager.popMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }

        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
        RenderUtil.rectangle(0.0, 0.0, 0.0, 0.0, -1);
    }

    public static String clean(String text) {
        String cleaned = text.replaceAll("§a", "");
        cleaned = cleaned.replaceAll("§b", "");
        cleaned = cleaned.replaceAll("§c", "");
        cleaned = cleaned.replaceAll("§d", "");
        cleaned = cleaned.replaceAll("§e", "");
        cleaned = cleaned.replaceAll("§f", "");
        cleaned = cleaned.replaceAll("§0", "");
        cleaned = cleaned.replaceAll("§1", "");
        cleaned = cleaned.replaceAll("§2", "");
        cleaned = cleaned.replaceAll("§3", "");
        cleaned = cleaned.replaceAll("§4", "");
        cleaned = cleaned.replaceAll("§5", "");
        cleaned = cleaned.replaceAll("§6", "");
        cleaned = cleaned.replaceAll("§7", "");
        cleaned = cleaned.replaceAll("§8", "");
        cleaned = cleaned.replaceAll("§9", "");
        return cleaned;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0D / inc;
        return (double)Math.round(val * one) / one;
    }

    public static void drawCustomString(String str, float x, float y, int color) {
//        GL11.glPushMatrix();
//        GL11.glScaled(0.5D, 0.5D, 0.5D);
        Minecraft.getMinecraft().fontRenderer.drawString(str, (int)(x - 1.0F), (int)y, 0);
        Minecraft.getMinecraft().fontRenderer.drawString(str, (int)x, (int)(y - 1.0F), 0);
        Minecraft.getMinecraft().fontRenderer.drawString(str, (int)x, (int)(y + 1.0F), 0);
        Minecraft.getMinecraft().fontRenderer.drawString(str, (int)(x + 1.0F), (int)y, 0);
        Minecraft.getMinecraft().fontRenderer.drawString(str, (int)(x + 1.0F), (int)(y - 1.0F), 0);
        Minecraft.getMinecraft().fontRenderer.drawString(str, (int)(x - 1.0F), (int)(y - 1.0F), 0);
        Minecraft.getMinecraft().fontRenderer.drawString(str, (int)(x - 1.0F), (int)(y + 1.0F), 0);
        Minecraft.getMinecraft().fontRenderer.drawString(str, (int)(x + 1.0F), (int)(y + 1.0F), 0);
        Minecraft.getMinecraft().fontRenderer.drawString(str, (int)x, (int)y, color);
//        GL11.glPopMatrix();
    }


    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        Color color = null;
        if (fractions == null) throw new IllegalArgumentException("Fractions can't be null");
        if (colors == null) throw new IllegalArgumentException("Colours can't be null");
        if (fractions.length != colors.length) throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        int[] indicies = getFractionIndicies(fractions, progress);
        float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
        Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
        float max = range[1] - range[0];
        float value = progress - range[0];
        float weight = value / max;
        return blend(colorRange[0], colorRange[1], 1.0f - weight);
    }
    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color = null;
        try {
            color = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color;
    }
    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    public static int healthColor(float health) {
        if ((double)health <= 20.0D) {
            return -57823;
        } else if ((double)health <= 15.0D) {
            return -48095;
        } else if ((double)health <= 10.0D) {
            return -22239;
        } else if ((double)health <= 5.0D) {
            return -9844389;
        } else {
            return (double)health < 1.5D ? -14428875 : -1;
        }
    }
    private void updatePositions() {
        this.entityConvertedPointsMap.clear();
        

        for (Object e2 : mc.theWorld.getLoadedEntityList()) {
            EntityPlayer ent;
            if (!(e2 instanceof EntityPlayer) || (ent = (EntityPlayer)e2) == mc.thePlayer) continue;
            double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double) JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosX + 0.36;
            double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosY;
            double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosZ + 0.36;
            double topY = y += (double)ent.height + 0.15;
            double[] convertedPoints = RenderUtil.convertTo2D(x, y, z);
            double[] convertedPoints22 = RenderUtil.convertTo2D(x - 0.36, y, z - 0.36);
            double xd = 0.0;
            if (convertedPoints22[2] < 0.0 || convertedPoints22[2] >= 1.0) continue;
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosX - 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosZ - 0.36;
            double[] convertedPointsBottom = RenderUtil.convertTo2D(x, y, z);
            y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosY - 0.05;
            double[] convertedPointsx = RenderUtil.convertTo2D(x, y, z);
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosX - 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosZ + 0.36;
            double[] convertedPointsTop1 = RenderUtil.convertTo2D(x, topY, z);
            double[] convertedPointsx1 = RenderUtil.convertTo2D(x, y, z);
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosX + 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosZ + 0.36;
            double[] convertedPointsz = RenderUtil.convertTo2D(x, y, z);
            x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosX + 0.36;
            z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)JReflectUtility.getRenderPartialTicks() - RenderManager.instance.viewerPosZ - 0.36;
            double[] convertedPointsTop2 = RenderUtil.convertTo2D(x, topY, z);
            double[] convertedPointsz1 = RenderUtil.convertTo2D(x, y, z);
            this.entityConvertedPointsMap.put(ent, new double[]{convertedPoints[0], convertedPoints[1], xd, convertedPoints[2], convertedPointsBottom[0], convertedPointsBottom[1], convertedPointsBottom[2], convertedPointsx[0], convertedPointsx[1], convertedPointsx[2], convertedPointsx1[0], convertedPointsx1[1], convertedPointsx1[2], convertedPointsz[0], convertedPointsz[1], convertedPointsz[2], convertedPointsz1[0], convertedPointsz1[1], convertedPointsz1[2], convertedPointsTop1[0], convertedPointsTop1[1], convertedPointsTop1[2], convertedPointsTop2[0], convertedPointsTop2[1], convertedPointsTop2[2]});
        }
    }
    private String getColor(int level) {
        if (level == 2) {
            return "\u00a7a";
        }
        if (level == 3) {
            return "\u00a73";
        }
        if (level == 4) {
            return "\u00a74";
        }
        if (level >= 5) {
            return "\u00a76";
        }
        return "\u00a7f";
    }
}
