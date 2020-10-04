package cn.snowflake.rose;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.manager.CommandManager;
import cn.snowflake.rose.manager.FileManager;
import cn.snowflake.rose.manager.FontManager;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.utils.JReflectUtility;
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
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.UnknownServiceException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Client {
    public static String shitname =null;
    public static String name = "Season";
    public static String version = "0.4";
    public static Client instance;
    public static boolean init = false;
    private static boolean loaded =false;
    public boolean font = false;
    public FileManager fileMgr;
    public ModManager modManager;
    public CommandManager commandMgr;
    public FontManager fontManager;
    public static String username = null;
    public Set<String> inGameList;

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
                inGameList = checkFile();
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

    public static Set<String> checkFile() {
        Set<String> fileHash = new HashSet<>();
        LaunchClassLoader lwClassloader = (LaunchClassLoader) Client.class.getClassLoader();
        for (URL source : lwClassloader.getSources()) {
            String hash = getFileHash(source);
            System.out.println("loaded jar : "+source
            + "hash " + hash);

            if (hash != null) fileHash.add(hash);
        }
        return fileHash;
    }
    private static String getFileHash(URL url) {
        String fileName = new File(url.getFile()).getName();
        try {
            try (InputStream in = url.openStream()) {
                return calcHash(in) + "\0" + fileName;
            }
        } catch (UnknownServiceException e) {
            return null;
        } catch (IOException e) {
            System.out.println(e.toString());
            return "0000000000000000000000000000000000000000\0" + (fileName.isEmpty() ? "unknown" : fileName);
        }
    }

    public byte salt = 0;

    private static String calcHash(InputStream in) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");

            final byte[] buffer = new byte[4096];
            int read = in.read(buffer, 0, 4096);

            while (read > -1) {
                md.update(buffer, 0, read);
                read = in.read(buffer, 0, 4096);
            }

            byte[] digest = md.digest();
            return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest)).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean DEBUG = true;

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
        int catversion =2;

//        if (eventFMLChannels.iMessage.getClass().toString().contains("CPacketHelloReply")){// get shit of message
//            eventFMLChannels.setCancelled(true);
//            try {
//                Field fsalt = eventFMLChannels.iMessage.getClass().getDeclaredField("salt");
//                fsalt.setAccessible(true);
//                Field fversion = eventFMLChannels.iMessage.getClass().getDeclaredField("version");
//                fversion.setAccessible(true);
//                salt = fsalt.getByte(eventFMLChannels.iMessage);
//                catversion = fversion.getInt(eventFMLChannels.iMessage);
//                if (DEBUG){
//                    System.out.println("反作弊版本 : "+catversion + " salt值: " + salt);
//                }
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
        if (eventFMLChannels.iMessage.getClass().toString().contains("CPacketInjectDetect")){// 1.2.7 以下猫反
            eventFMLChannels.setCancelled(true); //     拦截检测注入包
            List<String> list = new ArrayList();
            try {
                eventFMLChannels.sendToServer(eventFMLChannels.iMessage.getClass().getDeclaredConstructor(List.class).newInstance(list));//发送无参packet
                if (DEBUG){
                    LogManager.getLogger().info("拦截成功注入检测包,已发送为无注入class");
                }
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if (eventFMLChannels.iMessage.getClass().toString().contains("CPacketFileHash")){
            eventFMLChannels.setCancelled(true);
            try {
                Field field = eventFMLChannels.iMessage.getClass().getDeclaredField("salt");
                field.setAccessible(true);
                try {
                    eventFMLChannels.sendToServer(eventFMLChannels.iMessage.getClass().getDeclaredConstructor(List.class,byte.class).newInstance(new ArrayList<String>(inGameList),field.getByte(eventFMLChannels.iMessage)));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                if (DEBUG){
                    LogManager.getLogger().info("修改成功mods检测包,已设置为无异常列表");
                }
            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }


    }

}
