package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.PlayerUtil;
import cn.snowflake.rose.utils.TimeHelper;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Fly extends Module {
    public Value<Double> boost = new Value<Double>("Fly_MoitonBoost", 4.5, 1.0, 7.0, 0.1);
    public Value<String> mode = new Value("Fly_Mode", "Mode", 0);
    public Value<Boolean> antikick = new Value<>("Fly_AntiKick",true);
    private TimeHelper groundTimer = new TimeHelper();

    public Fly() {
        super("Fly","Fly", Category.MOVEMENT);
        this.mode.addValue("Motion");
        this.mode.addValue("Creative");

    }

    @EventTarget
    public void OnUpdate(EventMotion e) {
        this.setDisplayName(this.mode.getModeName());
        if (this.mode.isCurrentMode("Motion")) {
            this.mc.thePlayer.motionY = 0.0;
            if (this.mc.gameSettings.keyBindForward.getIsKeyPressed()
                    || this.mc.gameSettings.keyBindLeft.getIsKeyPressed()
                    || this.mc.gameSettings.keyBindRight.getIsKeyPressed()
                    || this.mc.gameSettings.keyBindBack.getIsKeyPressed()) {
                PlayerUtil.setSpeed(this.boost.getValueState());
            }

            if (this.mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
                --this.mc.thePlayer.motionY;
            }
            else if (this.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                ++this.mc.thePlayer.motionY;
            }
        }

        if (this.mode.isCurrentMode("Creative")){
            mc.thePlayer.capabilities.isFlying = true;
        }

        if (antikick.getValueState().booleanValue() ) {
            mc.thePlayer.motionY -= 0.05D;
        }

    }

  

    @Override
    public void onDisable() {
        mc.thePlayer.motionX =0;
        mc.thePlayer.motionZ =0;
        if (this.mode.isCurrentMode("Creative")){
            mc.thePlayer.capabilities.isFlying = false;
        }
        super.onDisable();
    }
}
