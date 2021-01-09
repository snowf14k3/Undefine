package cn.snowflake.rose.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;

import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.ui.notification.NotificationPublisher;
import cn.snowflake.rose.ui.notification.NotificationType;

public class Notifications extends Module{

	public Notifications() {
		super("Notifications", Category.RENDER);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		NotificationPublisher.queue("Enable", this.getdisplayName(), NotificationType.INFO);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		NotificationPublisher.queue("Disable", this.getdisplayName(), NotificationType.INFO);
	}
	@EventTarget
	public void on2DRenderEvent(EventRender2D event) {
		NotificationPublisher.draw();
	}
}
