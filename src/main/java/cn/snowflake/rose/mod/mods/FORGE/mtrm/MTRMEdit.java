package cn.snowflake.rose.mod.mods.FORGE.mtrm;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.notification.Notification;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MTRMEdit extends Module {

    public MTRMEdit(){
        super("MTRMEdit","MTRM Edit", Category.FORGE);
        try {
            Class.forName("net.doubledoordev.mtrm.network.MessageSend");
         } catch (Throwable ex) {
         this.setWorking(false);
        }
    }


    @Override
    public String getDescription() {
        return "修改服务器合成(在.m/MTRMEditScript.cfg写入合成)!";
    }

    @Override
    public void onEnable() {
        final File file = new File(mc.mcDataDir, "MTRMEditcript.cfg");
        final ByteBuf buf = Unpooled.buffer();
        boolean shapeless = false;
        boolean remove = false;
        boolean advanced = false;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            List<String> contents = FileUtils.readLines(file);
            block14: for (String content1 : contents) {
                String addition;
                String content;
                advanced = content1.startsWith("advanced:");
                if (advanced) {
                    String[] c1 = content1.split("\\|");
                    content = c1[0];
                    addition = c1[1];
                } else {
                    content = content1;
                    addition = "";
                }
                content = content.replaceAll("[\\[\\]]", "");
                content = content.replaceAll(" ", "");
                String[] c = content.split(",");
                String[] s = c[0].split("\\(<");
                s[1] = "<" + s[1];
                c[c.length - 1] = c[c.length - 1].replace(");", "");
                s[1] = s[1].replace(");", "");
                String[] s1 = s[0].split("\\.");
                switch (s1[1]) {
                    case "addShaped": {
                        shapeless = false;
                        break;
                    }
                    case "addShapeless": {
                        shapeless = true;
                        break;
                    }
                    case "remove": {
                        remove = true;
                        break;
                    }
                    default: {
                        Client.instance.getNotificationManager().addNotification(this,"\247cScriptError!", Notification.Type.ERROR);
                        continue block14;
                    }
                }
                if (advanced) {
                    c[8] = c[8] + "]]);" + addition + "//";
                }
                buf.writeByte(0);
                buf.writeBoolean(remove);
                buf.writeBoolean(shapeless);
                buf.writeBoolean(false);
                buf.writeInt(c.length);
                c[0] = s[1];
                for (String item : c) {
                    boolean tag = item != null && !item.equals("null");
                    buf.writeBoolean(tag);
                    if (!tag) continue;
                    ByteBufUtils.writeUTF8String(buf, item);
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MTRM", buf));
            }
            Client.instance.getNotificationManager().addNotification(this,"Recipes send to server!", Notification.Type.ERROR);
            this.set(false);
        }
        catch (IOException e) {
            Client.instance.getNotificationManager().addNotification(this,"\247cScriptError!", Notification.Type.ERROR);
            this.set(false);
            e.printStackTrace();
        }
    }


}
