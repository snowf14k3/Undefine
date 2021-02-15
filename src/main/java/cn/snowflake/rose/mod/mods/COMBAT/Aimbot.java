package cn.snowflake.rose.mod.mods.COMBAT;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.management.FriendManager;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import cn.snowflake.rose.utils.client.RotationUtil;
import cn.snowflake.rose.utils.math.Location;
import cn.snowflake.rose.utils.other.JReflectUtility;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Aimbot extends Module {

    public static Value<Double> index = new Value<Double>("Aimbot_pitchIndex", 1.0, -0.01, 1.0, 0.01);
    public static Value<Double> predict = new Value<Double>("Aimbot_Predict", 8.0, 0.0, 15.0, 1);

    public static Value<Double> range= new Value<Double>("Aimbot_Reach", 10.5D, 3.0D, 65.0D,0.1D);

    public Value<Double> fov = new Value<Double>("Aimbot_Fov", 10.0, 1.0, 180.0, 1.0);
    public Value<Boolean> throughwall = new Value<Boolean>("Aimbot_ThroughWall", false);

    public Value<Boolean> players = new Value<Boolean>("Aimbot_Player", true);
//    public Value<Boolean> otherentity = new Value<Boolean>("Aimbot_ModsEntity", false);
//    public Value<Boolean> animal = new Value<Boolean>("Aimbot_Animal", false);
//    public Value<Boolean> moster = new Value<Boolean>("Aimbot_Mob", false);
//    public Value<Boolean> village = new Value<Boolean>("Aimbot_village", false);
    public Value<Boolean> invisible = new Value<Boolean>("Aimbot_Invisible", false);
    public Value<Boolean> silent = new Value<Boolean>("Aimbot_Silent", false);
    public Value<Boolean> circle = new Value<Boolean>("Aimbot_Circle", false);

    public Value<String> sortingMode = new Value<String>("Aimbot","SortingMode", 0);

    public static EntityLivingBase target;

    public int buffer;
    private Map<EntityPlayer, List<Vec3>> playerPositions;


    public Aimbot() {
        super("Aimbot","Aim Bot", Category.COMBAT);
        this.sortingMode.addValue("Health");
        this.sortingMode.addValue("Distance");

        this.playerPositions = new HashMap<>();
        this.buffer = 10;
    }


    @Override
    public void onDisable(){
        target = null;
    }


    static double randomNumber(double start, double end) {
        return (Math.random() * (double)(start - end)) + end;
    }


    @EventTarget
    public void on2D(EventRender2D eventRender2D){
		ScaledResolution res = new ScaledResolution(this.mc,this.mc.displayWidth,this.mc.displayHeight);

        mc.fontRenderer.drawStringWithShadow(
                "HP: "+target.getHealth(),
                res.getScaledWidth() / 2 - 10 - mc.fontRenderer.getStringWidth("HP: "+target.getHealth()),
                res.getScaledHeight() / 2 - 10,
                16777215); // 测试模式画Entity信息

        mc.fontRenderer.drawStringWithShadow(
                "SPD: "+targetspeed,
                res.getScaledWidth() / 2 - 10 - mc.fontRenderer.getStringWidth("SPD: "+targetspeed),
                res.getScaledHeight() / 2,
                16777215); // 测试模式画Entity信息

        mc.fontRenderer.drawStringWithShadow(
                "name: "+target.getCommandSenderName(),
                res.getScaledWidth() / 2 + 10,
                res.getScaledHeight() / 2,
                16777215); // 测试模式画Entity信息

        mc.fontRenderer.drawStringWithShadow(
                "hurt : " + (target.hurtTime > 0),
                res.getScaledWidth() / 2 + 10,
                res.getScaledHeight() / 2 - 10,
                16777215); // 测试模式画Entity信息

        mc.fontRenderer.drawStringWithShadow(
                "aabbx : " + target.boundingBox.minY + " | " + target.boundingBox.maxY + " | different: " + (roundToPlace(target.boundingBox.maxY - target.boundingBox.minY,2)) + " eye: "+ target.getEyeHeight(),
                0,
                res.getScaledHeight() / 2 - 20,
                16777215); // 测试模式画Entity信息
        if (circle.getValueState()){

            drawCircle(res.getScaledWidth() / 2, res.getScaledHeight() / 2,
                    fov.getValueState().floatValue() * 3.5f, 500, -1);
        }
    }

    public static double roundToPlace(double p_roundToPlace_0_,int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }

	public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		r *= 2;
		cx *= 2;
		cy *= 2;
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		float theta = (float) (2 * 3.1415926 / (num_segments));
		float p = (float) Math.cos(theta);// calculate the sine and cosine
		float s = (float) Math.sin(theta);
		float t;
		GL11.glColor4f(f1, f2, f3, f);
		float x = r;
		float y = 0;// start at angle = 0
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(770, 771);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int ii = 0; ii < num_segments; ii++) {
			GL11.glVertex2f(x + cx, y + cy);// final vertex vertex

			// rotate the stuff
			t = x;
			x = p * x - s * y;
			y = s * t + p * y;
		}
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glScalef(2F, 2F, 2F);
	}

    @EventTarget(Priority.HIGH)
    public void ontick(EventUpdate e){
        if (mc.thePlayer != null) {
            if (ModManager.getModByName("NoRecoil").isEnabled() && NoRecoil.horizontal.getValueState()) {
                mc.thePlayer.rotationPitch = mc.thePlayer.prevRotationPitch;
            }
            if (ModManager.getModByName("NoRecoil").isEnabled() && NoRecoil.vertical.getValueState()){
                mc.thePlayer.rotationYaw = mc.thePlayer.prevRotationYaw;
            }
        }
    }

    public double getTargetWeight(EntityLivingBase p) {
        double weight = -mc.thePlayer.getDistanceToEntity(p);
        if (p.lastTickPosX == p.posX && p.lastTickPosY == p.posY && p.lastTickPosZ == p.posZ) {
            weight += 200.0;
        }
        weight -= p.getDistanceToEntity(mc.thePlayer) / 5.0f;
        return weight;
    }

    double targetspeed;



    @EventTarget
    public void onTick(EventTick eventTick){
        double targetWeight = Double.NEGATIVE_INFINITY;
        for (Object o : mc.theWorld.playerEntities) {
            EntityLivingBase p = (EntityLivingBase)o;
            if (canTarget(p)) {
                    if (target == null) {
                        target = p;
                        targetWeight = this.getTargetWeight(p);
                    } else {
                        if (this.getTargetWeight(p) <= targetWeight) {
                            continue;
                        }
                        target = p;
                        targetWeight = this.getTargetWeight(p);
                    }
//                else {
//                    if (target == null) {
//                        target = p;
//                    }
//                }
            }
        }
        addTarget();
        if (target != null){
            Entity enity = null;
            if (target instanceof EntityPlayer){
                enity = this.predictPlayerMovement(((EntityPlayer)target),predict.getValueState().intValue());
            }else {
                enity = getTarget();
            }
            double rotY = enity.posY;
            if (roundToPlace(enity.boundingBox.maxY - enity.boundingBox.minY,2) == 0.6){//lying
                rotY = enity.boundingBox.minY + 0.15;
            }else if (roundToPlace(enity.boundingBox.maxY - enity.boundingBox.minY,2) == 1.3){//squatting
                rotY = enity.boundingBox.minY + 0.65 + index.getValueState();
            }else if (roundToPlace(enity.boundingBox.maxY - enity.boundingBox.minY,2) == 1.8){//standing
                rotY = enity.boundingBox.minY + 0.45 + index.getValueState();
            }
            double X = Math.abs(target.motionX);
            double Z = Math.abs(target.motionZ);
            targetspeed = X + Z;
            float[] rotations = this.getPlayerRotations(mc.thePlayer, enity.posX, rotY , enity.posZ);
            if(!silent.getValueState()&& shouldAim()) {
                Minecraft.getMinecraft().thePlayer.rotationYaw = rotations[0];
                Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
            }
        }
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

    public Entity predictPlayerMovement(EntityPlayer player,  int ticks) {
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

//    @EventTarget(Priority.LOWEST)
//    public void onEvent(EventMotion em) {
//        target = getTarget();
//        if(shouldAim()){
//            if (target != null) {
//                float[] rotations = getRotationByBoundingBox(target,range.getValueState().floatValue(),false);
//                if(silent.getValueState()){
//                    em.setYaw(yaw);
//                    em.setPitch(pitch);
//                }
//            }
//       }
//    }


    private boolean canTarget(Entity entity) {
        if(!RotationUtil.canEntityBeSeen(entity) && !throughwall.getValueState()) {
            return false;
        }

		if(!RotationUtil.isVisibleFOV((EntityLivingBase) entity, fov.getValueState().floatValue())){
			return false;
		}
        if (entity instanceof EntityPlayer && !players.getValueState()) {
            return false;
        }
        if (!ModManager.getModByName("NoFriend").isEnabled() && FriendManager.isFriend(entity.getCommandSenderName())){
            return false;
        }
//        if (entity instanceof EntityAnimal && !animal.getValueState()) {
//            return false;
//        }
//        if ((entity instanceof EntitySlime || entity instanceof EntityMob )&& !moster.getValueState()) {
//            return false;
//        }
        if (entity instanceof EntityBat){
            return false;
        }
//        if (entity instanceof EntityVillager && !village.getValueState()) {
//            return false;
//        }
        if (entity.isInvisible() && !invisible.getValueState()) {
            return false;
        }
        if (entity instanceof EntitySquid){
            return false;
        }
        if (Client.nshowmod){
            if (Objects.requireNonNull(JReflectUtility.getEntityNumber()).isInstance(entity)){
                return false;
            }
        }       
        if (Client.deci){
            if (Objects.requireNonNull(JReflectUtility.getCorpse()).isInstance(entity)){
                return false;
            }
        }
//        if ( (entity instanceof EntityCreature) && !otherentity.getValueState() ) {
//            return false;
//        }
        return entity != mc.thePlayer && entity.isEntityAlive();
    }

    public boolean shouldAim(){
    	if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
			return false;
		}
        if(mc.thePlayer.inventory.getCurrentItem() == null){
            return false;
        }
        return true;
    }

    private void addTarget(){
        for (EntityPlayer player : this.playerPositions.keySet()) {
            if (!mc.theWorld.playerEntities.contains(player)) {
                this.playerPositions.remove(player);
            }
        }
        for (Object o : mc.theWorld.playerEntities) {
            EntityPlayer player = (EntityPlayer)o;
            this.playerPositions.putIfAbsent(player, new ArrayList<Vec3>());
            List<Vec3> previousPositions = this.playerPositions.get(player);
            previousPositions.add(Vec3.createVectorHelper(player.posX, player.posY, player.posZ));
            if (previousPositions.size() > this.buffer) {
                int i = 0;
                for (Vec3 position : new ArrayList<Vec3>(previousPositions)) {
                    if (i < previousPositions.size() - this.buffer) {
                        previousPositions.remove(previousPositions.get(i));
                    }
                    ++i;
                }
            }
        }
    }


    private EntityLivingBase getTarget() {
        List<EntityLivingBase> loaded = new ArrayList<EntityLivingBase>();
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) o;
                if (canTarget(ent)) {
                    if (ent == Aura.target) {
                        return ent;
                    }
                    loaded.add(ent);
                }
            }
        }
        if (loaded.isEmpty()) {
            return null;
        }
        EntityLivingBase target = null;

	        if (sortingMode.isCurrentMode("Distance")) {
	            loaded.sort((o1, o2) ->
	                    (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer))
	            );
	        }
	        if (sortingMode.isCurrentMode("Health")){
	            loaded.sort((o1, o2) ->
	                    (int) (o1.getHealth() - o2.getHealth())
	            );
	        }
	         target = loaded.get(0);
        return target;
    }

    public static float[] getRotationByBoundingBox(Entity ent,float maxRange ,boolean random){
        if(ent == null)
            return new float[]{0,0};
        AxisAlignedBB boundingBox = ent.boundingBox;

        double orPosX = ent.boundingBox.minX,orPosY=ent.boundingBox.minY ,orPosZ = ent.boundingBox.minZ;

        double pX = Minecraft.getMinecraft().thePlayer.boundingBox.minX;
        double pY = Minecraft.getMinecraft().thePlayer.boundingBox.minY + (Minecraft.getMinecraft().thePlayer.boundingBox.minY+Minecraft.getMinecraft().thePlayer.boundingBox.maxY)/2;
        double pZ = Minecraft.getMinecraft().thePlayer.boundingBox.minZ;
        double dX = pX - orPosX;
        double dZ = pZ - orPosZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        Location BestPos = new Location(ent.boundingBox.minX, orPosY, ent.boundingBox.minZ);
        Location myEyePos = new Location(Minecraft.getMinecraft().thePlayer.boundingBox.minX, Minecraft.getMinecraft().thePlayer.boundingBox.minY+ (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.boundingBox.minZ);
        double diffY;
        for(diffY = ent.boundingBox.minY + 0.7D; diffY < ent.boundingBox.maxY; diffY += 0.1D) {
            if (myEyePos.distanceTo(new Location(ent.boundingBox.minX, diffY, ent.boundingBox.minZ)) < myEyePos.distanceTo(BestPos)) {
                BestPos = new Location(ent.boundingBox.minX, diffY, ent.boundingBox.minZ);
            }
        }
        diffY = BestPos.getY() - (Minecraft.getMinecraft().thePlayer.boundingBox.minY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(dX * dX + dZ * dZ);
        float pitch = (float)(-(Math.atan2(diffY - index.getValueState().doubleValue(), dist) * 180.0D / 3.141592653589793D));
        return new float[]{(float) yaw,pitch};
    }

}
