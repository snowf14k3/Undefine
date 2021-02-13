package maki.utils;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.Season;
import cn.snowflake.rose.utils.auth.HWIDUtils;
import cn.snowflake.rose.utils.discord.Discord;

public class LoginUtil {
    public static void doLogin(String username, String password) {
        Season.username = username;
        Season.password = password;
        Season.hwid = HWIDUtils.getHWID();
        Discord.getDiscordUtil().sendMessage(Discord.getDiscordUtil().getTextChannelByName("verify"), "##LOGIN!" + Season.username + "::" + Season.password + "::" + Season.hwid+"::"+ Client.version);
    }

    public static boolean send;

    public static void proceedLoginFailed(String args) {

    }
}
