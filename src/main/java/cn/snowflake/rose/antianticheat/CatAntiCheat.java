package cn.snowflake.rose.antianticheat;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.FORGE.ScreenProtect;
import cn.snowflake.rose.utils.antianticheat.CatAntiCheatHelper;
import cn.snowflake.rose.utils.antianticheat.ScreenhostHelper;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

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


    @EventTarget
    public void onFml(EventFMLChannels eventFMLChannels){
        //catanticheat
        if (eventFMLChannels.iMessage.getClass().toString().contains("cac") || eventFMLChannels.iMessage.getClass().toString().contains("luohuayu.anticheat.message")) {
            try {//filehash and classfound check
                Constructor<? extends IMessage> filehash = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(
                        List.class, byte.class);
                if (filehash != null) {
                    Field[] fields = eventFMLChannels.iMessage.getClass().getDeclaredFields();
                    Field fieldlist = fields[0];
                    Field salt = fields[1];
                    salt.setAccessible(true);
                    fieldlist.setAccessible(true);
                    try {
                        salt.getByte(eventFMLChannels.iMessage);//检查变量 是否 调换位置
                    } catch (IllegalAccessException e) {
                        fieldlist = fields[1];
                        salt = fields[0];
                    }
                    if (salt != null && fieldlist != null) {
                        try {
                            List<String> list = (List<String>) fieldlist.get(eventFMLChannels.iMessage);
                            if (list.size() > 20) {
                                eventFMLChannels.setCancelled(true);
                                list.removeIf(inject ->
                                        inject.toString().endsWith(".tmp")
                                );
                                list.removeIf(mod ->
                                        mod.toString().toLowerCase().endsWith("-skipverify.jar")
                                );
                                eventFMLChannels.sendToServer((IMessage) filehash.newInstance(new ArrayList<>(list),
                                        salt.getByte(eventFMLChannels.iMessage)));
                            } else {
                                eventFMLChannels.sendToServer((IMessage) filehash.newInstance(new ArrayList<>(list),
                                        salt.getByte(eventFMLChannels.iMessage)));
                            }
                        } catch (Exception e1) {
                        }
                    }
                }
            } catch (NoSuchMethodException eee) {
                List<String> list = new ArrayList<String>();
                try {//injectdetect check
                    Constructor<? extends IMessage> injectdetect = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(
                            List.class);
                    if (injectdetect != null) {
                        try {
                            eventFMLChannels.setCancelled(true);
                            eventFMLChannels.sendToServer((IMessage) injectdetect.newInstance(list));
                        } catch (InstantiationException e) {
                        } catch (IllegalAccessException e) {
                        } catch (InvocationTargetException e) {
                        }
                    }
                } catch (NoSuchMethodException e) {
                    try {// screenhost check
                        Constructor<? extends IMessage> screenhost = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(
                                boolean.class, byte[].class);
                        if (screenhost != null) {
                            eventFMLChannels.setCancelled(true);
                            ByteArrayInputStream in = null;
                            ArrayList<Module> close = new ArrayList<>();

                            if (ScreenProtect.mode.isCurrentMode("CloseModule")) {
                                for (Module m : ModManager.modList) {
                                    if (m.getCategory() == Category.RENDER) {
                                        m.set(false);
                                        close.add(m);//添加已经关闭的功能
                                    }
                                }
                                in = new ByteArrayInputStream(CatAntiCheatHelper.screenshot());
                            }

                            if (ScreenProtect.mode.isCurrentMode("Custom")) {
                                if (ScreenhostHelper.catanticheatImage != null) {
                                    in = new ByteArrayInputStream(ScreenhostHelper.catanticheatImage);
                                } else {
                                    ScreenhostHelper.catanticheatImage = ScreenhostHelper.catAnticheatScreenhost();
                                    in = new ByteArrayInputStream(ScreenhostHelper.catanticheatImage);
                                }
                            }


                            try {
                                byte[] networkData = new byte[32763];
                                int size;
                                while ((size = in.read(networkData)) >= 0) {
                                    try {
                                        if (networkData.length == size) {
                                            eventFMLChannels.sendToServer((IMessage) screenhost.newInstance(
                                                    in.available() == 0, networkData));
                                        } else {
                                            eventFMLChannels.sendToServer((IMessage) screenhost.newInstance(
                                                    in.available() == 0, Arrays.copyOf(networkData, size)));
                                        }
                                        if (close != null) { //重新打开 关闭掉的功能
                                            for (Module c : close) {
                                                c.set(true);
                                            }
                                            close.clear();
                                        }
                                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException instantiationException) {
                                    }
                                }
                            } catch (IOException evv) {
                            }
                        }
                    } catch (NoSuchMethodException noSuchMethodException) {
                        try {    //lighting and transparentTexture check
                            Constructor<? extends IMessage> datacheck = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(
                                    boolean.class, boolean.class);
                            if (datacheck != null) {
                                eventFMLChannels.setCancelled(true);
                                try {
                                    eventFMLChannels.sendToServer((IMessage) datacheck.newInstance(
                                            false, false));
                                } catch (Exception e1) {
                                    e1.printStackTrace();
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
