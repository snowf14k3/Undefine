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
import net.minecraft.block.*;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {
    public Value<Double> placeDelay = new Value("Scaffold_PlaceDelay", 50, 0, 500, 1);
    public Value<Boolean> swing = new Value("Scaffold_Swing", true);
    public Value<Boolean> tower = new Value("Scaffold_Tower", true);
    private int facing = getRandomInRange(2, 4);

    private double height;

    private int currentHeldItem;

    private TimeHelper timer, itemTimer;

    private float keepYaw, keepPitch;

    public boolean placing, downwards;

    private static List<Block> badBlock = Arrays.asList(Blocks.air, Blocks.water, Blocks.tnt, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever);

    private static Random random = new Random();

    public static double getRandomInRange(double max, double min) {
        return min + (max - min) * random.nextDouble();
    }

    public static int getRandomInRange(int max, int min) {
        return (int) (min + (max - min) * random.nextDouble());
    }

    public Scaffold() {
        super("Scaffold", Category.MOVEMENT);
        timer = new TimeHelper();
        itemTimer = new TimeHelper();
    }

    public void onDisable() {
        mc.thePlayer.inventory.currentItem = currentHeldItem;
    }


    public void onEnable() {
        placing = false;
        currentHeldItem = mc.thePlayer.inventory.currentItem;
    }


//    @EventTarget
//    public void onEvent(EventMotion eventMotion) {
//        BlockPos underPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
//        Class<Vec3> clazz = null;
//        try {
//            clazz = (Class<Vec3>) Class.forName("net.minecraft.util.Vec3");
//        } catch (ClassNotFoundException e) {
//        }
//        Constructor vec3 = null;
//        try {
//            vec3 = clazz.getDeclaredConstructor(double.class,double.class,double.class);
//        } catch (NoSuchMethodException e) {
//        }
//        vec3.setAccessible(true);
//        BlockData blockData = null;
//        try {
//            blockData = find(new Vec3Helper(0, mc.gameSettings.keyBindSneak.getIsKeyPressed() && !mc.gameSettings.keyBindJump.getIsKeyPressed() ? -1 : 0, 0));
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//        if (eventMotion.getEventType().equals(EventType.PRE)) {
//            if (tower.getValueState()) {
//                if (getBlockSlot() != -1 && mc.gameSettings.keyBindJump.getIsKeyPressed() && !(mc.thePlayer.moveForward != 0)) {
//                    mc.thePlayer.motionZ = 0;
//                    mc.thePlayer.motionX = 0;
//                    if (mc.thePlayer.onGround) {
//                        mc.thePlayer.jump();
//                        if (timer.hasReached(1500)) {
//                            mc.thePlayer.motionY = -0.28;
//                            timer.reset();
//                        }
//                    } else if (mc.theWorld.getBlock(underPos.x,underPos.y,underPos.z).getMaterial().isReplaceable() && blockData != null)
//                        mc.thePlayer.motionY = .41955;
//                }
//            }
//        } else {
//            if (getBlockSlot() == -1)
//                if (itemTimer.hasReached(150)) {
//                    getBlocksFromInventory();
//                    itemTimer.reset();
//                }
//        }
//
//        if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
//            if (!downwards) {
//                mc.thePlayer.setSneaking(false);
//                downwards = true;
//            }
//        } else
//            downwards = false;
//
//        if (mc.theWorld.getBlock(underPos.x,underPos.y,underPos.z).getMaterial().isReplaceable() && blockData != null) {
//            placing = true;
//            if (getBlockSlot() != -1) {
//                switch (eventMotion.getEventType()) {
//                    case PRE:
//                        BlockPos sideBlock = blockData.position;
//                        eventMotion.setYaw(keepYaw = getBlockRotations(sideBlock.getX(), sideBlock.getY(), sideBlock.getZ(), blockData.face)[0] - 180);
//                        eventMotion.setPitch(keepPitch = getBlockRotations(sideBlock.getX(), sideBlock.getY(), sideBlock.getZ(), blockData.face)[1] + 12);
//                        break;
//                    case POST:
//                        eventMotion.setYaw(keepYaw);
//                        eventMotion.setPitch(keepPitch);
//                        mc.thePlayer.inventory.currentItem = getBlockSlot();
//                        double hitvecx = (blockData.position.getX() + height) + getRandomInRange(-.08,.29) + (blockData.face.getFrontOffsetX() / facing);
//                        double hitvecy = (blockData.position.getY() + height) + getRandomInRange(-.08,.29) + (blockData.face.getFrontOffsetY() / facing);
//                        double hitvecz = (blockData.position.getZ() + height) + getRandomInRange(-.08,.29) + (blockData.face.getFrontOffsetZ() / facing);
//
//                        Vec3 vec = null;
//                        try {
//                            vec = (Vec3) vec3.newInstance(hitvecx , hitvecy , hitvecz );
//                        } catch (InstantiationException e) {
//                            e.printStackTrace();
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        } catch (InvocationTargetException e) {
//                            e.printStackTrace();
//                        }
//                        if (timer.hasReached(placeDelay.getValueState())) {
//                            mc.playerController.onPlayerRightClick(mc.thePlayer,
//                                    mc.theWorld,
//                                    mc.thePlayer.inventory.getCurrentItem(),
//                                    blockData.position.x,blockData.position.y,blockData.position.z,
//                                    getFacing(blockData.face),
//                                    vec);
//                            if (swing.getValueState())
//                                mc.thePlayer.swingItem();
//                            else
////                                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0APacketAnimation());
//                            timer.reset();
//                        }
//                        mc.thePlayer.inventory.currentItem = currentHeldItem;
//                        break;
//                }
//            } else {
//                eventMotion.setYaw(keepYaw);
//                eventMotion.setPitch(keepPitch);
//            }
//        } else {
//            placing = false;
//            eventMotion.setYaw(keepYaw);
//            eventMotion.setPitch(keepPitch);
//        }
//    }
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
    private void getBlocksFromInventory() {
        if (mc.currentScreen instanceof GuiChest)
            return;
        for (int index = 9; index < 36; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                continue;
            if (isValid(stack.getItem())) {
                mc.playerController.windowClick(0, index, 6, 2, mc.thePlayer);
                break;
            }
        }
    }

    private int getBlockSlot() {
        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                continue;
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (isValid(stack.getItem()))
                return i - 36;
        }
        return -1;
    }

    public static boolean isValid(Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        } else {
            ItemBlock iBlock = (ItemBlock) item;
            Block block = iBlock.field_150939_a;
            return !badBlock.contains(block);
        }
    }

