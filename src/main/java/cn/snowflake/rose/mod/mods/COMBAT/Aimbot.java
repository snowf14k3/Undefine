package cn.snowflake.rose.mod.mods.COMBAT;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.asm.ClassTransformer;
import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.manager.FriendManager;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.lang.instrument.ClassFileTransformer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Aimbot extends Module {


    public static Value<Double> range= new Value<Double>("Aimbot_Reach", 10.5D, 3.0D, 65.0D,0.1D);
    public Value<Boolean> bat = new Value("Aimbot_Bat", false);
    public Value<Boolean> key = new Value("Aimbot_KeySwitch", false);

    public Value<Boolean> customnpcs = new Value("Aimbot_CustomNPCs", false);
    public Value<Boolean> customnpcsteam = new Value("Aimbot_CustomNPCTeam", false);

    public Value<Boolean> decientity = new Value("Aimbot_DeciEntity", true);
    public Value<Boolean> deci = new Value("Aimbot_DeciGun", true);


    public Value<Boolean> throughwall = new Value("Aimbot_ThroughWall", false);

    public Value<Boolean> players = new Value("Aimbot_Player", true);
    public Value<Boolean> otherentity = new Value("Aimbot_OtherEntity", false);
    public Value<Boolean> animal = new Value("Aimbot_Animal", false);
    public Value<Boolean> moster = new Value("Aimbot_Mob", false);
    public Value<Boolean> village = new Value("Aimbot_village", false);
    public Value<Boolean> invisible = new Value("Aimbot_Invisible", false);
    public Value<String> aimmode = new Value("Aimbot","Mode", 4);
    public Value<Boolean> SILENT = new Value("Aimbot_Invisible", false);

    public Value<String> sortingMode = new Value("Aimbot","SortingMode", 0);

    public static EntityLivingBase target;

    public Aimbot() {
        super("Aimbot","Aim Bot", Category.COMBAT);
        this.aimmode.addValue("Head");
        this.aimmode.addValue("Neck");
        this.aimmode.addValue("Body");
        this.aimmode.addValue("Feet");
        this.aimmode.addValue("Auto");
        this.sortingMode.addValue("Health");
        this.sortingMode.addValue("Distance");
    }
    @Override
    public void onDisable(){
        target = null;
    }


    public static float[] getRotationByBoundingBox(Entity ent,float maxRange ,boolean random){
        if(ent == null)
            return new float[]{0,0};
        AxisAlignedBB boundingBox = ent.boundingBox;
        double boundingX = (boundingBox.maxX-boundingBox.minX)/4;
        double boundingY = (boundingBox.maxY-boundingBox.minY)/8;
        double boundingZ = (boundingBox.maxZ-boundingBox.minZ)/4;
        double orPosX = ent.boundingBox.minX,orPosY=ent.boundingBox.minY,orPosZ = ent.boundingBox.minZ;
        if(random){
            orPosX+=randomNumber(boundingX,-boundingX);
            orPosY+=randomNumber(boundingY,-boundingY);
            orPosZ+=randomNumber(boundingZ,-boundingZ);
        }
        double pX = Minecraft.getMinecraft().thePlayer.boundingBox.minX;
        double pY = Minecraft.getMinecraft().thePlayer.boundingBox.minY + (Minecraft.getMinecraft().thePlayer.boundingBox.minY+Minecraft.getMinecraft().thePlayer.boundingBox.maxY)/2;
        double pZ = Minecraft.getMinecraft().thePlayer.boundingBox.minZ;
        double dX = pX - orPosX;
        double dZ = pZ - orPosZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        Location BestPos = new Location(ent.boundingBox.minX, orPosY, ent.boundingBox.minZ);
        Location myEyePos = new Location(Minecraft.getMinecraft().thePlayer.boundingBox.minX, Minecraft.getMinecraft().thePlayer.boundingBox.minY+ (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.boundingBox.minZ);
        double diffY;
        for(diffY = ent.boundingBox.minY + 0.7D; diffY < ent.boundingBox.maxY - 0.1D; diffY += 0.1D) {
            if (myEyePos.distanceTo(new Location(ent.boundingBox.minX, diffY, ent.boundingBox.minZ)) < myEyePos.distanceTo(BestPos)) {
                BestPos = new Location(ent.boundingBox.minX, diffY, ent.boundingBox.minZ);
            }
        }
        diffY = BestPos.getY() - (Minecraft.getMinecraft().thePlayer.boundingBox.minY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(dX * dX + dZ * dZ);
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
        return new float[]{(float) yaw,pitch};
    }

    static double randomNumber(double start, double end) {
        return (Math.random() * (double)(start - end)) + end;
    }

    @EventTarget
    public void ontick(EventTick eventTick){
        if (Keyboard.getEventKeyState()) {
            if (this.isEnabled() && Keyboard.KEY_LCONTROL == (Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey())) {
                System.out.println(Keyboard.getEventKey());
            }
        }
    }

    @EventTarget
    public void on2D(EventRender2D eventRender2D){
        if (Client.DEBUG) {
            mc.fontRenderer.drawStringWithShadow(target.getClass().getName(),
                    100, 90, 16777215); // 测试模式画Entity信息
            mc.fontRenderer.drawStringWithShadow(target.toString(),
                    100, 100, 16777215); // 测试模式画Entity信息
        }
    }

    @EventTarget
    public void onEvent(EventMotion em) {
        if (em.isPre()) {
            target = getTarget();
            if(shouldAim()){

                if (target != null) {
                    float[] rotations = this.aimmode.isCurrentMode("Auto") ? getRotationByBoundingBox(target,range.getValueState().floatValue(),false) : getEntityRotations(target);
                    boolean silent = SILENT.getValueState();
                    if(silent){
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], mc.thePlayer.onGround));
                    }else{
                        mc.thePlayer.rotationYaw = rotations[0];
                        mc.thePlayer.rotationPitch = rotations[1];
                    }
//                    if(AUTO_FIRE.getValueState()){
//                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(),true);
////                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(),false);
//
////                        MouseUtil.sendClick(0,true);
//                    }
                }

            }
        }
    }
    private boolean canTarget(Entity entity) {
        if(!mc.thePlayer.canEntityBeSeen(entity) && !throughwall.getValueState()) {
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
        if (entity instanceof EntityMob && !moster.getValueState()) {
            return false;
        }
        if (entity instanceof EntityBat && !bat.getValueState()){
            return false;
        }
        if (entity instanceof EntityVillager && !village.getValueState()) {
            return false;
        }
        if (entity.isInvisible() && !invisible.getValueState()) {
            return false;
        }
        if (Client.nshowmod){
            if (Objects.requireNonNull(JReflectUtility.getEntityNumber()).isInstance(entity)){
                return false;
            }
        }

        if (Client.customnpcs) {
            if (Objects.requireNonNull(JReflectUtility.getNPCEntity()).isInstance(entity) ){
                if (!customnpcs.getValueState()){
                    return false;
                }
                if (!customnpcsteam.getValueState() && mc.thePlayer.isOnSameTeam((EntityLivingBase) entity)){
                    return false;
                }
            }
        }else{
            if (customnpcs.getValueState() || customnpcsteam.getValueState()){
                customnpcs.setValueState(false);
                customnpcsteam.setValueState(false);
                ChatUtil.sendClientMessage("You have no install the customeNPCs");
            }
        }

        if (Client.deci){
            if (Objects.requireNonNull(JReflectUtility.getCorpse()).isInstance(entity)){
                return false;
            }
            if (!Objects.requireNonNull(JReflectUtility.getDeciEntity()).isInstance(entity) && !decientity.getValueState()){
                return false;
            }
        }else{
            if (decientity.getValueState()){
                decientity.setValueState(false);
                ChatUtil.sendClientMessage("You have no install the Deci");
            }
        }
        if ( (entity instanceof EntityCreature) && !otherentity.getValueState() ) {
            return false;
        }
        return entity != mc.thePlayer && entity.isEntityAlive();
    }
//|| !(deci.getValueState() && Objects.requireNonNull(JReflectUtility.getGunItem()).isInstance(mc.thePlayer.inventory.getCurrentItem().getItem()))
    public boolean shouldAim(){
        if(mc.thePlayer.inventory.getCurrentItem() == null ){
            return false;
        }
        if(mc.thePlayer.isUsingItem())
            return true;
        return true;
    }


    private EntityLivingBase getTarget() {
        List<EntityLivingBase> loaded = new ArrayList();
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
        if (aimmode.isCurrentMode("Distance")) {
            loaded.sort((o1, o2) ->
                    (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer))
            );
        }
        if (aimmode.isCurrentMode("Health")){
            loaded.sort((o1, o2) ->
                    (int) (o1.getHealth() - o2.getHealth())
            );
        }
        EntityLivingBase target = loaded.get(0);
        return target;
    }



    public float[] getEntityRotations(Entity target) {
        double xDiff = target.posX - mc.thePlayer.posX;
        double yDiff = target.posY - mc.thePlayer.posY;
        double zDiff = target.posZ - mc.thePlayer.posZ;
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / 0.0 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        if (this.aimmode.isCurrentMode("Body")) {
            pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.CHEST.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        } else if (this.aimmode.isCurrentMode("Feet")) {
            pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.FEET.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        } else if (this.aimmode.isCurrentMode("Head") || (Client.mw && target instanceof EntityPlayer && JReflectUtility.getProning((EntityPlayer) target)) ) {
            pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.HEAD.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        }else if (this.aimmode.isCurrentMode("Neck")) {
            pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.HEAD.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
        }
        return new float[]{yaw, pitch};
    }

    private static enum HitLocation {
        AUTO(0.0),
        HEAD(1.0),
        CHEST(1.5),
        FEET(3.5),
        NECK(1.25);
        private double offset;

        private HitLocation(double offset, int n3, double d2) {
            this.offset = offset;
        }

        private HitLocation(double offset) {
            this.offset = offset;
        }

        public double getOffset() {
            return this.offset;
        }
    }

}
