package cn.snowflake.rose.mod.mods.COMBAT;

import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aimbot2 extends Module 
{
    private float oldPitch;
    private float oldYaw;
    public int ticks;
    public int lookDelay;
    private EntityPlayer target;
    public int buffer;
    private Map<EntityPlayer, List<Vec3>> playerPositions;
    public static Value<Double> delay;
    public static Value<Boolean> noSpread;
    public static Value<Boolean> RCS;
    public static Value<Boolean> silent;
    public static Value<Boolean> autoShoot;
    public static Value<Double> FOV;
    public static Value<String> bone;
    public static Value<Double> rcsHorizontal;
    public static Value<Double> rcsVertical;

    static {
        delay = new Value("Aimbot2_Delay", 7.0f, 0.0f, 35.0f);
        
        noSpread = new Value("Aimbot2_noSpread", true);
        RCS = new Value( "Aimbot2_RCS", false);
        silent = new Value("Aimbot2_Silent", true);
        autoShoot = new Value("Aimbot2_AutoShoot", true);
        
        FOV = new Value( "Aimbot2_FOV", 360.0f, 1.0f, 360.0f);
        bone = new Value( "Aimbot2","Bone", 0);
        rcsHorizontal = new Value("Aimbot2_HRecoil", 0.1f, 0.1f, 2.0f);
        rcsVertical = new Value("Aimbot2_VRecoil", 0.5f, 0.1f, 2.0f);
    }

    public Aimbot2() {
        super("Aimbot2","Aimbot2", Category.COMBAT);
        this.buffer = 10;
        this.playerPositions = new HashMap<EntityPlayer, List<Vec3>>();
        bone.addValue("Head");
        bone.addValue("Neck");
        bone.addValue("Chest");
        bone.addValue("Jimmies");
        bone.addValue("Legs");
        bone.addValue("Feet");

    }
    @Override
    public String getDescription() {
        return "反机器人2!";
    }


    @EventTarget
    public void sendPacket(EventPacket e) {
        if (RCS.getValueState() && e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            ++this.ticks;
        }
    }

    @EventTarget(4)
    public void preTick( EventTick e) {
            if (silent.getValueState()) {
                this.oldPitch = mc.thePlayer.rotationPitch;
                this.oldYaw = mc.thePlayer.rotationYaw;
            }
            double targetWeight = Double.NEGATIVE_INFINITY;
            this.target = null;
            for ( Object o : mc.theWorld.playerEntities) {
                EntityPlayer p = (EntityPlayer)o;
                if (!p.equals(mc.thePlayer) && p.ticksExisted >= 40 && !p.isInvisible() && mc.thePlayer.canEntityBeSeen(p)) {
                    if (this.target == null) {
                        this.target = p;
                        targetWeight = this.getTargetWeight(p);
                    }
                    else {
                        if (this.getTargetWeight(p) <= targetWeight) {
                            continue;
                        }
                        this.target = p;
                        targetWeight = this.getTargetWeight(p);
                    }
                }
            }
            for ( EntityPlayer player : this.playerPositions.keySet()) {
                if (!mc.theWorld.playerEntities.contains(player)) {
                    this.playerPositions.remove(player);
                }
            }
            for ( Object o : mc.theWorld.playerEntities) {
                EntityPlayer player = (EntityPlayer)o;
                this.playerPositions.putIfAbsent(player, new ArrayList<Vec3>());
                 List<Vec3> previousPositions = this.playerPositions.get(player);
                previousPositions.add(Vec3.createVectorHelper(player.posX, player.posY, player.posZ));
                if (previousPositions.size() > this.buffer) {
                    int i = 0;
                    for ( Vec3 position : new ArrayList<Vec3>(previousPositions)) {
                        if (i < previousPositions.size() - this.buffer) {
                            previousPositions.remove(previousPositions.get(i));
                        }
                        ++i;
                    }
                }
            }
            if (this.target != null) {
                if (RCS.getValueState() && this.ticks >= 30) {
                    this.ticks = 0;
                }
                ++this.lookDelay;
                 Entity simulated = this.predictPlayerMovement(this.target);
                float offset = 0.0f;
                if (bone.getModeName().equalsIgnoreCase("Head")) {
                    offset = -0.2f;
                }
                if (bone.getModeName().equalsIgnoreCase("Neck")) {
                    offset = 0.1f;
                }
                if (bone.getModeName().equalsIgnoreCase("Chest")) {
                    offset = 0.4f;
                }
                if (bone.getModeName().equalsIgnoreCase("Jimmies")) {
                    offset = 0.85f;
                }
                if (bone.getModeName().equalsIgnoreCase("Legs")) {
                    offset = 1.0f;
                }
                if (bone.getModeName().equalsIgnoreCase("Feet")) {
                    offset = 1.5f;
                }
                 float[] rotations = this.getPlayerRotations(mc.thePlayer, simulated.posX, simulated.posY + this.target.getEyeHeight() - offset, simulated.posZ);
                if (RCS.getValueState()) {
                    mc.thePlayer.rotationYaw = rotations[0];
                    mc.thePlayer.rotationPitch = rotations[1] + rcsVertical.getValueState().floatValue() * this.ticks;
                    if (this.ticks >= 10) {
                        mc.thePlayer.rotationYaw = rotations[0] - rcsHorizontal.getValueState().floatValue() * this.ticks;
                    }
                    if (this.ticks >= 20) {
                        mc.thePlayer.rotationYaw = rotations[0] + rcsHorizontal.getValueState().floatValue() * this.ticks;
                    }
                }
                else {
                    mc.thePlayer.rotationYaw = rotations[0];
                    mc.thePlayer.rotationPitch = rotations[1];
                }
                if (this.lookDelay >= delay.getValueState()) {
                    if (autoShoot.getValueState()) {
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                    }
                    this.lookDelay = 0;
                }
            }
            else {
                --this.ticks;
                if (this.ticks <= 0) {
                    this.ticks = 0;
                }
            }
        
    }

    public double getTargetWeight( EntityPlayer p) {
        double weight = -mc.thePlayer.getDistanceToEntity(p);
        if (p.lastTickPosX == p.posX && p.lastTickPosY == p.posY && p.lastTickPosZ == p.posZ) {
            weight += 200.0;
        }
        weight -= p.getDistanceToEntity(mc.thePlayer) / 5.0f;
        return weight;
    }

    private Entity predictPlayerMovement( EntityPlayer target) {
//         int pingTicks = (int)Math.ceil(mc.getNetHandler().g(mc.thePlayer.getUniqueID()).getResponseTime() / 50.0);
        return this.predictPlayerLocation(target, 1);
    }

    public Entity predictPlayerLocation( EntityPlayer player,  int ticks) {
        if (this.playerPositions.containsKey(player)) {
             List<Vec3> previousPositions = this.playerPositions.get(player);
            if (previousPositions.size() > 1) {
                 Vec3 origin = previousPositions.get(0);
                 List<Vec3> deltas = new ArrayList<Vec3>();
                Vec3 previous = origin;
                for ( Vec3 position : previousPositions) {
                    deltas.add(Vec3.createVectorHelper(position.xCoord - previous.xCoord, position.yCoord - previous.yCoord, position.zCoord - previous.zCoord));
                    previous = position;
                }
                double x = 0.0;
                double y = 0.0;
                double z = 0.0;
                for ( Vec3 delta : deltas) {
                    x += delta.xCoord;
                    y += delta.yCoord;
                    z += delta.zCoord;
                }
                x /= deltas.size();
                y /= deltas.size();
                z /= deltas.size();
                 EntityPlayer simulated = new EntityOtherPlayerMP(mc.theWorld, player.getGameProfile());
                simulated.noClip = false;
                simulated.setPosition(player.posX, player.posY, player.posZ);
                for (int i = 0; i < ticks; ++i) {
                    simulated.moveEntity(x, y, z);
                }
                return simulated;
            }
        }
        return player;
    }



    private float[] getPlayerRotations( Entity player,  double x,  double y,  double z) {
         double deltaX = x - player.posX;
         double deltaY = y - player.posY - player.getEyeHeight() - 0.1;
         double deltaZ = z - player.posZ;
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
         double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        yawToEntity = wrapAngleTo180((float)yawToEntity);
        pitchToEntity = wrapAngleTo180((float)pitchToEntity);
        return new float[] { (float)yawToEntity, (float)pitchToEntity };
    }

    private static float wrapAngleTo180(float angle) {
        for (angle %= 360.0f; angle >= 180.0f; angle -= 360.0f) {}
        while (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
}