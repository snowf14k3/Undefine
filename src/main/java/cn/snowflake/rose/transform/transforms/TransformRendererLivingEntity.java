package cn.snowflake.rose.transform.transforms;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.utils.other.JReflectUtility;
import cn.snowflake.rose.utils.asm.ASMUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.injection.ClientLoader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class TransformRendererLivingEntity implements Opcodes {

	public static void transformRendererLivingEntity(ClassNode classNode, MethodNode method) {
		if (method.name.equalsIgnoreCase("doRender") || method.name.equalsIgnoreCase("func_76986_a")){
//			AbstractInsnNode prevRotationPitch = ASMUtil.findFieldInsnNode(method,GETFIELD, "net/minecraft/entity/EntityLivingBase", ClientLoader.runtimeDeobfuscationEnabled ? "field_70127_C" : "prevRotationPitch", "F");
//			if (prevRotationPitch != null){
//				InsnList insnList1 = new InsnList();
//				insnList1.add(new VarInsnNode(ALOAD,1));
//				insnList1.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(TransformRendererLivingEntity.class),"isEntityplayer","(Ljava/lang/Object;)Z",false));
//				LabelNode labelNode33 = new LabelNode();
//				insnList1.add(new JumpInsnNode(IFNE,labelNode33));
//
//				method.instructions.insertBefore(prevRotationPitch.getPrevious(),insnList1);
//				method.instructions.insert(ASMUtil.forward(prevRotationPitch,9),labelNode33);
//			}

			AbstractInsnNode renderLivingAt = ASMUtil.findMethodInsn(method,INVOKEVIRTUAL, "net/minecraft/client/renderer/entity/RendererLivingEntity", ClientLoader.runtimeDeobfuscationEnabled ? "func_77039_a" : "renderLivingAt", "(Lnet/minecraft/entity/EntityLivingBase;DDD)V");
			if (renderLivingAt != null){
				InsnList insnList = new InsnList();
				insnList.add(new VarInsnNode(ALOAD,1));
				insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(TransformRendererLivingEntity.class),"isEntityplayer","(Ljava/lang/Object;)Z",false));
				LabelNode labelNode = new LabelNode();
				insnList.add(new JumpInsnNode(IFEQ,labelNode));
				insnList.add(new FieldInsnNode(GETSTATIC,Type.getInternalName(EventMotion.class),"RPPITCH","F"));
				insnList.add(new FieldInsnNode(GETSTATIC,Type.getInternalName(EventMotion.class),"RPITCH","F"));
				insnList.add(new MethodInsnNode(INVOKESTATIC,Type.getInternalName(TransformRendererLivingEntity.class),"interpolateRotation","(FF)F",false));
				insnList.add(new VarInsnNode(FSTORE,13));
				insnList.add(labelNode);
				method.instructions.insertBefore(renderLivingAt,insnList);
			}
		}
	}

    public static float interpolateRotation(float p_77034_1_, float p_77034_2_) {
        float f3;
        for(f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F) {
        }

        while(f3 >= 180.0F) {
            f3 -= 360.0F;
        }

        return p_77034_1_ + JReflectUtility.getRenderPartialTicks() * f3;
    }

    public static boolean isEntityplayer(Object object){
        if (object instanceof EntityPlayerSP){
            return true;
        }
        return false;
    }


}
