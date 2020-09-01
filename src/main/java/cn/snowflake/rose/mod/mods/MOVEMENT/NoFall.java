package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;


public class NoFall extends Module {
    private int hypixel;
    public Value<String> mode = new Value("NoFall_Mode", "Mode", 0);

    public NoFall() {
        super("NoFall", Category.MOVEMENT);
        this.mode.mode.add("Hypixel");
    }

    @EventTarget
    public void OnPre(EventMotion e) {
        if(mode.isCurrentMode("Hypixel")) {
            if(mc.thePlayer.fallDistance > 3.0f) {
                e.setOnGround(true);
            }
        }
    }

}
