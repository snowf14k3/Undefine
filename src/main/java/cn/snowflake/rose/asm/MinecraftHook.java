package cn.snowflake.rose.asm;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.command.Command;
import cn.snowflake.rose.events.impl.*;
import cn.snowflake.rose.manager.CommandManager;
import cn.snowflake.rose.manager.FontManager;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.mod.mods.MOVEMENT.Jesus;
import cn.snowflake.rose.mod.mods.PLAYER.NoSlow;
import cn.snowflake.rose.mod.mods.RENDER.Chams;
import cn.snowflake.rose.mod.mods.RENDER.ChestESP;
import cn.snowflake.rose.mod.mods.RENDER.NoHurtcam;
import cn.snowflake.rose.mod.mods.RENDER.ViewClip;
import cn.snowflake.rose.mod.mods.WORLD.NoCommand;
import cn.snowflake.rose.mod.mods.WORLD.Xray;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Objects;

public class MinecraftHook {
    public static Rotation serverRotation = new Rotation(0F, 0F);
    public static List<URL> fuckSources(List<URL> sources){
        sources.removeIf(url ->
                url.toString().endsWith(".tmp")
        );
        return sources;
    }

    public static void startSectionHook(String info){
        if (info.equalsIgnoreCase("hand")){
            Event3D();
        }else if (info.equalsIgnoreCase("forgeHudText")){
            Event2D();
        }
    }


