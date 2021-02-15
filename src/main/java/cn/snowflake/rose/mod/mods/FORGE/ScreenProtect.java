package cn.snowflake.rose.mod.mods.FORGE;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;

public class ScreenProtect extends Module {
    public static Value<String> mode = new Value<String>("ScreenProtect","Mode",0);
    public static Value<Boolean> leave = new Value<Boolean>("ScreenProtect_AutoLeaveServer",false);

    public ScreenProtect() {
        super("ScreenProtect", "ScreenProtect", Category.FORGE);
//        mode.addValue("CloseModule");
        mode.addValue("Custom");
    }

}
