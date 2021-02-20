package me.skids.margeleisgay;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;

import cn.snowflake.rose.Client;
import me.skids.margeleisgay.auth.AuthModule;
import me.skids.margeleisgay.auth.impl.CheckHWID;
import me.skids.margeleisgay.auth.impl.CheckVMMac;
import me.skids.margeleisgay.auth.impl.CheckVMPath;
import me.skids.margeleisgay.auth.impl.CheckVMProcess;
import net.minecraft.client.Minecraft;

public class AuthMain {
	private ArrayList<AuthModule> games;
	
	public AuthMain() {
		games = new ArrayList<>();
		games.add(new CheckVMProcess());
		games.add(new CheckVMMac());
		games.add(new CheckVMPath());
		games.add(new CheckHWID());
	}
	
	public void setup() {
		for (AuthModule game : games) {
			LogManager.getLogger().log(Level.INFO, game.getClass().getCanonicalName());
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
		Client.isAuthed = true;
	}
}
