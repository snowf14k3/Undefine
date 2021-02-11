package maki.utils;

import cn.snowflake.rose.mod.mods.WORLD.IRC;
import cn.snowflake.rose.utils.auth.AntiReflex;

public class LoginUtil {
    public static void doLogin(String username, String password) {
        IRC.sendIRCMessage("#CLIENT#LOGIN::"+username+"::"+password+"::"+ AntiReflex.getHWID(),false);
    }
}
