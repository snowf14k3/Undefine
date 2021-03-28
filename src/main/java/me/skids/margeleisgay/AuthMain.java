package me.skids.margeleisgay;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.Season;
import cn.snowflake.rose.utils.auth.HWIDUtils;
import cn.snowflake.rose.utils.auth.HttpUtils;
import cn.snowflake.rose.utils.auth.ShitUtil;
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
		games = new ArrayList<>();
		games.add(new CheckVMProcess());
		games.add(new CheckVMMac());
		games.add(new CheckVMPath());
		games.add(new CheckVersion());
		games.add(new CheckHWID());

//		try {
//			Class<?> noUpdate = Class.forName("moe.mickey.forge.nonupdate.NonUpdate$1");
//			Field whitelist = noUpdate.getDeclaredField("whitelist");
//			whitelist.setAccessible(true);
//			whitelist.get(noUpdate).getClass().getMethod("add",String.class);
//		} catch (Exception e) {
//
//		}


		for (AuthModule game : games) {
			game.onEnable();

			if (game instanceof CheckHWID){
				if (ShitUtil.contains(((CheckHWID) game).getTargetHWID(),(((CheckHWID) game).getSelfHWID()))){
					Client.username = HWIDUtils.getSubString(( ((CheckHWID) game).getTargetHWID()),((CheckHWID) game).getSelfHWID()+"-","-");
					if (Client.username != null){
						Client.shitname = Client.username;
					}
				}
			}


			if(!game.run()) {
				if (game instanceof CheckVersion){
					try {
						Class<?> clazz = Class.forName("javax.swing.JOptionPane");
						String str1 = "\u4f60\u6ca1\u6709\u901a\u8fc7\u7248\u672c\u9a8c\u8bc1";//version check info
						String leastversion = HttpUtils.httpRequest("https://snowflake.coding.net/p/hwid/d/season/git/raw/master/version.txt?download=false");
						Method m = clazz.getMethod("showInputDialog", Component.class, Object.class, Object.class);
						m.invoke(m, null, str1, leastversion);
						Thread.sleep(10000000);
					} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InterruptedException e) {
						LogManager.getLogger().error("NMSL");
					}
					Display.destroy();
					Minecraft.getMinecraft().shutdown();
					Minecraft.getMinecraft().crashed(null);
					Minecraft.getMinecraft().shutdownMinecraftApplet();
					Minecraft.getMinecraft().currentScreen = null;
					Minecraft.getMinecraft().displayHeight = 0;
					Minecraft.getMinecraft().displayWidth = 0;
					Minecraft.getMinecraft().fontRenderer = null;
					return;
				}
				Display.destroy();
				Minecraft.getMinecraft().shutdown();
				Minecraft.getMinecraft().crashed(null);
				Minecraft.getMinecraft().shutdownMinecraftApplet();
				Minecraft.getMinecraft().currentScreen = null;
				Minecraft.getMinecraft().displayHeight = 0;
				Minecraft.getMinecraft().displayWidth = 0;
				Minecraft.getMinecraft().fontRenderer = null;
				while (true) {}
			}

			game.onDisable();
		}
		Season.isAuthed = true;
	}

}
