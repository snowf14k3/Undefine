package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.utils.client.ChatUtil;
import cn.snowflake.rose.utils.auth.LoginUtil;
import net.minecraft.client.Minecraft;

public class CommandLogin extends Command {
    public CommandLogin(String[] commands) {
        super(commands);
        setArgs("Change minecraft account");
    }

    @Override
    public void onCmd(String[] args) {
        try
        {
            if(args[1].contains(":")) {
                String email = "";
                String password = "";
                if(args[1].contains(":")) {
                    String[] split = args[1].split(":");
                    email = split[0];
                    password = split[1];
                }
                String log = LoginUtil.loginAlt(email, password);
                ChatUtil.sendClientMessage(log);
            }
            else
            {
                LoginUtil.changeCrackedName(args[1]);
                ChatUtil.sendClientMessage("Logged [Cracked]: " + Minecraft.getMinecraft().getSession().getUsername());
            }
        }
        catch(Exception e)
        {
            ChatUtil.sendClientMessage(getArgs());
        }
        super.onCmd(args);
    }
}
