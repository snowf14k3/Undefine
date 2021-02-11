package cn.snowflake.rose.antianticheat;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

public class DeciScreen {

    public DeciScreen(){
        EventManager.register(this);
    }


    @EventTarget
    public void onFml(EventFMLChannels eventFMLChannels){
//            if (eventFMLChannels.iMessage.toString().contains("deci.aE.a$ab")){
//            try {
//                Field fceiling_float_int = eventFMLChannels.iMessage.getClass().getDeclaredField("axM");
//                Field fi = eventFMLChannels.iMessage.getClass().getDeclaredField("axL");
//                Field fbytebuf = eventFMLChannels.iMessage.getClass().getDeclaredField("axU");
//                fceiling_float_int.setAccessible(true);
//                fi.setAccessible(true);
//                fbytebuf.setAccessible(true);
//                图片数据
//                BufferedImage bufferedImage = null;
//                    if (ScreenProtect.mode.isCurrentMode("Custom")){
//                        if (ScreenhostHelper.bufferedImage != null){
//                            bufferedImage = ScreenhostHelper.bufferedImage;//从计算机中获取图片
//                        }else {
//                            ScreenhostHelper.bufferedImage = ScreenhostHelper.getDeciScreenhost();
//                            bufferedImage = ScreenhostHelper.bufferedImage;
//                        }
//                    }
//                    ByteBuf buffer = Unpooled.buffer();
//                    if (bufferedImage.getData().getDataBuffer().getSize() / 1024 < 5000) {
//                        ImageIO.write(bufferedImage, "PNG", (OutputStream)new ByteBufOutputStream(buffer));
//                        for (int ceiling_float_int = MathHelper.ceiling_float_int(buffer.readableBytes() / 32763.0f), i = 0; i < ceiling_float_int; ++i) {
//                            int n = i * 32763;
//                            eventFMLChannels.sendToServer((IMessage) constructor.newInstance(
//                                    ceiling_float_int,
//                                    i,
//                                    buffer.slice(n, Math.min(buffer.readableBytes() - n, 32763)) )
//                            );
//                            fceiling_float_int.set(eventFMLChannels.iMessage,ceiling_float_int);
//                            fi.set(eventFMLChannels.iMessage,i);
//                            fbytebuf.set(eventFMLChannels.iMessage, buffer.slice(n, Math.min(buffer.readableBytes() - n, 32763)));
//                            ChatUtil.sendClientMessage("执行了" + ceiling_float_int + "次");
//
//                        }
//                    }
//            } catch (NoSuchFieldException ignored) {
//            }
//            }


    }

}
