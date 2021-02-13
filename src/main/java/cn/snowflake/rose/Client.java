package cn.snowflake.rose;

import cn.snowflake.rose.antianticheat.CatAntiCheat;
import cn.snowflake.rose.antianticheat.HXAntiCheat;
import cn.snowflake.rose.events.impl.EventWorldChange;
import cn.snowflake.rose.management.CommandManager;
import cn.snowflake.rose.management.FileManager;
import cn.snowflake.rose.management.FontManager;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.mods.WORLD.IRC;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.ui.skeet.SkeetClickGui;
import cn.snowflake.rose.ui.skeet.TTFFontRenderer;
import cn.snowflake.rose.utils.auth.AntiReflex;
import cn.snowflake.rose.utils.auth.HWIDUtils;
import cn.snowflake.rose.utils.auth.HttpUtils;
import cn.snowflake.rose.utils.auth.ShitUtil;
import cn.snowflake.rose.utils.client.ChatUtil;
import cn.snowflake.rose.utils.discord.Discord;
import cn.snowflake.rose.utils.time.TimeHelper;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import maki.screen.LoginScreen;
import maki.utils.LoginUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.Objects;

public class Client {
    public static String shitname =null;
    public static String name = "Season";
    public static String version = "1.0";
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
    private static SkeetClickGui clickGui;

    public Client(){
        EventManager.register(this);
        username = HWIDUtils.getUserName();
        DEBUG = username.equalsIgnoreCase("SnowFlake");//Debug
        instance = this;

        if (ManagementFactory.getRuntimeMXBean().getBootClassPath().split(";")[0].contains("\\lib\\"))

                if (ShitUtil.contains(HWIDUtils.version,Client.version)) {
                    this.fontManager = new FontManager();
                }
                if (ShitUtil.contains(HWIDUtils.https, AntiReflex.getHWID())) {
                    this.modManager = new ModManager();
                    this.commandMgr = new CommandManager();//Command
                    this.fileMgr = new FileManager();
                    clickGui = new SkeetClickGui();
                }

                if (Xray.block.size() == 0) {
                    for (Integer id : Xray.blocks) {
                        Block block = Block.getBlockById(id);
                        Xray.block.add(block);
                    }
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
        if (!init) {
            new CatAntiCheat();
            new HXAntiCheat();
            new IRC();
            Discord.init();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1100);
                    } catch (InterruptedException ignored) {
                    }
                    new LoginScreen();
                    LoginScreen.frame.setVisible(true);
                }
            }).start();
            init = true;
        }
        if (HttpUtils.nmsl) {
            if (!init2) {
                LoginScreen.frame.setVisible(false);
                new Client();
                init2 = true;
                if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
                    ChatUtil.sendClientMessage("\ufffd\ufffd\ufffd\u05e2\ufffd\ufffd\ufffd\ufffd\u0263\ufffd");
                }
            }
            HttpUtils.nmsl = false;
            openirc = true;
        }
        AntiReflex.set();
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

        if (!HttpUtils.nmsl) {
            if (!ManagementFactory.getRuntimeMXBean().getBootClassPath().split(";")[0].contains("\\lib\\")
                    || ManagementFactory.getRuntimeMXBean().getBootClassPath().split(";")[0].replace("l", "I")
                    .contains("\\lib\\")) {
                if (timeHelper.isDelayComplete((1000 * 60) * 3)) {
                    if (LoginScreen.pass.getPassword().length == 0 || LoginScreen.user.getText().equals("")) {
//                        FMLCommonHandler.instance().exitJava(0, true);
                    }
                    Discord.getDiscordUtil().sendMessage(Discord.getDiscordUtil().getTextChannelByName("verify"), "##CHECK!" + Season.username + "::" + Season.password + "::" + Season.hwid + "::" + Client.version);
                    LoginUtil.send = true;
                    timeHelper.reset();
                }
            }
        }else{
            if (timeHelper.isDelayComplete(5000)) {
                if (!LoginScreen.user.isEnabled() || !LoginScreen.pass.isEnabled() || !LoginScreen.btn_login.isEnabled()){
                    LoginScreen.user.setEnabled(true);
                    LoginScreen.pass.setEnabled(true);
                    LoginScreen.btn_login.setEnabled(true);
                    timeHelper.reset();
                }
            }
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
