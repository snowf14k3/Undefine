package cn.snowflake.rose.mod.mods.FORGE;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import net.minecraft.world.WorldSettings;

public class VisualCreative extends Module {
    public VisualCreative() {
        super("VisualCreative", "Visual Creative", Category.FORGE);
    }

    @Override
    public String getDescription() {
        return "给自己一个假创造(可配合一些mod刷物品)!";
    }

    @Override
    public void onEnable() {
        mc.playerController.setGameType(WorldSettings.GameType.CREATIVE);
        WorldSettings.GameType.CREATIVE.configurePlayerCapabilities(mc.thePlayer.capabilities);
        mc.thePlayer.sendPlayerAbilities();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.playerController.setGameType(WorldSettings.GameType.SURVIVAL);
        WorldSettings.GameType.CREATIVE.configurePlayerCapabilities(mc.thePlayer.capabilities);
        mc.thePlayer.sendPlayerAbilities();
        super.onEnable();
    }

}
