package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.utils.client.ChatUtil;
import cn.snowflake.rose.utils.antianticheat.ScreenhostHelper;

public class CommandScreenhost extends Command {
    public CommandScreenhost(String[] commands) {
        super(commands);
        setArgs("-screenhost cac/deci");
    }


    @Override
    public void onCmd(String[] args) {
        if (args.length < 2){
            ChatUtil.sendClientMessage(this.getArgs());
        }else {
            String c = args[1];
            if (c.equalsIgnoreCase("cac")){
                ScreenhostHelper.catanticheatImage = ScreenhostHelper.catAnticheatScreenhost();
            }
//            if (c.equalsIgnoreCase("deci")){
//                ScreenhostHelper.bufferedImage = ScreenhostHelper.customScreenshot();
//            }
            ChatUtil.sendClientMessage("Set screenhost successfully !");
        }
        super.onCmd(args);
    }
}