//    public BlockData find(Vec3Helper offset3) throws IllegalAccessException, InvocationTargetException, InstantiationException {
//
//        double x = mc.thePlayer.posX;
//        double y = mc.thePlayer.posY;
//        double z = mc.thePlayer.posZ;
//
//        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
//        BlockPos position = new BlockPos(new Vec3Helper(x, y, z).add(offset3).toVec3()).offset(EnumFacing.DOWN);
//
//        for (EnumFacing facing : EnumFacing.values()) {
//            BlockPos offset = position.offset(facing);
//            if (mc.theWorld.getBlock(offset.x,offset.y,offset.z) instanceof BlockAir || rayTrace(mc.thePlayer.getLook(0.0f), getPositionByFace(offset, invert[facing.ordinal()]).toVec3()))
//                continue;
//            return new BlockData(offset, invert[facing.ordinal()]);
//        }
//        BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 0, 2), new BlockPos(0, 0, -2), new BlockPos(2, 0, 0), new BlockPos(-2, 0, 0)};
//        for (BlockPos offset : offsets) {
//            BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
//            if (!(mc.theWorld.getBlock(offsetPos.x,offsetPos.y,offsetPos.z) instanceof BlockAir)) continue;
//            for (EnumFacing facing : EnumFacing.values()) {
//                BlockPos offset2 = offsetPos.offset(facing);
//                if (mc.theWorld.getBlock(offset2.x,offset2.y,offset2.z) instanceof BlockAir || rayTrace(mc.thePlayer.getLook(0.01f), getPositionByFace(offset, invert[facing.ordinal()]).toVec3()))
//                    continue;
//                return new BlockData(offset2, invert[facing.ordinal()]);
//            }
//        }
//        return null;
//    }

    private float[] getBlockRotations(int x, int y, int z, EnumFacing facing) {
        Entity temp = new EntitySnowball(mc.theWorld);
        temp.posX = (x + 0.5);
        temp.posY = (y + (height = 0.5));
        temp.posZ = (z + 0.5);
        return mc.thePlayer.canEntityBeSeen(temp) ? getAngles(temp) : getRotationToBlock(new BlockPos(x, y, z), facing);
    }

    private float[] getAngles(Entity e) {
        return new float[]{getYawChangeToEntity(e) + mc.thePlayer.rotationYaw, getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch};
    }

    private float getYawChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double yawToEntity;
        final double v = Math.toDegrees(Math.atan(deltaZ / deltaX));
        if ((deltaZ < 0) && (deltaX < 0)) {
            yawToEntity = 90 + v;
        } else {
            if ((deltaZ < 0) && (deltaX > 0.0D)) {
                yawToEntity = -90 + v;
            } else {
                yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
            }
        }
        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
    }

    private float getPitchChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - 0.4 - mc.thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
    }

    public float[] getRotationToBlock(BlockPos pos, EnumFacing face) {
        double random = getRandomInRange(.45, .55);
        int ranface = getRandomInRange(2, 4);
        double xDiff = pos.getX() + (height = random) - mc.thePlayer.posX + face.getDirectionVec().getX() / (facing = ranface);
        double zDiff = pos.getZ() + (height = random) - mc.thePlayer.posZ + face.getDirectionVec().getZ() / (facing = ranface);
        double yDiff = pos.getY() - mc.thePlayer.posY - 1;
        double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) -Math.toDegrees(Math.atan2(xDiff, zDiff));
        float pitch = (float) -Math.toDegrees(Math.atan(yDiff / distance));

        return new float[]{Math.abs(yaw - mc.thePlayer.rotationYaw) < .1 ? mc.thePlayer.rotationYaw : yaw, Math.abs(pitch - mc.thePlayer.rotationPitch) < .1 ? mc.thePlayer.rotationPitch : pitch};
    }

