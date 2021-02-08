package cn.snowflake.rose.mod.mods.COMBAT;

import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;

public class NoRecoil extends Module {
    public NoRecoil() {
        super("NoRecoil", "No Recoil", Category.COMBAT);
    }
    @EventTarget
    public void ontick(EventTick e){
        if (mc.thePlayer != null && mc.theWorld != null) {
            mc.thePlayer.rotationPitch = mc.thePlayer.prevRotationPitch;
            mc.thePlayer.rotationYaw = mc.thePlayer.prevRotationYaw;
        }
    }
}
