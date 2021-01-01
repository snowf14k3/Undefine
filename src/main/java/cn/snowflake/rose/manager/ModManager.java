package cn.snowflake.rose.manager;

import cn.snowflake.rose.friend.Friend;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.COMBAT.*;
import cn.snowflake.rose.mod.mods.FORGE.EIOXpGrab;
import cn.snowflake.rose.mod.mods.MOVEMENT.*;
import cn.snowflake.rose.mod.mods.PLAYER.*;
import cn.snowflake.rose.mod.mods.MOVEMENT.Teleport;
import cn.snowflake.rose.mod.mods.PLAYER.Blink;
import cn.snowflake.rose.mod.mods.RENDER.*;
import cn.snowflake.rose.mod.mods.WORLD.*;
import cn.snowflake.rose.mod.mods.FORGE.*;

import java.util.ArrayList;

public class ModManager {
    public static ArrayList<Module> modList = new ArrayList();

    public ModManager() {
        //COMBAT
        addMod(new Aura());
        addMod(new Regen());
        addMod(new Criticals());
        addMod(new FastBow());
        addMod(new AntiAim());
        addMod(new TPAura());
        addMod(new Aimbot());
        addMod(new Velocity());
        addMod(new AutoFish());
        addMod(new Blink());
        //MOVEMENT
        addMod(new Teleport());
        addMod(new Sprint());
        addMod(new AirJump());
        addMod(new Fly());
        addMod(new TargetStrafe());
        addMod(new Speed());
        addMod(new NoFall());
//        addMod(new Jesus());
        addMod(new Step());
        //RENDER
        addMod(new HUD());
        addMod(new NoHurtcam());
        addMod(new ArrowESP());
        addMod(new CrossHair());
        addMod(new FullBright());
        addMod(new ItemESP());

        addMod(new ClickGui());
        addMod(new Tracer());
        addMod(new Chams());
        addMod(new ViewClip());
        addMod(new ESP2D());
        addMod(new ChestESP());
        addMod(new BlockOverlay());
        addMod(new ESP());
        //PLAYER
        addMod(new NoSlowDown());
        addMod(new AutoTools());
//        addMod(new FastConsume());
        addMod(new Stealer());
        addMod(new AntiEffect());
        addMod(new FastBreak());
        addMod(new Freecam());
        addMod(new FastEat());
        addMod(new FastPlace());
        addMod(new FastUse());
        addMod(new InvMove());
//        addMod(new GhostHand());
        //WORLD
        addMod(new ServerCrasher());
        addMod(new Bot());
        addMod(new Xray());
        addMod(new NoFriend());
        addMod(new MineBot());
        addMod(new Spammer());
        addMod(new MCF());
//        addMod(new IRC());
        addMod(new NoCommand());
        addMod(new SecretClose());
        addMod(new Plugins());

        //FORGE
        addMod(new EIOXpGrab());
        addMod(new ScreenProtect());

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