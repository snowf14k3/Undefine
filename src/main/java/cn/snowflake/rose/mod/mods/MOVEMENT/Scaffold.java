package cn.snowflake.rose.mod.mods.MOVEMENT;


import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import com.darkmagician6.eventapi.EventTarget;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {
    public TimeHelper timer = new TimeHelper();
    public Value<String> mode = new Value( "Scaffold","Mode", 0 );
    public BlockData blockData;
   public Value<Boolean> safe = new Value("Scaffold_Safe", false);
    public Value<Boolean> silent = new Value( "Scaffold_Silent", false);

    private List<Block> badBlock = Arrays.asList(Blocks.air, Blocks.water, Blocks.tnt, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever);
    public Scaffold() {
        super("Scaffold", Category.MOVEMENT);
        mode.addValue("Normal");
    }
    @EventTarget
    public void onPre(EventMotion e) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (e.getEventType() == EventType.PRE) {
            mc.thePlayer.cameraYaw = 0.1f;
            if (mode.getModeName().equalsIgnoreCase("Normal")) {
                this.blockData = this.getBlockData(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY+ 1, mc.thePlayer.posZ));
                if (blockData != null) {
                    float[] rotations = RotationUtil.getRotationsNeededBlock(this.blockData.position.x,this.blockData.position.y,this.blockData.position.z);
                    e.setYaw(rotations[0]);
                    e.setPitch(rotations[1]);
                    mc.thePlayer.rotationYawHead = rotations[0];
                    mc.thePlayer.rotationYaw = rotations[0];
                    mc.thePlayer.rotationPitch = rotations[1];
                }
            }
        }else if (e.getEventType() == EventType.POST){
            if (blockData !=null) {
                int block = getBlockItem();
                if (block != -1 && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock || block != -1 && silent.getValueState()) {
                    if (silent.getValueState()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(block));
                    }
                    this.placeBlock(this.blockData.position, this.blockData.face, block);
                }
            }
        }
    }
    @Override
    public void onDisable() {
        if (silent.getValueState()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    }
    
    private int getBlockItem()
    {
        int block = -1;
        for (int i = 8; i >= 0; i--) {
            if ((this.mc.thePlayer.inventory.getStackInSlot(i) != null) &&
                    ((this.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock)) && (
                    (this.mc.thePlayer.getHeldItem() == this.mc.thePlayer.inventory.getStackInSlot(i)) || silent.getValueState())) {
                block = i;
            }
        }
        return block;
    }
    private boolean placeBlock(BlockPos pos, EnumFacing facing, int slotWithBlockInIt) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Vec3> clazz = null;
        try {
            clazz = (Class<Vec3>) Class.forName("net.minecraft.util.Vec3");
        } catch (ClassNotFoundException e) {
        }
        Constructor vec3 = null;
        try {
            vec3 = clazz.getDeclaredConstructor(double.class,double.class,double.class);
        } catch (NoSuchMethodException e) {
        }
        vec3.setAccessible(true);
        Vec3 eyesPos = (Vec3) vec3.newInstance(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(36 + slotWithBlockInIt).getStack();
        Vec3Helper vec3Helper = new Vec3Helper(this.blockData.position).addVector(0.5, 0.5, 0.5).add((new Vec3Helper(this.blockData.face.getDirectionVec())).scale(0.5));
            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemstack, pos.x,pos.y,pos.z, getFacing(facing),((Vec3)vec3.newInstance(vec3Helper.x,vec3Helper.y,vec3Helper.z)) );
            mc.thePlayer.swingItem();
            return true;
    }

    //    DOWN(0, 1, 0, -1, 0),
    //    UP(1, 0, 0, 1, 0),
    //    NORTH(2, 3, 0, 0, -1),
    //    SOUTH(3, 2, 0, 0, 1),
    //    EAST(4, 5, -1, 0, 0),
    //    WEST(5, 4, 1, 0, 0);
    //D-U-N-S-E-W.
    public int getFacing(EnumFacing enumFacing){
        if (enumFacing.DOWN == EnumFacing.DOWN){
            return 0;
        }else if (enumFacing.UP == EnumFacing.UP){
            return 1;
        }else if (enumFacing.NORTH == EnumFacing.NORTH){
            return 2;
        }else if (enumFacing.SOUTH == EnumFacing.SOUTH){
            return 3;
        }else if (enumFacing.EAST == EnumFacing.EAST){
            return 4;
        }else if (enumFacing.WEST == EnumFacing.WEST){
            return 5;
        }
        return 0;
    }

    private BlockData getBlockData(BlockPos pos) {
        if (!this.contains(getBlockState(pos))) {
            return new BlockData(pos, EnumFacing.UP);
        }
        if (!this.contains(getBlockState(pos))) {
            return new BlockData(pos, EnumFacing.EAST);
        }
        if (!this.contains(getBlockState(pos))) {
            return new BlockData(pos, EnumFacing.WEST);
        }
        if (!this.contains(getBlockState(pos))) {
            return new BlockData(pos, EnumFacing.SOUTH);
        }
        if (!this.contains(getBlockState(pos))) {
            return new BlockData(pos, EnumFacing.NORTH);
        }
//        BlockPos add = pos.add(-1, 0, 0);
//        if (!this.contains(getBlockState(add))) {
//            return new BlockData(add, EnumFacing.EAST);
//        }
//        if (!this.contains(getBlockState(add))) {
//            return new BlockData(add, EnumFacing.WEST);
//        }
//        if (!this.contains(getBlockState(add))) {
//            return new BlockData(add, EnumFacing.SOUTH);
//        }
//        if (!this.contains(getBlockState(add))) {
//            return new BlockData(add, EnumFacing.NORTH);
//        }
//        BlockPos add2 = pos.add(1, 0, 0);
//        if (!this.contains(getBlockState(add2))) {
//            return new BlockData(add2, EnumFacing.EAST);
//        }
//        if (!this.contains(getBlockState(add2))) {
//            return new BlockData(add2, EnumFacing.WEST);
//        }
//        if (!this.contains(getBlockState(add2))) {
//            return new BlockData(add2, EnumFacing.SOUTH);
//        }
//        if (!this.contains(getBlockState(add2))) {
//            return new BlockData(add2, EnumFacing.NORTH);
//        }
//        BlockPos add3 = pos.add(0, 0, -1);
//        if (!this.contains(getBlockState(add3))) {
//            return new BlockData(add3, EnumFacing.EAST);
//        }
//        if (!this.contains(getBlockState(add3))) {
//            return new BlockData(add3, EnumFacing.WEST);
//        }
//        if (!this.contains(getBlockState(add3))) {
//            return new BlockData(add3, EnumFacing.SOUTH);
//        }
//        if (!this.contains(getBlockState(add3))) {
//            return new BlockData(add3, EnumFacing.NORTH);
//        }
//        BlockPos add4 = pos.add(0, 0, 1);
//        if (!this.contains(getBlockState(add4))) {
//            return new BlockData(add4, EnumFacing.EAST);
//        }
//        if (!this.contains(getBlockState(add4))) {
//            return new BlockData(add4, EnumFacing.WEST);
//        }
//        if (!this.contains(getBlockState(add4))) {
//            return new BlockData(add4, EnumFacing.SOUTH);
//        }
//        if (!this.contains(getBlockState(add4))) {
//            return new BlockData(add4, EnumFacing.NORTH);
//        }
        BlockData blockData = null;
        return blockData;
    }

    private Block getBlockState(BlockPos add) {
        return mc.theWorld.getBlock(add.x,add.y, add.z);
    }

    private boolean contains(Block block) {
        return this.badBlock.stream().anyMatch(e -> e.equals(block));
    }
    class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos blockPos, EnumFacing enumFacing) {
            this.position = blockPos;
            this.face = enumFacing;
        }
    }
}

