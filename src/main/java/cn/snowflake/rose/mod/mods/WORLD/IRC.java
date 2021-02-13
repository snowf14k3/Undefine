package cn.snowflake.rose.mod.mods.WORLD;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.utils.auth.AntiReflex;
import cn.snowflake.rose.utils.auth.HWIDUtils;
import cn.snowflake.rose.utils.client.ChatUtil;
import cn.snowflake.rose.utils.time.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class IRC  {
    static PrintWriter pw;
    BufferedReader br;
    Socket socket;
    private TimeHelper timer = new TimeHelper();
    private boolean messageThread;
    public Minecraft mc = Minecraft.getMinecraft();

    public IRC() {
        new reconnect().start();
    }

    public void processMessage(String msg1) {
        msg1 = msg1.replace("\ufffd", "");//删除傻逼异常字符

        if (msg1 != null) {
            if (msg1.contains(HWIDUtils.getHWID())) {

                return;
            }
            if (msg1.contains("GETHELP")) {
                ChatUtil.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
                ChatUtil.sendMessageWithoutPrefix("\u00a7b\u00a7lSeason IRC Help");
                ChatUtil.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
                ChatUtil.sendMessageWithoutPrefix("\u00a7b\u00a7lUser Type");
                ChatUtil.sendMessageWithoutPrefix("\u00a7b[LIST] >\u00a77 Use -IRC \u00a7bLIST \u00a77To List All User");
                ChatUtil.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
                return;
            }
            msg1 = msg1.replace("搂","\247");
            if (msg1.contains("LIST")) {
                ChatUtil.sendMessageWithoutPrefix("");
                ChatUtil.sendMessageWithoutPrefix("\u00a77[\u00a7bIRC\u00a77]Getting List.Pls Wait A Minute.");
                sendIRCMessage("#IRC#Message:: Name: " + this.mc.thePlayer.getCommandSenderName() + " | User: " + Client.shitname, false);
                return;
            }
            if (msg1.contains("COUNTER//")) {
                ChatUtil.sendMessageWithoutPrefix("\u00a77[\u00a7bIRC\u00a77]" + msg1.split("//")[1]);
                return;
            }
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(msg1));
        }
    }

    public static void sendIRCMessage(String message,boolean prefix) {
        message = Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
        if(prefix) {
            pw.println(message);
        } else {
            pw.println(message);
        }
    }

    class connect extends Thread {
        @Override
        public void run() {
            this.setName("Connect");
            try {
                messageThread = false;
                socket = new Socket("45.253.67.78",56752);
                pw = new PrintWriter(socket.getOutputStream(), true);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                if (Client.openirc) {
                    String test = "#CLIENT#ALIVE::" + HWIDUtils.getHWID();
                    sendIRCMessage(test, false);
                }
                messageThread = true;
                new readMessage().start();
            }catch (Throwable ignored) {
            }
        }
    }
    class readMessage extends Thread {
        @Override
        public void run() {

            this.setName("ReadMessage");
            try {
                while (messageThread) {
                    String msg1 = br.readLine();
                    if (msg1 == null) continue;
                    if (!Client.openirc) {
                        AntiReflex.checkUser(msg1);

                        if (msg1.contains("nmsl")) {
//                            FMLCommonHandler.instance().exitJava(0, true);
                        }
                    }else{
                        processMessage(msg1);
                    }
                }
            }catch (IOException ignored) {
            }
        }
    }

    class reconnect extends Thread {
        @Override
        public void run() {
            this.setName("Reconnect");
            new connect().start();
            while (true) {
                try {
                    int i=1;
                    while (true) {
                        socket.sendUrgentData(0xff);
                        reconnect.sleep(3000L);
                    }
                }catch (IOException e) {
                    if (timer.isDelayComplete(2000L)) {
                        timer.reset();
                        new connect().start();
                    }
                }catch (NullPointerException | InterruptedException ignored) {
                }
            }
        }
    }


}
