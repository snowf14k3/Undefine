package net.minecraft.injection;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.transform.ClassTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.Type;

import java.util.Map;

public class ClientLoader implements IFMLLoadingPlugin {
	public static boolean runtimeDeobfuscationEnabled = true;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {Type.getInternalName(ClassTransformer.class).replace("/",".")};
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
		runtimeDeobfuscationEnabled = (boolean)data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
}
