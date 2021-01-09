package cn.snowflake.rose.ui.notification;

import java.awt.*;

public enum  NotificationType {
    SUCCESS(new Color(100, 255, 100).getRGB(), "H"),
    INFO(new Color(100, 100, 255).getRGB(), "J"),
    ERROR(new Color(255, 100, 100).getRGB(), "I"),
    WARNING(new Color(255, 211, 53).getRGB(), "K");

    private final int color;
    private final String colorstr;

    NotificationType(int color, String str) {
        this.color = color;
        this.colorstr = str;
    }

    public final int getColor() {
        return this.color;
    }

    public final String getColorstr() {
        return colorstr;
    }
}
