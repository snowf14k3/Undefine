package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.mods.MOVEMENT.Teleport;
import cn.snowflake.rose.utils.client.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;

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
                Teleport.x = Integer.parseInt(sx);
                Teleport.y = Integer.parseInt(sy);
                Teleport.z = Integer.parseInt(sz);
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
                            Teleport.x = (int) entity1.posX;
                            Teleport.y = (int) entity1.posY;
                            Teleport.z = (int) entity1.posZ;
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
