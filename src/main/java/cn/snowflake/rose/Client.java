package cn.snowflake.rose;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.events.impl.EventWorldChange;
import cn.snowflake.rose.management.CommandManager;
import cn.snowflake.rose.management.FileManager;
import cn.snowflake.rose.management.FontManager;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.FORGE.ScreenProtect;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.ui.skeet.SkeetClickGui;
import cn.snowflake.rose.ui.skeet.TTFFontRenderer;
import cn.snowflake.rose.utils.antianticheat.CatAntiCheatHelper;
import cn.snowflake.rose.utils.antianticheat.HXAntiCheatHelper;
import cn.snowflake.rose.utils.antianticheat.ScreenhostHelper;
import cn.snowflake.rose.utils.auth.AntiReflex;
import cn.snowflake.rose.utils.auth.HWIDUtils;
import cn.snowflake.rose.utils.auth.ShitUtil;
import cn.snowflake.rose.utils.client.ChatUtil;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.Gson;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Client {
    public static String shitname =null;
    public static String name = "Season";
    public static String version = "1.0";
    public static Client instance;
    public static boolean init = false;
    public static TTFFontRenderer fs;
    public static TTFFontRenderer fss;
    private static boolean loaded =false;
    public static boolean canCancle =false;
    public boolean font = false;
    public FileManager fileMgr;
    public ModManager modManager;
    public CommandManager commandMgr;
    public FontManager fontManager;
    public static String username = null;
    private static SkeetClickGui clickGui;

    public Client(){
        EventManager.register(this);
        username = HWIDUtils.getUserName();
        DEBUG = username.equalsIgnoreCase("SnowFlake");//Debug
        this.init = true;
        instance = this;
        if (!(HWIDUtils.version.contains(Client.version)
                && ShitUtil.contains(HWIDUtils.version,Client.version)) )
        {
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
            if (HWIDUtils.version.contains(Client.version)
                    && ShitUtil.contains(HWIDUtils.version,Client.version)
                    && shitname == null
                    || username == null && HWIDUtils.https.contains(HWIDUtils.getHWID())
                    && ShitUtil.contains(HWIDUtils.https, AntiReflex.getHWID())
            ) {
                for (int i = 0; i < 9999; i++) {
                    LogManager.getLogger().error("NMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSL");
                }
                Display.setTitle("NMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSLNMSL");
            }else{
                if (ShitUtil.contains(HWIDUtils.version,Client.version)) {
                    this.fontManager = new FontManager();
                }
                if (ShitUtil.contains(HWIDUtils.https, AntiReflex.getHWID())) {
                    this.modManager = new ModManager();

                    this.commandMgr = new CommandManager();//Command

                    this.fileMgr = new FileManager();
                }
                if (Xray.block.size() == 0) {
                    for (Integer id : Xray.blocks) {
                        Block block = Block.getBlockById(id);
                        Xray.block.add(block);
                    }
                }
                clickGui = new SkeetClickGui();
                loaded = true;

                for (ModContainer modContainer : Loader.instance().getModList())  {
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

                }

//                if (!ModManager.getModByName("IRC").isEnabled()){
//                    ModManager.getModByName("IRC").set(true);
//                }
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
        return clickGui;
    }

    @EventTarget
    public void ontick(EventWorldChange eventTick){
        Objects.requireNonNull(ModManager.getModByName("ServerCrasher")).set(false);
        Objects.requireNonNull(ModManager.getModByName("Aura")).set(false);
        Objects.requireNonNull(ModManager.getModByName("TPAura")).set(false);
        Objects.requireNonNull(ModManager.getModByName("Blink")).set(false);
        Objects.requireNonNull(ModManager.getModByName("Freecam")).set(false);
    }

    //copy from Hanabi
    public static boolean isGameInit = false;

    public static void onGameLoop() {
        isGameInit = true;
        WorldClient world = Minecraft.getMinecraft().theWorld;
        if (worldChange == null) {
            worldChange = world;
            return;
        }

        if (world == null) {
            worldChange = null;
            return;
        }

        if (worldChange != world) {
            worldChange = world;
            EventManager.call(new EventWorldChange());
        }
    }

    static int VL = 0;
    public static WorldClient worldChange;
    //copy from Hanabi

    @EventTarget
    public void onFml(EventFMLChannels eventFMLChannels){
        //hx anticheat
        if (eventFMLChannels.fmlProxyPacket != null){
            if (eventFMLChannels.fmlProxyPacket.channel().contains("AntiCheat")) {
                ChatUtil.sendClientMessage("[反反作弊] 已拦截傻逼HX反作弊检测");
                eventFMLChannels.setCancelled(true);//拦截
                Object[] encrypt = HXAntiCheatHelper.getHXPacketData(this.getClass().getClassLoader());
                if (encrypt.length == 0) {
                    return;
                }
                ByteBuf data = HXAntiCheatHelper.writeData(
                        -1262767116,
                        (HXAntiCheatHelper.currentTimeMillistoHexString() + "$" + (new Gson().toJson(Arrays.asList(encrypt)))).getBytes());
                FMLProxyPacket fpp = new FMLProxyPacket(data, "HX:AntiCheat");
                eventFMLChannels.sendToServer(fpp);
            }
        }
        //catanticheat
        if (eventFMLChannels.iMessage.getClass().toString().contains("cac")
                || eventFMLChannels.iMessage.getClass().toString().contains("luohuayu.anticheat.message")) {
            try {//filehash and classfound check
                Constructor<? extends IMessage> filehash = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(
                        List.class,byte.class);
                if (filehash != null){
                    Field[] fields = eventFMLChannels.iMessage.getClass().getDeclaredFields();
                    Field fieldlist = fields[0];
                    Field salt = fields[1];
                    salt.setAccessible(true);
                    fieldlist.setAccessible(true);
                    try {
                        salt.getByte(eventFMLChannels.iMessage);//检查变量 是否 调换位置
                    }catch (IllegalAccessException e) {
                        fieldlist = fields[1];
                        salt = fields[0];
                    }
                    if (salt != null && fieldlist !=null){
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
							}else {
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

                            if (ScreenProtect.mode.isCurrentMode("CloseModule")){
                                for (Module m : ModManager.modList){
                                    if (m.getCategory() == Category.RENDER){
                                        m.set(false);
                                        close.add(m);//添加已经关闭的功能
                                    }
                                }
                                in = new ByteArrayInputStream(CatAntiCheatHelper.screenshot());
                            }

                            if (ScreenProtect.mode.isCurrentMode("Custom")){
                                if (ScreenhostHelper.catanticheatImage != null){
                                    in = new ByteArrayInputStream(ScreenhostHelper.catanticheatImage);
                                }else {
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
                                        if (close != null){ //重新打开 关闭掉的功能
                                            for (Module c : close){
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
                    	try {	//lighting and transparentTexture check
							Constructor<? extends IMessage> datacheck = eventFMLChannels.iMessage.getClass().getDeclaredConstructor(
							        boolean.class, boolean.class);
							if(datacheck != null) {
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

        if (eventFMLChannels.iMessage.toString().contains("deci.aE.a$ab")){
            try {
                eventFMLChannels.setCancelled(true);
                Constructor<? extends IMessage> constructor = eventFMLChannels.iMessage.getClass().getConstructor(int.class,int.class,ByteBuf.class);
                constructor.setAccessible(true);

                try {
                    //图片数据
                    BufferedImage bufferedImage = null;
                    ArrayList<Module> close = new ArrayList<>();//关闭的mod


                    if (ScreenProtect.mode.isCurrentMode("CloseModule")){
                        for (Module m : ModManager.modList){
                            if (m.getCategory() == Category.RENDER || m.getClass().equals(Xray.class)){
                                m.set(false);
                                close.add(m);//添加已经关闭的功能
                            }
                        }
                        bufferedImage = ScreenhostHelper.getDeciScreenhost();//获取没有显示功能的图片
                    }

                    if (ScreenProtect.mode.isCurrentMode("Custom")){
                        if (ScreenhostHelper.bufferedImage != null){
                            bufferedImage = ScreenhostHelper.bufferedImage;//从计算机中获取图片
                        }else {
                            ScreenhostHelper.bufferedImage = ScreenhostHelper.getDeciScreenhost();
                            bufferedImage = ScreenhostHelper.bufferedImage;
                        }
                    }

                    final ByteBuf buffer = Unpooled.buffer();
                    if (bufferedImage.getData().getDataBuffer().getSize() / 1024 < 5000) {
                        ImageIO.write(bufferedImage, "PNG", (OutputStream)new ByteBufOutputStream(buffer));
                        for (int ceiling_float_int = MathHelper.ceiling_float_int(buffer.readableBytes() / 32763.0f), i = 0; i < ceiling_float_int; ++i) {
                            final int n = i * 32763;
                            eventFMLChannels.sendToServer((IMessage) constructor.newInstance(
                                    ceiling_float_int,
                                    i,
                                    buffer.slice(n, Math.min(buffer.readableBytes() - n, 32763)) )
                            );
                        }
                    }

                    if (close != null){ //重新打开 关闭掉的功能
                        for (Module c : close){
                            c.set(true);
                        }
                        close.clear();
                    }
                }catch (IOException | InstantiationException | InvocationTargetException ignored) {
//                    ChatUtil.sendClientMessage(ignored.getMessage());
                }
            } catch (IllegalAccessException | NoSuchMethodException ignored) {
            }
        }

    }





}
