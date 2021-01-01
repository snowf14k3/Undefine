package cn.snowflake.rose.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.nio.*;
import java.util.zip.GZIPOutputStream;

import net.minecraft.util.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.texture.*;

import javax.imageio.ImageIO;

public class CatAntiCheatHelper {


    public static byte[] screenshot() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
            final BufferedImage bufferedImage = createScreenshot(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
            ImageIO.write(bufferedImage, "png", gzipOutputStream);
            gzipOutputStream.flush();
            gzipOutputStream.close();
        }
        catch (Exception ex) {}
        return out.toByteArray();
    }
    public static BufferedImage createScreenshot(int width, int height, final Framebuffer framebufferIn) {
        IntBuffer pixelBuffer = (IntBuffer)ReflectionHelper.getPrivateValue((Class)ScreenShotHelper.class, (Object)null, new String[] { "pixelBuffer" });
        final int[] pixelValues = (int[])ReflectionHelper.getPrivateValue((Class)ScreenShotHelper.class, (Object)null, new String[] { "pixelValues" });
        if (OpenGlHelper.isFramebufferEnabled()) {
            width = framebufferIn.framebufferTextureWidth;
            height = framebufferIn.framebufferTextureHeight;
        }
        final int k = width * height;
        if (pixelBuffer == null || pixelBuffer.capacity() < k) {
            pixelBuffer = BufferUtils.createIntBuffer(k);
            ReflectionHelper.setPrivateValue((Class)ScreenShotHelper.class, (Object)null, (Object)new int[k], new String[] { "pixelValues" });
        }
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {
            GL11.glBindTexture(3553, framebufferIn.framebufferTexture);
            GL11.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
        }
        else {
            GL11.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
        }
        pixelBuffer.get(pixelValues);
        TextureUtil.func_147953_a(pixelValues, width, height);
        BufferedImage bufferedimage = null;
        if (OpenGlHelper.isFramebufferEnabled()) {
            bufferedimage = new BufferedImage(framebufferIn.framebufferWidth, framebufferIn.framebufferHeight, 1);
            int i1;
            for (int l = i1 = framebufferIn.framebufferTextureHeight - framebufferIn.framebufferHeight; i1 < framebufferIn.framebufferTextureHeight; ++i1) {
                for (int j1 = 0; j1 < framebufferIn.framebufferWidth; ++j1) {
                    bufferedimage.setRGB(j1, i1 - l, pixelValues[i1 * framebufferIn.framebufferTextureWidth + j1]);
                }
            }
        }
        else {
            bufferedimage = new BufferedImage(width, height, 1);
            bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
        }
        return bufferedimage;
    }

}
