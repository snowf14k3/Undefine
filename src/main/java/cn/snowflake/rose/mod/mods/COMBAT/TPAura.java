package cn.snowflake.rose.mod.mods.COMBAT;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.asm.ClassTransformer;
import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityCreature;
import net.minecraft.item.ItemSword;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class TPAura extends Module
{
    private double dashDistance;
    public Value<String> Mode;
    //    public Value<Boolean> ESP;
    public Value<Boolean> Tracers;
    public Value<Boolean> PATHESP;
    public Value<Boolean> PLAYERS;
    public Value<Boolean> ANIMALS;
    public Value<Boolean> INVISIBLES;
    public Value<Boolean> MOB;

    private Value<Double> MAXT;
    private Value<Double> RANGE;
    private Value<Double> CPS;
    private Value<Double> TIMER;
    public Value<Boolean> otherentity;
    private ArrayList<Vec3Util> path;
    private List<Vec3Util>[] test;
    private List<EntityLivingBase> targets;
    private TimeHelper cps;
    public static TimeHelper timer;
    public static boolean canReach;
    private Value<Boolean> block;
    private Value<Boolean> onground;

    public TPAura() {
        super("TPAura", Category.COMBAT);
        this.dashDistance = 5.0;
        this.Mode = new Value<String>("Tpaura", "Mode", 0);
//        this.ESP = new Value<Boolean>("Tpaura_ESP", true);
        this.Tracers = new Value<Boolean>("Tpaura_Tracers", false);
        this.MOB = new Value<Boolean>("Tpaura_Mob", false);
        block = new Value("TPAura_AutoBlock", true);
        onground = new Value("TPAura_OnGround", false);
        this.PATHESP = new Value<Boolean>("Tpaura_Path", true);
        this.PLAYERS = new Value<Boolean>("Tpaura_Players", true);
        this.ANIMALS = new Value<Boolean>("Tpaura_Animals", false);
        this.INVISIBLES = new Value<Boolean>("Tpaura_Invisible", true);
        this.MAXT = new Value<Double>("Tpaura_Maxtarget", 5.0, 1.0, 50.0, 1.0);
        this.RANGE = new Value<Double>("Tpaura_Reach", 30.0, 8.0, 100.0, 2.0);
        this.CPS = new Value<Double>("Tpaura_Cps", 8.0, 1.0, 20.0, 1.0);
        this.TIMER = new Value<Double>("Tpaura_Timer", 2.5, 0.1, 3.0, 0.1);
        this.otherentity = new Value<Boolean>("TPAura_OtherEntity", true);
//        this.customnpc = new Value<Boolean>("TPAura_Customnpc", true);
        this.path = new ArrayList<Vec3Util>();
        this.test = (List<Vec3Util>[])new ArrayList[50];
        this.targets = new CopyOnWriteArrayList<EntityLivingBase>();
        this.cps = new TimeHelper();
        this.Mode.mode.add("Vanilla");
        this.Mode.mode.add("Hypixel");
    }

    @Override
    public void onEnable() {
        TPAura.timer.reset();
//        this.targets.clear();
        if (this.Mode.isCurrentMode("Hypixel")) {
            if (this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically) {
                double x = this.mc.thePlayer.posX;
                double y = this.mc.thePlayer.posY;
                double z = this.mc.thePlayer.posZ;
                this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, this.mc.thePlayer.boundingBox.minY, y + 0.16, z, true));
                this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, this.mc.thePlayer.boundingBox.minY, y + 0.07, z, true));
                TPAura.canReach = false;
            }
            else {
                ChatUtil.sendClientMessage("Failed Tpaura.");
                this.set(false);
            }
        }
    }

    @EventTarget
    public void onPre(EventMotion em) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (this.Mode.isCurrentMode("Vanilla")) {
            this.setDisplayName("Vanilla");
        }

        int maxtTargets = this.MAXT.getValueState().intValue();
        int delayValue = 20 / this.CPS.getValueState().intValue() * 50;
        double hypixelTimer = this.TIMER.getValueState().doubleValue() * 1000.0;

        if (this.Mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
            if (!TPAura.canReach) {
                mc.thePlayer.motionX *= 0;
                mc.thePlayer.motionZ *= 0;
                mc.thePlayer.motionY *= 0;
                mc.thePlayer.onGround = false;
                mc.thePlayer.jumpMovementFactor = 0;
                return;
            }
            if (TPAura.timer.check((long)hypixelTimer)) {
                this.set(false);
            }
        }
        this.targets = this.getTargets();
        if (targets !=null) {
            if (block.getValueState() && !mc.thePlayer.isBlocking() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                startAutoBlock();
            }
        }else {
            if (block.getValueState() && mc.thePlayer.isBlocking()&& mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                stopAutoBlock();
            }
        }
        if (this.cps.isDelayComplete(delayValue) && this.targets.size() > 0 && (onground.getValueState().booleanValue() ? mc.thePlayer.onGround : true)) {
            this.test = (List<Vec3Util>[])new ArrayList[50];
            for (int i = 0; i < ((this.targets.size() > maxtTargets) ? maxtTargets : this.targets.size()); ++i) {
                EntityLivingBase T = this.targets.get(i);
                Vec3Util topFrom = new Vec3Util(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                Vec3Util to = new Vec3Util(T.posX, T.posY, T.posZ);
                this.path = this.computePath(topFrom, to);

                this.test[i] = this.path;
                for (Vec3Util pathElm : this.path) {
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), this.mc.thePlayer.boundingBox.minY, pathElm.getY(), pathElm.getZ(), true));
                }
                if (block.getValueState() && mc.thePlayer.isBlocking()&& mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                    stopAutoBlock();
                }
                this.mc.thePlayer.swingItem();
                this.mc.playerController.attackEntity((EntityPlayer)this.mc.thePlayer, (Entity)T);
                if (block.getValueState() && !mc.thePlayer.isBlocking() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                    startAutoBlock();
                }
                Collections.reverse(this.path);
                for (Vec3Util pathElm : this.path) {
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), this.mc.thePlayer.boundingBox.minY, pathElm.getY(), pathElm.getZ(), true));
                }

            }
            this.cps.reset();
        }
    }

    @Override
    public void onDisable() {
        stopAutoBlock();
        super.onDisable();
    }
    private void startAutoBlock() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(),true);

        this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem());
    }
    private void stopAutoBlock() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(),false);

        this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);

    }
    @EventTarget
    public void on3D(EventRender3D er) {
        int maxtTargets = MAXT.getValueState().intValue();
////        targets.size() == 0) &&
//        if (!(ESP.getValueState())) {
//            if (targets.size() > 0) {
//                for (int i = 0; i < (targets.size() > maxtTargets ? maxtTargets : targets.size()); i++) {
//                    int color = targets.get(i).hurtResistantTime > 15 ? Colors.getColor(new Color(255, 70, 70, 100)) : Colors.getColor(new Color(0, 70, 255, 100));
//                    drawESP(targets.get(i), color);
//                }
//            }
//        }
        if (!path.isEmpty() && PATHESP.getValueState()) {
            for (int i = 0; i < targets.size(); i++) {
                try {
                    if (test != null)

                        for (Vec3Util pos : test[i]) {
                            double n = pos.getX() - RenderManager.renderPosX;
                            double n2 = pos.getY() - RenderManager.renderPosY;
                            if (pos != null );
                        }

                    if (test != null && Tracers.getValueState().booleanValue()) {
                        glPushMatrix();
                        glDisable(GL_TEXTURE_2D);
                        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                        glEnable(GL_LINE_SMOOTH);
                        glEnable(GL_BLEND);
                        glDisable(GL_DEPTH_TEST);
                        mc.entityRenderer.disableLightmap(1);
                        glBegin(GL_LINE_STRIP);
                        RenderUtil.glColor(ColorUtil.getClickGUIColor().getRGB());
                        double renderPosX = RenderManager.instance.viewerPosX;
                        double renderPosY = RenderManager.instance.viewerPosY;
                        double renderPosZ = RenderManager.instance.viewerPosZ;
                        for(Vec3Util pos : test[i]) {
                            glVertex3d(pos.getX() - renderPosX, pos.getY()  - renderPosY, pos.getZ() - renderPosZ);
                        }
                        glColor4d(1, 1, 1, 1);
                        glEnd();
                        glEnable(GL_DEPTH_TEST);
                        glDisable(GL_LINE_SMOOTH);
                        glDisable(GL_BLEND);
                        glEnable(GL_TEXTURE_2D);
                        glPopMatrix();
                    }
                } catch (Exception e) {

                }
            }

            if (cps.check(1000)) {
                test = new ArrayList[50];
                path.clear();
            }
        }
    }

    @EventTarget
    public void onpack(EventPacket ep) {
        Packet p = ep.getPacket();
        if (this.Mode.isCurrentMode("Hypixel")) {
            if (p instanceof S08PacketPlayerPosLook && !TPAura.canReach) {
                TPAura.canReach = true;
                TPAura.timer.reset();
                S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)ep.getPacket();
            }
            if (p instanceof C03PacketPlayer && !TPAura.canReach) {
                ep.setCancelled(true);
            }
            if (p instanceof S08PacketPlayerPosLook) {
                if (TPAura.timer.getTime() < 10L || !TPAura.canReach) {
                    return;
                }
                ChatUtil.sendClientMessage("Tpaira-Disabled due to a lagback.");
                this.set(false);
                S08PacketPlayerPosLook s08PacketPlayerPosLook2 = (S08PacketPlayerPosLook)p;
            }
        }
    }

    private ArrayList<Vec3Util> computePath(Vec3Util topFrom, Vec3Util to) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!this.canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3Util lastLoc = null;
        Vec3Util lastDashLoc = null;
        ArrayList<Vec3Util> path = new ArrayList<Vec3Util>();
        ArrayList<Vec3Util> pathFinderPath = pathfinder.getPath();
        for (Vec3Util pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            }
            else {
                boolean canContinue = true;
                Label_0356: {
                    if (pathElm.squareDistanceTo(lastDashLoc) > this.dashDistance * this.dashDistance) {
                        canContinue = false;
                    }
                    else {
                        double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                        double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                        double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                        double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                        double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                        double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                        for (int x = (int)smallX; x <= bigX; ++x) {
                            for (int y = (int)smallY; y <= bigY; ++y) {
                                for (int z = (int)smallZ; z <= bigZ; ++z) {
                                    if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, onground.getValueState())) {
                                        canContinue = false;
                                        break Label_0356;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }

    private boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlock(pos.getX(), pos.getY(), pos.getZ());
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }

    private boolean canTarget(Entity entity) {
        if (!(entity instanceof EntityMob || entity instanceof EntityAnimal)
                && entity instanceof EntityCreature
                && !otherentity.getValueState()) {
            return false;
        }
        if (entity instanceof EntityPlayer && !PLAYERS.getValueState()) {
            return false;
        }
        if (entity instanceof EntityAnimal && !ANIMALS.getValueState()) {
            return false;
        }
        if (entity instanceof EntityMob && !MOB.getValueState()) {
            return false;
        }

        if (entity.isInvisible() && !INVISIBLES.getValueState()) {
            return false;
        }
        return entity != mc.thePlayer && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= RANGE.getValueState();
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

    static {
        TPAura.timer = new TimeHelper();
    }
}
