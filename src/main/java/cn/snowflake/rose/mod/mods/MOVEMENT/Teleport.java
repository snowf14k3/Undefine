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

    public Teleport() {
        super("Teleport", "Teleport", Category.MOVEMENT);
        modes.addValue("God");
    }

    @Override
    public void onEnable() {
        //sb disabler
        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.isFlying = true;
        playerCapabilities.allowFlying = true;
//		playerCapabilities.setFlySpeed((float) MathUtils.randomNumber(0.1, 9.0));
        mc.getNetHandler().addToSendQueue(new C0FPacketConfirmTransaction(0, (short) (-1), false));
        mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.17, mc.thePlayer.posY + 0.17, mc.thePlayer.posZ, true));
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06, mc.thePlayer.posY + 0.06, mc.thePlayer.posZ, true));
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
        if (x == 0 && y == 0 && z == 0) {
            ChatUtil.sendClientMessage("pls input position");
            set(false);
            return;
        }
        for (int i = 0; i < 20; i++) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        mc.thePlayer.sendQueue.addToSendQueue(new C0FPacketConfirmTransaction(0, (short) -1, false));
        //tp y
        for (double yPos = mc.thePlayer.posY; yPos < y - 3; yPos += 5) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, yPos, yPos, mc.thePlayer.posZ, true));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        for (double yPos = mc.thePlayer.posY; yPos > y + 3; yPos -= 5) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, yPos, yPos, mc.thePlayer.posZ, true));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        //tp x
        for (double xPos = mc.thePlayer.posX; xPos < x - 3; xPos += 5) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(xPos, mc.thePlayer.posY, mc.thePlayer.posY, mc.thePlayer.posZ, true));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        for (double xPos = mc.thePlayer.posX; xPos > x + 3; xPos -= 5) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(xPos, mc.thePlayer.posY, mc.thePlayer.posY, mc.thePlayer.posZ, true));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        //tp z
        for (double zPos = mc.thePlayer.posZ; zPos < z - 3; zPos += 5) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posY, zPos, true));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        for (double zPos = mc.thePlayer.posZ; zPos > z + 3; zPos -= 5) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posY, zPos, true));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        //tp
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x,y,y,z,true));
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x,y-0.5,y-0.5,z,false));
        ChatUtil.sendClientMessage("传送到" + x + " " + y + " " + z);
        x = 0;
        y = 0;
        z = 0;
        set(false);
    }
}
