package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright extends Module {

    public FullBright() {
        super("FullBright","Full Bright", Category.RENDER);
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        setDisplayName("Potion");
        mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 0));
    }

    @Override
    public void onDisable() {
        this.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

}

