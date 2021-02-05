package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.injection.ClientLoader;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HUD extends Module {
    public Value<String> text = new Value<>("HUD_Text","","Season");
    public Value<Boolean> logo = new Value<>("HUD_Logo",true);
    public Value<Boolean> info = new Value<>("HUD_Info",false);
    public Value<String> rainbow = new Value<>("HUD","ColorMode",0);
    public static Value<Double> rainbowindex = new Value<Double>("HUD_rainbow", 1.0, 1.0, 20.0, 1.0);

    public HUD() {
        super("HUD", Category.RENDER);
        this.rainbow.addValue("Gray");
        this.rainbow.addValue("Rainbow");
        this.rainbow.addValue("White");
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
        UnicodeFontRenderer font = Client.instance.fontManager.simpleton12;
        if (this.info.getValueState()) {
            //info

            ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);

            String xyz = "\247cX: \247f" + (int) mc.thePlayer.posX + " \247cY: \247f" + (int) mc.thePlayer.posY + " \247cZ: \247f" + (int) mc.thePlayer.posZ;
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
            String info = (Client.shitname.contains("SnowFlake") ? "\247cDev: \247f" : Client.shitname.contains("Chentg") ? "\247cHelper: \247f" : "\247cUser: \247f") + Client.shitname.substring(0,Client.shitname.length() -1) ;


            font.drawStringWithColor(xyz, sr.getScaledWidth() - font.getStringWidth(clean(xyz))-4, sr.getScaledHeight() - font.FONT_HEIGHT - (mc.currentScreen instanceof GuiChat ? 20 : 5), -1,0);
            font.drawStringWithColor(info, sr.getScaledWidth() - font.getStringWidth(clean(info)) -3, sr.getScaledHeight() - font.FONT_HEIGHT - (mc.currentScreen instanceof GuiChat ? 14 : 0), -1,0);
        }
        if (this.logo.getValueState()){
//            GL11.glPushMatrix();
//            GL11.glScalef(1.5f, 1.5f, 1.5f);
//            GL11.glColor3d(-1,-1,-1);
            String name = text.getText().isEmpty() ? "Season" : text.getText();
            Date date = new Date();
            SimpleDateFormat sdformat = new SimpleDateFormat("KK:mm a", Locale.ENGLISH);
            String result = sdformat.format(date);
            String server = mc.isSingleplayer() ? "local_server" : mc.func_147104_D().serverIP.toLowerCase();//getCurrentServerData
            String text = null;
            String text2 = null;
            try {
                text2 = JReflectUtility.getField(mc.getClass(), ClientLoader.runtimeDeobfuscationEnabled ? "field_71470_ab": "debugFPS",true).getInt(mc) + " fps | " +result +" | " + server;
                text = this.text.getText()+"\2472sense\247f | " +text2;
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
            float width = Client.instance.fontManager.simpleton11.getStringWidth(text) + 6;
            int height = Client.instance.fontManager.simpleton11.FONT_HEIGHT + 9;
            int posX = 2;
            int posY = 2;
            RenderUtil.drawRect(posX, posY, posX + width + 2, posY + height, new Color(5, 5, 5, 255).getRGB());
            RenderUtil.drawBorderedRect(posX + .5, posY + .5, posX + width + 1.5, posY + height - .5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
            RenderUtil.drawBorderedRect(posX + 2, posY + 2, posX + width, posY + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
            RenderUtil.drawRect(posX + 2.5, posY + 2.5, posX + width - .5, posY + 4.5, new Color(9, 9, 9, 255).getRGB());

            RenderUtil.drawGradientSideways(4, posY + 3, 4 + (width / 3), posY + 4,
                    rainbow(100),
                    rainbow(1000));
            RenderUtil.drawGradientSideways(4 + (width / 3), posY + 3, 4 + ((width / 3) * 2), posY + 4,
                    rainbow(1000),
                    rainbow(1900));
            RenderUtil.drawGradientSideways(4 + ((width / 3) * 2), posY + 3, ((width / 3) * 3) + 1, posY + 4,
                    rainbow(1900),
                    rainbow(2800));


            Client.instance.fontManager.simpleton11.drawStringWithColor(text, 4 + posX, 5 + posY, -1,0);

//            renderStringWave(name.substring(0,1), 7, 7, 1);
//            font.drawStringWithColor(name.substring(1),9 + font.getStringWidth(name.substring(0,1)), 7, -1,0);
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
    public static Color rainbow(long time, float count, float fade) {
        float hue = ((float)time + (1.0F + count) * 2.0E8F) /(HUD.rainbowindex.getValueState().intValue() * 1.0E9F) % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 0.5F, 1.0F)).intValue()), 16);
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0F * fade, (float)c.getGreen() / 255.0F * fade, (float)c.getBlue() / 255.0F * fade, (float)c.getAlpha() / 255.0F);
    }

    private void RenderArraylist() {
        ArrayList<Module> mods = new ArrayList<>(ModManager.getModList());

        ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
//        FontRenderer font = this.font;
        UnicodeFontRenderer arraylistfont = Client.instance.fontManager.robotoregular19;
        mods.removeIf(Module::isHidden);
        mods.sort(Comparator.comparingDouble(m1 -> - arraylistfont.getStringWidth(m1.getRenderName() + (m1.getdisplayName() == null ? "" : m1.getdisplayName()))));
        int countMod = 0;
        int color = -1;
        float yAxis = 0;
        int[] var3 = new int[1];//rainbow
        for (Module m2 : mods) {
            if (m2.hidden)continue;
            int rainbowCol2 = rainbow(System.nanoTime(), (float) countMod, 1).getRGB();
            ++countMod;
            int c = rainbowCol2;
            Color col2 = new Color(c);
            int color2 = (new Color((float) col2.getRed() / 255.0F, (float) col2.getGreen() / 255.0F,(float) col2.getBlue() / 255.0F, 1)).getRGB();
            int Ranbow = (new Color(col2.getGreen() / 255.0F, col2.getGreen() / 255.0F, col2.getGreen() / 255.0F))
                    .getRGB();

            float x = (float)(sr.getScaledWidth() - arraylistfont.getStringWidth(m2.getName()) - 1);
            if(m2.isEnabled()) {
                String disname = m2.getdisplayName() == null ? "" : "" + m2.getdisplayName();
                switch (rainbow.getModeName()){
                    case "Rainbow" :
                        int rainbowCol = rainbow1(System.nanoTime() + 4400l, (float) yAxis * +5, 1.1F).getRGB();
                        int c1 = rainbowCol;
                        Color col = new Color(c1);
                        color = new Color(col.getRed(), col.getGreen(), col.getBlue()).brighter().getRGB();
                        break;
                    case "Gray":
                        color = (new Color(col2.getRed() / 1, col2.getRed() / 1, col2.getRed() / 1))
                                .getRGB();
                        break;

                    case "White":
                        color = -1;
                        break;
                }

                arraylistfont.drawStringWithShadow( m2.getRenderName(), sr.getScaledWidth() - arraylistfont.getStringWidth(m2.getRenderName() + disname) -3, yAxis ,color);
                arraylistfont.drawStringWithShadow(disname, sr.getScaledWidth() - arraylistfont.getStringWidth(disname) - 1, yAxis,new Color(166,168,168).getRGB());

                int[] arrn = var3;
                arrn[0] = arrn[0] + 2;
                yAxis += 12F;
            }

        }
    }


}