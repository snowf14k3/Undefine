package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.Season;
import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventManager;
import me.skids.margeleisgay.AuthMain;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class TransformMinecraft implements Opcodes{

    public static void transformMinecraft(ClassNode clazz, MethodNode method) {
        if (method.name.equals("func_71407_l") || method.name.equals("runTick")){
            //if (!Season.isAuthed){
            //   new AuthMain();
            //}
            //new TransformMinecraft.runTick();
            InsnList insnList = new InsnList();
            LabelNode labelNode = new LabelNode();
            insnList.add(new FieldInsnNode(GETSTATIC,Type.getInternalName(Season.class),Season.class.getFields()[0].getName(),"Z"));
            insnList.add(new JumpInsnNode(IFNE,labelNode));
            insnList.add(new TypeInsnNode(NEW,Type.getInternalName(AuthMain.class)));
            insnList.add(new InsnNode(DUP));
            insnList.add(new MethodInsnNode(INVOKESPECIAL,Type.getInternalName(AuthMain.class),"<init>","()V", false));
            insnList.add(new InsnNode(POP));
            insnList.add(labelNode);
            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(TransformMinecraft.class), "runTick", "()V", false));
            
            method.instructions.insert(method.instructions.getFirst(),insnList);

        }
        if (method.name.equals("func_152348_aa")) {
            method.instructions.insert(method.instructions.getFirst(),new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(TransformMinecraft.class), "dispatchKeypressesHook", "()V", false));
        }
//        if (method.name.equalsIgnoreCase(""))
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