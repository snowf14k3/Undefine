package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.utils.client.ChatUtil;
import cn.snowflake.rose.utils.antianticheat.ScreenshotUtil;

public class CommandScreenshot extends Command {
    public CommandScreenshot(String[] commands) {
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
                ScreenshotUtil.catanticheatImage = ScreenshotUtil.catAnticheatScreenhost();
            }
//            if (c.equalsIgnoreCase("deci")){
//                ScreenhostHelper.bufferedImage = ScreenhostHelper.customScreenshot();
//            }
            ChatUtil.sendClientMessage("Set screenhost successfully !");
        }
        super.onCmd(args);
    }
}
