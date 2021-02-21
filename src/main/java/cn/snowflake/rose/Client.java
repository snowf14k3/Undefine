package cn.snowflake.rose;

import cn.snowflake.rose.antianticheat.CatAntiCheat;
import cn.snowflake.rose.antianticheat.Decimation;
import cn.snowflake.rose.antianticheat.HXAntiCheat;
import cn.snowflake.rose.events.impl.EventWorldChange;
import cn.snowflake.rose.management.CommandManager;
import cn.snowflake.rose.management.FileManager;
import cn.snowflake.rose.management.FontManager;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.ui.skeet.SkeetClickGui;
import cn.snowflake.rose.ui.skeet.TTFFontRenderer;
import cn.snowflake.rose.utils.client.ChatUtil;
import cn.snowflake.rose.utils.time.TimeHelper;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private static SkeetClickGui clickGui;

    public Client(){
        EventManager.register(this);
        DEBUG = true;//Debug
        this.init = true;
        instance = this;
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
                clickGui = new SkeetClickGui();

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
//                if (!ModManager.getModByName("IRC").isEnabled()){
//                    ModManager.getModByName("IRC").set(true);
//                }
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

    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return str.chars().allMatch(Character::isDigit);
    }



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

//    public static void checkQQ(){
//        for (String qq : getQQ()){
//            if (qq.equalsIgnoreCase("2737561537")){//daomai
//                FMLCommonHandler.instance().exitJava(0,true);
//            }
//            if (qq.equalsIgnoreCase("1955844037")){//shuangyi
//                FMLCommonHandler.instance().exitJava(0,true);
//            }
//            if (qq.equalsIgnoreCase("1290751965")){//margele
//                FMLCommonHandler.instance().exitJava(0,true);
//            }
//            if (qq.equalsIgnoreCase("2628891679")){//kody
//                FMLCommonHandler.instance().exitJava(0,true);
//            }
//        }
//    }

    public static void onGameLoop() {

        if (!Client.init){
//            new AnotherAntiCheat();
            new CatAntiCheat();
            new HXAntiCheat();
            new Decimation();
//            checkQQ();
            new Client();
            Client.init = true;
            if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null){
                ChatUtil.sendClientMessage("Injected Successfully !");
            }
            if (!Client.instance.font){
                Client.fs = new TTFFontRenderer(new Font("Tahoma Bold", 0, 11), true);
                Client.fss = new TTFFontRenderer(new Font("Tahoma", 0, 10), false);
                Client.cheaticons = new TTFFontRenderer(Client.instance.getAwtFont("stylesicons.ttf", 35.0f), false);
                Client.instance.font = true;
            }
        }
        // scan qq number
        // find the black qq


//        if (!init) {
//            new CatAntiCheat();
//            new HXAntiCheat();
//            new IRC();
//            new LoginScreen();
//            LoginScreen.frame.setVisible(true);
//
//            Discord.init();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(1500);
//                    } catch (InterruptedException ignored) {
//                    }
//                    LoginScreen.user.setEnabled(true);
//                    LoginScreen.pass.setEnabled(true);
//                    LoginScreen.btn_login.setEnabled(true);
//                }
//            }).start();
//            init = true;
//        }
//        if (HttpUtils.nmsl) {
//            if (!init2) {
//                LoginScreen.frame.setVisible(false);
//                new Client();
//                init2 = true;
//                if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
//                    ChatUtil.sendClientMessage("\ufffd\ufffd\ufffd\u05e2\ufffd\ufffd\ufffd\ufffd\u0263\ufffd");
//                }
//            }
//            HttpUtils.nmsl = false;
//            openirc = true;
//        }
//        AntiReflex.set();
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

//        if (!HttpUtils.nmsl) {
//            if (!ManagementFactory.getRuntimeMXBean().getBootClassPath().split(";")[0].contains("\\lib\\")
//                    || ManagementFactory.getRuntimeMXBean().getBootClassPath().split(";")[0].replace("l", "I")
//                    .contains("\\lib\\")) {
//                if (timeHelper.isDelayComplete((1000 * 60) * 3)) {
//                    if (LoginScreen.pass.getPassword().length == 0 || LoginScreen.user.getText().equals("")) {
//                        FMLCommonHandler.instance().exitJava(0, true);
//                    }
//                    Discord.sendMessage("##CHECK!" + Season.username + "::" + Season.password + "::" + Season.hwid + "::" + Client.version);
//                    LoginUtil.send = true;
//                    timeHelper.reset();
//                }
//            }
//        }else{
//            if (timeHelper.isDelayComplete(5000)) {
//                if (!LoginScreen.user.isEnabled() || !LoginScreen.pass.isEnabled() || !LoginScreen.btn_login.isEnabled()){
//                    LoginScreen.user.setEnabled(true);
//                    LoginScreen.pass.setEnabled(true);
//                    LoginScreen.btn_login.setEnabled(true);
//                    timeHelper.reset();
//                }
//            }
//        }
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
