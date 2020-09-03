package cn.snowflake.rose.asm;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.function.BiConsumer;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.events.impl.EventMove;
import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.asm.ASMUtil;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import cpw.mods.fml.common.Loader;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import javax.swing.*;

public class ClassTransformer implements IClassTransformer, ClassFileTransformer,Opcodes{
	
	public static Set<String> classNameSet;
	private byte[] transformMethods(byte[] bytes, BiConsumer<ClassNode, MethodNode> transformer) {
		ClassReader classReader = new ClassReader(bytes);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);
		LogManager.getLogger().info("transform "+classNode.name);
		classNode.methods.forEach(m ->
					transformer.accept(classNode, m)

		);
		ClassWriter classWriter = new ClassWriter(0);
		classNode.accept(classWriter);
		return classWriter.toByteArray();
	}
	static {
		classNameSet = new HashSet<String>();
		String[] nameArray = new String[] {
				"net.minecraft.client.entity.EntityClientPlayerMP",
				"net.minecraft.client.Minecraft",
				"net.minecraft.network.NetworkManager",
				"net.minecraft.network.NetHandlerPlayServer",
				"net.minecraft.client.entity.EntityPlayerSP",
				"net.minecraft.block.Block",
				"net.minecraft.client.renderer.EntityRenderer",
				"net.minecraftforge.client.GuiIngameForge",
				"com.vicmatskiv.weaponlib.ClientEventHandler",
				"luohuayu.anticheat.message.CPacketInjectDetect",
				"net.minecraft.entity.Entity",
				"cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper"
		};
		for (int i=0; i<nameArray.length; i++) {
				classNameSet.add(nameArray[i]);
		}
	}







	public static boolean needTransform(String name) {
		return classNameSet.contains(name);
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] classByte) {
		return transform(transformedName, classByte);
	}

	//  TODO SHIT OF runtimeDeobfuscationEnabled
	public static  boolean runtimeDeobfuscationEnabled = false;

//	public static  boolean runtimeDeobfuscationEnabled = false;

	public byte[] transform(String name, byte[] classByte) {
		try {
			if (name.equals("net.minecraft.client.Minecraft")) {
				Field field = LaunchClassLoader.class.getDeclaredField("transformers");
				field.setAccessible(true);
				List<IClassTransformer> transformers = (List<IClassTransformer>) field.get(net.minecraft.launchwrapper.Launch.classLoader);
				if (transformers.getClass().getName().equals("com.xue.vapu.ModList")) {
					Field needInterupt = transformers.getClass().getDeclaredField("needInterupt");
					needInterupt.set(transformers, new Boolean(false));
				}
				return transformMethods(classByte, this::transformMinecraft);
			}
			else if (name.equalsIgnoreCase("net.minecraft.client.renderer.EntityRenderer")){//3d
				return transformMethods(classByte, this::transformRenderEntityRenderer);
			}
			else if(name.equals("net.minecraft.client.entity.EntityPlayerSP")){  //fixed
				return  transformMethods(classByte, this::transformEntityPlayerSP);
			}
			else if (name.equalsIgnoreCase("net.minecraftforge.client.GuiIngameForge")){
				return this.transformMethods(classByte,this::transform2D);
			}
			else if (name.equals("net.minecraft.client.entity.EntityClientPlayerMP")) {
				return this.transformMethods(classByte,this::transformEntityClientPlayerMP);
			}
			else if (name.equalsIgnoreCase("net.minecraft.network.NetHandlerPlayServer")){
				return this.transformMethods(classByte,this::transformNetHandlerPlayServer);
			}
			else if (name.equalsIgnoreCase("net.minecraft.network.NetworkManager")){ //EventPacket
				return this.transformMethods(classByte,this::transformNetworkManager);
			}
			else if (name.equalsIgnoreCase("net.minecraft.block.Block")){
				return this.transformMethods(classByte,this::transformBlock);
			}
			else if (name.equalsIgnoreCase("net.minecraft.entity.Entity")){
				return this.transformMethods(classByte,this::transformEntity);
			}
			else if (name.equalsIgnoreCase("cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper")){
				return this.transformMethods(classByte,this::transformSimpleNetworkWrapper);
			}

		}catch(Exception e) {
			LogManager.getLogger().log(Level.ERROR, ExceptionUtils.getStackTrace(e));
			
		}
		return classByte;
	}

	private void transformSimpleNetworkWrapper(ClassNode classNode, MethodNode methodNode) {
		if (methodNode.name.equalsIgnoreCase("sendToServer")) {
			JOptionPane.showMessageDialog(null, "Successfully initialized FML hook !","提示", 1);

			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(ALOAD,1));

			insnList.add(new VarInsnNode(ALOAD,0));
			insnList.add(new FieldInsnNode(GETFIELD, "cpw/mods/fml/common/network/simpleimpl/SimpleNetworkWrapper", "channels", "Ljava/util/EnumMap;"));

			insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ClassTransformer.class), "simpleNetworkWrapperHook", "(Ljava/lang/Object;Ljava/util/EnumMap;)Z", false));
			LabelNode labelNode = new LabelNode();
			insnList.add(new JumpInsnNode(IFEQ,labelNode));
			insnList.add(new InsnNode(RETURN));
			insnList.add(labelNode);
