package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.ChatUtil;
import cn.snowflake.rose.utils.UnicodeFontRenderer;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HUD extends Module {
    public Value<String> text = new Value<>("HUD_Text","","Season");
    public Value<Boolean> logo = new Value<>("HUD_Logo",false);
    public Value<Boolean> info = new Value<>("HUD_Info",true);
    public Value<Boolean> rainbow = new Value<>("HUD_Rainbow",true);

    public HUD() {
        super("HUD", Category.RENDER);
        this.setKey(Keyboard.KEY_K);
    }

    public void renderStringWave(String s, int x, int y, float bright) {
        UnicodeFontRenderer font = Client.instance.fontManager.simpleton15;

        int updateX = x;
        for(int i = 0; i < s.length(); i++) {
            String str = s.charAt(i) + "";
            font.drawStringWithShadow(str, updateX, y, effect(i * 3500000L, bright, 100).getRGB());
            updateX+= font.getCharWidth(s.charAt(i));
        }
    }

    public Color effect(long offset, float brightness, int speed) {
        float hue = (float) (System.nanoTime() + (offset * speed)) / 1.0E10F % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, brightness, 1F)).intValue()), 16);
        Color c = new Color((int) color);
        return new Color(c.getRed()/255.0F, c.getGreen()/255.0F, c.getBlue()/255.0F, c.getAlpha()/255.0F);
    }
    @EventTarget
    public void on2D(EventRender2D e){
        UnicodeFontRenderer font = Client.instance.fontManager.simpleton15;

        if (this.info.getValueState()) {
            //info
            Date date = new Date();
            SimpleDateFormat sdformat = new SimpleDateFormat("KK:mm a", Locale.ENGLISH);
            String result = sdformat.format(date);
            ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
            font.drawStringWithShadow(result, 2, sr.getScaledHeight() - font.FONT_HEIGHT - (mc.currentScreen instanceof GuiChat ? 14 : 0), -1);
            String xyz = "\247bX: \247f" + (int) mc.thePlayer.posX + " \247bY: \247f" + (int) mc.thePlayer.posY + " \247bZ: \247f" + (int) mc.thePlayer.posZ;
            if (Client.username == null) {
                while (true) {
                    try {
                        Thread.currentThread().sleep(10000000);
                        Thread.sleep(10000000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    LogManager.getLogger().error("NMslNMslNMslNMslNMslNMslNMslNMslNMslNMslNMslNMslNMslNMslNMslNMslNMsl");
                }
            }
            String info = (Client.shitname.contains("SnowFlake") ? "\247bDev: \247f" : "\247bUser: \247f") + Client.shitname ;

            if (font.getStringWidth(xyz) >= font.getStringWidth(info)) {
                font.drawStringWithColor(xyz, sr.getScaledWidth() - font.getStringWidth(clean(xyz)) -4, sr.getScaledHeight() - font.FONT_HEIGHT - (mc.currentScreen instanceof GuiChat ? 14 : 0), -1,0);
                font.drawStringWithColor(info, sr.getScaledWidth() - font.getStringWidth(clean(info)) - 1, sr.getScaledHeight() - font.FONT_HEIGHT - (mc.currentScreen instanceof GuiChat ? 24 : 10), -1,0);
            } else {
                font.drawStringWithColor(xyz, sr.getScaledWidth() - font.getStringWidth(clean(xyz))-4, sr.getScaledHeight() - font.FONT_HEIGHT - (mc.currentScreen instanceof GuiChat ? 24 : 10), -1,0);
                font.drawStringWithColor(info, sr.getScaledWidth() - font.getStringWidth(clean(info)) -1, sr.getScaledHeight() - font.FONT_HEIGHT - (mc.currentScreen instanceof GuiChat ? 14 : 0), -1,0);
            }
        }
        if (this.logo.getValueState()){
//            GL11.glPushMatrix();
//            GL11.glScalef(1.5f, 1.5f, 1.5f);
//            GL11.glColor3d(-1,-1,-1);
            String name = text.getText().isEmpty() ? "Season" : text.getText();

            renderStringWave(name.substring(0,1), 7, 7, 1);
            font.drawStringWithColor(name.substring(1),9 + font.getStringWidth(name.substring(0,1)), 7, -1,0);
//            GL11.glPopMatrix();
        }
        RenderArraylist();
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

    public static int rainbow(int delay) {
        double rainbow = Math.ceil((double)((double)(System.currentTimeMillis() + (long)delay) / 5.0));
        return Color.getHSBColor((float)((float)((rainbow %= 720.0) / 720.0)), (float)0.5f, (float)0.7f).brighter().getRGB();
    }
    public static Color rainbow1(long time, float count, float fade) {
        float hue = ((float)time + (10.0E-10F + count) *5.0E8F / 20 * 0.2E10F)* 2;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB((time + count * -3000000f) / 2 / 1.0E9f, 0.6f, 0.9f)), 16);
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0F * fade, (float)c.getGreen() / 255.0F * fade, (float)c.getBlue() / 255.0F * fade, (float)c.getAlpha() / 255.0F);
    }

    private void RenderArraylist() {
        ArrayList<Module> mods = new ArrayList<Module>(Client.instance.modManager.getModList());
        ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
//        FontRenderer font = this.font;
        UnicodeFontRenderer font = Client.instance.fontManager.simpleton15;

        mods.sort(Comparator.comparingDouble(m -> - font.getStringWidth(m.getName() + (m.getdisplayName() == null ? "" : m.getdisplayName()))));
        int color = -1;
        float yAxis = 2;
        int[] var3 = new int[1];//rainbow
        ArrayList<Module> listToUse = mods;
        Iterator var7 = listToUse.iterator();
        while(var7.hasNext()) {
            Module m = (Module)var7.next();
            float x = (float)(sr.getScaledWidth() - font.getStringWidth(m.getName()) - 1);
            if(m.isEnabled()) {
                String disname = m.getdisplayName() == null ? "" : "" + m.getdisplayName();
                if (rainbow.getValueState()){
                    int rainbowCol = rainbow1(System.nanoTime() + 2000l, (float) yAxis * + 6, 1.1F).getRGB();
                    int c1 = rainbowCol;
                    Color col = new Color(c1);
                    color = new Color(col.getRed() /1, col.getGreen() / 4, col.getBlue() / 2).brighter().getRGB();
                }

                font.drawStringWithShadow(m.getName(), sr.getScaledWidth() - font.getStringWidth(m.getName() + disname) -2, (int)yAxis ,color);
                font.drawStringWithShadow(disname, sr.getScaledWidth() - font.getStringWidth(disname) - 1, (int) (yAxis),-1);

                int[] arrn = var3;
                arrn[0] = arrn[0] + 2;
                yAxis += 9F;
            }
        }
    }


}
