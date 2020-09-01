package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.JReflectUtility;
import com.darkmagician6.eventapi.EventTarget;


public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace", Category.PLAYER);
    }

    @EventTarget
    public void OnUpdate(EventUpdate e) {
        JReflectUtility.setRightClickDelayTimer(0);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        JReflectUtility.setRightClickDelayTimer(6);
        super.onDisable();
    }

}