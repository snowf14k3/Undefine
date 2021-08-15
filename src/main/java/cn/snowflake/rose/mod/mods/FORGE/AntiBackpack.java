package cn.snowflake.rose.mod.mods.FORGE;

import cn.snowflake.rose.events.impl.EventGuiOpen;
import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

public class AntiBackpack extends Module {


    public AntiBackpack() {
        super("AntiBackpack", "Anti Backpack", Category.FORGE);
    }


    @EventTarget
    public void guiOpen(EventGuiOpen eventGuiOpen){
        if (eventGuiOpen.gui instanceof GuiContainer){
            GuiContainer container = (GuiContainer) eventGuiOpen.gui;
            if (container.inventorySlots.inventorySlots.size() == 36){
                eventGuiOpen.setCancelled(true);
            }
        }
    }


}
