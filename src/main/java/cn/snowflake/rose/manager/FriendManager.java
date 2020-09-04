package cn.snowflake.rose.manager;

import cn.snowflake.rose.friend.Friend;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class FriendManager {
    public static ArrayList<Friend> friends = new ArrayList();

    public static boolean isFriend(EntityPlayer entityPlayer){
        for (Friend friend : friends) {
            return friend.name.equalsIgnoreCase(entityPlayer.getCommandSenderName());
        }
        return false;
    }

    public static boolean isFriend(String name){
        for (Friend friend : friends) {
            return friend.name.equalsIgnoreCase(name);
        }
        return false;
    }

    public static Friend getFriend(String name){
        for (Friend friend : friends) {
            if (friend.name.equalsIgnoreCase(name)){
                return friend;
            }
        }
        return null;
    }

}
