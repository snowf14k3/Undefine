package cn.snowflake.rose.antianticheat;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.mod.mods.FORGE.ScreenProtect;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class Decimation {
    Minecraft mc = Minecraft.getMinecraft();

    public Decimation(){
        EventManager.register(this);
    }


    @EventTarget
    public void onFml(EventFMLChannels eventFMLChannels){
//        if (eventFMLChannels.iMessage.toString().contains("a$r")){
//            try {
//                Field field = eventFMLChannels.iMessage.getClass().getDeclaredField("awZ");
//                field.setAccessible(true);
//
//                Field field2 = eventFMLChannels.iMessage.getClass().getDeclaredField("UZ");
//                field2.setAccessible(true);
//
//                Field field3 = eventFMLChannels.iMessage.getClass().getDeclaredField("Va");
//                field3.setAccessible(true);
//
//                if (mc.thePlayer.isRiding()){
//                    if (mc.gameSettings.keyBindForward.getIsKeyPressed()){
//                        field2.set(eventFMLChannels.iMessage,100);
//                    }
//                }
//
////                ChatUtil.sendClientMessage("角度 :  "+field.getFloat(eventFMLChannels.iMessage) + " " + field2.getFloat(eventFMLChannels.iMessage) + " | " + field3.getFloat(eventFMLChannels.iMessage));
//
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//            }
//        }
        if (ScreenProtect.mode.isCurrentMode("leave") && mc.theWorld != null){
                if (eventFMLChannels.iMessage.toString().contains("deci.aE.a$ab")) {
                    eventFMLChannels.setCancelled(true);
                    mc.theWorld.sendQuittingDisconnectingPacket();
                    mc.loadWorld(null);
                    this.mc.displayGuiScreen(new GuiMainMenu());
                }
            }

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
