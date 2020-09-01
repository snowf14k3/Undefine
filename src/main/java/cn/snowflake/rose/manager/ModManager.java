package cn.snowflake.rose.manager;

import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.COMBAT.*;
import cn.snowflake.rose.mod.mods.MOVEMENT.*;
import cn.snowflake.rose.mod.mods.PLAYER.*;
import cn.snowflake.rose.mod.mods.MOVEMENT.Teleport;
import cn.snowflake.rose.mod.mods.RENDER.*;
import cn.snowflake.rose.mod.mods.WORLD.Bot;
import cn.snowflake.rose.mod.mods.WORLD.NoCommand;
import cn.snowflake.rose.mod.mods.WORLD.ServerCrasher;
import cn.snowflake.rose.mod.mods.WORLD.Xray;

import java.util.ArrayList;

public class ModManager {
    public static ArrayList<Module> modList = new ArrayList();

    public ModManager() {
        //COMBAT
        addMod(new Aura());
        addMod(new Regen());
        addMod(new Criticals());
        addMod(new TPAura());
        addMod(new SuperKnockback());
        addMod(new Aimbot());
        addMod(new Velocity());
        //MOVEMENT
        addMod(new Teleport());
        addMod(new Sprint());
        addMod(new AirJump());
        addMod(new Fly());
        addMod(new Speed());
        addMod(new NoFall());
        addMod(new Jesus());
        addMod(new Step());
        //RENDER
        addMod(new HUD());
        addMod(new Animation());
        addMod(new FullBright());
        addMod(new ClickGui());
        addMod(new ViewClip());
        addMod(new ChestESP());
        addMod(new ESP());
        //PLAYER
        addMod(new NoSlow());
        addMod(new AutoTools());
        addMod(new FastBreak());
        addMod(new FastPlace());
        addMod(new FastUse());
        addMod(new InvMove());
        //WORLD
        addMod(new ServerCrasher());
        addMod(new Bot());
        addMod(new Xray());
        addMod(new NoCommand());
    }


    public void addMod(Module m) {
        modList.add(m);
    }

    public static Module getModByName(String mod) {
        for (Module m : modList) {
            if (!m.getName().equalsIgnoreCase(mod)) continue;
            return m;
        }
        return null;
    }

    public static ArrayList<Module> getModList() {
        return modList;
    }

}
