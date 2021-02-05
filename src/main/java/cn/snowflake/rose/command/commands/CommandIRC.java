package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.mods.WORLD.IRC;

public class CommandIRC
        extends Command {
    public CommandIRC(String[] commands) {
        super(commands);
        this.setArgs("IRC <Text>");
    }
    public void onCmd(String[] args) {
        if (args.length == 1) {
            return;
        }
        String msg = "";
        for (int i = 1; i < args.length; ++i) {
            msg = String.valueOf((Object)String.valueOf((Object)String.valueOf((Object)msg))) + args[i] + " ";
        }
        if (ModManager.getModByName((String)"IRC").isEnabled()) {
            IRC.sendIRCMessage((String)msg, (boolean)true);
        } else {
            System.out.println("error");
        }
    }
}
