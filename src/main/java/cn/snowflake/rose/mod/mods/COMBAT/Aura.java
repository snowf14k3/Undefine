package cn.snowflake.rose.mod.mods.COMBAT;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.asm.ClassTransformer;
import cn.snowflake.rose.asm.MinecraftHook;
import cn.snowflake.rose.events.impl.EventAura;
import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.manager.FriendManager;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import com.darkmagician6.eventapi.types.EventType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 *
 * @author TAIFENG
 *
 */
public class Aura extends Module {
    private TimeHelper time = new TimeHelper();
    public TimeHelper timer2 = new TimeHelper();
    private TimeHelper switchtime = new TimeHelper();
    public static EntityLivingBase target;
    public Value<Boolean> players = new Value("Aura_Player", true);
    private static int switchIndex;
    public Value<Boolean> otherentity = new Value("Aura_Otherentity", true);
    public Value<Boolean> animal = new Value("Aura_Animal", false);
    public Value<Boolean> moster = new Value("Aura_Mob", false);
    public Value<Boolean> village = new Value("Aura_village", false);
    public Value<Boolean> invisible = new Value("Aura_Invisible", false);
    public Value<Boolean> block = new Value("Aura_AutoBlock", true);
    public Value<Double> switchdelay= new Value<Double>("Aura_SwitchDelay", 200.0D, 1.0D, 1000.0D,10.0D);
    public static Value<Double> range= new Value<Double>("Aura_Reach", 4.5D, 1.0D, 7.0D,0.1D);
    public Value<Double> cps = new Value<Double>("Aura_CPS", 12.0D, 1.0D, 20.0D, 1.0D);
    public Value<String> mode = new Value<String>("Aura","Mode", 0);
    public Value<Boolean> wall = new Value<Boolean>("Aura_ThroughWall", true);

    public Value<Boolean> customnpcs = new Value("Aura_CustomNPCs", false);
    public Value<Boolean> customnpcsteam = new Value("Aura_CustomNPCTeam", false);

    public Aura() {
        super("Aura","Aura", Category.COMBAT);
        this.mode.addValue("Single");
        this.mode.addValue("Switch");
    }

    @SubscribeEvent
    public void On2D(RenderGameOverlayEvent e) {
        if(target !=null) {
            ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int height = sr.getScaledHeight() / 2 - 20;
            int width = sr.getScaledWidth() / 2 + 15;
//		    RenderUtil.drawRect(width - 2, height, sr.getScaledWidth() / 2 + 80, height + mc.fontRenderer.FONT_HEIGHT + 2, ClientUtil.reAlpha(Colors.BLACK.c, 0.4f));
            mc.fontRenderer.drawStringWithShadow(target.getCommandSenderName() + " \2474\u2764\247f" + Math.round(target.getHealth()), width,  height + 2, -1);
            RenderUtil.drawEntityOnScreen(width - 10, height + 20, 20, target.rotationYaw,9, target);
        }
    }

    @EventTarget
    public void OnPre(EventMotion e) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (e.isPre()) {
            target = getTarget(range.getValueState());
            if (target != null) {
                if (block.getValueState() && !mc.thePlayer.isBlocking() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                    startAutoBlock();
                }
            } else {
                if (block.getValueState() && mc.thePlayer.isBlocking() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                    stopAutoBlock();
                }
            }
            if (mode.getModeName().equalsIgnoreCase("Single")) {
                setDisplayName("Single");
                target = getTarget(range.getValueState());
                if (target == null) {
                    return;
                }
                float[] rotations = RotationUtil.getRotations(target);
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], mc.thePlayer.onGround));
                mc.thePlayer.rotationYawHead = rotations[0];
                mc.thePlayer.renderYawOffset = rotations[0];
//                e.setYaw(rotations[0]);
//                e.setPitch(rotations[1]);
//                mc.thePlayer.rotationYawHead = e.getYaw();
//                mc.thePlayer.renderYawOffset = e.getYaw();
//                MinecraftHook.serverRotation = new Rotation(e.getYaw(),e.getPitch());
            }
            if (mode.getModeName().equalsIgnoreCase("Switch")) {
                setDisplayName("Switch");
                if (this.getTargets().size() <= 0) {
                    target = null;
                    return;
                }
                if (switchIndex >= this.getTargets().size()) {
                    switchIndex = 0;
                }
                target = (EntityLivingBase) this.getTargets().get(switchIndex);
                if (this.switchtime.isDelayComplete(switchdelay.getValueState().longValue())) {
                    ++switchIndex;
                    this.switchtime.reset();
                }
                float[] rotations = RotationUtil.getRotations(target);
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], mc.thePlayer.onGround));
                mc.thePlayer.rotationYawHead = rotations[0];
                mc.thePlayer.renderYawOffset = rotations[0];
