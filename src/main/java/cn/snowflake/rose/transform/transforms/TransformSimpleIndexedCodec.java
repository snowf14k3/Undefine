package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.events.impl.EventFromByte;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class TransformSimpleIndexedCodec implements Opcodes {

    public static void transformSimpleIndexedCodec(ClassNode classNode, MethodNode methodNode) {
        if (methodNode.name.equalsIgnoreCase("decodeInto")){
            InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(ALOAD,2));//bytebuf
            insnList.add(new VarInsnNode(ALOAD,3));//imessage

            insnList.add(new FieldInsnNode(
                     Opcodes.GETSTATIC,
                    "com/darkmagician6/eventapi/types/EventType",
                    "RECIEVE",
                    "Lcom/darkmagician6/eventapi/types/EventType;")
            );

            insnList.add(new MethodInsnNode(
                    INVOKESTATIC,
                    Type.getInternalName(TransformSimpleIndexedCodec.class),
                    "decodeIntoHook","(Lio/netty/buffer/ByteBuf;Lcpw/mods/fml/common/network/simpleimpl/IMessage;Lcom/darkmagician6/eventapi/types/EventType;)V",
                    false)
            );
            methodNode.instructions.insert(methodNode.instructions.getFirst(),insnList);
        }
        if (methodNode.name.equalsIgnoreCase("encodeInto")){
            InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(ALOAD,3));//imessage
            insnList.add(new VarInsnNode(ALOAD,2));//bytebuf

            insnList.add(new FieldInsnNode(
                    Opcodes.GETSTATIC,
                    "com/darkmagician6/eventapi/types/EventType",
                    "SEND",
                    "Lcom/darkmagician6/eventapi/types/EventType;")
            );

            insnList.add(new MethodInsnNode(
                    INVOKESTATIC,
                    Type.getInternalName(TransformSimpleIndexedCodec.class),
                    "decodeIntoHook","(Lio/netty/buffer/ByteBuf;Lcpw/mods/fml/common/network/simpleimpl/IMessage;Lcom/darkmagician6/eventapi/types/EventType;)V",
                    false)
            );
            methodNode.instructions.insert(methodNode.instructions.getFirst(),insnList);
        }

    }

    public static void decodeIntoHook(ByteBuf buf, IMessage iMessage, EventType eventType){
        EventFromByte efb = new EventFromByte(buf,iMessage,eventType);
        EventManager.call(efb);
    }

}
