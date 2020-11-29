package cn.snowflake.rose.mod.mods.WORLD;


import java.util.ArrayList;
import java.util.List;


import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.Value;
import net.minecraft.block.Block;

public class Xray extends Module {
    public static ArrayList<Block> block = new ArrayList();
//    public static Value<Boolean> cave = new Value<Boolean>("Xray_Cave", false);
    public static Value<Double> OPACITY = new Value<Double>("Xray_Opacity", 160.0, 0.0, 255.0, 5.0);
    private static int opacity = 160;
    public static List<Integer> blocks = new ArrayList<Integer>();
    public static boolean x = false;
    public Xray() {
        super("Xray","Xray",  Category.WORLD);
        blocks.add(16);
        blocks.add(56);
        blocks.add(14);
        blocks.add(15);
        blocks.add(129);//Emerald Ore
        blocks.add(73);

    }

    @Override
    public void onEnable() {
        x = true;
        this.mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        x = false;
        this.mc.renderGlobal.loadRenderers();
    }

    public static boolean containsID(Block b) {
        return block.contains(b);
    }
    public static List<Integer> getBlocks() {
        return blocks;
    }

}
