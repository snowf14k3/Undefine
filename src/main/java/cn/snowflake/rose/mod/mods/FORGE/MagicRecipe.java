package cn.snowflake.rose.mod.mods.FORGE;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class MagicRecipe extends Module {
    public MagicRecipe() {
        super("MagicRecipe", "Magic Recipe", Category.FORGE);
        try {
            Class.forName((String)"com.anotherera.magicrecipe.common.network.packet.MinecraftRecipeChangePacket");
        }catch (Throwable ex) {
            working = false;
        }
    }

    @Override
    public void onEnable() {

        if (mc.thePlayer.openContainer instanceof ContainerWorkbench) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeBoolean(true);
            buf.writeBoolean(true);
            C17PacketCustomPayload packet = new C17PacketCustomPayload("anothermagicrecipe", buf);
            mc.thePlayer.sendQueue.addToSendQueue((Packet)packet);
        }
        set(false);
        super.onEnable();
    }
}
