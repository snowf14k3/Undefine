package cn.snowflake.rose.events.impl;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventGuiOpen implements Event {

    public GuiScreen gui;
    public EventGuiOpen(GuiScreen gui)
    {
        this.gui = gui;
    }


}
