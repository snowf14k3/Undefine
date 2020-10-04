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
    private TimeHelper groundTimer = new TimeHelper();

    public Fly() {
        super("Fly", Category.MOVEMENT);
        this.mode.addValue("AntiKick");
        this.mode.addValue("Motion");
        this.mode.addValue("Creative");

    }

    @EventTarget
    public void OnUpdate(EventMotion e) {
        if (this.mode.isCurrentMode("Motion")) {
            this.mc.thePlayer.motionY = 0.0;
            if (this.mc.gameSettings.keyBindForward.getIsKeyPressed()
                    || this.mc.gameSettings.keyBindLeft.getIsKeyPressed()
                    || this.mc.gameSettings.keyBindRight.getIsKeyPressed()
                    || this.mc.gameSettings.keyBindBack.getIsKeyPressed()) {
                PlayerUtil.setSpeed(this.boost.getValueState());
            }

            if (this.mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
                EntityPlayerSP thePlayer = this.mc.thePlayer;
                --thePlayer.motionY;
            }
            else if (this.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                ++thePlayer2.motionY;
            }
        }
        if (this.mode.isCurrentMode("AntiKick")){
            float speed =  this.boost.getValueState().floatValue() * 0.9f;
            this.mc.thePlayer.capabilities.setFlySpeed(0.35f * speed);
            if (this.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                mc.thePlayer.motionY += speed;
            }
            else {
                mc.thePlayer.motionY -= ((speed * 0.02 > 0.08) ? 0.08 : (speed * 0.02));
            }
            if (this.mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
                mc.thePlayer.motionY -= speed;
            }
        }
        if (this.mode.isCurrentMode("Creative")){
            mc.thePlayer.capabilities.isFlying = true;
        }

        if (mc.thePlayer.ticksExisted % 5 == 0) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, mc.thePlayer.posY - 0.03125D, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
        }
//        handleVanillaKickBypass();
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
