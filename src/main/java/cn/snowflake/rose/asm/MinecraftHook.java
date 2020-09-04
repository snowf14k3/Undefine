package cn.snowflake.rose.asm;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.manager.CommandManager;
import cn.snowflake.rose.manager.FontManager;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.PLAYER.NoSlow;
import cn.snowflake.rose.mod.mods.RENDER.ViewClip;
import cn.snowflake.rose.mod.mods.WORLD.NoCommand;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.utils.ChatUtil;
import cn.snowflake.rose.utils.GlStateManager;
import cn.snowflake.rose.utils.Rotation;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.lwjgl.input.Keyboard;

public class MinecraftHook {
    public static Rotation serverRotation = new Rotation(0F, 0F);


    //Client Start
    public static void runClient() {
        if (!Client.init){
            new Client();
            Client.init = true;
            if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null){
                ChatUtil.sendClientMessage("Client Injected !");
            }
        }
        if (!Client.instance.font){
                Client.instance.fontManager = new FontManager();
                Client.instance.font = true;
        }
    }

    public static void onUpdateWalkingPlayerHook(EventType stage) {
        EventMotion em = new EventMotion(stage);
        EventManager.call(em);
    }

    ///Client End
    public static void Event3D(){
        EventRender3D er3 = new EventRender3D();
        EventManager.call(er3);
    }

    public static void Event2D(){
        GlStateManager.pushMatrix();
        EventRender2D er = new EventRender2D();
        EventManager.call(er);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    //noslow start
    private static float cacheStrafe;
    private static float cacheForward;

    public static boolean onNoSlowEnable2() {
        return NoSlow.no;
    }

    public static boolean isSlow() {
        return Minecraft.getMinecraft().thePlayer.isUsingItem() && !Minecraft.getMinecraft().thePlayer.isRiding();
    }
    public static void onNoSlowEnable() {
        if (!isSlow()) {
            return;
        }
        if (ModManager.getModByName("NoSlow").isEnabled()) {
            cacheStrafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
            cacheForward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        }
    }
    public static void onToggledTimerZero(){
        if (!isSlow()) {
            return;
        }
        if (ModManager.getModByName("NoSlow").isEnabled()) {
            Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe = cacheStrafe;
            Minecraft.getMinecraft().thePlayer.movementInput.moveForward = cacheForward;
        }
    }
    //noslow end
    public static boolean isViewClipEnabled() {
        return ViewClip.x;
    }

    public static boolean isXrayEnabled() {
        return Xray.x;
    }


    public static void runTick(){
        if (Keyboard.getEventKeyState() && Minecraft.getMinecraft().currentScreen == null) {
            for (Module mod : ModManager.getModList()) {
                if (mod.getKey() != (Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey())) continue;
                mod.set(!mod.isEnabled());
                break;
            }
        }
    }

    public static void onUpdate(){
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            EventUpdate event = new EventUpdate();
            EventManager.call(event);
        }
    }

    public static void command(String message){
        String s = CommandManager.removeSpaces(message);
        if (message.startsWith("-") && !NoCommand.n) {
            for (Command cmd : CommandManager.getCommands()) {
                int i = 0;
                while (i < cmd.getCommands().length) {
                    //TODO 符号
                    if (s.split(" ")[0].equals("-" + cmd.getCommands()[i])) {
                        cmd.onCmd(s.split(" "));
                        return;
                    }
                    ++i;
                }
            }
            return;
        }
    }
    public static boolean isNoCommandEnabled(String s,String s1) {
        return s.startsWith(s1) && !NoCommand.n;
    }


}
