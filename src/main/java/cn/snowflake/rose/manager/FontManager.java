package cn.snowflake.rose.manager;


import java.awt.Font;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.snowflake.rose.utils.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;

/**
 *
 * @author SnowFlake
 *
 * 1:13:30 2019
 */
public class FontManager {
    //    public static String fucku = FUCKU.class.toString();
    private HashMap<String, HashMap<Float, UnicodeFontRenderer>> fonts = new HashMap();
    public UnicodeFontRenderer simpleton10;
    public UnicodeFontRenderer simpleton11;
    public UnicodeFontRenderer simpleton12;
    public UnicodeFontRenderer simpleton13;
    public UnicodeFontRenderer simpleton15;
    public UnicodeFontRenderer simpleton16;
    public UnicodeFontRenderer simpleton17;
    public UnicodeFontRenderer simpleton18;
    public UnicodeFontRenderer simpleton20;
    public UnicodeFontRenderer simpleton25;
    public UnicodeFontRenderer simpleton30;
    public UnicodeFontRenderer simpleton35;
    public UnicodeFontRenderer simpleton40;
    public UnicodeFontRenderer simpleton45;
    public UnicodeFontRenderer simpleton50;
    public UnicodeFontRenderer simpleton70;



    /*
     * Init
     */
    public FontManager() {
        this.simpleton10 = this.getFont("simpleton", 10.0f, true);
        this.simpleton11 = this.getFont("simpleton", 11.0f, true);
        this.simpleton12 = this.getFont("simpleton", 12.0f, true);
        this.simpleton13 = this.getFont("simpleton", 13.0f, true);
        this.simpleton15 = this.getFont("simpleton", 15.0f, true);
        this.simpleton16 = this.getFont("simpleton", 16.0f, true);
        this.simpleton17 = this.getFont("simpleton", 17.0f, true);
        this.simpleton18 = this.getFont("simpleton", 18.0f, true);
        this.simpleton20 = this.getFont("simpleton", 20.0f, true);
        this.simpleton25 = this.getFont("simpleton", 25.0f, true);
        this.simpleton30 = this.getFont("simpleton", 30.0f, true);
        this.simpleton35 = this.getFont("simpleton", 35.0f, true);
        this.simpleton40 = this.getFont("simpleton", 40.0f, true);
        this.simpleton45 = this.getFont("simpleton", 45.0f, true);
        this.simpleton50 = this.getFont("simpleton", 50.0f, true);
        this.simpleton70 = this.getFont("simpleton", 70.0f, true);


    }
    /**
     *  ����otf����
     * @param name
     * @param size
     * @param b
     * @return
     */
    public UnicodeFontRenderer getFont(String name, float size, boolean b) {
//        if(!fucku.split(" ")[1].equals("p.ew.e.FUCKU")) {
//            System.out.println(fucku.split(" ")[1]);
//            Minecraft.getMinecraft().shutdown();
//            System.exit(0);
//        }
        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && ((HashMap)this.fonts.get(name)).containsKey(Float.valueOf((float)size))) {
                return (UnicodeFontRenderer)((HashMap)this.fonts.get(name)).get(Float.valueOf((float)size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("/assets/fonts/" + name + ".otf");
            Font font = null;
            font = Font.createFont((int)0, (InputStream)inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());

            HashMap map = new HashMap();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map)this.fonts.get(name));
            }
            map.put(Float.valueOf((float)size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }

    /**
     * ttf
     * @param name
     * @param size
     * @return
     */
    public UnicodeFontRenderer getFont(String name, float size) {
        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && ((HashMap)this.fonts.get(name)).containsKey(Float.valueOf((float)size))) {
                return (UnicodeFontRenderer)((HashMap)this.fonts.get(name)).get(Float.valueOf((float)size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("/assets/fonts/" + name + ".ttf");

            Font font = null;
            font = Font.createFont((int)0, (InputStream)inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());

            HashMap map = new HashMap();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map)this.fonts.get(name));
            }
            map.put(Float.valueOf((float)size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }

}
