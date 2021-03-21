package cn.snowflake.rose.antianticheat;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.mod.mods.FORGE.ScreenProtect;
import cn.snowflake.rose.utils.antianticheat.ScreenshotUtil;
import cn.snowflake.rose.utils.client.ChatUtil;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.MathHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Decimation {
    Minecraft mc = Minecraft.getMinecraft();

    public Decimation(){
        EventManager.register(this);
    }


        @EventTarget
        public void onFml(EventFMLChannels eventFMLChannels) {
                if (Client.deci1_21_9f ? eventFMLChannels.iMessage.toString().contains("deci.aE.a$af") : //true
                        eventFMLChannels.iMessage.toString().contains("deci.aE.a$ab") //false
                ) {
                    if (ScreenProtect.mode.isCurrentMode("leave") && mc.theWorld != null){
                        eventFMLChannels.setCancelled(true);
                        mc.theWorld.sendQuittingDisconnectingPacket();
                        mc.loadWorld(null);
                        this.mc.displayGuiScreen(new GuiMainMenu());
                        return;
                    }
                Constructor<?> constructor = null;
                try {
                   constructor = eventFMLChannels.iMessage.getClass().getConstructor(int.class,int.class,ByteBuf.class);

                   Field fceiling_float_int =
                           Client.deci1_21_9f ? eventFMLChannels.iMessage.getClass().getDeclaredField("ayR")
                           :
                           eventFMLChannels.iMessage.getClass().getDeclaredField("axM")
                           ;

                   Field fi = Client.deci1_21_9f ? eventFMLChannels.iMessage.getClass().getDeclaredField("ayQ") : eventFMLChannels.iMessage.getClass().getDeclaredField("axL");

                   Field fbytebuf = Client.deci1_21_9f ? eventFMLChannels.iMessage.getClass().getDeclaredField("ayZ") : eventFMLChannels.iMessage.getClass().getDeclaredField("axU");

                   fceiling_float_int.setAccessible(true);
                   fi.setAccessible(true);
                   fbytebuf.setAccessible(true);


                   int max = fceiling_float_int.getInt(eventFMLChannels.iMessage);
                   int step = fi.getInt(eventFMLChannels.iMessage);

                   if (max != step){
                       eventFMLChannels.setCancelled(true);
//                       ChatUtil.sendClientMessage(max + " - " +step);
                       if ((max - 1) == step){
                           //图片数据
                           BufferedImage bufferedImage = null;
                           if (ScreenProtect.mode.isCurrentMode("Custom")){
                               if (ScreenshotUtil.bufferedImage != null){
                                   bufferedImage = ScreenshotUtil.bufferedImage;//从计算机中获取图片
                               }else {
                                   ScreenshotUtil.bufferedImage = ScreenshotUtil.getDeciScreenhost();
                                   bufferedImage = ScreenshotUtil.bufferedImage;
                               }
                           }
                           ByteBuf buffer = Unpooled.buffer();
                           if (bufferedImage.getData().getDataBuffer().getSize() / 1024 < 5000) {
                               ImageIO.write(bufferedImage, "PNG", (OutputStream)new ByteBufOutputStream(buffer));
                               for (int ceiling_float_int = MathHelper.ceiling_float_int(buffer.readableBytes() / 32763.0f), i = 0; i < ceiling_float_int; ++i) {
                                   int n = i * 32763;
                                   eventFMLChannels.sendToServer((IMessage) constructor.newInstance(
                                           ceiling_float_int,
                                           i,
                                           buffer.slice(n, Math.min(buffer.readableBytes() - n, 32763)) )
                                   );
                               }
                           }
                       }
                   }
               } catch (Exception e) {
                    ChatUtil.sendClientMessage(e.getMessage());
               }
            }
        }

}
