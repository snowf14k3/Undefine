package net.minecraft.injection;

import cn.snowflake.rose.Client;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import java.util.Map;

public class ClientLoader implements IFMLLoadingPlugin {
	public static boolean runtimeDeobfuscationEnabled = true;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"cn.snowflake.rose.asm.ClassTransformer"};
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
