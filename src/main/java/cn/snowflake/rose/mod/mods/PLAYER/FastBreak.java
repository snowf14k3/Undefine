package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;


public class FastBreak extends Module {
    public Value<Double> speed = new Value<Double>("FastBreak_Damage",0.8,0.5,0.8,0.1);

    public FastBreak() {
        super("FastBreak", Category.PLAYER);
    }

    @EventTarget
    public void OnUpdata(EventUpdate e) {
        this.setDisplayName(""+speed.getValueState().doubleValue());
        if(JReflectUtility.getCurBlockDamageMP() > speed.getValueState().floatValue())
            JReflectUtility.setCurBlockDamageMP(1);
            JReflectUtility.setBlockHitDelay(1);
    }
}

