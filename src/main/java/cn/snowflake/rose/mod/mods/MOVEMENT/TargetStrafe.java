package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventMove;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.COMBAT.Aura;
import cn.snowflake.rose.utils.client.PlayerUtil;
import cn.snowflake.rose.utils.client.RotationUtil;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

import java.util.Objects;

public class TargetStrafe extends Module {
    public Value<Double> range = new Value("TargetStrafe_Range", 2.0D, 1.0D, 4.0D, 0.1D);
    private final Value<Boolean> antiVoid = new Value<>("TargetStrafe_AntiVoid", true);
    private final Value<Boolean> antiBlock = new Value<>("TargetStrafe_AntiBlock", true);
    private final Value<Boolean> key = new Value("TargetStrafe_Key", true);
    private final Value<Boolean> third = new Value("TargetStrafe_ThirdPersonView", true);
    public Entity target;
    public static int bindInitiating;
    private double degree = 0.0D;
    private boolean left = true;
    private int oldView;
    private boolean strafing;

    public TargetStrafe() {
        super("TargetStrafe", "Target Strafe", Category.MOVEMENT);
    }
    @EventTarget
    private void onUpdate(EventMotion event) {
        if (event.isPre()){
            if (!this.key.getValueState() || mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                if (Objects.requireNonNull(ModManager.getModByName("Aura")).isEnabled()) {
                    this.target = Aura.target;
                }else {
                    this.target = null;
                }
                if (mc.gameSettings.keyBindLeft.isPressed()) {
                    this.left = true;
                }
                if (mc.gameSettings.keyBindRight.isPressed()) {
                    this.left = false;
                }
            } else {
                this.target = null;
                bindInitiating = 0;
            }
        }
    }
    private int direction = -1;
    private void switchDirection() {
        this.direction = this.direction == 1 ? -1 : 1;
    }


    public void onEnable() {
        this.degree = 0.0D;
        this.left = true;
        this.target = null;
    }
    public float getDistanceXZToEntity(EntityLivingBase entityLivingBase,Entity entityIn) {
        float f = (float)(entityLivingBase.posX - entityIn.posX);
        float f2 = (float)(entityLivingBase.posZ - entityIn.posZ);
        return MathHelper.sqrt_float(f * f + f2 * f2);
    }
    public static float toDegree(double x, double z) {
        return (float)(Math.atan2(z - (Minecraft.getMinecraft()).thePlayer.posZ, x - (Minecraft.getMinecraft()).thePlayer.posX) * 180.0D / Math.PI) - 90.0F;
    }
    @EventTarget
    private void onMove(EventMove e) {
        if (e.getEntity() != mc.thePlayer){
            return;
        }
        if (canStrafe()) {
            ++bindInitiating;
            this.strafing = true;
            if (this.third.getValueState()) mc.gameSettings.thirdPersonView = 1;
            double speed = this.getSpeed();
            this.degree = Math.atan2(mc.thePlayer.posZ - this.target.posZ, mc.thePlayer.posX - this.target.posX);
            if ((this.antiBlock.getValueState() && mc.thePlayer.isCollidedHorizontally) || (this.antiVoid.getValueState() && this.isNotUnderBlock())) {
                this.left = !this.left;
            }
            this.degree += this.left ? (speed / getDistanceXZToEntity(mc.thePlayer,this.target)) : -(speed / getDistanceXZToEntity(mc.thePlayer,this.target));
            double x = this.target.posX + this.range.getValueState() * Math.cos(this.degree);
            double z = this.target.posZ + this.range.getValueState() * Math.sin(this.degree);
            e.setX(speed * -Math.sin((float) Math.toRadians(toDegree(x, z))));
            e.setZ(speed * Math.cos((float) Math.toRadians(toDegree(x, z))));
        } else {
            if (strafing && this.third.getValueState()) mc.gameSettings.thirdPersonView = this.oldView;
            this.oldView = mc.gameSettings.thirdPersonView;
            this.strafing = false;
            bindInitiating = 0;
        }
    }
    public boolean canStrafe() {
        Aura ka = (Aura) ModManager.getModByName("Aura");
        Speed speed = (Speed) ModManager.getModByName("Speed");
        return (this.target != null && PlayerUtil.isMoving() && ka.isEnabled() && (speed.isEnabled()));
    }

    public boolean isNotUnderBlock() {
        if (ModManager.getModByName("Fly").isEnabled()) {
            return false;
        }
        for (int i = (int)Math.ceil(mc.thePlayer.posY); i >= (int)Math.ceil(mc.thePlayer.posY - 4); --i) {
            if (mc.theWorld.getBlock((int)mc.thePlayer.posX, i, (int)mc.thePlayer.posZ) != Blocks.air) {
                return false;
            }
        }
        return true;
    }

    private double getSpeed() {
        Speed speed = (Speed) ModManager.getModByName("Speed");
        return speed.speed * 0.87654321D;
    }

    public final void doStrafe(EventMove event, double moveSpeed) {
        if(Aura.target==null)return;
        float[] rotations = RotationUtil.getRotations(Aura.target);
        if ((double) Minecraft.getMinecraft().thePlayer.getDistanceToEntity((Entity) Aura.target) <=2) {
            this.setSpeed((EventMove)event, (double)moveSpeed, (float)rotations[0], (double)this.direction, (double)0.0);
        } else {
            this.setSpeed((EventMove)event, (double)moveSpeed, (float)rotations[0], (double)this.direction, (double)1.0);
        }
    }
    public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe,
                                double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (float) (forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += (float) (forward > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }


}
