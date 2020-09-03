package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.server.S29PacketSoundEffect;


public class AutoFish extends Module {
    public AutoFish() {
        super("AutoFish", Category.PLAYER);
    }

    @EventTarget
    public void onPacket(EventPacket e){
            S29PacketSoundEffect packet = (S29PacketSoundEffect) e.getPacket();
            if(packet.func_149212_c().contains("random.splash")) {
                if (mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod) {
                    this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem());
                    mc.thePlayer.swingItem();
                    this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem());
                    mc.thePlayer.swingItem();
                }
            }
    }

}
