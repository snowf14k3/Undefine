package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.mods.RENDER.NameProtect;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class TransformFontRenderer implements Opcodes {

    public static void transformFontRenderer(ClassNode classNode, MethodNode methodInsnNode){
        if (methodInsnNode.desc.equalsIgnoreCase("(Ljava/lang/String;IIIZ)I")
                && (methodInsnNode.name.equalsIgnoreCase("drawString")
                || methodInsnNode.name.equalsIgnoreCase("func_85187_a") )
        ){
            InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(ALOAD,1));
            insnList.add(new MethodInsnNode(INVOKESTATIC,Type.getInternalName(TransformFontRenderer.class),
                    "getName",
                    "(Ljava/lang/String;)Ljava/lang/String;",
                    false));
            insnList.add(new VarInsnNode(ASTORE,1));

            methodInsnNode.instructions.insert(methodInsnNode.instructions.getFirst(),insnList);
        }
    }

    public static String getName(String old){
        if (Minecraft.getMinecraft().getSession() != null && ModManager.ok){
            return old.replace(
                    Minecraft.getMinecraft().getSession().getUsername(),
                    ModManager.getModByClass(NameProtect.class).isEnabled()
                            ? NameProtect.name.getText() : Minecraft.getMinecraft().getSession().getUsername() );
        }
        return old;
    }

}