//    public Vec3Helper getPositionByFace(BlockPos position, EnumFacing facing) {
//        Vec3Helper offset = new Vec3Helper((double) facing.getDirectionVec().getX() / 2.0, (double) facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
//        Vec3Helper point = new Vec3Helper((double) position.getX() + 0.5, (double) position.getY() + 0.5, (double) position.getZ() + 0.5);
//        return point.add(offset);
//    }

    private boolean rayTrace(Vec3 origin, Vec3 position) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Vec3 difference = position.subtract(origin);
        int steps = 10;
        double x = difference.xCoord / (double) steps;
        double y = difference.yCoord / (double) steps;
        double z = difference.zCoord / (double) steps;
        Vec3 point = origin;
        for (int i = 0; i < steps; ++i) {
            BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
            Block blockState = mc.theWorld.getBlock(blockPosition.x,blockPosition.y,blockPosition.z);
            if (blockState instanceof BlockLiquid || blockState instanceof BlockAir) continue;
            AxisAlignedBB boundingBox = blockState.getCollisionBoundingBoxFromPool(mc.theWorld, blockPosition.x,blockPosition.y,blockPosition.z);
            if (boundingBox == null) {
                Class<Vec3> clazz = null;
                try {
                    clazz = (Class<Vec3>) Class.forName("net.minecraft.util.AxisAlignedBB");
                } catch (ClassNotFoundException e) {
                }
                Constructor abb = null;
                try {
                    abb = clazz.getDeclaredConstructor(double.class,double.class,double.class,double.class,double.class,double.class);
                } catch (NoSuchMethodException e) {
                }
                abb.setAccessible(true);
                boundingBox = (AxisAlignedBB) abb.newInstance(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            }
            if (!boundingBox.offset(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()).isVecInside(point))
                continue;
            return true;
        }
        return false;
    }

    private static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }



}

