package cn.snowflake.rose.mod.mods.RENDER;

import java.awt.Color;

import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;


import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;

public class BlockOverlay extends Module {

    private Value<Boolean> renderString = new Value<Boolean>("BlockHighlight_Render String", true);

    public BlockOverlay() {
        super("BlockOverlay", Category.RENDER);
    }

    @EventTarget
    public void onRender(EventRender2D event) {
        Block block = this.mc.theWorld.getBlock(this.mc.objectMouseOver.blockX,this.mc.objectMouseOver.blockY,this.mc.objectMouseOver.blockZ);
        String s = String.valueOf(block.getLocalizedName());
        String s1 = block.getLocalizedName()+" | "+Block.getIdFromBlock(block);
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && (this.renderString.getValueState()).booleanValue()) {
            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            ScaledResolution res = new ScaledResolution(this.mc,this.mc.displayWidth,this.mc.displayHeight);
            int x = res.getScaledWidth() / 2 - font.getStringWidth(s1) / 2;
            int y = res.getScaledHeight() / 2 + 8;
            RenderUtil.drawRect((float)x, (float)y, (float)(x + font.getStringWidth(s1) + 3), (float)((float)(y + font.FONT_HEIGHT) + 0.5f), (int)RenderUtil.reAlpha(Colors.BLACK.c, (float)0.4f));
            font.drawString(s1, (x + 1), y + 1, -1, true);
        }
    }

    @EventTarget
    public void render(EventRender3D event) {
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = new BlockPos(this.mc.objectMouseOver.blockX,this.mc.objectMouseOver.blockY,this.mc.objectMouseOver.blockZ);
            Block block = this.mc.theWorld.getBlock(this.mc.objectMouseOver.blockX,this.mc.objectMouseOver.blockY,this.mc.objectMouseOver.blockZ);
            String s = block.getLocalizedName();
            double x = (double)pos.getX() - RenderManager.renderPosX;
            double y = (double)pos.getY() - RenderManager.renderPosY;
            double z = (double)pos.getZ() - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glColor4f((float)0.2f, (float)0.5f, (float)0.8f, (float)0.25f);
            double minX = block instanceof BlockStairs || Block.getIdFromBlock((Block)block) == 134 ? 0.0 : block.getBlockBoundsMinX();
            double minY = block instanceof BlockStairs || Block.getIdFromBlock((Block)block) == 134 ? 0.0 : block.getBlockBoundsMinY();
            double minZ = block instanceof BlockStairs || Block.getIdFromBlock((Block)block) == 134 ? 0.0 : block.getBlockBoundsMinZ();
            RenderUtil.drawBoundingBox(new AltAxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
            GL11.glColor4f((float)0.2f, (float)0.5f, (float)0.4f, (float)1.0f);
            GL11.glLineWidth((float)0.5f);
            RenderUtil.drawOutlinedBoundingBox(new AltAxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

}
