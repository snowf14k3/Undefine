package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

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
            for (Module mod : ModManager.getModList()) {
                if (Minecraft.getMinecraft().currentScreen == null) {
                    if (mod.getKey() != (Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey())) continue;
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