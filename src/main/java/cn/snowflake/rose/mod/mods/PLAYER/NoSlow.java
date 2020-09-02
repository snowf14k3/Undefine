package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;

public class NoSlow extends Module {


    public NoSlow() {
        super("NoSlow", Category.PLAYER);
    }

    public static boolean no = false;
    @Override
    public void onEnable() {
        no = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        no = false;
        super.onDisable();
    }
}
