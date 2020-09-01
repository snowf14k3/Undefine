package com.xue;

import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class Utils {
	public static void sendClientMessage(String message) {
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\u00a7b[Season] "+"\u00a7e"+message));
	}
	
	public static void log(String s, Object... args) {
		LogManager.getLogger().log(Level.INFO, String.format(s, args));
	}
	
	public static void logerror(String s, Object... args) {
		LogManager.getLogger().log(Level.ERROR, String.format(s, args));
	}
	
	public static void logException(Throwable e) {
		logerror(ExceptionUtils.getStackTrace(e));
	}
}
