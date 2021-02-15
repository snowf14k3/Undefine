package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import cn.snowflake.rose.utils.client.PlayerUtil;
import cn.snowflake.rose.utils.time.TimeHelper;
import cn.snowflake.rose.utils.time.WaitTimer;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Fly extends Module {
    public Value<Double> boost = new Value<Double>("Fly_MoitonBoost", 4.5, 1.0, 7.0, 0.1);
    public Value<String> mode = new Value("Fly_Mode", "Mode", 0);
    public Value<Boolean> antikick = new Value<>("Fly_AntiKick",true);
    private WaitTimer groundTimer = new WaitTimer();
    
    public Fly() {
        super("Fly","Fly", Category.MOVEMENT);
        this.mode.addValue("Motion");
        this.mode.addValue("Creative");

    }

    @EventTarget
    public void OnUpdate(EventMotion e) {
        this.setDisplayName(this.mode.getModeName());
        if (this.mode.isCurrentMode("Motion")) {
            mc.thePlayer.capabilities.isFlying = false;
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

        if (antikick.getValueState() ) {
            mc.thePlayer.posY -= 0.05d;
//                updateFlyHeight();
//                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
//                if (((this.flyHeight <= 290.0D) && (this.kickTimer.delay(500.0F))) || ((this.flyHeight > 290.0D) &&
//                        (this.kickTimer.delay(100.0F)))){
//                    goToGround();
//                    this.kickTimer.reset();
//            }
        }

    }

    public void updateFlyHeight() {
        double h = 1;
        AxisAlignedBB box = mc.thePlayer.boundingBox.expand(0.0625, 0.0625, 0.0625);
        for (flyHeight = 0; flyHeight < mc.thePlayer.posY; flyHeight += h) {
            AxisAlignedBB nextBox = box.offset(0, -flyHeight, 0);

            if (mc.theWorld.checkBlockCollision(nextBox)) {
                if (h < 0.0625)
                    break;

                flyHeight -= h;
                h /= 2;
            }
        }
    }

    public void goToGround() {
        if (flyHeight > 320)
            return;

        double minY = mc.thePlayer.posY - flyHeight;

        if (minY <= 0)
            return;

        for (double y = mc.thePlayer.posY; y > minY;) {
            y -= 9.9;
            if (y < minY)
                y = minY;

            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,0, y, mc.thePlayer.posZ, true);
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }

        for (double y = minY; y < mc.thePlayer.posY;) {
            y += 9.9;
            if (y > mc.thePlayer.posY)
                y = mc.thePlayer.posY;

            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,0, y, mc.thePlayer.posZ, true);
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
    }


    private double flyHeight;
    TimeHelper kickTimer = new TimeHelper();


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
