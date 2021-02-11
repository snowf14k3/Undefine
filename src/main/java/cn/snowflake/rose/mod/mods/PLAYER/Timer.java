package cn.snowflake.rose.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import cn.snowflake.rose.utils.other.JReflectUtility;

public class Timer extends Module {
    public static Value<Double> speed = new Value<Double>("Timer_Timer", 1.0D, 0.1D, 10.0D,0.1D);
	
    public Timer() {
		super("Timer", Category.PLAYER);
	}
    
    @Override
    public void onDisable() {
    	super.onDisable();
    	JReflectUtility.setTimerSpeed(1.0f);
    }
    
    @EventTarget
    private void onUpdateEvent(EventUpdate event) {
    	JReflectUtility.setTimerSpeed(speed.getValueState().floatValue());
	}
}
