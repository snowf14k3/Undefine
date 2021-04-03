package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;

public class NameProtect extends Module {
    public static Value<String> name = new Value<>("NameProtect_Name","","SeasonUser");



    public NameProtect() {
        super("NameProtect", "Name Protect", Category.RENDER);
    }
}
