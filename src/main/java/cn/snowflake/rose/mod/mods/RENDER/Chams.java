package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;

public class Chams extends Module {
    public Chams() {
        super("Chams","Chams", Category.RENDER);
    }

    public static boolean chams;

    @Override
    public void onDisable() {
        chams = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onupdate(EventUpdate e){
        chams = this.isEnabled();
    }

//    @EventTarget
//    public void onrenderplayer(EventRenderPlayer e){
//        if (e.getType() == EventType.PRE){
//            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
//            GL11.glPolygonOffset(1.0F, -2000000F);
//        }else if (e.getType() == EventType.POST){
//            GL11.glPolygonOffset(1.0F, 2000000F);
//            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
//        }
//    }
}