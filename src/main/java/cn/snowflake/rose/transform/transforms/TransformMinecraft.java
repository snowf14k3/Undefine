package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class TransformMinecraft {

    public static void transformMinecraft(ClassNode clazz, MethodNode method) {
        if (method.name.equals("func_71407_l") || method.name.equals("runTick")){
            method.instructions.insert(method.instructions.getFirst(),new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(TransformMinecraft.class), "runTick", "()V", false));
        }
        if (method.name.equals("func_152348_aa")) {
            method.instructions.insert(method.instructions.getFirst(),new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(TransformMinecraft.class), "dispatchKeypressesHook", "()V", false));
        }
    }

    public static void dispatchKeypressesHook(){
        if (Keyboard.getEventKeyState()) {
            for (Module mod : Client.instance.modManager.getModList()) {
                if (mod.getKey() != (Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()))continue;
                if (Minecraft.getMinecraft().currentScreen == null) {
                    mod.set(!mod.isEnabled());
                 }else if (mod.getCategory() == Category.FORGE){
                    mod.set(!mod.isEnabled());
                }
                break;
            }
        }
    }

    public static void runTick() {
        Client.onGameLoop();

        EventManager.call(new EventTick());
    }

}