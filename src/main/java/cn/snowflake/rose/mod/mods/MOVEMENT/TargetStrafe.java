package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventMove;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.COMBAT.Aura;
import cn.snowflake.rose.utils.RotationUtil;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class TargetStrafe extends Module {
    public TargetStrafe() {
        super("TargetStrafe", "Target Strafe", Category.MOVEMENT);
    }
    @EventTarget
    private void onUpdate(EventMotion event) {
        if(mc.thePlayer.hurtTime>20&&!mc.thePlayer.isBurning()||mc.thePlayer.isCollidedHorizontally) {
            this.switchDirection();
        }
    }
    private int direction = -1;
    private void switchDirection() {
        this.direction = this.direction == 1 ? -1 : 1;
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
