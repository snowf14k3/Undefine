package cn.snowflake.rose.utils;



import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
/**
 *
 * @author SnowFlake
 *
 * 下午5:53:19 2019
 */
public class UnicodeFontRenderer extends FontRenderer {
    public HashMap<String, Float> widthMap = new HashMap<String, Float>();//储存string的长度
    private final UnicodeFont font;
    public int[] colorCode;

    /**
     *
     * @param awtFont Loaded Font ttf
     */
    public UnicodeFontRenderer(Font awtFont) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        this.font = new UnicodeFont(awtFont);
        this.font.addAsciiGlyphs();
        this.font.addGlyphs(22, 255);
        this.colorCode = new int[32];

        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.font.loadGlyphs();//
        } catch (SlickException exception) {
            throw new RuntimeException((Throwable)exception);//抛出异常
        }
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
    }

    public UnicodeFontRenderer(Font awtFont,boolean chinese) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        this.font = new UnicodeFont(awtFont);
        this.font.addAsciiGlyphs();
        this.font.addGlyphs(22, 35535);//加载的字体数 调试时不用
        this.colorCode = new int[32];

        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.font.loadGlyphs();//
        } catch (SlickException exception) {
            throw new RuntimeException((Throwable)exception);//抛出异常
        }
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
    }
    /**
     *
     * @param string
     * @return 字符串长度
     */
    public int GetLength(String string) {
        if(this.widthMap.containsKey(string)) {
            return this.widthMap.get(string).intValue();
        } else {
            float width = (float)(this.font.getWidth(string) / 2);
            this.widthMap.put(string, Float.valueOf(width));
            return (int)width;
        }
    }

    /**
     *
     * @param red R
     * @param green G
     * @param blue B
     * @param alpha 透明度
     * @return 返回颜色
     */
    public int getColor(int red, int green, int blue, int alpha) {
        byte color = 0;
        int color1 = color | alpha << 24;
        color1 |= red << 16;
        color1 |= green << 8;
        color1 |= blue;
        return color1;
    }



    public double drawString(final String string, double x, double y, final int color) {
        if (string == null) {
            return 0.0;
        }
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        final boolean blend = GL11.glIsEnabled(3042);
        final boolean lighting = GL11.glIsEnabled(2896);
        final boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (lighting) {
            GL11.glDisable(2896);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        x *= 2.0;
        y *= 2.0;
        String name = string;
        if (string.contains("\247")) {
            for (final Character character : string.toCharArray()) {
                if (character.equals('\247')) {
                    name = string.replaceAll("\247" + string.charAt(string.indexOf(character) + 1), "");
                    this.font.getEffects().add(new ColorEffect(new Color(this.getColorCode(string.charAt(string.indexOf(character) + 1)))));
                }
            }
        }
        this.font.drawString((float)x, (float)y, name, new org.newdawn.slick.Color(color));
        if (texture) {
            GL11.glEnable(3553);
        }
        if (lighting) {
            GL11.glEnable(2896);
        }
        if (!blend) {
            GL11.glDisable(3042);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return x;
    }
    /**
     *  @author 玻璃
     *  @param text 输入的文字
     *  @param x posX
     *  @param y posY
     *  @param color 颜色
     *  @param alpha 透明度
     **/
    public int drawStringWithColor(String text, float x, float y, int color, int alpha) {
        String[] array;
        text = "\u00a7r" + text;
        float len = -1.0f;
        for (String str : array = text.split("\u00a7")) {
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = new Color(255, 255, 255).getRGB();
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            this.drawString(str, x + len + 0.5f, y + 0.5f, Colors.BLACK.c);
            this.drawString(str, x + len, y, this.getColor(col.getRed(), col.getGreen(), col.getBlue(), alpha));
            len += (float)(this.GetLength(str) + 1);
        }
        return (int)len;
    }

    public int getColorCode(final char character) {
        final int i = "0123456789abcdef".indexOf(character);
        return (i >= 0 && i < this.colorCode.length) ? this.colorCode[i] : 16777215;
    }
    //    public int drawStringWithShadow(String p_78261_1_, int p_78261_2_, int p_78261_3_, int p_78261_4_) {
//        return this.drawString(p_78261_1_, p_78261_2_, p_78261_3_, p_78261_4_, true);
//    }
    public int drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x + 0.5f, y+ 0.5f, Colors.BLACK.c);
        return (int)this.drawString(text, (float)x, (float)y, color);
    }

    @Override
    public int drawStringWithShadow(String text, int x, int y, int color) {
        this.drawString(text, x + 0.5f, y+ 0.5f,Colors.BLACK.c);
        return (int)drawString(text, (float)x, (float)y, color);
    }
    @Override
    public int getCharWidth(final char c) {
        return this.getStringWidth(Character.toString(c));
    }

    @Override
    public int getStringWidth(final String string) {
        return this.font.getWidth(string) / 2;
    }
    /**
     *
     * @param string
     * @return 获取高度
     */
    public int getStringHeight(String string) {
        return this.font.getHeight(string) / 2;
    }
    /**
     *
     * @param text
     * @param x
     * @param y
     * @param color
     */
    public void drawCenteredString(String text, float x, float y, int color) {
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public void drawBoldString(String text, float x, float y, int color) {
        this.drawString(text, x, y, color);
    }


}