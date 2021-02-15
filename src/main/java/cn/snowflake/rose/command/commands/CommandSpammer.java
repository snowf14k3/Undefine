package cn.snowflake.rose.command.commands;


import cn.snowflake.rose.Client;
import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.mod.mods.WORLD.Spammer;
import cn.snowflake.rose.utils.client.ChatUtil;

public class CommandSpammer extends Command {

    public CommandSpammer(String[] commands) {
        super(commands);
        this.setArgs("-sp <text>");
    }

    public void onCmd(String[] args) {
        if (args.length != 2) {
            ChatUtil.sendClientMessage(this.getArgs());
        } else {
            args[1] = args[1].replace("&", "\247");
            if (args[1].equalsIgnoreCase("load")){
                Client.instance.fileMgr.loadSpammerText();
                return;
            }
            Spammer.customtext = args[1];
            Client.instance.fileMgr.saveSpammerText();
            ChatUtil.sendClientMessage("Spammer customText Changed to " + args[1]);
            super.onCmd(args);
        }
    }

}
