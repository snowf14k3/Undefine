package cn.snowflake.rose.ui.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.utils.AnimationUtil;
import cn.snowflake.rose.utils.RenderUtil;
import cn.snowflake.rose.utils.UnicodeFontRenderer;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NotificationPublisher {
    private static final List<Notification> NOTIFICATIONS = new CopyOnWriteArrayList<>();

    public static void draw() {
        if (NOTIFICATIONS.isEmpty())
            return;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(),Minecraft.getMinecraft().displayWidth,Minecraft.getMinecraft().displayHeight);
        int srScaledHeight = sr.getScaledHeight();
        int scaledWidth = sr.getScaledWidth();
        int y = srScaledHeight - 30;
        UnicodeFontRenderer icon = Client.instance.fontManager.notif;
        for (Notification notification : NOTIFICATIONS) {
        	NotificationTranslate translate = notification.getTranslate();
            int width = notification.getWidth();
            if (!notification.getTimer().elapsed(notification.getTime())) {
                notification.scissorBoxWidth = AnimationUtil.animate(width, notification.scissorBoxWidth, 0.25D);
                translate.interpolate((scaledWidth - width), y, (float) 0.15D);
            } else {
                notification.scissorBoxWidth = AnimationUtil.animate(0.0, notification.scissorBoxWidth, 0.25D);
                if (notification.getWidth() > scaledWidth) {
                    NOTIFICATIONS.remove(notification);
                }
                y += 35;
            }
            float translateX = (float) translate.getX();
            float translateY = (float) translate.getY();
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.doGlScissor((float) (scaledWidth - notification.scissorBoxWidth), translateY, scaledWidth, translateY + 30.0F);
            RenderUtil.drawRect(translateX, translateY, scaledWidth, (translateY + 28.0F), new Color(10, 10, 10, 180).getRGB());
            RenderUtil.drawRect(translateX, (translateY + 28.0F - 1.0F), scaledWidth, (translateY + 28.0F), new Color(10, 10, 10, 180).getRGB());
            RenderUtil.drawRect(translateX, (translateY + 28.0F - 1.0F), translateX + width * (notification.getTime() - notification.getTimer().getElapsedTime()) / notification.getTime(), (translateY + 28.0F), notification.getType().getColor());

            icon.drawString(notification.getType().getColorstr(), translateX + 2.0F, translateY + 4.0F, notification.getType().getColor());
            Client.instance.fontManager.robotoregular19.drawStringWithShadow(notification.getTitle(), (int)(translateX + 28), (int)(translateY + 16), -1);
            //Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(notification.getTitle(), (int)(translateX + 28), (int)(translateY + 4), -1);
            //Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(notification.getContent(), (int)(translateX + 28), (int)(translateY + 16), -1);
            GL11.glDisable(3089);
            GL11.glPopMatrix();
            y -= 35;
        }
    }

    public static void queue(String title, String content, NotificationType type) {
        NOTIFICATIONS.add(new Notification(title, content, type, Minecraft.getMinecraft().fontRenderer));
    }
}
