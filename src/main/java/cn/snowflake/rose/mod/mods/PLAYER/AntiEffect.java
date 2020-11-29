package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;

public class AntiEffect extends Module {
    public AntiEffect() {
        super("AntiEffect", "Anti Effect", Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        setDisplayName("Remove");
        if (mc.thePlayer.isPotionActive(Potion.blindness)){
            mc.thePlayer.removePotionEffect(Potion.blindness.id);
        }
        if (mc.thePlayer.isPotionActive(Potion.confusion)){
            mc.thePlayer.removePotionEffect(Potion.confusion.id);
        }
    }

}
