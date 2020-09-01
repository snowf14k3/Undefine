package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;


public class Step extends Module {
    public Value<Double> height = new Value<Double>("Step_height", 1.0d,2.0d,5.0d,0.5d);

    public Step() {
        super("Step", Category.MOVEMENT);
    }

    @EventTarget
    public void OnUpdate(EventUpdate e) {
        mc.thePlayer.stepHeight = height.getValueState().floatValue();
    }
    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.5F;
    }
}

