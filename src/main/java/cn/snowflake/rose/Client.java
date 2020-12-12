package cn.snowflake.rose;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.manager.CommandManager;
import cn.snowflake.rose.manager.FileManager;
import cn.snowflake.rose.manager.FontManager;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.mods.WORLD.IRC;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.ui.skeet.SkeetClickGui;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.UnicodeFontRenderer;
import cn.snowflake.rose.utils.verify.AntiReflex;
import cn.snowflake.rose.utils.verify.HWIDUtils;
import cn.snowflake.rose.utils.verify.ShitUtil;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AngelCodeFont;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
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
import java.util.*;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class Client {
    public static String shitname =null;
    public static String name = "Season";
    public static String version = "0.8";
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
        DEBUG = username.equalsIgnoreCase("SnowFlake");
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
                this.fontManager = new FontManager();
                
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
                if (!ModManager.getModByName("IRC").isEnabled()){
                    ModManager.getModByName("IRC").set(true);
                }
            }
    }



    public static boolean catanticheat = false;
    public static boolean catanticheatnetwork130 = false;

    public static boolean deci = false;
    public static boolean mw = false;

    public static boolean customnpcs = false;
    public static boolean nshowmod = false;// shit of number mob
    public static boolean DEBUG = false;

    public static SkeetClickGui getSkeetClickGui() {
        return new SkeetClickGui();
    }




    @EventTarget
    public void onupdate(EventUpdate e) throws NoSuchFieldException, IllegalAccessException {
        if (Minecraft.getMinecraft().thePlayer == null && Minecraft.getMinecraft().theWorld == null){
            Objects.requireNonNull(ModManager.getModByName("ServerCrasher")).set(false);
            Objects.requireNonNull(ModManager.getModByName("Aura")).set(false);
            Objects.requireNonNull(ModManager.getModByName("TPAura")).set(false);
        }
    }

    @EventTarget
    public void onFml(EventFMLChannels eventFMLChannels){
        if (eventFMLChannels.iMessage.getClass().toString().contains("cac")
                || eventFMLChannels.iMessage.getClass().toString().contains("luohuayu.anticheat.message")){
            List<String> list = new ArrayList();
            try {
                Constructor injectdetect = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(
                        List.class);
                if (injectdetect != null){
                    try {
                        eventFMLChannels.setCancelled(true);
                        eventFMLChannels.sendToServer((IMessage) injectdetect.newInstance(list));
                        LogManager.getLogger().info("\u005b\u004b\u0041\u0043\u005d\u0020\u68c0\u6d4b\u5230\u6ce8\u5165\u68c0\u6d4b\u8bf7\u6c42\uff0c\u5df2\u8fd4\u56de\u65e0\u6ce8\u5165");
                    } catch (InstantiationException e) {
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                }
            } catch (NoSuchMethodException e) {
                try {
                    Constructor screenhost = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(
                            boolean.class,byte[].class);
                    if (screenhost!= null){
                        eventFMLChannels.setCancelled(true);
                        ByteArrayInputStream in = new ByteArrayInputStream(screenshot());
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
                                } catch (InstantiationException instantiationException) {
                                } catch (IllegalAccessException illegalAccessException) {
                                } catch (InvocationTargetException invocationTargetException) {
                                }
                            }
                        } catch (IOException evv) {
                        }
                    }
                } catch (NoSuchMethodException noSuchMethodException) {
                }
            }
        }
    }

    public static byte[] screenshot() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
            JFileChooser jf = new JFileChooser();
            jf.setCurrentDirectory(new File(Minecraft.getMinecraft().mcDataDir.getParent()));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("\u6587\u4ef6(*.png,*.jpg,*.jpeg,*.bmp)", new String[]{"png","jpg","jpeg","bmp"});
            jf.setFileFilter(filter);
            int option = jf.showOpenDialog(jf);
            jf.setDialogTitle("\u8bf7\u5feb\u901f\u9009\u62e9\u4f60\u8981\u53d1\u9001\u7684\u622a\u56fe\u0028\u5c0f\u4e8e\u0031\u0036\u006d\u0029");
            if (option == 0) {
                File file = jf.getSelectedFile();
                if (file.exists()) {
                    try {
                        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(file));
                        ImageIO.write(bufferedImage, "png", gzipOutputStream);
                    } catch (IOException ioException) {
                    }
                }
            }
            gzipOutputStream.flush();
            gzipOutputStream.close();
        } catch (Exception ignored) {}

        return out.toByteArray();
    }

}
