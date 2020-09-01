package cn.snowflake.rose.mod.mods.PLAYER;
import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastUse extends Module {
    public boolean canBoost = false;

    public FastUse() {
        super("FastUse", Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventMotion event) {
        if (mc.thePlayer.getItemInUseDuration() == 16) {
            this.canBoost = true;
        }
        if (mc.thePlayer.onGround && this.canBoost && !(mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)) {
            this.canBoost = false;
            for (int i = 0; i < 20; ++i) {
                this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
            }
        }
    }
}