//                e.setYaw(rotations[0]);
//                e.setPitch(rotations[1]);
//                mc.thePlayer.rotationYawHead = e.getYaw();
//                mc.thePlayer.renderYawOffset = e.getYaw();
//                MinecraftHook.serverRotation = new Rotation(e.getYaw(),e.getPitch());
            }
        }else if (e.getEventType() == EventType.POST){
            if(target != null) {
                DoAttack();
            }
        }
    }

    @Override
    public void onDisable() {
        stopAutoBlock();
        target = null;
        super.onDisable();
    }
//    private void startAutoBlock() {
//        mc.getNetHandler().addToSendQueue(
//                new C08PacketPlayerBlockPlacement(
//                        -1,
//                        -1,
//                        -1,
//                        255,
//                        mc.thePlayer.getCurrentEquippedItem(),
//                        0,
//                        0,
//                        0));
//    }
//    private void stopAutoBlock() {
//        mc.getNetHandler().addToSendQueue((
//                new C07PacketPlayerDigging(
//                        5,
//                        -1,
//                        -1,
//                        -1,
//                        0)));
////        this.mc.thePlayer.sendQueue.addToSendQueue((Packet) new C07PacketPlayerDigging(5, 0,0,0, 0));
////        if (target == null){
////            mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 0);
////        }
//    }
    private void startAutoBlock() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(),true);
        this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem());
    }
    private void stopAutoBlock() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(),false);

        this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);

    }



    private List getTargets() {
        try {
            ArrayList<EntityLivingBase> targets = new ArrayList();
            for(Object entity : mc.theWorld.loadedEntityList) {
                if(canTarget((Entity) entity) && entity instanceof EntityLivingBase) {
                    targets.add((EntityLivingBase) entity);
                }
            }
            targets.sort((o1, o2) -> {
                float[] rot1 = RotationUtil.getRotations(o1);
                float[] rot2 = RotationUtil.getRotations(o2);
                return (int)(mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
            });
            return targets;
        } catch (Exception e) {
            return null;
        }
    }

    private void DoAttack() {
        int aps = ((Double)this.cps.getValueState()).intValue();
        int DelayValue = 1000 / aps + this.RANDOM.nextInt(50) - 30;
        if ((double) mc.thePlayer.getDistanceToEntity(target) <= this.range.getValueState().floatValue() && this.timer2.isDelayComplete((DelayValue - 20 + this.RANDOM.nextInt(50)))) {
            this.atttack();
        }
    }
    private void atttack() {
        if (block.getValueState() && mc.thePlayer.isBlocking()&& mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            stopAutoBlock();
        }
        EventAura ea = new EventAura(target);
        EventManager.call(ea);

        if (!mc.thePlayer.isBlocking() && block.getValueState()) {
            mc.thePlayer.swingItem();
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        }else if (!block.getValueState()) {
            mc.thePlayer.swingItem();
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        }
        if (block.getValueState() && !mc.thePlayer.isBlocking() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            startAutoBlock();
        }
    }

    private EntityLivingBase getTarget(double range) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        double dist = range;
        EntityLivingBase target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                Entity player =  entity;
                if (canTarget(player)) {
                    double currentDist = mc.thePlayer.getDistanceToEntity(player);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = (EntityLivingBase) player;
                    }
                }
            }
        }
        return target;
    }

    private boolean canTarget(Entity entity) {
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
        if (!ModManager.getModByName("NoFriend").isEnabled() && FriendManager.isFriend(entity.getCommandSenderName())){
            return false;
        }
        if(!mc.thePlayer.canEntityBeSeen(entity) && !wall.getValueState().booleanValue()) {
            return false;
        }
        if (entity instanceof EntityPlayer && !players.getValueState()) {
            return false;
        }
        if (entity instanceof EntityAnimal && !animal.getValueState()) {
            return false;
        }
        if (entity instanceof EntityMob && !moster.getValueState()) {
            return false;
        }
        if (entity instanceof EntityVillager && !village.getValueState()) {
            return false;
        }
        if (entity.isInvisible() && !invisible.getValueState()) {
            return false;
        }
//        if (!(entity instanceof EntityMob || entity instanceof EntityAnimal)
//                && entity instanceof EntityCreature
//                && !otherentity.getValueState()) {
//            return false;
//        }
        return entity != mc.thePlayer && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= range.getValueState();
    }

    private final Random RANDOM = new Random();

    public int random(double min, double max) {
        return (int) (RANDOM.nextInt((int) (max - min)) + min);
    }

}
