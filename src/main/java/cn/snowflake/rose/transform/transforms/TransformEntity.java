package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.events.impl.EventMove;
import com.darkmagician6.eventapi.EventManager;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class TransformEntity {

    public static void transformEntity(ClassNode classNode, MethodNode methodNode) {
        if (methodNode.name.equalsIgnoreCase("moveEntity") || methodNode.name.equalsIgnoreCase("func_70091_d")) {
            final InsnList insnList = new InsnList();
            insnList.add(new TypeInsnNode(Opcodes.NEW, Type.getInternalName(EventMove.class)));
            insnList.add(new InsnNode(Opcodes.DUP));
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
            insnList.add(new VarInsnNode(Opcodes.DLOAD, 1));
            insnList.add(new VarInsnNode(Opcodes.DLOAD, 3));
            insnList.add(new VarInsnNode(Opcodes.DLOAD, 5));
            insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Type.getInternalName(EventMove.class), "<init>","(Ljava/lang/Object;DDD)V", false));
            insnList.add(new VarInsnNode(Opcodes.ASTORE, 11));
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 11));
            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EventManager.class), "call", "(Lcom/darkmagician6/eventapi/events/Event;)Lcom/darkmagician6/eventapi/events/Event;", false));
            insnList.add(new InsnNode(Opcodes.POP));
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 11));
            insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Type.getInternalName(EventMove.class), "getX", "()D", false));
            insnList.add(new VarInsnNode(Opcodes.DSTORE, 1));
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 11));
            insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Type.getInternalName(EventMove.class), "getY", "()D", false));
            insnList.add(new VarInsnNode(Opcodes.DSTORE, 3));
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 11));
            insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Type.getInternalName(EventMove.class), "getZ", "()D", false));
            insnList.add(new VarInsnNode(Opcodes.DSTORE, 5));
            methodNode.instructions.insert(insnList);
        }
    }

}
