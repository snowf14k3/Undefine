package cn.snowflake.rose.ui.notification;

import cn.snowflake.rose.utils.AnimationUtil;

public class NotificationTranslate {
    private double x;

    private double y;

    public NotificationTranslate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final void interpolate(double targetX, double targetY, double smoothing) {
        this.x = AnimationUtil.animate(targetX, this.x, smoothing);
        this.y = AnimationUtil.animate(targetY, this.y, smoothing);
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
