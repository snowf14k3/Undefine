package cn.snowflake.rose.management;


import java.awt.Font;
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
    private HashMap<String, HashMap<Float, UnicodeFontRenderer>> fonts = new HashMap();
    public UnicodeFontRenderer simpleton10;
    public UnicodeFontRenderer simpleton11;
    public UnicodeFontRenderer simpleton12;
    public UnicodeFontRenderer simpleton13;
    public UnicodeFontRenderer simpleton15;
    public UnicodeFontRenderer simpleton20;
    public UnicodeFontRenderer simpleton30;

    public UnicodeFontRenderer robotoregular19;
    
    public UnicodeFontRenderer notif;

    public FontManager() {
        this.simpleton10 = this.getFont("simpleton", 10.0f, true);
        this.simpleton11 = this.getFont("simpleton", 11.0f, true);
        this.simpleton12 = this.getFont("simpleton", 12.0f, true);
        this.simpleton13 = this.getFont("simpleton", 13.0f, true);
        this.simpleton15 = this.getFont("simpleton", 15.0f, true);
        this.robotoregular19 = this.getFont("roboto-regular", 19.0f);
        this.simpleton20 = this.getFont("simpleton", 20.0f, true);
        this.simpleton30 = this.getFont("simpleton", 30.0f, true);
        this.notif = this.getFont("stylesicons", 45.0f);
    }

    public UnicodeFontRenderer getFont(String name, float size, boolean b) {
        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && ((HashMap)this.fonts.get(name)).containsKey(Float.valueOf(size))) {
                return (UnicodeFontRenderer)((HashMap)this.fonts.get(name)).get(Float.valueOf(size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("/assets/fonts/" + name + ".otf");
            Font font = null;
            font = Font.createFont((int)0, (InputStream)inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());

            HashMap map = new HashMap();
            if (this.fonts.containsKey(name)) {
                map.putAll(this.fonts.get(name));
            }
            map.put(Float.valueOf(size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }

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
