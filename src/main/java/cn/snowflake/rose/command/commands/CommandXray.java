package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.utils.client.ChatUtil;
import net.minecraft.block.Block;

import java.util.regex.Pattern;

public class CommandXray extends Command {
    public CommandXray(String[] commands) {
        super(commands);
        this.setArgs("-xray add id / -xray remove id");
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    @Override
    public void onCmd(String[] args){
        if (args.length < 3){
            ChatUtil.sendClientMessage(this.getArgs());
        }else{
            String strid = args[2];
            String c = args[1];
            if (isNumeric(strid)){
                Block block = Block.getBlockById( Integer.parseInt(strid));
                if (c.equalsIgnoreCase("add")){
                    if (!Xray.block.contains(block)){
                        Xray.block.add(block);
                        ChatUtil.sendClientMessage("Add " + block.getLocalizedName() + " Successfully !");
                        Client.instance.fileMgr.saveBlocks();
                    }else{
                        ChatUtil.sendClientMessage("You are already add this block !");
                    }
                }
                if (c.equalsIgnoreCase("remove")){
                    if (Xray.block.contains(block)){
                        Xray.block.remove(block);
                        ChatUtil.sendClientMessage("Remove " + block.getLocalizedName() + " Successfully !");
                        Client.instance.fileMgr.saveBlocks();
                    }else{
                        ChatUtil.sendClientMessage("You have no add this block !");
                    }
                }
            }else{
                ChatUtil.sendClientMessage(this.getArgs());
            }
        }
    }

}
