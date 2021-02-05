package cn.snowflake.rose.command.commands;


import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.ChatUtil;

public class CommandToggle extends Command {

    public CommandToggle(String[] commands) {
        super(commands);
        this.setArgs("-toggle <module>");
    }

    @Override
    public void onCmd(String[] args) {
        if(args.length < 2) {
            ChatUtil.sendClientMessage(this.getArgs());
        } else {
            String mod = args[1];
            for (Module m : ModManager.getModList()) {
                if(m.getName().equalsIgnoreCase(mod)) {
                    m.set(!m.isEnabled());
                    ChatUtil.sendClientMessage("Module " + m.getName() + " " + (!m.isEnabled() ? "Disable" : "Toggle"));
                    return;
                }
            }
        }
    }
}