    //Client Start
    public static void runClient() {
        if (!Client.init){
            new Client();
            Client.init = true;
            if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null){
                ChatUtil.sendClientMessage("Client Injected !");
            }
        }
        if (!Client.instance.font){
                Client.instance.fontManager = new FontManager();
                Client.fs = Client.instance.fontManager.simpleton11;
                Client.fss = Client.instance.fontManager.simpleton10;
                Client.instance.font = true;
        }
    }

    public static boolean getRenderBlockPass(Block block){
        return Xray.containsID(block);
    }

    public static AxisAlignedBB jesusHook(BlockLiquid bf, int x, int y, int z) {
        return !Jesus.jesus ? null : Minecraft.getMinecraft().thePlayer.isSneaking() ? null : AxisAlignedBB.getBoundingBox((double)x + bf.getBlockBoundsMinX(), (double)y + bf.getBlockBoundsMinY(), (double)z + bf.getBlockBoundsMinZ(), (double)x + bf.getBlockBoundsMaxY(), (double)y + bf.getBlockBoundsMaxY(), (double)z + bf.getBlockBoundsMaxZ());
    }
    public static boolean isInsideBlock() {
        if(Minecraft.getMinecraft().thePlayer == null) {
            return false;
        } else {
            AxisAlignedBB var1 = Minecraft.getMinecraft().thePlayer.getBoundingBox();
            boolean var2 = false;

            for(int var3 = MathHelper.floor_double(var1.minX); var3 < MathHelper.floor_double(var1.maxX) + 1; ++var3) {
                int var4 = (int)var1.minY;

                for(int var5 = MathHelper.floor_double(var1.minZ); var5 < MathHelper.floor_double(var1.maxZ) + 1; ++var5) {
                    Block var6 = Minecraft.getMinecraft().theWorld.getBlock(var3, var4, var5);
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
    public static void chamsHook1(Object object){
        if (ModManager.getModByName("Chams").isEnabled() && object instanceof EntityPlayer){
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0F, -2000000F);
        }
    }
    public static void chamsHook2(Object object){
        if (Objects.requireNonNull(ModManager.getModByName("Chams")).isEnabled() && object instanceof EntityPlayer){
            GL11.glPolygonOffset(1.0F, 2000000F);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }
    public static void onUpdateWalkingPlayerHook(EventType stage) {
        EventMotion em = new EventMotion(stage);
        EventManager.call(em);
    }
    public static boolean isNohurtcamEnable(){
        return NoHurtcam.no;
    }
    ///Client End
    public static void Event3D(){
        GLUProjection projection = GLUProjection.getInstance();
        IntBuffer viewPort = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projectionPort = GLAllocation.createDirectFloatBuffer(16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projectionPort);
        GL11.glGetInteger((int)2978, (IntBuffer)viewPort);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(),Minecraft.getMinecraft().displayWidth,Minecraft.getMinecraft().displayHeight);
        projection.updateMatrices(viewPort, modelView, projectionPort, (double)scaledResolution.getScaledWidth() / (double)Minecraft.getMinecraft().displayWidth, (double)scaledResolution.getScaledHeight() / (double)Minecraft.getMinecraft().displayHeight);
        EventRender3D er3 = new EventRender3D();
        EventManager.call(er3);
    }

    public static boolean insideHook(){
        EventInsideBlock event = new EventInsideBlock();
        EventManager.call(event);
        return event.cancel;
    }
    public static boolean pushOutOfBlocksHooks(){
        EventPushOut event = new EventPushOut();
        EventManager.call(event);
        return !event.cancel;
    }
    public static void Event2D(){
        GlStateManager.pushMatrix();
        EventRender2D er = new EventRender2D();
        EventManager.call(er);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    //noslow start
    private static float cacheStrafe;
    private static float cacheForward;

    public static boolean onNoSlowEnable2() {
        return NoSlow.no;
    }

    public static boolean isSlow() {
        return Minecraft.getMinecraft().thePlayer.isUsingItem() && !Minecraft.getMinecraft().thePlayer.isRiding();
    }

    public static void eventStepHook1(float stepheight){
        EventManager.call(new EventStep(EventType.PRE,stepheight));
    }
    public static void eventStepHook2(float stepheight){
        EventManager.call(new EventStep(EventType.POST,stepheight));
    }

    public static void onNoSlowEnable() {
        if (!isSlow()) {
            return;
        }
        if (ModManager.getModByName("NoSlow").isEnabled()) {
            cacheStrafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
            cacheForward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        }
    }
    public static void onToggledTimerZero(){
        if (!isSlow()) {
            return;
        }
        if (ModManager.getModByName("NoSlow").isEnabled()) {
            Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe = cacheStrafe;
            Minecraft.getMinecraft().thePlayer.movementInput.moveForward = cacheForward;
        }
    }
    //noslow end
    public static boolean isViewClipEnabled() {
        return Objects.requireNonNull(ModManager.getModByName("ViewClip")).isEnabled();
    }
    public static boolean isXrayContains(Block blcok) {
        return  Xray.containsID(blcok);
    }
    public static boolean isXrayCaveEnabled() {
//        return  isXrayEnabled() & !Xray.cave.getValueState();
        return false;
    }
    public static boolean isXrayEnabled() {
        return Xray.x;
    }

    public static int getOpacity(){
        return Xray.OPACITY.getValueState().intValue();
    }

    public static void runTick(){
        EventManager.call(new EventTick());
        if (Keyboard.getEventKeyState() && Minecraft.getMinecraft().currentScreen == null) {
            for (Module mod : ModManager.getModList()) {
                if (mod.getKey() != (Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey())) continue;
                mod.set(!mod.isEnabled());
                break;
            }
        }
    }

    public static void onUpdate(){
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            EventUpdate event = new EventUpdate();
            EventManager.call(event);
        }
    }

    public static void command(String message){
        String s = CommandManager.removeSpaces(message);
        if (message.startsWith("-") && !NoCommand.n) {
            for (Command cmd : CommandManager.getCommands()) {
                int i = 0;
                while (i < cmd.getCommands().length) {
                    //TODO 符号
                    if (s.split(" ")[0].equals("-" + cmd.getCommands()[i])) {
                        cmd.onCmd(s.split(" "));
                        return;
                    }
                    ++i;
                }
            }
            return;
        }
    }
    public static boolean isNoCommandEnabled(String s,String s1) {
        return s.startsWith(s1) && !NoCommand.n;
    }


}
