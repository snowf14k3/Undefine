package cn.snowflake.rose.mod.mods.WORLD;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;

public class MCF extends Module {
    public MCF() {
        super("MCF", Category.WORLD);
    }

    @EventTarget
    public void onupdate(EventUpdate e){
        if (mc.objectMouseOver.entityHit != null){

        }
    }



}
