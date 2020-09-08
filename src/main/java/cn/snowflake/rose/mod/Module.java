package cn.snowflake.rose.mod;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.mod.mods.RENDER.Chams;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Module {
    private String name;
    private String displayName;
    private Category category;
    private int key;
    public Minecraft mc = Minecraft.getMinecraft();
    private boolean isEnabled;
    public boolean openValues;

    public Module(String name,Category category) {
        this.name = name;
        this.category = category;
    }

    public void onDisable() {
    }

    public String getName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getdisplayName() {
        return displayName;
    }
    public void setKey(int key) {
        this.key = key;
    }
    public int getKey() {
        return this.key;
    }
    public boolean isEnabled() {
        return this.isEnabled;
    }
    public Category getCategory() {
        return this.category;
    }
    public boolean hasValues() {
        for (Value value : Value.list) {
            String name = value.getValueName().split("_")[0];
            if (!name.equalsIgnoreCase(this.getName())) continue;
            return true;
        }
        return false;
    }
    public void onToggle() {
    }
    public void set(boolean state, boolean safe) {
        this.isEnabled = state;
        this.onToggle();
        if (state) {
            if (this.mc.theWorld != null) {
                this.onEnable();
            }
            EventManager.register(this);
        } else {
            if (this.mc.theWorld != null) {
                this.onDisable();
            }
            EventManager.unregister(this);
        }
        if (safe) {
	       Client.instance.fileMgr.saveMods();
        }
    }
    public void onEnable() {

    }

    public void set(boolean state) {
        this.set(state, false);
        Client.instance.fileMgr.saveMods();
    }
}
