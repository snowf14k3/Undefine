package cn.snowflake.rose;

import cn.snowflake.rose.antianticheat.CatAntiCheat;
import cn.snowflake.rose.antianticheat.Decimation;
import cn.snowflake.rose.antianticheat.HXAntiCheat;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.events.impl.EventWorldChange;
import cn.snowflake.rose.events.impl.Events;
import cn.snowflake.rose.management.CommandManager;
import cn.snowflake.rose.management.FileManager;
import cn.snowflake.rose.management.FontManager;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.mods.FORGE.PacketFlood;
import cn.snowflake.rose.mod.mods.WORLD.IRC;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.notification.Notification;
import cn.snowflake.rose.notification.NotificationManager;
import cn.snowflake.rose.ui.skeet.SkeetClickGui;
import cn.snowflake.rose.ui.skeet.TTFFontRenderer;
import cn.snowflake.rose.utils.client.ChatUtil;
import cn.snowflake.rose.utils.time.TimeHelper;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.injection.ClientLoader;
import net.minecraftforge.common.MinecraftForge;
import scala.collection.parallel.ParIterableLike;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Objects;

public class Client {
    public static String shitname =null;
    public static String name = "Season";
    public static String version = "1.1-fix";
    public static Client instance;
    public static boolean init = false;
    public static TTFFontRenderer fs;
    public static TTFFontRenderer fss;
    public static TTFFontRenderer cheaticons;
    public static boolean canCancle =false;
    public boolean font = false;
    public FileManager fileMgr;
    public ModManager modManager;
    public CommandManager commandMgr;
    public FontManager fontManager;
    public static String username = null;
    public static SkeetClickGui clickGui;
    public static boolean chinese ;
    public NotificationManager notificationmgr = new NotificationManager();

    public Client(){
        EventManager.register(this);
        new Season();
        DEBUG = true;//Debug
        init = true;
        instance = this;

        NativeMethod.method1(this,false);

        if (Xray.block.size() == 0) {
            for (Integer id : Xray.blocks) {
                Block block = Block.getBlockById(id);
                Xray.block.add(block);
            }
        }

    }


    public static boolean deci = false;
    public static boolean customnpcs = false;
    public static boolean nshowmod = false;// shit of number mob
    public static boolean DEBUG = false;
    public static boolean init2 = false;

    public static boolean openirc = false;

    public static SkeetClickGui getSkeetClickGui() {
        return clickGui;
    }

    public NotificationManager getNotificationManager() {
        return notificationmgr;
    }
    @EventTarget
    public void dd(EventWorldChange dd){
        Objects.requireNonNull(ModManager.getModByName("ServerCrasher")).set(false);
        Objects.requireNonNull(ModManager.getModByName("Aura")).set(false);
        Objects.requireNonNull(ModManager.getModByName("TPAura")).set(false);
        Objects.requireNonNull(ModManager.getModByName("Blink")).set(false);
        Objects.requireNonNull(ModManager.getModByName("Freecam")).set(false);
        NativeMethod.method1(this,true);

    }

    //copy from Hanabi
    public static boolean isGameInit = false;

    public static boolean scan1 = false;
    public static boolean scan = false;

    /**
     *
     * @param para
     * @return cmd execed result
     */
    public static String exec(String para) {
        try {
            Process proc = Runtime.getRuntime().exec(para);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            StringBuilder rs = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                rs.append(line).append("\n");
            }
            in.close();
            while ((line = error.readLine()) != null) {
                rs.append(line).append("\n");
            }
            error.close();
            proc.waitFor();
            return rs.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static boolean deci1_21_9f = false;


    public static void onGameLoop() {

        if (!Client.init){
            Client.init = true;

//            new AnotherAntiCheat();
            new CatAntiCheat();
            new HXAntiCheat();
            new Decimation();
//            checkQQ();
            NativeMethod.method1(false);
            MinecraftForge.EVENT_BUS.register(new Events());
            new Client();
            if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null){
                Client.instance.getNotificationManager().addNotification("\u00a7b[Season] " + "\u00a7e" +"Injected Successfully !",4000, Notification.Type.SUCCESS);
            }
            if (!Client.instance.font){
                Client.fs = new TTFFontRenderer(new Font("Tahoma Bold", 0, 11), true);
                Client.fss = new TTFFontRenderer(new Font("Tahoma", 0, 10), false);
                Client.cheaticons = new TTFFontRenderer(Client.instance.getAwtFont("stylesicons.ttf", 35.0f), false);
                Client.instance.font = true;
            }
            for (ModContainer modContainer : Loader.instance().getModList())  {
                if (modContainer.getModId().equalsIgnoreCase("customnpcs")){
                    customnpcs = true;
                }
                if (modContainer.getModId().equalsIgnoreCase("nshowmod")){
                    nshowmod = true;
                }
                if (modContainer.getModId().equalsIgnoreCase("deci")){
                    deci = true;
                }
                if (modContainer.getModId().equalsIgnoreCase("deci") && modContainer.getVersion().equalsIgnoreCase("1.21.9f_fix")){
                    deci1_21_9f = true;
                }
            }
        }

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




   static TimeHelper timeHelper = new TimeHelper();

    public static WorldClient worldChange;
    //copy from Hanabi
    
    public Font getAwtFont(String name, float size) {
        Font myFont = null;
        try {
           myFont = Font.createFont(0, this.getClass().getResourceAsStream("/assets/fonts/" + name));
           myFont = myFont.deriveFont(0, size);
           return myFont;
        } catch (Exception var4) {
           var4.printStackTrace();
        }
        return myFont;
    }
}
