package cn.snowflake.rose.antianticheat;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import java.lang.reflect.Constructor;

public class AnotherAntiCheat {

    public AnotherAntiCheat(){
        EventManager.register(this);
    }

    @EventTarget
    public void onFml(EventFMLChannels eventFMLChannels){
        try {
            if (eventFMLChannels.iMessage.toString().contains("AntiCheatCTSPacketMessage")){
                eventFMLChannels.setCancelled(true);//À¹½Ø
                Constructor constructor = eventFMLChannels.iMessage.getClass().getConstructor(byte[][].class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
