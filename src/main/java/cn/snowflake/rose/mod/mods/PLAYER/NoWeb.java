package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.asm.ClassTransformer;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.JReflectUtility;
import com.darkmagician6.eventapi.EventTarget;

public class NoWeb extends Module {
    public NoWeb() {
        super("NoWeb", Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        JReflectUtility.setField(mc.thePlayer.getClass(),mc.thePlayer, ClassTransformer.runtimeDeobfuscationEnabled ? "field_70134_J": "isInWeb",false);
    }

}
