package cn.snowflake.rose.mod.mods.COMBAT;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.management.FriendManager;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Aimbot extends Module {

    public static Value<Double> index = new Value<Double>("Aimbot_pitchIndex", 1.0, -0.5, 1.0, 0.01);

    public static Value<Double> range= new Value<Double>("Aimbot_Reach", 10.5D, 3.0D, 65.0D,0.1D);

    public Value<Double> fov = new Value<Double>("Aimbot_Fov", 10.0, 1.0, 180.0, 1.0);
    public Value<Boolean> throughwall = new Value<Boolean>("Aimbot_ThroughWall", false);

    public Value<Boolean> players = new Value<Boolean>("Aimbot_Player", true);
    public Value<Boolean> otherentity = new Value<Boolean>("Aimbot_ModsEntity", false);
    public Value<Boolean> animal = new Value<Boolean>("Aimbot_Animal", false);
    public Value<Boolean> moster = new Value<Boolean>("Aimbot_Mob", false);
    public Value<Boolean> village = new Value<Boolean>("Aimbot_village", false);
    public Value<Boolean> invisible = new Value<Boolean>("Aimbot_Invisible", false);
    public Value<Boolean> silent = new Value<Boolean>("Aimbot_Silent", false);

    public Value<String> sortingMode = new Value<String>("Aimbot","SortingMode", 0);

    public static EntityLivingBase target;

    public Aimbot() {
        super("Aimbot","Aim Bot", Category.COMBAT);
        this.sortingMode.addValue("Health");
        this.sortingMode.addValue("Distance");
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
        if (Client.DEBUG) {
            mc.fontRenderer.drawStringWithShadow(target.getClass().getName(),
                    100, 90, 16777215); // 测试模式画Entity信息
            mc.fontRenderer.drawStringWithShadow(target.toString(),
                    100, 100, 16777215); // 测试模式画Entity信息
        }
		drawCircle(res.getScaledWidth() / 2, res.getScaledHeight() / 2,
					fov.getValueState().floatValue() * 3.5f, 500, -1);
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


    @EventTarget
    public void onEvent(EventMotion em) {
        if (em.isPre()) {
            target = getTarget();
            if(shouldAim()){
                if (target != null) {
                    float[] rotations = getRotationByBoundingBox(target,range.getValueState().floatValue(),false);
//                    atuoSetPitchIndex(target);
                    if(silent.getValueState()){
                    	em.setYaw(rotations[0]);
                    	em.setPitch(rotations[1]);
                    }else{
                        mc.thePlayer.rotationYaw = rotations[0];
                        mc.thePlayer.rotationPitch = rotations[1];
                    }
                }

            }
        }else if (em.getEventType() == EventType.POST){

        }
    }
    public boolean atuoSetPitchIndex(Entity e){
        Vec3 vec1 = Vec3.createVectorHelper(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),mc.thePlayer.posZ);

        AxisAlignedBB box = e.boundingBox;
        Vec3 vec2 = Vec3.createVectorHelper(e.posX, e.posY + (e.getEyeHeight()/1.32F),e.posZ);
        double minx = e.posX - 0.25;
        double maxx = e.posX + 0.25;
        double miny = e.posY;
        double maxy = e.posY + Math.abs(e.posY - box.maxY) ;
        double minz = e.posZ - 0.25;
        double maxz = e.posZ + 0.25;
        boolean see =  mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
        if(see) {//露头
            index.setValueState(-0.5d);
            return true;
        }
        vec2 = Vec3.createVectorHelper(maxx,miny,minz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
        if(see) {
            Log.info("feet11");
            return true;
        }
        vec2 = Vec3.createVectorHelper(minx,miny,minz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;

        if(see) {
            Log.info("feet9");

            return true;
        }
        vec2 = Vec3.createVectorHelper(minx,miny,maxz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
        if(see) {
            Log.info("feet6");

            return true;
        }
        vec2 = Vec3.createVectorHelper(maxx,miny,maxz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
        if(see) {
            Log.info("feet1");

            return true;
        }
        vec2 = Vec3.createVectorHelper(maxx, maxy,minz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;

        if(see) {// ???????不知道怎么描述
            index.setValueState(1d);


            return true;
        }
        vec2 = Vec3.createVectorHelper(minx, maxy,minz);

        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
        if(see) {
            Log.info("feet2");

            return true;
        }
        vec2 = Vec3.createVectorHelper(minx, maxy,maxz - 0.1);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
        if(see) {
            Log.info("feet3");

            return true;
        }
        vec2 = Vec3.createVectorHelper(maxx, maxy,maxz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null? true:false;
        if(see) {
            Log.info("feet4");
            return true;
        }

        return false;
    }


    private boolean canTarget(Entity entity) {
        if(!mc.thePlayer.canEntityBeSeen(entity) && !throughwall.getValueState()) {
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
        if (entity instanceof EntityAnimal && !animal.getValueState()) {
            return false;
        }
        if ((entity instanceof EntitySlime || entity instanceof EntityMob )&& !moster.getValueState()) {
            return false;
        }
        if (entity instanceof EntityBat){
            return false;
        }
        if (entity instanceof EntityVillager && !village.getValueState()) {
            return false;
        }
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
        if ( (entity instanceof EntityCreature) && !otherentity.getValueState() ) {
            return false;
        }
        return entity != mc.thePlayer && entity.isEntityAlive();
    }

    public static float getYawChange(Entity entity) {
        double x = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double z = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity;
        if ((z < 0.0D) && (x < 0.0D)) {
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(z / x));
        } else {
            if ((z < 0.0D) && (x > 0.0D)) {
                yawToEntity = -90.0D + Math.toDegrees(Math.atan(z / x));
            } else {
                yawToEntity = Math.toDegrees(-Math.atan(x / z));
            }
        }

        return wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float) yawToEntity));
    }
    public static float wrapAngleTo180_float(float par0) {
        par0 %= 360.0F;

        if (par0 >= 180.0F) {
            par0 -= 360.0F;
        }

        if (par0 < -180.0F) {
            par0 += 360.0F;
        }

        return par0;
    }
//|| !(deci.getValueState() && Objects.requireNonNull(JReflectUtility.getGunItem()).isInstance(mc.thePlayer.inventory.getCurrentItem().getItem()))
    public boolean shouldAim(){
    	if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)) {
			return false;
		}
        if(mc.thePlayer.inventory.getCurrentItem() == null ){
            return false;
        }
        return true;
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
