package cn.snowflake.rose.transform;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.function.BiConsumer;

import cn.snowflake.rose.transform.transforms.*;
import net.minecraft.injection.ClientLoader;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;


public class ClassTransformer implements IClassTransformer, ClassFileTransformer,Opcodes{
	
	private byte[] transformMethods(byte[] bytes, BiConsumer<ClassNode, MethodNode> transformer) {
		ClassReader classReader = new ClassReader(bytes);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);
		LogManager.getLogger().info("transform -> "+classNode.name);
		classNode.methods.forEach(m ->
			transformer.accept(classNode, m)
		);
		ClassWriter classWriter = new ClassWriter(0);
		classNode.accept(classWriter);
		return classWriter.toByteArray();
	}

	public static Set<String> classNameSet;

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
				"net.minecraft.entity.Entity",
				"cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper",
				"cpw.mods.fml.common.network.FMLEventChannel",
				"net.minecraft.entity.player.EntityPlayer",
				"net.minecraft.client.renderer.Tessellator",
				"net.minecraft.profiler.Profiler",
				"net.minecraft.client.renderer.entity.RenderPlayer",
				"net.minecraft.network.NetHandlerPlayServer",
				"net.minecraft.util.MovementInputFromOptions"
		};
		for (int i=0; i<nameArray.length; i++) {
				classNameSet.add(nameArray[i]);
		}
	}

	public static boolean needTransform(String name) {
		return classNameSet.contains(name);
	}


	public byte[] transform(String name, byte[] classByte) {
		try {
			 if (name.equals("net.minecraft.client.entity.EntityClientPlayerMP")) {
				return transformMethods(classByte,TransformEntityClientPlayerMP::transformEntityClientPlayerMP);
			}
			else if (name.equals("net.minecraft.client.Minecraft")) {
				return transformMethods(classByte, TransformMinecraft::transformMinecraft);
			}
			else if (name.equalsIgnoreCase("net.minecraft.client.renderer.EntityRenderer")){//3d
				return transformMethods(classByte, TransformEntityRenderer::transformRenderEntityRenderer);
			}
			 else if (name.equalsIgnoreCase("net.minecraft.util.MovementInputFromOptions")){
				 return transformMethods(classByte, TransformMovementInputFromOptions::transformMovementInputFromOptions);
			 }
			else if(name.equals("net.minecraft.client.entity.EntityPlayerSP")){
				return  transformMethods(classByte, TransformEntityPlayerSP::transformEntityPlayerSP);
			}
			else if (name.equalsIgnoreCase("net.minecraft.network.NetworkManager")){ //EventPacket
				return this.transformMethods(classByte,TransformNetworkManager::transformNetworkManager);
			}
			else if (name.equalsIgnoreCase("net.minecraft.entity.player.EntityPlayer")){
				return this.transformMethods(classByte,TransformEntityPlayer::transformEntityPlayer);
			 }
			else if (name.equalsIgnoreCase("net.minecraft.block.Block")){
				return this.transformMethods(classByte,TransformBlock::transformBlock);
			}
			else if (name.equalsIgnoreCase("net.minecraft.entity.Entity")){
				return this.transformMethods(classByte,TransformEntity::transformEntity);
			}
			else if (name.equalsIgnoreCase("cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper")){
				return this.transformMethods(classByte,TransformSimpleNetworkWrapper::transformSimpleNetworkWrapper);
			}
			else if (name.equalsIgnoreCase("net.minecraft.profiler.Profiler")){
				return this.transformMethods(classByte,TransformProfiler::transformProfiler);
			}
			else if (name.equalsIgnoreCase("net.minecraft.network.NetHandlerPlayServer")){
				 return this.transformMethods(classByte,TransformNetHandlerPlayServer::transformNetHandlerPlayServer);
			 }
			 else if (name.equalsIgnoreCase("net.minecraft.client.renderer.entity.RenderPlayer")){
				 return this.transformMethods(classByte, TransformRenderPlayer::transformRenderPlayer);
			 }
			 else if (name.equalsIgnoreCase("cpw.mods.fml.common.network.FMLEventChannel")){
			 	return this.transformMethods(classByte,TransformFMLEventChannel::transformFMLEventChannel);
			 }
			 else if (name.equalsIgnoreCase("net.minecraft.client.renderer.entity.RendererLivingEntity")){
				 return this.transformMethods(classByte, TransformRendererLivingEntity::transformRendererLivingEntity);
			 }
		}catch(Exception e) {
			LogManager.getLogger().log(Level.ERROR, ExceptionUtils.getStackTrace(e));
			
		}
		return classByte;
	}

	@Override
	public byte[] transform(ClassLoader arg0, String name, Class<?> clazz, ProtectionDomain arg3, byte[] classByte)
			throws IllegalClassFormatException {
		ClientLoader.runtimeDeobfuscationEnabled = true;
		return transform(clazz.getName(), classByte);
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] classByte) {
		return transform(transformedName, classByte);
	}

}