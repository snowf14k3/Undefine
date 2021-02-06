package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.mods.MOVEMENT.TP;
import cn.snowflake.rose.utils.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;

import java.util.regex.Pattern;

public class CommandTP extends Command {
    public CommandTP(String[] command) {
        super(command);
        this.setArgs("-tp <x> <y> <z>");
    }

    public static boolean isNumeric(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 2) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(this.getArgs()));
        } else {
            String mod = args[1];
            if (mod.contains(":")) {
                String[] xyz = mod.split(":");
                String sx = xyz[0];
                String sy = xyz[1];
                String sz = xyz[2];
                TP.x = Integer.parseInt(sx);
                TP.y = Integer.parseInt(sy);
                TP.z = Integer.parseInt(sz);
                ModManager.getModByName("TP").set(true);
            } else {
                String playername = args[1];

                    if (mc.theWorld.getPlayerEntityByName(playername) != null) {
                        EntityLivingBase entity1 = (EntityLivingBase) mc.theWorld.getPlayerEntityByName(playername);
                        if (entity1.getCommandSenderName().startsWith(playername)) {
                            if (entity1.equals(mc.thePlayer)) {
                                ChatUtil.sendClientMessage(" can not tp youself");
                                return;
                            }
                            TP.x = (int) entity1.posX;
                            TP.y = (int) entity1.posY;
                            TP.z = (int) entity1.posZ;
                            ModManager.getModByName("TP").set(true);
                        } else {
                            System.out.println(playername + " " + entity1.getCommandSenderName());
                            ChatUtil.sendClientMessage(" can not find the player");
                        }
                    }
                return;
            }
        }
    }

}
