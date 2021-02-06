package cn.snowflake.rose.command.commands;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.friend.Friend;
import cn.snowflake.rose.management.FriendManager;
import cn.snowflake.rose.utils.ChatUtil;

public class CommandFriend extends Command {
    public CommandFriend(String[] commands) {
        super(commands);
        setArgs("friend/f add/remove Friendname");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 3) {
            ChatUtil.sendClientMessage(this.getArgs());
        } else {
            String option = args[1];
            String name = args[2];
            String alias = args.length > 3 ? args[3] : name;
            Friend friend = FriendManager.getFriend(name);
            if (!option.equalsIgnoreCase("a") && !option.equalsIgnoreCase("add")) {
                if (!option.equalsIgnoreCase("r") && !option.equalsIgnoreCase("remove")) {
                    ChatUtil.sendClientMessage(this.getArgs());
                } else if (friend != null) {
                    FriendManager.getFriends().remove(friend);
                    ChatUtil.sendClientMessage("Removed friend");
                }
            } else if (friend == null) {
                Friend newFriend = new Friend(name, alias);
                ChatUtil.sendClientMessage("Added friend " + name + " as " + alias);
                FriendManager.getFriends().add(newFriend);
            } else {
                friend.setAlias(alias);
                ChatUtil.sendClientMessage("Changed alias to " + alias);
            }
        }
    }

}
