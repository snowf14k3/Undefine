package cn.snowflake.rose.mod.mods.PLAYER;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.client.PlayerUtil;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoWeb extends Module {
    public NoWeb() {
        super("NoWeb","No Web", Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) throws IllegalAccessException {
//        JReflectUtility.setField(mc.thePlayer.getClass(),mc.thePlayer, ClassTransformer.runtimeDeobfuscationEnabled ? "field_70134_J": "isInWeb",false);
        if (mc.thePlayer.isInsideOfMaterial(Material.web)) {
            mc.thePlayer.motionY = 0;
            if (mc.thePlayer.moveStrafing != 0 || mc.thePlayer.moveForward != 0) {
                PlayerUtil.setSpeed(0.10F);
            }
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX,mc.thePlayer.boundingBox.minY, mc.thePlayer.posY + (mc.gameSettings.keyBindJump.isPressed() ? 0.0625 : mc.gameSettings.keyBindSneak.isPressed() ? -0.0625 : 0), mc.thePlayer.posZ + mc.thePlayer.motionZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX,mc.thePlayer.boundingBox.minY,
                    mc.theWorld.getHeight(), mc.thePlayer.posZ + mc.thePlayer.motionZ, true));
        }
    }

}
