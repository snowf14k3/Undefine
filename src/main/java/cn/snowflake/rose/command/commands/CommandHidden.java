package cn.snowflake.rose.command.commands;


import cn.snowflake.rose.Client;
import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.client.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class CommandHidden extends Command {

    public CommandHidden(String[] commands) {
        super(commands);
        this.setArgs("-hidden <module>");
    }

    @Override
    public void onCmd(String[] args) {
        if(args.length < 2) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(this.getArgs()));
        } else {
            String mod = args[1];
            for (Module m : ModManager.getModList()) {
                if(m.getName().equalsIgnoreCase(mod)) {
                    m.setHidden(!m.isHidden());
                    Client.instance.fileMgr.saveHidden();
                    ChatUtil.sendClientMessage("Module " + m.getName() + " " + (!m.isHidden() ? "display" : "hidden"));
                    return;
                }
            }
        }
    }
}

