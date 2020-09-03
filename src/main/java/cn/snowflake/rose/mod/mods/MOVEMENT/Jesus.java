package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventMove;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.BlockUtils;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;


import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class Jesus extends Module {
    public Value<String> mode = new Value<>("Jesus","Mode",0);


    public Jesus() {
        super("Jesus", Category.MOVEMENT);
        this.mode.addValue("AAC");
        this.mode.addValue("Solid");
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        if (this.mode.isCurrentMode("Solid")) {
            this.setDisplayName("Solid");
            if (BlockUtils.collideBlock(mc.thePlayer.getBoundingBox()) && mc.thePlayer.isInsideOfMaterial(Material.air) && !mc.thePlayer.isSneaking()){
                mc.thePlayer.motionY = 0.08D;
            }
        }
    }

    @EventTarget
    public void onMotionUpdate(EventMove e) {
        if (e.getEntity() != mc.thePlayer){
            return;
        }
        if (this.mode.isCurrentMode("AAC")) {
            this.setDisplayName("AAC");
            if (this.isInsideBlock()) {
                this.a = !this.a;
                double y;
                if (this.mc.thePlayer.isSneaking()) {
                    y = -0.12D;
                } else if (this.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                    y = 0.12D;
                    e.setOnGround(true);
                } else {
                    y = this.a ? 0.05D : 0.12D;
                }
                this.mc.thePlayer.motionY = y;
            }
        }
    }

    protected boolean isInsideBlock() {
        if(this.mc.thePlayer == null) {
            return false;
        } else {
            AxisAlignedBB b = this.mc.thePlayer.getBoundingBox();
            boolean var2 = false;

            for(int x = MathHelper.floor_double(b.minX); x < MathHelper.floor_double(b.maxX) + 1; ++x) {
                int y = (int)b.minY;
                for(int z = MathHelper.floor_double(b.minZ); z < MathHelper.floor_double(b.maxZ) + 1; ++z) {
                    Block var6 = this.mc.theWorld.getBlock(x,y,z);
                    if(var6 != null && !(var6 instanceof BlockAir)) {
                        if(!(var6 instanceof BlockLiquid)) {
                            return false;
                        }

                        var2 = true;
                    }
                }
            }

            return var2;
        }
    }

    public static boolean isInLiquid() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) {
            return false;
        }
        boolean inLiquid = false;
        final int y = (int)(mc.thePlayer.boundingBox.minY + 0.02);
        for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                Block block = mc.theWorld.getBlock(x,y,z);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }

    public static boolean isOnLiquid() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) {
            return false;
        }
        boolean onLiquid = false;
        final int y = (int)mc.thePlayer.boundingBox.offset(0.0, -0.0, 0.0).minY;
        for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                Block block = mc.theWorld.getBlock(x,y,z);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

    private boolean canJeboos() {
        if (!(this.mc.thePlayer.fallDistance >= 3.0f || this.mc.gameSettings.keyBindJump.isPressed() || isInLiquid() || this.mc.thePlayer.isSneaking())) {
            return true;
        }
        return false;
    }
    private boolean a;

}