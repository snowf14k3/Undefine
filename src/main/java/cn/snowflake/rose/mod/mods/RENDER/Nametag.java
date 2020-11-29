package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.events.impl.EventRender3D;
import cn.snowflake.rose.manager.FriendManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.GlStateManager;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.RenderUtil;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Nametag extends Module {
    public Nametag() {
        super("Nametag", "Name Tag", Category.RENDER);
    }
    private Value<Double> size = new Value<Double>("NameTag_Size", Double.valueOf(1D), Double.valueOf(1D), Double.valueOf(10D), 0.1D);
    public Value<Boolean> invisible = new Value<Boolean>("NameTag_Invisible", false);


    @EventTarget
    public void on3D(EventRender3D eventRender3D){
        for (Object o : this.mc.theWorld.playerEntities) {
            EntityPlayer p = (EntityPlayer) o;
            if(p != mc.thePlayer) {
                double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * JReflectUtility.getRenderPartialTicks()
                        - RenderManager.renderPosX;
                double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * JReflectUtility.getRenderPartialTicks()
                        - RenderManager.renderPosY;
                double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * JReflectUtility.getRenderPartialTicks()
                        - RenderManager.renderPosZ;
                renderNameTag(p, String.valueOf(p.getDisplayName()) , pX, pY, pZ);
            }
        }
    }
    public void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
        FontRenderer fr = super.mc.fontRenderer;
        float var10 = mc.thePlayer.getDistanceToEntity(entity) / 6.0F;
        if(var10 < 0.8F) {
            var10 = 0.8F;
        }

        if(entity.isInvisible() && invisible.getValueState().booleanValue()) {
            return;
        }

        pY += entity.isSneaking()?0.5D:0.7D;
        float var11 = (float) (var10 * this.size.getValueState().doubleValue());
        var11 /= 100.0F;
        tag = entity.getCommandSenderName();
        String var12 = "";
        String var13 = "";
        if(!FriendManager.isFriend(entity)) { //Teams 和 ClientFriend
            var13 = "";
        } else {
            var13 = "\u00a7b[Friend]";
        }

        if((var13 + var12).equals("")) {
            var13 = "\u00a7a";
        }

        String var14 = var13 + var12 + tag;
        String var15 = "\u00a77HP:" + (int)entity.getHealth();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)pX, (float)pY + 1.4F, (float)pZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-var11, -var11, var11);
        setGLCap(2896, false);
        setGLCap(2929, false);
        int var16 = super.mc.fontRenderer.getStringWidth(var14) / 2;
        setGLCap(3042, true);
        GL11.glBlendFunc(770, 771);
        this.drawBorderedRectNameTag((float)(-var16 - 2), (float)(-(super.mc.fontRenderer.FONT_HEIGHT)), (float)(var16 + 2), 2.0F, 1.0F, RenderUtil.reAlpha(Color.BLACK.getRGB(), 0.3F), RenderUtil.reAlpha(Color.BLACK.getRGB(), 0.8F));
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        fr.drawString(var14, -var16, -(super.mc.fontRenderer.FONT_HEIGHT -2), -1);
//        fr.drawString(var15, -super.mc.fontRenderer.getStringWidth(var15) / 2, -(super.mc.fontRenderer.FONT_HEIGHT - 2), -1);
        int var17 = (new Color(0,120,215)).getRGB();

        float var18 = (float)Math.ceil((double)(entity.getHealth() + entity.getAbsorptionAmount()));
        float var19 = var18 / (entity.getMaxHealth() + entity.getAbsorptionAmount());
        RenderUtil.drawRect((float)var16 + var19 * 40.0F - 40.0F + 2.0F, 2.0F, (float)(-var16) - 1.98F, 0.9F, var17);
        GL11.glPushMatrix();
        int var20 = 0;
        ItemStack[] var24 = entity.inventory.armorInventory;
        int index = entity.inventory.armorInventory.length;

        ItemStack var21;
        for(int var22 = 0; var22 < index; ++var22) {
            var21 = var24[var22];
            if(var21 != null) {
                var20 -= 11;
            }
        }

        if(entity.getHeldItem() != null) {
            var20 -= 8;
            var21 = entity.getHeldItem().copy();
            if(((ItemStack)var21).hasEffect() && (((ItemStack)var21).getItem() instanceof ItemTool || ((ItemStack)var21).getItem() instanceof ItemArmor)) {
                ((ItemStack)var21).stackSize = 1;
            }

            this.renderItemStack(var21, var20, -35);
            var20 += 20;
        }

        ItemStack[] stacks = entity.inventory.armorInventory;
        int var28 = entity.inventory.armorInventory.length;

        for(index = 0; index < var28; ++index) {
            ItemStack var27 = stacks[index];
            if(var27 != null) {
                ItemStack itemStack = var27.copy();
                if(itemStack.hasEffect(0) && (itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemArmor)) {
                    itemStack.stackSize = 1;
                }
                this.renderItemStack(itemStack, var20, -35);
                var20 += 20;
            }
        }

        GL11.glPopMatrix();
        revertAllCaps();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void revertAllCaps()
    {
        for (Iterator localIterator = glCapMap.keySet().iterator(); localIterator.hasNext();)
        {
            int cap = ((Integer)localIterator.next()).intValue();
            revertGLCap(cap);
        }
    }

    public static void revertGLCap(int cap)
    {
        Boolean origCap = (Boolean)glCapMap.get(Integer.valueOf(cap));
        if (origCap != null) {
            if (origCap.booleanValue()) {
                GL11.glEnable(cap);
            } else {
                GL11.glDisable(cap);
            }
        }
    }
    public void renderItemStack(ItemStack var1, int var2, int var3) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        RenderItem.getInstance().zLevel = -25.0f;
        whatTheFuckOpenGLThisFixesItemGlint();
        RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer,mc.getTextureManager(),var1, var2, var3);
        RenderItem.getInstance().renderItemOverlayIntoGUI(mc.fontRenderer,mc.getTextureManager(), var1, var2, var3);
        RenderItem.getInstance().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GL11.glPopMatrix();
    }

    public void whatTheFuckOpenGLThisFixesItemGlint() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    private static Map<Integer, Boolean> glCapMap = new HashMap();

    public static void setGLCap(int cap, boolean flag)
    {
        glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
        if (flag) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }
    }

    public void drawBorderedRectNameTag(float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
        RenderUtil.drawRect(var1, var2, var3, var4, var7);
        float var8 = (float)(var6 >> 24 & 255) / 255.0F;
        float var9 = (float)(var6 >> 16 & 255) / 255.0F;
        float var10 = (float)(var6 >> 8 & 255) / 255.0F;
        float var11 = (float)(var6 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(var9, var10, var11, var8);
        GL11.glLineWidth(var5);
        GL11.glBegin(1);
        GL11.glVertex2d((double)var1, (double)var2);
        GL11.glVertex2d((double)var1, (double)var4);
        GL11.glVertex2d((double)var3, (double)var4);
        GL11.glVertex2d((double)var3, (double)var2);
        GL11.glVertex2d((double)var1, (double)var2);
        GL11.glVertex2d((double)var3, (double)var2);
        GL11.glVertex2d((double)var1, (double)var4);
        GL11.glVertex2d((double)var3, (double)var4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
}
