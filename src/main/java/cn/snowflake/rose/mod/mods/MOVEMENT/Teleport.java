package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.client.ChatUtil;
import cn.snowflake.rose.utils.path.TPUtil;
import cn.snowflake.rose.utils.Value;

import cn.snowflake.rose.utils.math.Vec3Util;
import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;


public class Teleport extends Module {
    public static Value modes = new Value("Teleport", "Mode", 0);
    public static boolean isTPPlayer;
    public static int x;
    public static int y;
    public static int z;

    public Teleport(){
        super("Teleport","Teleport", Category.MOVEMENT);
        modes.addValue("God");
    }
    @Override
    public void onEnable() {
        //sb disabler
        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.isFlying = true;
        playerCapabilities.allowFlying = true;
//		playerCapabilities.setFlySpeed((float) MathUtils.randomNumber(0.1, 9.0));
        mc.getNetHandler().addToSendQueue(new C0FPacketConfirmTransaction(0, (short)(-1), false));
        mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.17,mc.thePlayer.posY + 0.17, mc.thePlayer.posZ, true));
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06,mc.thePlayer.posY + 0.06, mc.thePlayer.posZ, true));
        mc.thePlayer.stepHeight = 0.0f;
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
    }
    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.625f;
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.isFlying = false;
        playerCapabilities.allowFlying = true;
        mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (x == 0 && y == 0 && z == 0){
            ChatUtil.sendClientMessage("pls input position");
            set(false);
            return;
        }
        mc.getNetHandler().addToSendQueue(new C0CPacketInput(0.0f, 0.0f, true, true));
        double lastY = mc.thePlayer.posY, downY = 0;
        for (Vec3Util vec3 : TPUtil.computePath(
                new Vec3Util(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                new Vec3Util(Teleport.x, Teleport.y, Teleport.z))) {
            if (vec3.getY() < lastY) {
                downY += (lastY - vec3.getY());
            }
            if (downY > 2.5) {
                downY = 0;
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(),
                        vec3.getY(), vec3.getY(), vec3.getZ(), true));
            } else {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(),
                        vec3.getY(), vec3.getY(), vec3.getZ(), false));
            }
            lastY = vec3.getY();
        }
        //Teleported
        mc.thePlayer.setPosition(x, y, z);
        ChatUtil.sendClientMessage("传送到"+x+" "+y+" "+z);
        x = 0;
        y = 0;
        z = 0;
        set(false);

    }



}
