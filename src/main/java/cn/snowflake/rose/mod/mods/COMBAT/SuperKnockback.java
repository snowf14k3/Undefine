package cn.snowflake.rose.mod.mods.COMBAT;

import cn.snowflake.rose.events.impl.EventAura;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class SuperKnockback extends Module {
    private Value<Double> hurtTimeValue = new Value("SuperKnockback_HurtTime", 5d,0d,10d,1d);

    public SuperKnockback() {
        super("SuperKnockback", Category.COMBAT);
    }

    @EventTarget
    public void Eventaura(EventAura e) {
        if (e.getTarget() instanceof EntityLivingBase) {
            if (e.getTarget().hurtTime > hurtTimeValue.getValueState().intValue()){
                return;
            }
            if (mc.thePlayer.isSprinting()) {
                mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 4));
            }
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 3));
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 4));
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 3));
            mc.thePlayer.setSprinting(true);
            JReflectUtility.setwasSprinting(true);
        }
    }
}
