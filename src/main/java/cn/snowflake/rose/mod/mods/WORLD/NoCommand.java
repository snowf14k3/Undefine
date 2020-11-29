package cn.snowflake.rose.mod.mods.WORLD;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;

public class NoCommand extends Module {
    public static boolean n = false;
    public NoCommand() {
        super("NoCommand","No Command", Category.WORLD);
    }

    @Override
    public void onDisable() {
        n = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        n = true;
        super.onEnable();
    }
}
