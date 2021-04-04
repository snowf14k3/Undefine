package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;

public class NoHurtcam extends Module {
    public NoHurtcam() {
        super("NoHurtcam","No Hurt cam", Category.RENDER);
    }

    public static boolean no;

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
