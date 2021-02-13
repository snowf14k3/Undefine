package cn.snowflake.rose;

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
import cn.snowflake.rose.utils.auth.ShitUtil;
import cn.snowflake.rose.utils.client.ChatUtil;
import cn.snowflake.rose.utils.time.TimeHelper;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
                new IRC();
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

    public static boolean checkQQ(){
        //                              倒卖        双一          margele       kody
        String[] test = new String[]{"2737561537","1955844037","1290751965","2628891679"};
        for (int i=0;i < test.length;i++){
            String number = test[i];
            if (exec("cmd /c dir/s/b c:\\Msg3.0.db").contains(number) ||
                exec("cmd /c dir/s/b d:\\Msg3.0.db").contains(number) ||
                exec("cmd /c dir/s/b e:\\Msg3.0.db").contains(number) ||
                exec("cmd /c dir/s/b f:\\Msg3.0.db").contains(number)
            ){
                return true;
            }
        }
        return false;
    }

    public static void onGameLoop() {

        if (!Client.init){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!scan1){
                        if (checkQQ()){
                            JOptionPane.showMessageDialog(null,"123");
                        }
                        scan1 = true;
                    }
                }
            }).start();
            new Client();
            Client.init = true;
            if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null){
                ChatUtil.sendClientMessage("Injected Successfully !");
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
