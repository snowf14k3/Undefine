package cn.snowflake.rose.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventMove;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import cn.snowflake.rose.utils.client.PlayerUtil;
import cn.snowflake.rose.utils.time.TimeHelper;
import cn.snowflake.rose.utils.time.WaitTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;

public class Fly extends Module {
    public Value<Double> boost = new Value<Double>("Fly_MoitonBoost", 4.5, 1.0, 7.0, 0.1);
    public Value<String> mode = new Value("Fly_Mode", "Mode", 0);
    public Value<Boolean> antikick = new Value<>("Fly_AntiKick",true);
    private WaitTimer groundTimer = new WaitTimer();
    
    public Fly() {
        super("Fly","Fly", Category.MOVEMENT);
        this.mode.addValue("Motion");
        this.mode.addValue("Vanilla");
        this.mode.addValue("Creative");

    }

    @EventTarget
    public void OnUpdate(EventMotion e) {
        this.setDisplayName(this.mode.getModeName());
        if (this.mode.isCurrentMode("Vanilla")) {
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
        }

    }

    @EventTarget
	   private void onUpdate(EventMotion e) {
    	  if (this.mode.isCurrentMode("Motion")) {
	      double mspeed = Math.max((double)boost.getValueState(), getBaseMoveSpeed());
	      if (this.mc.thePlayer.movementInput.jump) {
	         this.mc.thePlayer.motionY = mspeed * 0.6D;
	      } else if (this.mc.thePlayer.movementInput.sneak) {
	         this.mc.thePlayer.motionY = -mspeed * 0.6D;
	      } else {
	         this.mc.thePlayer.motionY = 0.0D;
	      }
    	  }
	   }

	   @EventTarget
	   public void onMove(EventMove e) {
		 if (this.mode.isCurrentMode("Motion") && e.entity == mc.thePlayer) {
	      double speed = (double)boost.getValueState();
	      Fly.setMoveSpeed(e, speed);
		   }
	   }

	   public static void setMoveSpeed(EventMove event, double speed) {
		      double forward = (double)mc.thePlayer.moveForward;
		      double strafe = (double)mc.thePlayer.moveStrafing;
		      float yaw = mc.thePlayer.rotationYaw;
		      if (forward == 0.0D && strafe == 0.0D) {
		         event.setX(0.0D);
		         event.setZ(0.0D);
		      } else {
		         if (forward != 0.0D) {
		            if (strafe > 0.0D) {
		               yaw += (float)(forward > 0.0D ? -45 : 45);
		            } else if (strafe < 0.0D) {
		               yaw += (float)(forward > 0.0D ? 45 : -45);
		            }

		            strafe = 0.0D;
		            if (forward > 0.0D) {
		               forward = 1.0D;
		            } else if (forward < 0.0D) {
		               forward = -1.0D;
		            }
		         }

		         event.setX(forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
		         event.setZ(forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
		      }

		   }
	   
	   public static double getBaseMoveSpeed() {
	      double baseSpeed = 0.2873D;
	      if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
	         int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
	         baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
	      }

	      return baseSpeed;
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
