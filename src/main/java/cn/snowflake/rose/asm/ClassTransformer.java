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
import cn.snowflake.rose.utils.Rotation;
import cn.snowflake.rose.utils.asm.ASMUtil;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import cpw.mods.fml.common.Loader;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
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
				"cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper",
				"net.minecraft.client.renderer.entity.RendererLivingEntity",
				"net.minecraft.entity.player.EntityPlayer"
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
	public static  boolean runtimeDeobfuscationEnabled = true;

//	public static  boolean runtimeDeobfuscationEnabled = false;

	public byte[] transform(String name, byte[] classByte) {
		try {

			 if (name.equals("net.minecraft.client.entity.EntityClientPlayerMP")) {
				return this.transformMethods(classByte,this::transformEntityClientPlayerMP);
			}
			 else if (name.equals("net.minecraft.client.Minecraft")) {
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
			else if (name.equalsIgnoreCase("net.minecraft.client.renderer.tileentity.TileEntityChestRenderer")){
				 return this.transformMethods(classByte,this::transformTileEntityChestRenderer);
			 }
			else if(name.equals("net.minecraft.client.entity.EntityPlayerSP")){  //fixed
				return  transformMethods(classByte, this::transformEntityPlayerSP);
			}
			else if (name.equalsIgnoreCase("net.minecraftforge.client.GuiIngameForge")){
				return this.transformMethods(classByte,this::transform2D);
			}
			else if (name.equalsIgnoreCase("net.minecraft.network.NetHandlerPlayServer")){
				return this.transformMethods(classByte,this::transformNetHandlerPlayServer);
			}
			else if (name.equalsIgnoreCase("net.minecraft.network.NetworkManager")){ //EventPacket
				return this.transformMethods(classByte,this::transformNetworkManager);
			}
			else if (name.equalsIgnoreCase("net.minecraft.entity.player.EntityPlayer")){
				return this.transformMethods(classByte,this::transformEntityPlayer);
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
			else if (name.equalsIgnoreCase("net.minecraft.client.renderer.tileentity.RendererLivingEntity")){
				return this.transformMethods(classByte,this::transformRendererLivingEntity);
			}
//			else if (name.equalsIgnoreCase("net.minecraft.client.model.ModelBiped")){
//				return this.transformMethods(classByte,this::transformModelBiped);
//			}
		}catch(Exception e) {
			LogManager.getLogger().log(Level.ERROR, ExceptionUtils.getStackTrace(e));
			
		}
		return classByte;
	}

	private void transformEntityPlayer(ClassNode classNode, MethodNode methodNode) {
		if (methodNode.name.equalsIgnoreCase("isEntityInsideOpaqueBlock") || methodNode.name.equalsIgnoreCase("func_70094_T")){
				InsnList insnList = new InsnList();
				insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "insideHook", "()Z", false));
				LabelNode labelNode = new LabelNode();
				insnList.add(new JumpInsnNode(IFEQ, labelNode));
				insnList.add(new InsnNode(ICONST_0));
				insnList.add(new InsnNode(IRETURN));
				insnList.add(labelNode);
				methodNode.instructions.insert(insnList);
		}
	}

	private void transformRendererLivingEntity(ClassNode classNode, MethodNode methodNode) {
		if (methodNode.name.equalsIgnoreCase("doRender") || methodNode.name.equalsIgnoreCase("func_76986_a")){
			InsnList insnList1 = new InsnList();
			InsnList insnList2 = new InsnList();
			insnList1.add(new VarInsnNode(ALOAD,1));
			insnList1.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "chamsHook1", "(Ljava/lang/Object;)V", false));
			methodNode.instructions.insert(insnList1);

			insnList2.add(new VarInsnNode(ALOAD,1));
			insnList2.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "chamsHook2", "(Ljava/lang/Object;)V", false));
			methodNode.instructions.insertBefore(ASMUtil.bottom(methodNode),insnList2);
		}
	}



	private void transformSimpleNetworkWrapper(ClassNode classNode, MethodNode methodNode) {
		if (methodNode.name.equalsIgnoreCase("sendToServer")) {

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
			LogManager.getLogger().info("Successfully initialized FML hook !");

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

//			InsnList steplist = new InsnList();
//			steplist.add(new FieldInsnNode(GETFIELD,"net/minecraft/entity/Entity",runtimeDeobfuscationEnabled ? "field_70138_W" : "stepHeight","F"));
//			steplist.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "eventStepHook1", "(F)Z", false));
//			for (AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
//				if (abstractInsnNode.getOpcode() == ALOAD &
//						abstractInsnNode.getNext() instanceof FieldInsnNode &
//						abstractInsnNode.getNext().getOpcode() == GETFIELD &
//						abstractInsnNode.getNext().getNext() instanceof InsnNode &
//						abstractInsnNode.getNext().getNext().getOpcode() == F2D
//				) {
//					methodNode.instructions.insert(abstractInsnNode.getNext().getNext(),steplist);
//				}
//			}
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
			insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "onNoSlowEnable2", "()Z", false));
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
			insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "isXrayEnabled", "()Z", false));
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
//		if (methodNode.name.equalsIgnoreCase("isCollidable") || methodNode.name.equalsIgnoreCase("func_149703_v")){
//			final InsnList insnList = new InsnList();
//			insnList.add(new );
//		}
	}
	//transformNetworkManager start
	private void transformNetworkManager(ClassNode classNode, MethodNode methodNode) {
		if (methodNode.name.equalsIgnoreCase("scheduleOutboundPacket") || methodNode.name.equalsIgnoreCase("func_150725_a")){
			final InsnList preInsn = new InsnList();
			preInsn.add(new VarInsnNode(Opcodes.ALOAD, 1));//方法的第一个参数
			preInsn.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "SEND", "Lcom/darkmagician6/eventapi/types/EventType;"));
			preInsn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "channelRead0Hook","(Ljava/lang/Object;Lcom/darkmagician6/eventapi/types/EventType;)Z", false));
			final LabelNode jmp = new LabelNode();
			preInsn.add(new JumpInsnNode(Opcodes.IFEQ, jmp));
			preInsn.add(new InsnNode(Opcodes.RETURN));
			preInsn.add(jmp);
			preInsn.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			methodNode.instructions.insert(preInsn);
		}
		if (methodNode.name.equalsIgnoreCase("channelRead0") && methodNode.desc.equalsIgnoreCase("(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V")){
			final InsnList preInsn = new InsnList();
			preInsn.add(new VarInsnNode(Opcodes.ALOAD, 2));
			preInsn.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "RECIEVE", "Lcom/darkmagician6/eventapi/types/EventType;"));
			preInsn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "channelRead0Hook","(Ljava/lang/Object;Lcom/darkmagician6/eventapi/types/EventType;)Z", false));
			final LabelNode jmp = new LabelNode();
			preInsn.add(new JumpInsnNode(Opcodes.IFEQ, jmp));
			preInsn.add(new InsnNode(Opcodes.RETURN));
			preInsn.add(jmp);
			preInsn.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			methodNode.instructions.insert(preInsn);
			/**
			 * if(channelRead0Hook(packet,EventType.RECIEVE)){
			 *  return ;
			 * }
			 */
		}
	}
	public static boolean channelRead0Hook(Object packet, EventType eventType) {
		if(packet != null) {
			if (packet instanceof S05PacketSpawnPosition){
				Client.canCancle = false;
			}
			final EventPacket event = new EventPacket(eventType,packet);
			EventManager.call(event);
			if (event.getPacket() instanceof S08PacketPlayerPosLook){
				Client.canCancle = true;
			}
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
			AbstractInsnNode target = ASMUtil.findInsnLdc(method,"hand");
			if (target != null){
				method.instructions.insert(target, new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "Event3D", "()V", false));
			}
		}
		if (method.name.equalsIgnoreCase("hurtCameraEffect") || method.name.equalsIgnoreCase("func_78482_e")){
			InsnList insnList = new InsnList();
			insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "isNohurtcamEnable", "()Z", false));
			LabelNode labelNode = new LabelNode();
			insnList.add(new JumpInsnNode(IFEQ, labelNode));
			insnList.add(new InsnNode(RETURN));
			insnList.add(labelNode);
			method.instructions.insert(insnList);
		}
		if ((method.name.equalsIgnoreCase("orientCamera") || method.name.equalsIgnoreCase("func_78467_g")) && method.desc.equalsIgnoreCase("(F)V")){
			AbstractInsnNode target = ASMUtil.findMethodInsn(method, INVOKEVIRTUAL,"net/minecraft/util/Vec3", runtimeDeobfuscationEnabled ?"func_72438_d" : "distanceTo","(Lnet/minecraft/util/Vec3;)D");
			if (target != null){
				InsnList insnList2 = new InsnList();

				InsnList insnList = new InsnList();
				insnList.add(new MethodInsnNode(INVOKESTATIC,Type.getInternalName(MinecraftHook.class),"isViewClipEnabled","()Z",false));
				LabelNode labelNode = new LabelNode();
				insnList.add(new JumpInsnNode(IFNE,labelNode));
				method.instructions.insertBefore(ASMUtil.forward(target,8)
						,insnList);
				insnList2.add(labelNode);
				insnList2.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
				method.instructions.insert(ASMUtil.forward(target,13)
						,insnList2);
				//   dump net.minecraft.client.renderer.EntityRenderer
				// jad net.minecraft.client.renderer.EntityRenderer
			}
		}
	}
	private void transformTileEntityChestRenderer(ClassNode classNode, MethodNode methodNode) {
		if (methodNode.name.equalsIgnoreCase("renderTileEntityAt") || methodNode.name.equalsIgnoreCase("func_147500_a")) {
			InsnList insnList1 = new InsnList();
			InsnList insnList2 = new InsnList();
			insnList1.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "chestesphook1", "()V", false));
			methodNode.instructions.insert(insnList1);

			insnList2.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "chestesphook2", "()V", false));
			methodNode.instructions.insertBefore(ASMUtil.bottom(methodNode),insnList2);

		}
	}
	private void transform2D(ClassNode clazz, MethodNode method) {
		if (method.name.equalsIgnoreCase("func_73830_a") || method.name.equalsIgnoreCase("renderGameOverlay")){
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(FLOAD,1));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "Event2D", "(F)V", false));
			method.instructions.insert(ASMUtil.bottom(method).getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious(),insnList);

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
					method.instructions.insert(insn,new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "runClient", "()V", false));
				}
				if (methodInsn.name.equals("func_152348_aa")) {
					method.instructions.insert(insn,new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "runTick", "()V", false));
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
						method.instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "onNoSlowEnable", "()V", false));
					}
					if (methodInsn.name.equals("func_145771_j")) {
						method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "onToggledTimerZero", "()V", false));
					}
				}
			}
		}
		if (method.name.equalsIgnoreCase("func_145771_j")){
			final InsnList insnList = new InsnList();
			insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "pushOutOfBlocksHooks","()Z", false));
			final LabelNode jmp = new LabelNode();
			insnList.add(new JumpInsnNode(Opcodes.IFEQ, jmp));
			insnList.add(new InsnNode(ICONST_0));
			insnList.add(new InsnNode(IRETURN));
			insnList.add(jmp);
			insnList.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			method.instructions.insert(insnList);
		}
	}
	private void transformEntityClientPlayerMP(ClassNode clazz, MethodNode method) {
		//jad net.minecraft.client.entity.EntityClientPlayerMP

		if (method.name.equals("onUpdate") || method.name.equals("func_70071_h_") ) {
			method.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "onUpdate", "()V", false));
		}
		if (method.name.equalsIgnoreCase("sendMotionUpdates") || method.name.equalsIgnoreCase("func_71166_b")){
			//replace the shit of old

//			InsnList preInsn = new InsnList();
//			//new EventMotion();
//			preInsn.add(new TypeInsnNode(NEW,"cn/snowflake/rose/events/impl/EventMotion"));
//			preInsn.add(new InsnNode(DUP));
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
//
//
//			// EventMotion(this.posY,this.rotationYaw,this.rotationPitch,this.onGround);
//			preInsn.add(new MethodInsnNode(INVOKESPECIAL, "cn/snowflake/rose/events/impl/EventMotion", "<init>", "(DFFZ)V", false));
//			// var18
//			preInsn.add(new VarInsnNode(ASTORE,22));
//
//			//EventManager.call( var18 );
//			preInsn.add(new VarInsnNode(ALOAD,22));
//			preInsn.add(new MethodInsnNode(INVOKESTATIC, "com/darkmagician6/eventapi/EventManager", "call", "(Lcom/darkmagician6/eventapi/events/Event;)Lcom/darkmagician6/eventapi/events/Event;", false));
//			preInsn.add(new InsnNode(POP));
//
//			method.instructions.insert(preInsn);



			InsnList preInsn = new InsnList();
			preInsn.add(new FieldInsnNode(GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "PRE", "Lcom/darkmagician6/eventapi/types/EventType;"));
			preInsn.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "onUpdateWalkingPlayerHook","(Lcom/darkmagician6/eventapi/types/EventType;)V", false));
			method.instructions.insert(preInsn);

			InsnList postInsn = new InsnList();
			postInsn.add(new FieldInsnNode(GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "POST", "Lcom/darkmagician6/eventapi/types/EventType;"));
			postInsn.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "onUpdateWalkingPlayerHook","(Lcom/darkmagician6/eventapi/types/EventType;)V", false));
			method.instructions.insertBefore(ASMUtil.bottom(method), postInsn);



