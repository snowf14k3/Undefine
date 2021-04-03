package cn.snowflake.rose.management;

import java.util.ArrayList;

import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.COMBAT.Aimbot;
import cn.snowflake.rose.mod.mods.COMBAT.AntiAim;
import cn.snowflake.rose.mod.mods.COMBAT.Aura;
import cn.snowflake.rose.mod.mods.COMBAT.Criticals;
import cn.snowflake.rose.mod.mods.COMBAT.FastBow;
import cn.snowflake.rose.mod.mods.COMBAT.NoRecoil;
import cn.snowflake.rose.mod.mods.COMBAT.Regen;
import cn.snowflake.rose.mod.mods.COMBAT.TPAura;
import cn.snowflake.rose.mod.mods.COMBAT.Velocity;
import cn.snowflake.rose.mod.mods.FORGE.EIOXpGrab;
import cn.snowflake.rose.mod.mods.FORGE.FTBSatchelDupe;
import cn.snowflake.rose.mod.mods.FORGE.MagicGod;
import cn.snowflake.rose.mod.mods.FORGE.OneWayTicket;
import cn.snowflake.rose.mod.mods.FORGE.ResearchGod;
import cn.snowflake.rose.mod.mods.FORGE.ScreenProtect;
import cn.snowflake.rose.mod.mods.MOVEMENT.AirJump;
import cn.snowflake.rose.mod.mods.MOVEMENT.FastClimb;
import cn.snowflake.rose.mod.mods.MOVEMENT.Fly;
import cn.snowflake.rose.mod.mods.MOVEMENT.Freecam;
import cn.snowflake.rose.mod.mods.MOVEMENT.Jesus;
import cn.snowflake.rose.mod.mods.MOVEMENT.NoFall;
import cn.snowflake.rose.mod.mods.MOVEMENT.Scaffold;
import cn.snowflake.rose.mod.mods.MOVEMENT.Speed;
import cn.snowflake.rose.mod.mods.MOVEMENT.Step;
import cn.snowflake.rose.mod.mods.MOVEMENT.TargetStrafe;
import cn.snowflake.rose.mod.mods.MOVEMENT.Teleport;
import cn.snowflake.rose.mod.mods.MOVEMENT.VClipDown;
import cn.snowflake.rose.mod.mods.MOVEMENT.VClipUP;
import cn.snowflake.rose.mod.mods.PLAYER.AntiEffect;
import cn.snowflake.rose.mod.mods.PLAYER.AutoFish;
import cn.snowflake.rose.mod.mods.PLAYER.AutoTool;
import cn.snowflake.rose.mod.mods.PLAYER.Blink;
import cn.snowflake.rose.mod.mods.PLAYER.FastBreak;
import cn.snowflake.rose.mod.mods.PLAYER.FastEat;
import cn.snowflake.rose.mod.mods.PLAYER.FastPlace;
import cn.snowflake.rose.mod.mods.PLAYER.FastUse;
import cn.snowflake.rose.mod.mods.PLAYER.InvMove;
import cn.snowflake.rose.mod.mods.PLAYER.NoSlowDown;
import cn.snowflake.rose.mod.mods.PLAYER.Sprint;
import cn.snowflake.rose.mod.mods.PLAYER.Stealer;
import cn.snowflake.rose.mod.mods.PLAYER.Timer;
import cn.snowflake.rose.mod.mods.RENDER.ArrowESP;
import cn.snowflake.rose.mod.mods.RENDER.BlockOverlay;
import cn.snowflake.rose.mod.mods.RENDER.Chams;
import cn.snowflake.rose.mod.mods.RENDER.ChestESP;
import cn.snowflake.rose.mod.mods.RENDER.ClickGui;
import cn.snowflake.rose.mod.mods.RENDER.CrossHair;
import cn.snowflake.rose.mod.mods.RENDER.ESP;
import cn.snowflake.rose.mod.mods.RENDER.ESP2D;
import cn.snowflake.rose.mod.mods.RENDER.FullBright;
import cn.snowflake.rose.mod.mods.RENDER.HUD;
import cn.snowflake.rose.mod.mods.RENDER.ItemESP;
import cn.snowflake.rose.mod.mods.RENDER.NoHurtcam;
import cn.snowflake.rose.mod.mods.RENDER.PlayerList;
import cn.snowflake.rose.mod.mods.RENDER.SimpleHUD;
import cn.snowflake.rose.mod.mods.RENDER.Tracer;
import cn.snowflake.rose.mod.mods.RENDER.ViewClip;
import cn.snowflake.rose.mod.mods.WORLD.Bot;
import cn.snowflake.rose.mod.mods.WORLD.FakeLag;
import cn.snowflake.rose.mod.mods.WORLD.IRC;
import cn.snowflake.rose.mod.mods.WORLD.MCF;
import cn.snowflake.rose.mod.mods.WORLD.MineBot;
import cn.snowflake.rose.mod.mods.WORLD.NoCommand;
import cn.snowflake.rose.mod.mods.WORLD.NoFriend;
import cn.snowflake.rose.mod.mods.WORLD.Plugins;
import cn.snowflake.rose.mod.mods.WORLD.SecretClose;
import cn.snowflake.rose.mod.mods.WORLD.ServerCrasher;
import cn.snowflake.rose.mod.mods.WORLD.Spammer;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cpw.mods.fml.common.Loader;

