package me.skids.margeleisgay;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.NativeMethod;
import cn.snowflake.rose.Season;
import cn.snowflake.rose.utils.auth.HWIDUtils;
import cn.snowflake.rose.utils.auth.HttpUtils;
import cn.snowflake.rose.utils.auth.ShitUtil;
import cn.snowflake.rose.utils.other.JReflectUtility;
import me.skids.margeleisgay.auth.AuthModule;
import me.skids.margeleisgay.auth.impl.*;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class AuthMain {
	private ArrayList<AuthModule> games;



	public AuthMain() {
		JReflectUtility.setSecurityManager(null);
		games = new ArrayList<>();
		games.add(new CheckVMProcess());
		games.add(new CheckVMMac());
		games.add(new CheckVMPath());
//		games.add(new CheckVersion());
//		games.add(new CheckHWID());


		for (AuthModule game : games) {
			game.onEnable();
//			NativeMethod.method2(game,"onEnable");

			boolean pass = game.run();
			if(!pass) {
				Display.destroy();
				Minecraft.getMinecraft().shutdown();
				Minecraft.getMinecraft().crashed(null);
				Minecraft.getMinecraft().shutdownMinecraftApplet();
				Minecraft.getMinecraft().currentScreen = null;
				Minecraft.getMinecraft().displayHeight = 0;
				Minecraft.getMinecraft().displayWidth = 0;
				Minecraft.getMinecraft().fontRenderer = null;
//				while (true) {}
			}
//			NativeMethod.method1(game);
			game.onDisable();
//			NativeMethod.method2(game,"onDisable");
		}
		Season.isAuthed = true;
	}

}
