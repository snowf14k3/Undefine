package cn.snowflake.rose.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {
    public Minecraft mc = Minecraft.getMinecraft();

    public static void sendClientMessage(String string) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\u00a7b[Season] "+"\u00a7e"+string));
    }
}
