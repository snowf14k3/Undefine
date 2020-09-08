package cn.snowflake.rose.mod.mods.COMBAT;

import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

import java.util.ArrayList;

public class Blink extends Module {
    public ArrayList<Packet> packetlist = new ArrayList<Packet>();
    public Blink() {
        super("Blink", Category.COMBAT);
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket){
        Packet p = eventPacket.getPacket();
        if(p instanceof C03PacketPlayer ||
                p instanceof C03PacketPlayer.C04PacketPlayerPosition ||
                p instanceof C03PacketPlayer.C05PacketPlayerLook ||
                p instanceof C03PacketPlayer.C06PacketPlayerPosLook ||
                p instanceof C02PacketUseEntity ||
                p instanceof C08PacketPlayerBlockPlacement){
            packetlist.add(p);
            eventPacket.setCancelled(true);
        }
    }

    @Override
    public void onDisable() {
        for (Packet p : packetlist){
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(p);
        }
        packetlist.clear();
        super.onDisable();
    }
}
