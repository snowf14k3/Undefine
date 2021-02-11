package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.Season;
import cn.snowflake.rose.antianticheat.CatAntiCheat;
import cn.snowflake.rose.antianticheat.HXAntiCheat;
import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.WORLD.IRC;
import cn.snowflake.rose.ui.skeet.TTFFontRenderer;
import cn.snowflake.rose.utils.auth.HttpUtils;
import cn.snowflake.rose.utils.client.ChatUtil;
import com.darkmagician6.eventapi.EventManager;
import maki.screen.LoginScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.awt.*;
import java.util.Iterator;

public class TransformMinecraft {

    public static void transformMinecraft(ClassNode clazz, MethodNode method) {
        Iterator<AbstractInsnNode> iter = method.instructions.iterator();
        while (iter.hasNext()) {
            AbstractInsnNode insn = iter.next();
            if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (methodInsn.name.equals("func_71407_l") || methodInsn.name.equals("runTick")){
                    method.instructions.insert(insn,new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(TransformMinecraft.class), "runTick", "()V", false));
                }
                if (methodInsn.name.equals("func_152348_aa")) {
                    method.instructions.insert(insn,new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(TransformMinecraft.class), "dispatchKeypressesHook", "()V", false));
                }
            }
        }
    }

    public static void dispatchKeypressesHook(){
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
            if (key == Keyboard.KEY_PERIOD){
                Minecraft.getMinecraft().displayGuiScreen(new GuiChat("-irc"));
            }
            for (Module mod : ModManager.getModList()) {
                if (Minecraft.getMinecraft().currentScreen == null) {
                    if (mod.getKey() != key) continue;
                    mod.set(!mod.isEnabled());
                }
                break;
            }
        }
    }

    public static void runTick() {
        Client.onGameLoop();
        EventManager.call(new EventTick());


        if (!Season.init){
            new CatAntiCheat();
            new HXAntiCheat();
            new IRC();
            new LoginScreen();
            LoginScreen.frame.setVisible(true);
            Season.init = true;
        }

        if (!LoginScreen.kkkkkk){
            LoginScreen.frame.setVisible(true);
        }

        if(HttpUtils.nmsl){
            if (!Client.init) {
                LoginScreen.frame.setVisible(false);
                new Client();
                if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
                    ChatUtil.sendClientMessage("外挂注入完成！");
                }
                HttpUtils.nmsl = false;

                if (!Client.instance.font) {
                    Client.fs = new TTFFontRenderer(new Font("Tahoma Bold", 0, 11), true);
                    Client.fss = new TTFFontRenderer(new Font("Tahoma", 0, 10), false);
                    Client.cheaticons = new TTFFontRenderer(Client.instance.getAwtFont("stylesicons.ttf", 35.0f), false);
                    Client.instance.font = true;
                }
                Client.init = true;
            }
        }



    }

}