//			insnList.add(new FrameNode(F_APPEND, 1, new Object[]{"cn/snowflake/rose/events/impl/EventFMLChannels"}, 0, null));
			methodNode.instructions.insert(insnList);
		}
	}


	public static boolean simpleNetworkWrapperHook(Object object,EnumMap enumMap){
		EventFMLChannels eventFMLChannels = new EventFMLChannels(object,enumMap);
		EventManager.call(eventFMLChannels);
		return eventFMLChannels.isCancelled();
	}

	private void transformEntity(ClassNode classNode, MethodNode methodNode) {
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

	private void transformClientEventHandler(ClassNode classNode, MethodNode methodNode) {
		if (methodNode.name.equalsIgnoreCase("slowPlayerDown")){
			AbstractInsnNode fist = methodNode.instructions.getFirst();
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(ALOAD,1));
			insnList.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;", false));
			//field_71439_g thePlayer
			insnList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/Minecraft", "field_71439_g", "Lnet/minecraft/client/entity/EntityClientPlayerMP;"));
			LabelNode labelNode = new LabelNode();
			insnList.add(new JumpInsnNode(IF_ACMPNE,labelNode));
			insnList.add(new MethodInsnNode(INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "onNoSlowEnable2", "()Z", false));
			insnList.add(new JumpInsnNode(IFEQ,labelNode));
			insnList.add(new InsnNode(RETURN));
			insnList.add(labelNode);
			insnList.add(new FrameNode(F_SAME, 0, null, 0, null));
			methodNode.instructions.insertBefore(fist.getNext(),insnList);
		}
	}



	private void transformBlock(ClassNode classNode, MethodNode methodNode) {
		if (methodNode.name.equalsIgnoreCase("shouldSideBeRendered") || methodNode.name.equalsIgnoreCase("func_149646_a")){
			LogManager.getLogger().info(methodNode.name);
			final InsnList insnList = new InsnList();
			insnList.add(new MethodInsnNode(INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "isXrayEnabled", "()Z", false));
			LabelNode jmp = new LabelNode();
			insnList.add(new JumpInsnNode(IFEQ,jmp));
			insnList.add(new FieldInsnNode(GETSTATIC, Type.getInternalName(Xray.class), "block", "Ljava/util/ArrayList;"));
			insnList.add(new VarInsnNode(ALOAD,0));// == this
			insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/ArrayList", "contains", "(Ljava/lang/Object;)Z", false));
			insnList.add(new InsnNode(IRETURN));
			insnList.add(jmp);
			insnList.add(new FrameNode(F_SAME, 0, null, 0, null));
			methodNode.instructions.insert(insnList);
		}
	}
	//transformNetworkManager start
	private void transformNetworkManager(ClassNode classNode, MethodNode methodNode) {
		if (methodNode.name.equalsIgnoreCase("channelRead0")){
			LogManager.getLogger().info(methodNode.name);
			final InsnList preInsn = new InsnList();
			preInsn.add(new VarInsnNode(Opcodes.ALOAD, 2));
			preInsn.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "PRE", "Lcom/darkmagician6/eventapi/types/EventType;"));
			preInsn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "channelRead0Hook","(Ljava/lang/Object;Lcom/darkmagician6/eventapi/types/EventType;)Z", false));
			final LabelNode jmp = new LabelNode();
			preInsn.add(new JumpInsnNode(Opcodes.IFEQ, jmp));
			preInsn.add(new InsnNode(Opcodes.RETURN));
			preInsn.add(jmp);
			preInsn.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			methodNode.instructions.insert(preInsn);

			/**
			 * if(channelRead0Hook(packet,EventType.PRE)){
			 *  return ;
			 * }
			 */

			final InsnList postInsn = new InsnList();
			postInsn.add(new VarInsnNode(Opcodes.ALOAD, 2));
			postInsn.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "POST", "Lcom/darkmagician6/eventapi/types/EventType;"));
			postInsn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "channelRead0Hook","(Ljava/lang/Object;Lcom/darkmagician6/eventapi/types/EventType;)Z", false));
			preInsn.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			methodNode.instructions.insertBefore(ASMUtil.bottom(methodNode), postInsn);

			/**
			 * if(channelRead0Hook(packet,EventType.POST)){
			 *      return;
			 * }
			 */
		}
	}
	public static boolean channelRead0Hook(Object packet, EventType eventType) {
		if(packet != null) {
			final EventPacket event = new EventPacket(eventType,packet);
			EventManager.call(event);
			return event.isCancelled();
		}
		return false;
	}
	private void transformNetHandlerPlayServer(ClassNode classNode, MethodNode methodNode) {
		InsnList nodelist = methodNode.instructions;
		if (methodNode.name.equalsIgnoreCase("func_147360_c") || methodNode.name.equalsIgnoreCase("kickPlayerFromServer")){
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(ALOAD,1));
			insnList.add(new LdcInsnNode("Illegal stance"));
			insnList.add(new MethodInsnNode(INVOKEVIRTUAL,"java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
			LabelNode l1 = new LabelNode();
			insnList.add(new JumpInsnNode(IFEQ,l1));
			insnList.add(new InsnNode(RETURN));
			insnList.add(l1);
			insnList.add(new FrameNode(F_SAME, 4, null, 0, null));
			methodNode.instructions.insertBefore(nodelist.getFirst().getNext(),insnList);
		}
	}



	private void transformRenderEntityRenderer(ClassNode classNode, MethodNode method) {
		if ((method.name.equalsIgnoreCase("renderWorld") || method.name.equalsIgnoreCase("func_78471_a") )) {
			
			Iterator<AbstractInsnNode> iter = method.instructions.iterator();
			while (iter.hasNext()) {
				AbstractInsnNode insn = iter.next();
				if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
					MethodInsnNode methodInsn = (MethodInsnNode) insn;
					if (methodInsn.name.equals("dispatchRenderLast")) {
						method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "Event3D", "()V", false));
					}
				}
			}
		}
		if ((method.name.equalsIgnoreCase("orientCamera") || method.name.equalsIgnoreCase("func_78467_g")) && method.desc.equalsIgnoreCase("(F)V")){
			
			AbstractInsnNode target = ASMUtil.findMethodInsn(method, INVOKEVIRTUAL,"net/minecraft/util/Vec3", runtimeDeobfuscationEnabled ?"func_72438_d" : "distanceTo","(Lnet/minecraft/util/Vec3;)D");
			if (target != null){
				final InsnList insnList = new InsnList();
				insnList.add(new MethodInsnNode(INVOKESTATIC,"cn/snowflake/rose/asm/MinecraftHook","isViewClipEnabled","()Z",false));
				final LabelNode jmp = new LabelNode();
				insnList.add(new JumpInsnNode(IFEQ, jmp));
				insnList.add(new InsnNode(ACONST_NULL));
				insnList.add(new VarInsnNode(ASTORE, 24));
				insnList.add(jmp);
				method.instructions.insert(target.getNext(), insnList);
				//   dump net.minecraft.client.renderer.EntityRenderer
				// jad net.minecraft.client.renderer.EntityRenderer
			}
		}
	}

	private void transform2D(ClassNode clazz, MethodNode method) {
		Iterator<AbstractInsnNode> iter = method.instructions.iterator();
		while (iter.hasNext()) {
			AbstractInsnNode insn = iter.next();
			if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
				MethodInsnNode methodInsn = (MethodInsnNode) insn;
				if (methodInsn.name.equals("renderSleepFade")){
					
					method.instructions.insert(insn,new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "Event2D", "()V", false));
				}
			}
		}
	}

	//Minecraft start
	private void transformMinecraft(ClassNode clazz, MethodNode method) {
		Iterator<AbstractInsnNode> iter = method.instructions.iterator();
		while (iter.hasNext()) {
			AbstractInsnNode insn = iter.next();
			if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
				MethodInsnNode methodInsn = (MethodInsnNode) insn;
				if (methodInsn.name.equals("func_71407_l") || methodInsn.name.equals("runTick")){
					method.instructions.insert(insn,new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "runClient", "()V", false));
				}
				if (methodInsn.name.equals("func_152348_aa")) {
					method.instructions.insert(insn,new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "runTick", "()V", false));
				}
			}
		}
	}
	//minecraft end

	//noslow start
	private void transformEntityPlayerSP(ClassNode clazz, MethodNode method) {
		if (method.name.equalsIgnoreCase("func_70636_d") || method.name.equalsIgnoreCase("onLivingUpdate")) {
			Iterator<AbstractInsnNode> iter = method.instructions.iterator();
			while (iter.hasNext()) {
				AbstractInsnNode insn = iter.next();
				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
					MethodInsnNode methodInsn = (MethodInsnNode) insn;
					if (methodInsn.name.equals("updatePlayerMoveState") || methodInsn.name.equals("func_78898_a")) {
						method.instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "onNoSlowEnable", "()V", false));
					}
					if (methodInsn.name.equals("func_145771_j")) {
						method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "onToggledTimerZero", "()V", false));
					}
				}
			}
		}
