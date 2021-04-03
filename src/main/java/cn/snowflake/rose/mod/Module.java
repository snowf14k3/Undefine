package cn.snowflake.rose.mod;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class Module {
    private String name;
    private String displayName;
    private Category category;
    private int key;
    public static Minecraft mc = Minecraft.getMinecraft();
    private boolean isEnabled;
    public boolean openValues;
    private String rendername;

    public Module(String name,Category category) {
        this.name = name;
        this.category = category;
        this.key = -1;
    }
    public Module(String name,String rendername,Category category) {
        this.name = name;
        this.category = category;
        this.rendername = rendername;
        this.key = -1;
    }
    public boolean hidden ;

    public void onDisable() {
    }
    public boolean isHidden() {
        return hidden;
    }
    public String getRenderName() {
        return rendername;
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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

    public int getValueSize(){
        ArrayList<Value> size = new ArrayList();
        for (Value value : Value.list){
            if (value.getValueName().split("_")[0].equalsIgnoreCase(this.name)){
                size.add(value);
            }
        }
        return size.size();
    }

    public void set(boolean state) {
        this.set(state, false);
        Client.instance.fileMgr.saveMods();
    }
}