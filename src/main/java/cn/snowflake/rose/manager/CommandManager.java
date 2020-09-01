package cn.snowflake.rose.manager;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.command.commands.*;

import java.util.ArrayList;

public class CommandManager {
    private static ArrayList<Command> commands = new ArrayList();

    public CommandManager() {
        add(new CommandHelp(new String[] {"help"}));
        add(new CommandToggle(new String[] {"toggle", "t"}));
        add(new CommandBind(new String[] {"bind"}));
        add(new CommandWalkTo(new String[]{"to"}));
        add(new CommandXray(new String[]{"xray"}));
    }

    public void add(Command c) {
        this.commands.add(c);
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

    public static String removeSpaces(String message) {
        String space = " ";
        String doubleSpace = "  ";
        while (message.contains(doubleSpace)) {
            message = message.replace(doubleSpace, space);
        }
        return message;
    }
}