//			for (AbstractInsnNode abstractInsnNode : method.instructions.toArray()){
//				if (abstractInsnNode.getOpcode() == ALOAD &
//						abstractInsnNode.getNext() instanceof FieldInsnNode
//				){
//					if ( ((FieldInsnNode) abstractInsnNode.getNext()).name.equalsIgnoreCase(runtimeDeobfuscationEnabled ? "field_70163_u" : "posY") && !(abstractInsnNode.getPrevious().getPrevious() instanceof TypeInsnNode)
//					){
//						method.instructions.set(abstractInsnNode.getNext(),new FieldInsnNode(GETFIELD,"cn/snowflake/rose/events/impl/EventMotion","y","D"));
//						method.instructions.set(abstractInsnNode,new VarInsnNode(ALOAD,22));
//					}else if ( ((FieldInsnNode) abstractInsnNode.getNext()).name.equalsIgnoreCase(runtimeDeobfuscationEnabled ? "field_70177_z" : "rotationYaw") && !(abstractInsnNode.getPrevious().getPrevious().getPrevious().getPrevious() instanceof TypeInsnNode)
//					){
//						method.instructions.set(abstractInsnNode.getNext(),new FieldInsnNode(GETFIELD,"cn/snowflake/rose/events/impl/EventMotion","yaw","F"));
//						method.instructions.set(abstractInsnNode,new VarInsnNode(ALOAD,22));
//					}else if ( ((FieldInsnNode) abstractInsnNode.getNext()).name.equalsIgnoreCase(runtimeDeobfuscationEnabled ? "field_70125_A" : "rotationPitch") && !(abstractInsnNode.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious() instanceof TypeInsnNode)
//					){
//						method.instructions.set(abstractInsnNode.getNext(),new FieldInsnNode(GETFIELD,"cn/snowflake/rose/events/impl/EventMotion","pitch","F"));
//						method.instructions.set(abstractInsnNode,new VarInsnNode(ALOAD,22));
//					}else if ( ((FieldInsnNode) abstractInsnNode.getNext()).name.equalsIgnoreCase(runtimeDeobfuscationEnabled ? "field_70122_E" : "onGround") && !(abstractInsnNode.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious() instanceof TypeInsnNode)
//					){
//						method.instructions.set(abstractInsnNode.getNext(),new FieldInsnNode(GETFIELD,"cn/snowflake/rose/events/impl/EventMotion","onGround","Z"));
//						method.instructions.set(abstractInsnNode,new VarInsnNode(ALOAD,22));
//					}
//				}
//			}
		}
		if (method.name.equalsIgnoreCase("func_71165_d") || method.name.equalsIgnoreCase("sendChatMessage")) {

			final InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "command", "(Ljava/lang/String;)V", false));

			insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
			insnList.add(new LdcInsnNode("-"));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "isNoCommandEnabled", "(Ljava/lang/String;Ljava/lang/String;)Z", false));

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
