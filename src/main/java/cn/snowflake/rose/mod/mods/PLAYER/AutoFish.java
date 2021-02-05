package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S29PacketSoundEffect;


public class AutoFish extends Module {
    public AutoFish() {
        super("AutoFish","Auto Fish", Category.PLAYER);
    }

    @EventTarget
    public void onPacket(EventPacket e){
        S29PacketSoundEffect packet = (S29PacketSoundEffect) e.getPacket();
        if(packet.func_149212_c().contains("random.splash")) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1500);
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                        return;
                    }catch (Exception e) {}
                }
            }.start();
        }
    }

}
