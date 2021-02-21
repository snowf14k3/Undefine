package me.skids.margeleisgay;

import cn.snowflake.rose.Season;
import me.skids.margeleisgay.auth.AuthModule;
import me.skids.margeleisgay.auth.impl.CheckHWID;
import me.skids.margeleisgay.auth.impl.CheckVMMac;
import me.skids.margeleisgay.auth.impl.CheckVMPath;
import me.skids.margeleisgay.auth.impl.CheckVMProcess;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;

public class AuthMain {
	private ArrayList<AuthModule> games;
	
	public AuthMain() {
		games = new ArrayList<>();
		games.add(new CheckVMProcess());
		games.add(new CheckVMMac());
		games.add(new CheckVMPath());
		games.add(new CheckHWID());
		for (AuthModule game : games) {
			game.onEnable();
			if(!game.run()) {
				Display.destroy();
				Minecraft.getMinecraft().shutdown();
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
