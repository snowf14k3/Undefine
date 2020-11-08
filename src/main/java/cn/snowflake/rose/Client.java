package cn.snowflake.rose;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.manager.CommandManager;
import cn.snowflake.rose.manager.FileManager;
import cn.snowflake.rose.manager.FontManager;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.ui.skeet.SkeetClickGui;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.UnicodeFontRenderer;
import cn.snowflake.rose.utils.verify.AntiReflex;
import cn.snowflake.rose.utils.verify.HWIDUtils;
import cn.snowflake.rose.utils.verify.ShitUtil;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AngelCodeFont;

import javax.swing.*;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static String shitname =null;
    public static String name = "Season";
    public static String version = "0.6g";
    public static Client instance;
    public static boolean init = false;
    public static UnicodeFontRenderer fs;
    public static UnicodeFontRenderer fss;
    private static boolean loaded =false;
    public static boolean canCancle =false;
    public boolean font = false;
    public FileManager fileMgr;
    public ModManager modManager;
    public CommandManager commandMgr;
    public FontManager fontManager;
    public static String username = null;


    public Client(){
        EventManager.register(this);
        username = HWIDUtils.getUserName();
        this.init = true;
        instance = this;
        if (!(HWIDUtils.version.contains(Client.version) && ShitUtil.contains(HWIDUtils.version,Client.version)) ){
            try {
                Class clazz = Class.forName("javax.swing.JOptionPane");
                String str1 = new String("未通过版本验证！请更新你滴版本");
                Method m = clazz.getDeclaredMethod("showInputDialog", Component.class, Object.class, Object.class);
                /**
                 *  第一个参数 是调用的 方法Object
                 */
                m.invoke(m, null, str1, HWIDUtils.version);
            } catch (ClassNotFoundException e) {
                LogManager.getLogger().error("NMSL");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                this.getClass().getClassLoader().loadClass(null);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (ManagementFactory.getRuntimeMXBean().getBootClassPath().split(";")[0].contains("\\lib\\"))
            if (HWIDUtils.version.contains(Client.version) && ShitUtil.contains(HWIDUtils.version,Client.version) && shitname == null || this.username == null && true && true && !false && !false && HWIDUtils.https.contains(HWIDUtils.getHWID()) &&  ShitUtil.contains(HWIDUtils.https, AntiReflex.getHWID())) {
                for (int i = 0; i < 9999; i++) {
                    LogManager.getLogger().error("NMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSL");
                }
                Display.setTitle("NMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSL");
            }else{
                this.modManager = new ModManager();

                this.commandMgr = new CommandManager();//Command

                this.fileMgr = new FileManager();

                if (Xray.block.size() == 0) {
                    for (Integer id : Xray.blocks) {
                        Block block = Block.getBlockById(id);
                        Xray.block.add(block);
                    }
                }

                loaded = true;

                Loader.instance().getModList().forEach(modContainer -> {
                    if (modContainer.getModId().equalsIgnoreCase("catanticheat") && modContainer.getVersion().equalsIgnoreCase("1.2.7")){
                        catanticheat = true;
                    }
                    if (modContainer.getModId().equalsIgnoreCase("customnpcs")){
                        customnpcs = true;
                    }
                    if (modContainer.getModId().equalsIgnoreCase("nshowmod")){
                        nshowmod = true;
                    }
                    if (modContainer.getModId().equalsIgnoreCase("deci")){
                        deci = true;
                    }
                    if (modContainer.getModId().equalsIgnoreCase("mw")){
                        mw = true;
                    }
                });

            }
    }



    public static boolean catanticheat = false;
    public static boolean catanticheatnetwork130 = false;

    public static boolean deci = false;
    public static boolean mw = false;

    public static boolean customnpcs = false;
    public static boolean nshowmod = false;// shit of number mob

    public static SkeetClickGui getSkeetClickGui() {
        return new SkeetClickGui();
    }


    @EventTarget
    public void onFml(EventFMLChannels eventFMLChannels){
//        Constructor constructor = null;// 1.3猫反
//        try {
//            constructor = eventFMLChannels.iMessage.getClass().getConstructor(List.class);
//        } catch (NoSuchMethodException e) {
//        }
//        if (constructor != null
//                && eventFMLChannels.iMessage.getClass().getInterfaces()[0].equals(IMessage.class)
//                && eventFMLChannels.iMessage.getClass().getInterfaces().length == 1
//                && eventFMLChannels.iMessage.getClass().getDeclaredFields().length == 1
//                && eventFMLChannels.iMessage.getClass().getDeclaredFields()[0].toString().contains("java.util.List")
//        ){
//            eventFMLChannels.setCancelled(true); //     拦截检测注入包
//            try {
//                eventFMLChannels.sendToServer(eventFMLChannels.iMessage.getClass().getDeclaredConstructor().newInstance());//发送无参packet
//            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
//            }
//        }
        //====================================================================================================================================
        if (eventFMLChannels.iMessage.getClass().toString().contains("CPacketInjectDetect")){// 1.2.7 以下猫反
            eventFMLChannels.setCancelled(true); //     拦截检测注入包
            List<String> list = new ArrayList();
            try {
                eventFMLChannels.sendToServer(eventFMLChannels.iMessage.getClass().getDeclaredConstructor(List.class).newInstance(list));//发送无参packet
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
