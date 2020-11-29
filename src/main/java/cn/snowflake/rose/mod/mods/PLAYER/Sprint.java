package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.PlayerUtil;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;

public class Sprint extends Module {
    private Value<String> mode = new Value("Sprint", "Mode", 1);

    public Sprint() {
        super("Sprint","Sprint", Category.MOVEMENT);
        this.mode.addValue("Single");
        this.mode.addValue("AllDirection");
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        boolean canSprint = mc.thePlayer.getFoodStats().getFoodLevel() > 6.0F || mc.thePlayer.capabilities.allowFlying;
        if (this.mode.isCurrentMode("AllDirection")  && PlayerUtil.isMoving()  && canSprint) {
            mc.thePlayer.setSprinting(true);
        }
        if (this.mode.isCurrentMode("Single")) {
            if (mc.thePlayer.moveForward > 0.0F && PlayerUtil.isMoving() && canSprint) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }
    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
        super.onDisable();
    }
}
