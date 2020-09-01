package net.minecraft.injection;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.List;
import java.util.Map;

import com.xue.ModList;
import com.xue.Utils;
import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.logging.log4j.LogManager;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class RegisterPlugin implements IFMLLoadingPlugin {
	
	
	public RegisterPlugin() {
		try {
			URLClassLoader appClassLoader = (URLClassLoader) Launch.class.getClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(appClassLoader, this.getClass().getProtectionDomain().getCodeSource().getLocation());
			MethodUtils.invokeStaticMethod(appClassLoader.loadClass(this.getClass().getName()), "registerTransformer");
		}
		catch(Exception e) {
			Utils.logException(e);
		}
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[0]; //{"cn.snowflake.rose.classTransformer.ClassTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}
	
	public static void registerTransformer() {
		try {
			LaunchClassLoader classLoader = net.minecraft.launchwrapper.Launch.classLoader;
			Field field;
			field = LaunchClassLoader.class.getDeclaredField("transformers");
			field.setAccessible(true);
			List<IClassTransformer> transformers = (List<IClassTransformer>) field.get(classLoader);
			IClassTransformer trasformer = (IClassTransformer)Class.forName("cn.snowflake.rose.asm.ClassTransformer", true, classLoader).newInstance();;
			ModList list = new ModList();
			field.set(classLoader, list);
			
			Utils.logerror("interupt transforms list");
			Utils.logerror("interupt transforms list");
			Utils.logerror("interupt transforms list");
			Utils.logerror("interupt transforms list");
			Utils.logerror("interupt transforms list");
			
			for (IClassTransformer transformer : transformers) {
				list.add(transformer);
				Utils.logerror(transformer.toString());
			}
			
			list.add((IClassTransformer) trasformer);
			
	        CodeSource codeSource = RegisterPlugin.class.getProtectionDomain().getCodeSource();
	        if (codeSource != null) {
	            URL location = codeSource.getLocation();
	            try {
	                File file = new File(location.toURI());
	                if (file.isFile()) {
	               CoreModManager.getLoadedCoremods().remove(file.getName());

	                }
	            } catch (URISyntaxException e) {
	                e.printStackTrace();
	            }
	        } else {
	            LogManager.getLogger().warn("No CodeSource, if this is not a development environment we might run into problems!");
	            LogManager.getLogger().warn(RegisterPlugin.class.getProtectionDomain());
	        }
		} catch (NoSuchFieldException e) {
			Utils.logException(e);
		} catch (SecurityException e) {
			Utils.logException(e);
		} catch (IllegalArgumentException e) {
			Utils.logException(e);
		} catch (IllegalAccessException e) {
			Utils.logException(e);
		} catch (InstantiationException e) {
			Utils.logException(e);
		} catch (ClassNotFoundException e) {
			Utils.logException(e);
		}
		
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
	}