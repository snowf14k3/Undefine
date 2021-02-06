package cn.snowflake.rose.mod.mods.WORLD;


import java.util.Iterator;

import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.TimeHelper;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;


import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import java.util.concurrent.CopyOnWriteArrayList;

public class FakeLag extends Module
{
    Value<Double> lagValue;
    CopyOnWriteArrayList<C03PacketPlayer> packetList;
    TimeHelper lagHelper;
    C03PacketPlayer lastPacket;

    public FakeLag() {
        super("FakeLag", "Fake Lag", Category.WORLD);
        this.lagValue = new Value<Double>("FakeLag_Delay", 3000.0, 300.0, 5000.0);
        this.packetList = new CopyOnWriteArrayList<C03PacketPlayer>();
        this.lagHelper = new TimeHelper();
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getType() == EventType.RECIEVE) {
            Packet packet = eventPacket.getPacket();
            if (packet instanceof C03PacketPlayer) {
                C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) packet;
                if (this.packetList.contains(c03PacketPlayer)) {
                    this.packetList.remove(c03PacketPlayer);
                } else {
                    this.packetList.add(c03PacketPlayer);
                    eventPacket.setCancelled(true);
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (this.lagHelper.isDelayComplete(this.lagValue.getValueState().longValue())) {
            if (this.mc.theWorld.getEntityByID(-1) != null) {
                this.mc.theWorld.removeEntityFromWorld(-1);
            }
            for (C03PacketPlayer c03PacketPlayer : this.packetList) {
                this.mc.thePlayer.sendQueue.addToSendQueue(c03PacketPlayer);
                this.lastPacket = c03PacketPlayer;
            }
            if (this.lastPacket != null && this.mc.gameSettings.thirdPersonView != 0) {
                EntityOtherPlayerMP p_addEntityToWorld_2_ = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
                p_addEntityToWorld_2_.setPositionAndRotation(this.lastPacket.func_149464_c(), this.lastPacket.func_149467_d(), this.lastPacket.func_149472_e(), this.lastPacket.func_149462_g(), this.lastPacket.func_149470_h());
                p_addEntityToWorld_2_.inventory = this.mc.thePlayer.inventory;
                p_addEntityToWorld_2_.inventoryContainer = this.mc.thePlayer.inventoryContainer;
                p_addEntityToWorld_2_.rotationYawHead = this.mc.thePlayer.rotationYawHead;
                this.mc.theWorld.addEntityToWorld(-1, p_addEntityToWorld_2_);
            }
            this.lagHelper.reset();
        }
    }
}

