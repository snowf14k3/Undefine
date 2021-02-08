package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.other.JReflectUtility;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;


public class FastBreak extends Module {
    public Value<Double> speed = new Value<Double>("FastBreak_Damage",0.8,0.5,0.8,0.1);

    public FastBreak() {
        super("FastBreak","Fast Break", Category.PLAYER);
    }


    @EventTarget
    public void OnUpdata(EventUpdate e) {
        this.setDisplayName(""+speed.getValueState());
        JReflectUtility.setBlockHitDelay(0);

        if(JReflectUtility.getCurBlockDamageMP() >= speed.getValueState().floatValue()){
            JReflectUtility.setCurBlockDamageMP(1);
            boolean item = mc.thePlayer.getCurrentEquippedItem() == null;
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 20, item?1:0));
        }
    }
    public void onDisable() {
        mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
    }
}

