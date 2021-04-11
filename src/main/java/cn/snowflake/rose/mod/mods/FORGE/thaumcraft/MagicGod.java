package cn.snowflake.rose.mod.mods.FORGE.thaumcraft;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.util.ArrayList;
import java.util.Iterator;

public class MagicGod extends Module {
    public MagicGod() {
        super("MagicGod", "Magic God", Category.FORGE);
        try {
            Class.forName("thaumcraft.api.aspects.Aspect");
            Class.forName("thaumcraft.common.Thaumcraft");
        } catch (Exception var2) {
            this.working = false;
        }
    }

    @Override
    public String getDescription() {
        return "封包解锁神秘所有要素!";
    }

    @Override
     public void onEnable() {

        try {
           ArrayList aspects = (ArrayList)Class.forName("thaumcraft.api.aspects.Aspect").getMethod("getCompoundAspects").invoke((Object)null);
           Iterator var2 = aspects.iterator();

           while(var2.hasNext()) {
              Object aspect = var2.next();
              Object aspect1 = ((Object[])((Object[])Class.forName("thaumcraft.api.aspects.Aspect").getMethod("getComponents").invoke(aspect)))[0];
              Object aspect2 = ((Object[])((Object[])Class.forName("thaumcraft.api.aspects.Aspect").getMethod("getComponents").invoke(aspect)))[1];
              String a1 = (String)Class.forName("thaumcraft.api.aspects.Aspect").getMethod("getTag").invoke(aspect1);
              String a2 = (String)Class.forName("thaumcraft.api.aspects.Aspect").getMethod("getTag").invoke(aspect2);
              this.doGive(a1, a2);
           }

           this.set(false);
        } catch (Exception var8) {
        }

     }

    private void doGive(String a1, String a2) {
        ByteBuf buf = Unpooled.buffer(0);
        buf.writeByte(13);
        buf.writeInt(mc.thePlayer.dimension);
        buf.writeInt(mc.thePlayer.getEntityId());
        buf.writeInt(0);
        buf.writeInt(0);
        buf.writeInt(0);
        ByteBufUtils.writeUTF8String(buf, a1);
        ByteBufUtils.writeUTF8String(buf, a2);
        buf.writeBoolean(true);
        buf.writeBoolean(true);
        final C17PacketCustomPayload packet = new C17PacketCustomPayload("thaumcraft", buf);
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
    
}