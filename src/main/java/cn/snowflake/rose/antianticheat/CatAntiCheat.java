package cn.snowflake.rose.antianticheat;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.mod.mods.FORGE.ScreenProtect;
import cn.snowflake.rose.utils.antianticheat.ScreenshotUtil;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatAntiCheat {

    public CatAntiCheat(){
        EventManager.register(this);
    }

    Minecraft mc = Minecraft.getMinecraft();

    @EventTarget
    public void onFml(EventFMLChannels eventFMLChannels){
        //catanticheat
        if (eventFMLChannels.iMessage.getClass().toString().contains("1710") || eventFMLChannels.iMessage.getClass().toString().contains("luohuayu.anticheat.message")) {
            try {//filehash and classfound check
                Constructor<? extends IMessage> fwithc = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(List.class, byte.class);
                if (fwithc != null) {
                        Field[] fields = eventFMLChannels.iMessage.getClass().getDeclaredFields();

                        Field fieldlist = null;
                        Field salt = null;

                        Field field1 = fields[0];
                        Field field2 = fields[1];

                        try{
                            field1.getByte(eventFMLChannels.iMessage);
                            salt = field1;
                            fieldlist = field2;
                        }catch(IllegalAccessException e){
                            salt = field2;
                            fieldlist = field1;
                        }

                        try {
                            List<String> list = ((List<String>) fieldlist.get(eventFMLChannels.iMessage));
                            System.out.println("list"+list);
                            if (list.size() > 30) {
                                eventFMLChannels.setCancelled(true);

                                list.removeIf(inject ->
                                        inject.toString().endsWith(".tmp")
                                );
                                list.removeIf(mod ->
                                        mod.toString().toLowerCase().endsWith("-skipverify.jar")
                                );

                                eventFMLChannels.sendToServer(
                                        fwithc.newInstance(new ArrayList<>(list),
                                        salt.getByte(eventFMLChannels.iMessage))
                                );

                            }else{
                                eventFMLChannels.setCancelled(true);
                                List<String> classifieds = new ArrayList<>();
                                eventFMLChannels.sendToServer(
                                        fwithc.newInstance(classifieds,
                                        salt.getByte(eventFMLChannels.iMessage))
                                );
                            }

                        } catch (Exception e1) {
                        }
                    }
            } catch (NoSuchMethodException eee) {
                List<String> list = new ArrayList<String>();
                try {//injectdetect check
                    Constructor<? extends IMessage> injectdetect = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(List.class);
                    try {
                        eventFMLChannels.setCancelled(true);
                        eventFMLChannels.sendToServer(injectdetect.newInstance(list));
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException ignored) {
                    }
                } catch (NoSuchMethodException e) {
                    try {// screenhost check
                        Constructor<? extends IMessage> screenhost = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(boolean.class, byte[].class);
                        if (screenhost != null) {

                            eventFMLChannels.setCancelled(true);
                            ByteArrayInputStream in = null;
                            if (ScreenProtect.mode.isCurrentMode("leave") && mc.theWorld != null){
                                mc.theWorld.sendQuittingDisconnectingPacket();
                                mc.loadWorld(null);
                                this.mc.displayGuiScreen(new GuiMainMenu());
                            }

                            if (ScreenProtect.mode.isCurrentMode("Custom")) {
                                if (ScreenshotUtil.catanticheatImage != null) {
                                    in = new ByteArrayInputStream(ScreenshotUtil.catanticheatImage);
                                } else {
                                    ScreenshotUtil.catanticheatImage = ScreenshotUtil.catAnticheatScreenhost();
                                    in = new ByteArrayInputStream(ScreenshotUtil.catanticheatImage);
                                }
                            }

                            try {
                                byte[] networkData = new byte[32763];
                                int size;
                                while ((size = in.read(networkData)) >= 0) {
                                    try {
                                        if (networkData.length == size) {
                                            eventFMLChannels.sendToServer(screenhost.newInstance(
                                                    in.available() == 0, networkData));
                                        } else {
                                            eventFMLChannels.sendToServer(screenhost.newInstance(
                                                    in.available() == 0, Arrays.copyOf(networkData, size)));
                                        }
                                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException instantiationException) {
                                    }
                                }
                            } catch (IOException evv) {
                            }

                        }
                    } catch (NoSuchMethodException noSuchMethodException) {

                        try {    //lighting and transparentTexture check
                            Constructor<? extends IMessage> datacheck = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(boolean.class, boolean.class);
                            if (datacheck != null) {
                                eventFMLChannels.setCancelled(true);
                                try {
                                    eventFMLChannels.sendToServer((IMessage) datacheck.newInstance(
                                            false, false));
                                } catch (Exception e1) {
                                }
                            }
                        } catch (NoSuchMethodException e1) {
                        }

                    }
                }
            }
        }
    }

}
