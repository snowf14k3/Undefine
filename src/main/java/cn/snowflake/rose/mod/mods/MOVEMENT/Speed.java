package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventMove;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.TimeHelper;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class Speed extends Module {
    public Value<String> mode = new Value("Speed", "Mode", 0);
    public boolean shouldslow = false;
    boolean collided;
    boolean lessSlow;
    private int stage;
    private int stageOG = 1;
    double less;
    double stair;
    private double speed;
    public double slow;
    TimeHelper timer = new TimeHelper();
    TimeHelper lastCheck = new TimeHelper();
    private int needpress;
    public int time = 0;

    public Speed() {
        super("Speed", Category.MOVEMENT);
        this.mode.addValue("USHypixel");
        this.mode.addValue("Hypixel");
    }


    private double getHypixelCNSpeed(int stage) {
        double value = defaultSpeed() + 0.028 * (double)this.getSpeedEffect() + (double)this.getSpeedEffect() / 15.0;
        double firstvalue = 0.4145 + (double)this.getSpeedEffect() / 12.5;
        double decr = (double)stage / 500.0 * 2.0;
        if (stage == 0) {
            if (this.timer.delay(300.0f)) {
                this.timer.reset();
            }
            if (!this.lastCheck.delay(500.0f)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            } else if (this.shouldslow) {
                this.shouldslow = false;
            }
            value = 0.64 + ((double)this.getSpeedEffect() + 0.028 * (double)this.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
//            if (this.mc.timer.timerSpeed == 1.354f) {
//                // empty if block
//            }
            value = firstvalue;
        } else if (stage >= 2) {
//            if (this.mc.timer.timerSpeed == 1.254f) {
//                // empty if block
//            }
            value = firstvalue - decr;
        }
        if (this.shouldslow || !this.lastCheck.delay(500.0f) || this.collided) {
            value = 0.2;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, this.shouldslow ? value : defaultSpeed() + 0.028 * (double)this.getSpeedEffect());
    }

    public boolean isMoving2() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }


    public int getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }

    public int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }

    public static int randomNumber(int max, int min) {
        return Math.round((float)min + (float)Math.random() * (float)(max - min));
    }

    public double defaultSpeed() {
        double baseSpeed = 0.2873;
        Minecraft.getMinecraft();
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            Minecraft.getMinecraft();
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    private boolean canZoom() {
        return this.isMoving2() && mc.thePlayer.onGround;
    }

    private void setMotion(EventMove em, double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            em.setX(0.0);
            em.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    @EventTarget
    public void OnMove(EventMove e) {
        if (this.mode.isCurrentMode("USHypixel")) {
//            this.setDisplayName("USHypixel");
            if (mc.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
//                this.mc.timer.timerSpeed = 1.0f;
                this.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            this.less -= this.less > 1.0 ? 0.12 : 0.11;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!mc.thePlayer.isInWater() && mc.thePlayer.onGround && this.isMoving2()) {
                this.collided = mc.thePlayer.isCollidedHorizontally;
                if (this.stage >= 0 || this.collided) {
                    this.stage = 0;
                    double a = 0.41999742;
                    double motY = a + (double)this.getJumpEffect() * 0.1;
                    if (this.stair == 0.0) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionY = motY;
                        e.setY(mc.thePlayer.motionY);
                    }
                    this.less += 1.0;
                    this.lessSlow = this.less > 1.0 && !this.lessSlow;
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.speed = this.getHypixelSpeed(this.stage) + 0.01 + Math.random() / 200.0;
            this.speed *= 0.97;
            if (this.stair > 0.0) {
                this.speed *= 0.8 - (double)this.getSpeedEffect() * 0.1;
            }
            if (this.stage < 0) {
                this.speed = defaultSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 1.05;
            }
//            if (this.isInLiquid()) {
//                this.speed = 0.12;
//            }
            if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
                this.setMotion(e, this.speed);
                ++this.stage;
            }
        } else if (this.mode.isCurrentMode("Hypixel")) {
//            this.setDisplayName("Hypixel");
            if (mc.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
//                this.mc.timer.timerSpeed = 1.0f;
                this.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            this.less -= this.less > 1.0 ? 0.12 : 0.11;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!mc.thePlayer.isInWater() && mc.thePlayer.onGround && this.isMoving2()) {
                this.collided = mc.thePlayer.isCollidedHorizontally;
                if (this.stage >= 0 || this.collided) {
                    this.stage = 0;
//                    double a = ModManager.getModByName("Scaffold").isEnabled() ? 0.407 : 0.41999742;
                    double a = 0.41999742;
                    double motY = a + (double)this.getJumpEffect() * 0.1;
                    if (this.stair == 0.0) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionY = motY;
                        e.setY(mc.thePlayer.motionY);
                    }
                    this.less += 1.0;
                    this.lessSlow = this.less > 1.0 && !this.lessSlow;
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.speed = this.getHypixelSpeed(this.stage) + 0.01 + Math.random() / 500.0;
            this.speed *= 0.87;
            if (this.stair > 0.0) {
                this.speed *= 0.7 - (double)this.getSpeedEffect() * 0.1;
            }
            if (this.stage < 0) {
                this.speed = defaultSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 0.95;
            }
//            if (this.isInLiquid()) {
//                this.speed = 0.12;
//            }
            if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
                this.setMotion(e, this.speed * 1.5d);
                ++this.stage;
            }
        }
    }


    private double getHypixelSpeed(int stage2) {
        double value = defaultSpeed() + 0.028 * (double)this.getSpeedEffect() + (double)this.getSpeedEffect() / 15.0;
        double firstvalue = 0.4145 + (double)this.getSpeedEffect() / 12.5;
        double thirdvalue = 0.3945 + (double)this.getSpeedEffect() / 12.5;
        double decr = (double)stage / 500.0 * 3.0;
        if (stage == 0) {
            if (this.timer.delay(300.0f)) {
                this.timer.reset();
            }
            if (!this.lastCheck.delay(500.0f)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            } else if (this.shouldslow) {
                this.shouldslow = false;
            }
            value = 0.64 + ((double)this.getSpeedEffect() + 0.028 * (double)this.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
            value = firstvalue;
        } else if (stage == 2) {
//            if (this.mc.timer.timerSpeed == 1.354f) {
//                // empty if block
//            }
            value = thirdvalue;
        } else if (stage >= 3) {
//            if (this.mc.timer.timerSpeed == 1.254f) {
//                // empty if block
//            }
            value = thirdvalue - decr;
        }
        if (this.shouldslow || !this.lastCheck.delay(500.0f) || this.collided) {
            value = 0.2;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, this.shouldslow ? value : defaultSpeed() + 0.028 * (double)this.getSpeedEffect());
    }

}
