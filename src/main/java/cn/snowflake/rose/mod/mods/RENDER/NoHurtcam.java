package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;

public class NoHurtcam extends Module {
    public NoHurtcam() {
        super("NoHurtcam","No Hurt cam", Category.RENDER);
    }



    public static boolean no;

    @Override
    public void set(boolean state) {
        if (state){
            no = true;
        }else{
            no = false;
        }
        super.set(state);
    }
}
