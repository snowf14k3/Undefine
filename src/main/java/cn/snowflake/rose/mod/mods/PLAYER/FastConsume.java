package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.time.TimeHelper;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Random;

public class FastConsume extends Module {
    private Value mode = new Value("FastConsume", "Mode", 0);
    private boolean send;
    private TimeHelper timer = new TimeHelper();
    private Random random = new Random();

    public FastConsume() {
        super("FastConsume","FastConsume", Category.PLAYER);
        this.mode.mode.add("AAC");
        this.mode.mode.add("NCP");
        this.mode.mode.add("Vanilla");
    }

    @EventTarget
    public void onUpdate(EventMotion event) {
        if (event.getEventType() == EventType.PRE) {
            if (mc.gameSettings.keyBindUseItem.getIsKeyPressed()
            ) {
                if (this.send && this.timer.isDelayComplete((long) (1000 + this.random.nextInt(100)))) {
                    for (int i = 0; i < (this.mode.isCurrentMode("NCP") ? 17 : (this.mode.isCurrentMode("AAC") ? 6 : 50)); ++i) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    }

                    this.send = false;
                    this.timer.reset();
                }
            } else {
                this.send = true;
            }
        }
    }

}
