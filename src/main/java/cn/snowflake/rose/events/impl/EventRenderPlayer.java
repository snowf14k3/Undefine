package cn.snowflake.rose.events.impl;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.types.EventType;

public class EventRenderPlayer implements Event {
    private EventType type;


    public EventRenderPlayer(EventType e){
        this.type = e;
    }

    public EventType getType() {
        return type;
    }
}
