package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.friend.Friend;
import cn.snowflake.rose.manager.FriendManager;
import cn.snowflake.rose.utils.ChatUtil;

public class CommandFriend extends Command {
    public CommandFriend(String[] commands) {
        super(commands);
        setArgs("friend/f add/remove Friendname");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 3){
            ChatUtil.sendClientMessage(this.getArgs());
            return;
        }
        String mode = args[1];
        String friendname = args[2];
        if (mode.equalsIgnoreCase("add")){
            FriendManager.friends.add(new Friend(friendname));
            ChatUtil.sendClientMessage("Added Friend "+friendname+" Successfully !");
        }else if (mode.equalsIgnoreCase("remove")){
            for (Friend f : FriendManager.friends){
                if (f.getName().equalsIgnoreCase(friendname)){
                    FriendManager.friends.remove(f);
                    ChatUtil.sendClientMessage("Removed Friend "+friendname+" Successfully !");
                }
            }
        }
        super.onCmd(args);
    }

}
