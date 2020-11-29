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
    public void autoFish(S12PacketEntityVelocity ev) {

    }
    @EventTarget
    public void onPacket(EventPacket e){
        S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();
                if(mc.thePlayer.getCurrentEquippedItem() != null) {
                    if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFishingRod) {
                        if(packet.func_149411_d() == 0 && packet.func_149409_f() == 0 && packet.func_149410_e() < 0) {
                            Entity ev = mc.theWorld.getEntityByID(packet.func_149412_c());
                            if(ev instanceof EntityFishHook) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, 255, (ItemStack)null, 0.0F, 0.0F, 0.0F));
                                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, 255, (ItemStack)null, 0.0F, 0.0F, 0.0F));
                            }
                        }
                    }
        }
    }

}
