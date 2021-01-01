package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.utils.ChatUtil;

public class CommandVClip extends Command {
    public CommandVClip(String[] commands) {
        super(commands);
        setArgs("-vclip distance or -v distance");
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
        if (args.length < 2){
            ChatUtil.sendClientMessage(getArgs());
        }else{
            if (isNumeric(args[1])){
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + Double.parseDouble(args[1]), mc.thePlayer.posZ);
            }
        }
        super.onCmd(args);
    }
}