//		MethodNode mainMethod = new MethodNode( ACC_PUBLIC, runtimeDeobfuscationEnabled ? "func_70091_d" : "moveEntity", "(DDD)V", null, null);
//		mainMethod.instructions.add(new TypeInsnNode(NEW, "cn/snowflake/rose/events/impl/EventMove"));
//		mainMethod.instructions.add(new InsnNode(DUP));
//		mainMethod.instructions.add(new VarInsnNode(DLOAD,1));
//		mainMethod.instructions.add(new VarInsnNode(DLOAD,3));
//		mainMethod.instructions.add(new VarInsnNode(DLOAD,5));
//		mainMethod.instructions.add(new MethodInsnNode(INVOKESPECIAL, "cn/snowflake/rose/events/impl/EventMove", "<init>", "(DDD)V", false));
//		mainMethod.instructions.add(new VarInsnNode(ASTORE,7));
//
//		mainMethod.instructions.add(new VarInsnNode(ALOAD,7));
//		mainMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/darkmagician6/eventapi/EventManager", "call", "(Lcom/darkmagician6/eventapi/events/Event;)Lcom/darkmagician6/eventapi/events/Event;", false));
//		mainMethod.instructions.add(new InsnNode(POP));
//
//		mainMethod.instructions.add(new VarInsnNode(ALOAD,7));
//		mainMethod.instructions.add(new FieldInsnNode(GETFIELD, "cn/snowflake/rose/events/impl/EventMove", "x", "D"));
//		mainMethod.instructions.add(new VarInsnNode(DSTORE,1));
//
//		mainMethod.instructions.add(new VarInsnNode(ALOAD,7));
//		mainMethod.instructions.add(new FieldInsnNode(GETFIELD, "cn/snowflake/rose/events/impl/EventMove", "y", "D"));
//		mainMethod.instructions.add(new VarInsnNode(DSTORE,3));
//
//		mainMethod.instructions.add(new VarInsnNode(ALOAD,7));
//		mainMethod.instructions.add(new FieldInsnNode(GETFIELD, "cn/snowflake/rose/events/impl/EventMove", "z", "D"));
//		mainMethod.instructions.add(new VarInsnNode(DSTORE,5));
//
//
//		mainMethod.instructions.add(new VarInsnNode(ALOAD,0));
//		mainMethod.instructions.add(new VarInsnNode(DLOAD,1));
//		mainMethod.instructions.add(new VarInsnNode(DLOAD,3));
//		mainMethod.instructions.add(new VarInsnNode(DLOAD,5));
//		mainMethod.instructions.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/client/entity/AbstractClientPlayer", runtimeDeobfuscationEnabled ? "func_70091_d" : "moveEntity", "(DDD)V", false));
//		mainMethod.instructions.add(new InsnNode(Opcodes.RETURN));
//		clazz.methods.add(mainMethod);
	}
	private void transformEntityClientPlayerMP(ClassNode clazz, MethodNode method) {
		if (method.name.equals("onUpdate") || method.name.equals("func_70071_h_") ) {
			method.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "onUpdate", "()V", false));
		}
		if (method.name.equalsIgnoreCase("sendMotionUpdates") || method.name.equalsIgnoreCase("func_71166_b")){
//			InsnList preInsn = new InsnList();
//
//			preInsn.add(new FieldInsnNode(GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "PRE", "Lcom/darkmagician6/eventapi/types/EventType;"));
//			preInsn.add(new MethodInsnNode(INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "onUpdateWalkingPlayerHook","(Lcom/darkmagician6/eventapi/types/EventType;)V", false));
//
//			//new EventMotion();
//			preInsn.add(new TypeInsnNode(NEW,"cn/snowflake/rose/events/impl/EventMotion"));
//			preInsn.add(new InsnNode(DUP));
//
//			//this.posY
//			preInsn.add(new VarInsnNode(ALOAD,0));
//			preInsn.add(new FieldInsnNode(GETFIELD,"net/minecraft/client/entity/EntityClientPlayerMP",runtimeDeobfuscationEnabled ? "field_70163_u" : "posY","D"));
//
//			//this.rotationYaw
//			preInsn.add(new VarInsnNode(ALOAD,0));
//			preInsn.add(new FieldInsnNode(GETFIELD,"net/minecraft/client/entity/EntityClientPlayerMP",runtimeDeobfuscationEnabled ? "field_70177_z" : "rotationYaw","F"));
//
//			//this.rotationPitch
//			preInsn.add(new VarInsnNode(ALOAD,0));//this
//			preInsn.add(new FieldInsnNode(GETFIELD,"net/minecraft/client/entity/EntityClientPlayerMP", runtimeDeobfuscationEnabled ? "field_70125_A" : "rotationPitch","F"));
//
//			//this.onGround
//			preInsn.add(new VarInsnNode(ALOAD,0));//this
//			preInsn.add(new FieldInsnNode(GETFIELD,"net/minecraft/client/entity/EntityClientPlayerMP",runtimeDeobfuscationEnabled ? "field_70122_E" : "onGround","Z"));
//			//EventType.PRE
//			preInsn.add(new FieldInsnNode(GETSTATIC,Type.getInternalName(EventType.class),"PRE","Lcom/darkmagician6/eventapi/types/EventType;"));
//			// EventMotion(this.posY,this.rotationYaw,this.rotationPitch,this.onGround,EventType.PRE);
//			preInsn.add(new MethodInsnNode(INVOKESPECIAL,"cn/snowflake/rose/events/impl/EventMotion" ,"<init>", "(DFFZLcom/darkmagician6/eventapi/types/EventType;)V", false));
//			// var18
//			preInsn.add(new VarInsnNode(ASTORE,18));
//
//			//EventManager.call( var18 );
//			preInsn.add(new VarInsnNode(ALOAD,18));
//			preInsn.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(EventManager.class), "call", "(Lcom/darkmagician6/eventapi/events/Event;)Lcom/darkmagician6/eventapi/events/Event;", false));
//			preInsn.add(new InsnNode(POP));
//
//			// jad net.minecraft.client.entity.EntityPlayerSP
//			// if(var18.isCancel()){
//			//   MinecraftHook.onUpdateWalkingPlayerHookPOST(EventType.POST);
//			// }
//			preInsn.add(new VarInsnNode(ALOAD,18));
//			preInsn.add(new MethodInsnNode(INVOKEVIRTUAL, "cn/snowflake/rose/events/impl/EventMotion", "isCancel", "()Z", false));
//			LabelNode jmp1 = new LabelNode();
//			preInsn.add(new JumpInsnNode(IFEQ,jmp1));
//			preInsn.add(new FieldInsnNode(GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "POST", "Lcom/darkmagician6/eventapi/types/EventType;"));
//			preInsn.add(new MethodInsnNode(INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "onUpdateWalkingPlayerHookPOST","(Lcom/darkmagician6/eventapi/types/EventType;)V", false));
//			preInsn.add(new InsnNode(RETURN));
//			preInsn.add(jmp1);
//			preInsn.add(new FrameNode(F_SAME, 4, null, 0, null));
//
//			method.instructions.insert(preInsn);
//			InsnList insnList1 = new InsnList();
//			for (AbstractInsnNode abstractInsnNode : method.instructions.toArray()){
//				AbstractInsnNode target1 = null;
//				if (abstractInsnNode.getOpcode() == ALOAD &
//					abstractInsnNode.getNext() instanceof FieldInsnNode
//				){
//					if (((FieldInsnNode) abstractInsnNode.getNext()).name.equalsIgnoreCase(runtimeDeobfuscationEnabled ? "field_70177_z" : "rotationYaw")){
//						target1 = abstractInsnNode;
//
//						insnList1.add(new VarInsnNode(ALOAD,18));
//						insnList1.add(new FieldInsnNode(GETSTATIC,"cn/snowflake/rose/events/impl/EventMotion","yaw","F"));
//						method.instructions.insertBefore(target1,insnList1);
//
//						method.instructions.remove(target1.getNext().getNext());
//						method.instructions.remove(target1);
//						//	  jad net.minecraft.client.entity.EntityClientPlayerMP
//						// 	  dump net.minecraft.client.entity.EntityClientPlayerMP
//
//					}
//				}
//			}

//			preInsn.add(new VarInsnNode(ALOAD,0));//this
//			preInsn.add(new VarInsnNode(ALOAD,18));//pre
//			preInsn.add(new FieldInsnNode(GETFIELD, "cn/snowflake/rose/events/impl/EventMotion", "y", "D"));
//			preInsn.add(new FieldInsnNode(PUTFIELD, "net/minecraft/client/entity/EntityPlayerSP", runtimeDeobfuscationEnabled ? "field_70165_t" : "posY", "D"));
//
//			preInsn.add(new VarInsnNode(ALOAD,0));//this
//			preInsn.add(new VarInsnNode(ALOAD,18)); // pre
//			preInsn.add(new FieldInsnNode(GETFIELD, "cn/snowflake/rose/events/impl/EventMotion", "yaw", "F"));
//			preInsn.add(new FieldInsnNode(PUTFIELD, "net/minecraft/client/entity/EntityPlayerSP", runtimeDeobfuscationEnabled ? "field_70177_z" : "rotationYaw", "F"));
//
//			preInsn.add(new VarInsnNode(ALOAD,0));//this
//			preInsn.add(new VarInsnNode(ALOAD,18));//pre
//			preInsn.add(new FieldInsnNode(GETFIELD, "cn/snowflake/rose/events/impl/EventMotion", "pitch", "F"));
//			preInsn.add(new FieldInsnNode(PUTFIELD, "net/minecraft/client/entity/EntityPlayerSP",  runtimeDeobfuscationEnabled ? "field_70125_A" : "rotationPitch", "F"));
//
//			preInsn.add(new VarInsnNode(ALOAD,0));//this
//			preInsn.add(new VarInsnNode(ALOAD,18));//pre
//			preInsn.add(new FieldInsnNode(GETFIELD, "cn/snowflake/rose/events/impl/EventMotion", "onGround", "Z"));//onGround
//			preInsn.add(new FieldInsnNode(PUTFIELD, "net/minecraft/client/entity/EntityPlayerSP", runtimeDeobfuscationEnabled ? "field_70122_E" : "onGround","Z"));
			InsnList preInsn = new InsnList();
			preInsn.add(new FieldInsnNode(GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "PRE", "Lcom/darkmagician6/eventapi/types/EventType;"));
			preInsn.add(new MethodInsnNode(INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "onUpdateWalkingPlayerHook","(Lcom/darkmagician6/eventapi/types/EventType;)V", false));
			method.instructions.insert(preInsn);
			//EventMotionPost2
			InsnList postInsn = new InsnList();
			postInsn.add(new FieldInsnNode(GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "POST", "Lcom/darkmagician6/eventapi/types/EventType;"));
			postInsn.add(new MethodInsnNode(INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "onUpdateWalkingPlayerHook","(Lcom/darkmagician6/eventapi/types/EventType;)V", false));
			method.instructions.insertBefore(ASMUtil.bottom(method), postInsn);
		}
		if (method.name.equalsIgnoreCase("func_71165_d") || method.name.equalsIgnoreCase("sendChatMessage")) {

			final InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "command", "(Ljava/lang/String;)V", false));

			insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
			insnList.add(new LdcInsnNode("-"));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/snowflake/rose/asm/MinecraftHook", "isNoCommandEnabled", "(Ljava/lang/String;Ljava/lang/String;)Z", false));

			final LabelNode jmp = new LabelNode();
			insnList.add(new JumpInsnNode(Opcodes.IFEQ, jmp));
			insnList.add(new InsnNode(Opcodes.RETURN));
			insnList.add(jmp);
			insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			method.instructions.insert(insnList);
		}
	}


	@Override
	public byte[] transform(ClassLoader arg0, String name, Class<?> clazz, ProtectionDomain arg3, byte[] classByte)
			throws IllegalClassFormatException {
		runtimeDeobfuscationEnabled = true;
		return transform(clazz.getName(), classByte);
	}


}
