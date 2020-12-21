package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.manager.ModManager;

public class CommandPlugins extends Command {
    public CommandPlugins(String[] commands) {
        super(commands);
        this.setArgs("-plugins");
    }

    @Override
    public void onCmd(String[] args) {
    	ModManager.getModByName("Plugins").set(true);
        super.onCmd(args);
    }
}
