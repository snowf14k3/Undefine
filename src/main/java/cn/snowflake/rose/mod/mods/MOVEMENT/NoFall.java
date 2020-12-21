package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventMove;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;


public class NoFall extends Module {
    private int hypixel;
    public Value<String> mode = new Value("NoFall_Mode", "Mode", 0);

    public NoFall() {
        super("NoFall","No Fall",  Category.MOVEMENT);
        this.mode.mode.add("onGround");
    }

    @EventTarget
    public void OnPre(EventMotion e) {
        if(mode.isCurrentMode("onGround")) {
            if(mc.thePlayer.fallDistance > 2) {
               mc.thePlayer.onGround = !mc.thePlayer.onGround;
            }
        }
    }

}