public class ModManager {
    public static ArrayList<Module> modList = new ArrayList();

    public ModManager() {
        //COMBAT
        addMod(new Aura());
        addMod(new Regen());
        addMod(new NoRecoil());
        addMod(new Criticals());
        addMod(new FastBow());
        addMod(new AntiAim());
//        addMod(new Aimbot2());
        addMod(new TPAura());
        addMod(new Aimbot());
        addMod(new Velocity());
        addMod(new AutoFish());
        addMod(new Blink());
        //MOVEMENT
        addMod(new Sprint());
        addMod(new AirJump());
        addMod(new Fly());
        addMod(new VClipDown());
        addMod(new VClipUP());
//        addMod(new VehicleSpeed());
        addMod(new FastClimb());
        addMod(new Scaffold());
        addMod(new Speed());
        addMod(new TargetStrafe());
        addMod(new NoFall());
        addMod(new Jesus());
        addMod(new Step());
        addMod(new Teleport());
        
        //RENDER
        addMod(new HUD());
        addMod(new NoHurtcam());
        addMod(new ArrowESP());
        addMod(new CrossHair());
        addMod(new PlayerList());
        addMod(new FullBright());
        addMod(new ItemESP());
        addMod(new ClickGui());
        addMod(new Tracer());
        addMod(new Chams());
        addMod(new ViewClip());
        addMod(new ESP2D());
        addMod(new ChestESP());
        addMod(new SimpleHUD());
        addMod(new BlockOverlay());
        addMod(new ESP());
//        addMod(new Notifications());
        //PLAYER
        addMod(new NoSlowDown());
        addMod(new AutoTool());
        addMod(new Stealer());
        addMod(new AntiEffect());
        addMod(new FastBreak());
        addMod(new Freecam());
        addMod(new FastEat());
        addMod(new FastPlace());
        addMod(new FastUse());
        addMod(new InvMove());
        addMod(new Timer());
        //WORLD
        addMod(new ServerCrasher());
        addMod(new Bot());
        addMod(new Xray());
        addMod(new NoFriend());
        addMod(new MineBot());
        addMod(new Spammer());
        addMod(new MCF());
        addMod(new FakeLag());
        addMod(new IRC());
        addMod(new NoCommand());
        addMod(new Plugins());

        //FORGE
        addMod(new ScreenProtect());
        addMod(new SecretClose());
        addMod(new MagicGod());
        addMod(new ResearchGod());

        if (Loader.isModLoaded("EnderIO")) {
            addMod(new EIOXpGrab());
        }
        if (Loader.isModLoaded("Railcraft")){
            addMod(new OneWayTicket());
        }
        if (Loader.isModLoaded("ThermalExpansion") && Loader.isModLoaded("FTBL")){
            addMod(new FTBSatchelDupe());
        }

        ok = true;
    }
    public static boolean ok = false;

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

    public ArrayList<Module> getModList() {
        return modList;
    }

	public static Module getModByClass(Class<? extends Module> cls) {
		for (Object m1 : modList) {
			Module m = (Module) m1;
		if (m.getClass() != cls)
			continue;
		return m;
	}
	return null;
	}

}